package com.lanou.web;

import com.lanou.common.constant.Constants;
import com.lanou.model.loan.IncomeRecord;
import com.lanou.model.user.User;
import com.lanou.model.vo.PaginationVO;
import com.lanou.service.loan.IncomeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName : IncomeController
 * PackageName : com.lanou.web
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/2 14:09
 * @Version : 1.0
 */
@Controller
public class IncomeController {
    @Autowired
    private IncomeRecordService incomeRecordService;

    @RequestMapping("/loan/myIncome")
    public String myIncome(HttpServletRequest request, Model model,
                           @RequestParam(value = "currentPage",required = false) Integer currentPage) {
        if(currentPage == null) {
            currentPage = 1;
        }
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("uid",sessionUser.getId());
        int pageSize = 10;
        paramMap.put("startIndex",(currentPage - 1) * pageSize);
        paramMap.put("pageSize",pageSize);

        PaginationVO<IncomeRecord> paginationVO = incomeRecordService.queryIncomeRecordByPage(paramMap);

        model.addAttribute("totalRows",paginationVO.getTotal());

        int totalPage = paginationVO.getTotal().intValue() / pageSize;
        if(paginationVO.getTotal().intValue() % pageSize > 0) {
            totalPage ++;
        }

        model.addAttribute("totalPage",totalPage);

        model.addAttribute("incomeRecordDataList",paginationVO.getDataList());
        model.addAttribute("currentPage",currentPage);

        return "myIncome";
    }
}
