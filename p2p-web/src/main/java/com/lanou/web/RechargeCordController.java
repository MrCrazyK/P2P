package com.lanou.web;

import com.lanou.common.constant.Constants;
import com.lanou.model.loan.RechargeRecord;
import com.lanou.model.user.User;
import com.lanou.model.vo.PaginationVO;
import com.lanou.service.loan.RechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName : RechargeCordController
 * PackageName : com.lanou.web
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/2 12:34
 * @Version : 1.0
 */
@Controller
public class RechargeCordController {
    @Autowired
    private RechargeRecordService rechargeRecordService;

    @RequestMapping("/loan/myRecharge")
    public String myRecharge(HttpServletRequest request, Model model,
                             @RequestParam(value = "currentPage",required = false) Integer currentPage) {
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        if(currentPage == null) {
            currentPage = 1;
        }

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("uid",sessionUser.getId());
        int pageSize = 10;
        paramMap.put("startIndex",(currentPage - 1) * pageSize);
        paramMap.put("pageSize",pageSize);

        PaginationVO<RechargeRecord> paginationVO = rechargeRecordService.queryRechargeRecordByPage(paramMap);

        //计算总页数
        int totalPage = paginationVO.getTotal().intValue() / pageSize;
        int mod = paginationVO.getTotal().intValue() % pageSize;
        if(mod > 0) {
            totalPage ++;
        }

        model.addAttribute("totalPage",totalPage);
        model.addAttribute("totalRows",paginationVO.getTotal());
        model.addAttribute("rechargeRecordList",paginationVO.getDataList());
        model.addAttribute("currentPage",currentPage);

        return "myRecharge";
    }

}
