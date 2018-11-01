package com.lanou.service.loan;

import com.lanou.model.loan.IncomeRecord;
import com.lanou.model.vo.PaginationVO;

import java.util.List;
import java.util.Map; /**
 * ClassName : IncomeRecordService
 * PackageName : com.lanou.service.loan
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/1 19:54
 * @Version : 1.0
 */
public interface IncomeRecordService {
    List<IncomeRecord> queryIncomeRecordTopByUid(Map<String, Object> paramMap);

    PaginationVO<IncomeRecord> queryIncomeRecordByPage(Map<String, Object> paramMap);

    /**
     * 生成收益计划
     */
    void generateIncomePlan();

    /**
     * 收益返还
     */
    void generateIncomeBack();
}
