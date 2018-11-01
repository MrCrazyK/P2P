package com.lanou.service.loan;

import com.lanou.model.loan.BidInfo;
import com.lanou.model.user.ResultObject;
import com.lanou.model.vo.PaginationVO;
import com.lanou.model.vo.UserBid;

import java.util.List;
import java.util.Map;

/**
 * ClassName : BidInfoService
 * PackageName : com.lanou.service.loan
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/8/17 11:32
 * @Version : 1.0
 */
public interface BidInfoService {

    /**
     * 获取平台累计投资金额
     * @return
     */
    Double queryBidMoneySum();

    /**
     * 根据产品标识获取该产品的所有投资记录（包含用户信息）
     * @param id
     * @return
     */
    List<BidInfo> queryBidInfoByLoanId(Integer loanId);


    List<BidInfo> queryBidInfoByUid(Map<String,Object> paramMap);

    PaginationVO<BidInfo> queryBidInfoByPage(Map<String, Object> paramMap);

    ResultObject invest(Map<String, Object> paramMap);

    List<UserBid> queryUserBidTop();
}
