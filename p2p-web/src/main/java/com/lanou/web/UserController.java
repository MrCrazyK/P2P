package com.lanou.web;

import com.alibaba.fastjson.JSONObject;
import com.lanou.constant.Constants;
import com.lanou.config.Config;
import com.lanou.model.loan.BidInfo;
import com.lanou.model.loan.IncomeRecord;
import com.lanou.model.loan.RechargeRecord;
import com.lanou.model.user.FinanceAccount;
import com.lanou.model.user.ResultObject;
import com.lanou.model.user.User;
import com.lanou.service.loan.BidInfoService;
import com.lanou.service.loan.IncomeRecordService;
import com.lanou.service.user.FinanceAccountService;
import com.lanou.service.loan.LoanInfoService;
import com.lanou.service.loan.RechargeRecordService;
import com.lanou.service.user.UserService;
import com.lanou.util.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * ClassName : UserController
 * PackageName : com.lanou.web
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/8/19 19:27
 * @Version : 1.0
 */
@Controller        //类上加@Controller + 方法上加上@ResponseBody  等价于  @RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Config config;

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private BidInfoService bidInfoService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @Autowired
    private RechargeRecordService rechargeRecordService;

    @Autowired
    private IncomeRecordService incomeRecordService;

    /**
     * 验证手机号是否存在
     * 网网关地址：http://localhost:8080/p2p/loan/checkPhone
     * 请求方式：只支持POST
     * @param request
     * @param phone 必填
     * @return json格式的字符串{"errorMessage" : "提示信息"}
     */
   // @RequestMapping(value="/loan/checkPhone",method = RequestMethod.POST)等价于@PostMapping(value="/loan/checkPhone")
    @PostMapping(value = "/loan/checkPhone")
    public @ResponseBody Object checkPhone(HttpServletRequest request,
                                           @RequestParam(value = "phone",required = true)String phone) {
        Map<String,Object> retMap = new HashMap<>();

        //根据手机号查询用户信息(手机号) -> 返回User|boolean|int
        User user =  userService.queryUserByPhone(phone);

        //判断用户是否存在
        if (user != null) {
            //该用户存在，手机号已被注册，请更换手机号
            retMap.put(Constants.ERROR_MESSAGE,"手机号已被注册，请更换手机号");
            return retMap;
        }
        retMap.put(Constants.ERROR_MESSAGE,Constants.OK);
        return retMap;
    }

    @PostMapping(value = "/loan/checkCaptcha")
    public @ResponseBody Object checkCaptcha(HttpServletRequest request,
                                           @RequestParam (value = "captcha",required = true) String captcha) {
        Map<String,Object> retMap = new HashMap<>();

        //从session中获取图形验证码
        String sessionCaptcha = (String) request.getSession().getAttribute(Constants.CAPTCHA);

        if(!StringUtils.equalsIgnoreCase(sessionCaptcha,captcha)) {
            retMap.put(Constants.ERROR_MESSAGE,"请输入正确的图形验证码");
            return retMap;
        }

        retMap.put(Constants.ERROR_MESSAGE,Constants.OK);

        return retMap;
    }

    @RequestMapping(value = "/loan/register")
    public @ResponseBody Map<String,Object> register(HttpServletRequest request,
                                       @RequestParam(value = "phone",required = true)String phone,
                                       @RequestParam(value = "loginPassword",required = true)String loginPassword,
                                       @RequestParam(value = "replayLoginPassword",required = true)String replayLoginPassword) {
        Map<String,Object> retMap = new HashMap<>();

        //--------------参数验证------------------
        if(!Pattern.matches("^1[1-9]\\d{9}$",phone)) {
            retMap.put(Constants.ERROR_MESSAGE,"请输入正确的手机号");
            return retMap;
        }

        if(!StringUtils.equals(loginPassword,replayLoginPassword)) {
            retMap.put(Constants.ERROR_MESSAGE,"两次输入密码不一致");
            return retMap;
        }

        //用户注册【新增用户，开立账户】(手机号，登录密码) -> 返回int|boolean|结果对象resultObject
        ResultObject resultObject = userService.register(phone,loginPassword);

        //判断用户是否注册成功
        if(StringUtils.equals(resultObject.getErrorCode(),Constants.SUCCESS)) {
            //将用户的信息存放到session中
            request.getSession().setAttribute(Constants.SESSION_USER,userService.queryUserByPhone(phone));
            retMap.put(Constants.ERROR_MESSAGE,Constants.OK);
        } else {
            retMap.put(Constants.ERROR_MESSAGE,"注册失败,请稍后重试...");
            return retMap;
        }

        return retMap;
    }

    //实名认证
    @RequestMapping("/loan/verifyRealName")
    public @ResponseBody Object verifyRealName(HttpServletRequest request,
                                 @RequestParam(value = "realName",required = true) String realName,
                                 @RequestParam(value = "idCard",required = true) String idCard,
                                 @RequestParam(value = "replayIdCard",required = true) String replayIdCard) {
        Map<String,Object> retMap = new HashMap<>();

        //参数验证
        if(!Pattern.matches("^[\\u4e00-\\u9fa5]{0,}$",realName)) {
            retMap.put(Constants.ERROR_MESSAGE,"真实姓名只支持中文");
            return retMap;
        }

        if(!Pattern.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)",realName)) {
            retMap.put(Constants.ERROR_MESSAGE,"请输入正确的身份证号");
        }

        if(!StringUtils.equals(idCard,replayIdCard)) {
            retMap.put(Constants.ERROR_MESSAGE,"两次身份证号不一致");
        }

        //进行实名认证,借助于互联网的实名认证接口
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("appkey",config.getRealNameAppKey());//接口请求的key
        paramMap.put("cardNo",idCard);
        paramMap.put("realName",realName);
        String resultJson = HttpClientUtils.doPost(config.getRealNameUrl(),paramMap);

        //解析json格式的字符串，使用fastjson
        //1.将json格式字符串转换成JSON对象
        JSONObject jsonObject = JSONObject.parseObject(resultJson);
        //2.获取指定key对应的value
        //获取通信标识
        String code = jsonObject.getString("code");
        //判断通信是否成功
        if(StringUtils.equals("10000",code)) {
            //通信成功,获取业务处理结果
            Boolean isok = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");
            //判断业务处理结果
            if(isok) {
                User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
                User user = new User();
                user.setId(sessionUser.getId());
                user.setName(realName);
                user.setIdCard(idCard);
                //更新用户信息
                int modifyUserCount = userService.modifyUserById(user);
                if(modifyUserCount == 1) {
                    //更新session中用户的信息
                    request.getSession().setAttribute(Constants.SESSION_USER,userService.queryUserByPhone(sessionUser.getPhone()));
                    retMap.put(Constants.ERROR_MESSAGE,Constants.OK);
                } else {
                    retMap.put(Constants.ERROR_MESSAGE,"实名认证失败");

                    return retMap;
                }


            }
        } else {
            //通信失败
            retMap.put(Constants.ERROR_MESSAGE,"实名认证失败");
            return retMap;
        }

        return retMap;
    }

    @RequestMapping("/loan/loanStat")
    public @ResponseBody Object loandStat(HttpServletRequest request) {
        Map<String,Object> retMap = new HashMap<>();
        //历史平均年化收益率
        Double historicalAverageRate = loanInfoService.queryHistoricalAverageRate();

        //平台注册总人数
        Long userCount = userService.queryUserCount();

        //平台投资总金额
         Double bidMoneySum = bidInfoService.queryBidMoneySum();

         retMap.put("historicalAverageRate",historicalAverageRate);
         retMap.put("bidMoneySum",bidMoneySum);
         retMap.put("userCount",userCount);
        return retMap;
    }

    @RequestMapping("/loan/login")
    public @ResponseBody Object login(HttpServletRequest request,
                        @RequestParam(value="phone",required = true) String phone,
                        @RequestParam(value="loginPassword",required = true) String loginPassword) {

        Map<String,Object> retMap = new HashMap<>();

        //进行登录(手机号,登录密码)[根据手机号和密码查询用户,更新最近登录时间] -> 返回User
        User user = userService.login(phone,loginPassword);

        //判断用户是否登录
        if (user == null) {
            retMap.put(Constants.ERROR_MESSAGE,"用户名或密码有误");
            return retMap;
        }
        request.getSession().setAttribute(Constants.SESSION_USER,user);
        retMap.put(Constants.ERROR_MESSAGE,Constants.OK);

        return retMap;
    }

    @RequestMapping(value="/loan/logout")
    public String logout(HttpServletRequest request) {
        //清除session中的值
        request.getSession().removeAttribute(Constants.SESSION_USER);
        return "redirect:/index";
    }

    @RequestMapping("/loan/myCenter")
    public String myCenter(HttpServletRequest request,Model model) {
        //根据用户标识获取账户资金信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());

        //查询的参数
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("uid",sessionUser.getId());
        paramMap.put("startIndex",0);
        paramMap.put("pageSize",5);

        //根据用户标识获取最近的投资记录
        List<BidInfo> bidInfoList = bidInfoService.queryBidInfoByUid(paramMap);

        //根据用户标识获取最近的充值记录
        List<RechargeRecord> rechargeRecordList = rechargeRecordService.queryRechargeRecordTopByUid(paramMap);

        //根据标识获取最近的收益记录
        List<IncomeRecord> incomeRecordList = incomeRecordService.queryIncomeRecordTopByUid(paramMap);

        model.addAttribute("financeAccount",financeAccount);
        model.addAttribute("bidInfoList",bidInfoList);
        model.addAttribute("rechargeRecordList",rechargeRecordList);
        model.addAttribute("incomeRecordList",incomeRecordList);

        return "myCenter";
    }
}
