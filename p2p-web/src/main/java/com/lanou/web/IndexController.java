package com.lanou.web;

        import com.lanou.constant.Constants;
        import com.lanou.model.loan.LoanInfo;
        import com.lanou.service.loan.BidInfoService;
        import com.lanou.service.loan.LoanInfoService;
        import com.lanou.service.user.UserService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.RequestMapping;

        import javax.servlet.http.HttpServletRequest;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

/**
 * ClassName : IndexController
 * PackageName : com.lanou.web
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/8/15 21:50
 * @Version : 1.0
 */

@Controller
public class IndexController {

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidInfoService bidInfoService;

    @RequestMapping(value="/index")
    public String index(HttpServletRequest request,Model model){

        //历史平均年化收益率
        Double historicalAverageRate = loanInfoService.queryHistoricalAverageRate();
        model.addAttribute(Constants.HISTORICAL_AVERAGE_RATE,historicalAverageRate);

        //平台注册总人数
        Long userCount = userService.queryUserCount();
        model.addAttribute(Constants.USER_COUNT,userCount);

        //平台累计投资金额
        Double bidMoneySum =  bidInfoService.queryBidMoneySum();
        model.addAttribute(Constants.BID_MONEY_SUM,bidMoneySum);

        //将以下方法看成分页，根据产品标识获取信息列表(产品类型,页码,每页显示的条数) -> 返回List<产品>
        //准备查询参数
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("startIndex",0);//页码

        //新手宝：显示第1页，每页显示1个
        paramMap.put("productType",Constants.PRODUCT_TYPE_X);
        paramMap.put("pageSize",1);
        List<LoanInfo> xLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        //优先：显示第1个，每页显示4个
        paramMap.put("productType",Constants.PRODUCT_TYPE_U_);
        paramMap.put("pageSize",4);
        List<LoanInfo> uLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        //散标：显示第1页，每页显示4个
        paramMap.put("productType",Constants.PRODUCT_TYPE_S);
        paramMap.put("pageSize",8);
        List<LoanInfo> sLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        model.addAttribute("xLoanInfoList",xLoanInfoList);
        model.addAttribute("uLoanInfoList",uLoanInfoList);
        model.addAttribute("sLoanInfoList",sLoanInfoList);

        return "index";
    }
}
