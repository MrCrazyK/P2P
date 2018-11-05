package com.lanou.web;

import com.lanou.constant.Constants;
import com.lanou.model.loan.BidInfo;
import com.lanou.model.user.ResultObject;
import com.lanou.model.user.User;
import com.lanou.model.vo.PaginationVO;
import com.lanou.service.loan.BidInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName : BidInfoController
 * PackageName : com.lanou.web
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/1 22:57
 * @Version : 1.0
 */
@Controller
public class BidInfoController {
    @Autowired
    BidInfoService bidInfoService;

    @RequestMapping("/loan/myInvest")
    public String myInvest(HttpServletRequest request, Model model,
                           @RequestParam (value = "currentPage",required = false) Integer currentPage){
        //判断是否是第一页
        if (currentPage == null) {
            currentPage = 1;
        }
        //从seesion中获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        //准备分页查询参数
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("uid",sessionUser.getId());
        int pageSize = 10;
        paramMap.put("startIndex",(currentPage - 1) * pageSize);
        paramMap.put("pageSize",pageSize);

        //根据用户标识分页查询投资记录（用户标识，页码，每页显示的条数） -> PaginationVO
        PaginationVO<BidInfo> paginationVO = bidInfoService.queryBidInfoByPage(paramMap);

        //计算总页数
        int totalPage = paginationVO.getTotal().intValue() / pageSize;
        int mod = paginationVO.getTotal().intValue() % pageSize;
        if(mod > 0) {
            totalPage ++;
        }

        model.addAttribute("totalPage",totalPage);
        model.addAttribute("totalRows",paginationVO.getTotal());
        model.addAttribute("bidInfoList",paginationVO.getDataList());
        model.addAttribute("currentPage",currentPage);

        return "myInvest";
    }

    @RequestMapping("/loan/invest")
    public @ResponseBody Object invest(HttpServletRequest request,
                                       @RequestParam(value = "loanId",required = true) Integer loanId,
                                       @RequestParam(value = "bidMoney",required = true) Double bidMoney) {
        Map<String,Object> retMap = new HashMap<>();
        //用户投资 （用户标识，投资产品标识，投资金额） -> 返回结果
        Map<String,Object> paramMap = new HashMap<>();

        //从session中获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        paramMap.put("uid",sessionUser.getId());
        paramMap.put("loanId",loanId);
        paramMap.put("bidMoney",bidMoney);
        paramMap.put("phone",sessionUser.getPhone());

        ResultObject resultObject = bidInfoService.invest(paramMap);

        //判断投资结果
        if(StringUtils.equals(Constants.SUCCESS,resultObject.getErrorCode())) {
            retMap.put(Constants.ERROR_MESSAGE,Constants.OK);
        } else {
            retMap.put(Constants.ERROR_MESSAGE,"投资失败");
            return retMap;
        }

        return retMap;
    }
}
