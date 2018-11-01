package com.lanou.service.loan;

import com.lanou.mapper.loan.RechargeRecordMapper;
import com.lanou.model.loan.RechargeRecord;
import com.lanou.model.vo.PaginationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ClassName : RechargeRecordServiceimpl
 * PackageName : com.lanou.service.loan
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/1 19:34
 * @Version : 1.0
 */
@Service("rechargeRecordServiceimpl")
public class RechargeRecordServiceimpl implements RechargeRecordService{
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Override
    public List<RechargeRecord> queryRechargeRecordTopByUid(Map<String, Object> paramMap) {
        return rechargeRecordMapper.selectRechargeReordByPage(paramMap);
    }

    @Override
    public PaginationVO<RechargeRecord> queryRechargeRecordByPage(Map<String, Object> paramMap) {
        PaginationVO<RechargeRecord> paginationVO = new PaginationVO<>();
        paginationVO.setTotal(rechargeRecordMapper.selectTotal());
        paginationVO.setDataList(rechargeRecordMapper.selectRechargeReordByPage(paramMap));

        return paginationVO;
    }
}
