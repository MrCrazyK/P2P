package com.lanou.web;

import com.lanou.constant.Constants;
import com.lanou.model.loan.BidInfo;
import com.lanou.model.loan.LoanInfo;
import com.lanou.model.user.FinanceAccount;
import com.lanou.model.user.User;
import com.lanou.model.vo.PaginationVO;
import com.lanou.model.vo.UserBid;
import com.lanou.service.loan.BidInfoService;
import com.lanou.service.loan.LoanInfoService;
import com.lanou.service.user.FinanceAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName : LonInfoController
 * PackageName : com.lanou.web
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/8/17 21:30
 * @Version : 1.0
 */
@Controller
public class LoanInfoController {

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private BidInfoService bidInfoService;

    @Autowired
    private FinanceAccountService financeAccountService;

    @RequestMapping(value="/loan/loan")
    public String loan(HttpServletRequest request, Model model,
                       @RequestParam (value = "ptype",required = false) Integer ptype,
                       @RequestParam (value = "currentPage",required = false) Integer currentPage ){
        //判断是否为首页
        if (currentPage == null) {
            //默认第一页
            currentPage = 1;
        }

        //准备一个分页查询的参数
        Map<String,Object> paramMap = new HashMap<>();
        Integer pageSize = 9;
        paramMap.put("startIndex",(currentPage - 1) * pageSize);
        paramMap.put("pageSize",pageSize);
        if (ptype != null) {
            paramMap.put("productType",ptype);
        }

        //分页查询信息列表(类型,页码,显示条数)    -->   返回List<产品>,定义一个分页查询对象paginationVO
        PaginationVO<LoanInfo> paginationVO = loanInfoService.queryLoanInfoByPage(paramMap);

        //计算总页数
        int totalPage = paginationVO.getTotal().intValue() / pageSize;
        int mod = paginationVO.getTotal().intValue() % pageSize;
        if(mod > 0){
            totalPage += 1;
        }

        model.addAttribute("totalRows",paginationVO.getTotal());
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("loanInfoList",paginationVO.getDataList());
        model.addAttribute("currentPage",currentPage);
        if (ptype != null) {
            model.addAttribute("ptype",ptype);
        }

        //用户投资排行榜
        List<UserBid> userBidList = bidInfoService.queryUserBidTop();
        model.addAttribute("userBidList",userBidList);

        return "loan";
    }

    @RequestMapping(value = "/loan/loanInfo")
    public String loanInfo(HttpServletRequest request,Model model,
                           @RequestParam(value = "id",required = true) Integer id){

        //根据产品标识获取产品信息详情
        LoanInfo loanInfo = loanInfoService.queryLoanInfoById(id);

        //根据产品标识获取该产品的所有投资记录
        List<BidInfo> bidInfoList = bidInfoService.queryBidInfoByLoanId(id);

        model.addAttribute("loanInfo",loanInfo);
        model.addAttribute("bidInfoList",bidInfoList);

        //从session获取用户的信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        //判断用户是否登录
        if (sessionUser != null) {
            //根据用户标识查询账户资金信息
            FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());
            model.addAttribute("financeAccount",financeAccount);
        }

        return "loanInfo";
    }


}
