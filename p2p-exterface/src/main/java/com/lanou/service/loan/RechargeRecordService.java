package com.lanou.service.loan;

import com.lanou.model.loan.RechargeRecord;
import com.lanou.model.vo.PaginationVO;

import java.util.List;
import java.util.Map; /**
 * ClassName : RechargeRecordService
 * PackageName : com.lanou.service.user
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/1 19:32
 * @Version : 1.0
 */
public interface RechargeRecordService {
    List<RechargeRecord> queryRechargeRecordTopByUid(Map<String, Object> paramMap);

    PaginationVO<RechargeRecord> queryRechargeRecordByPage(Map<String, Object> paramMap);
}
