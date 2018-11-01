package com.lanou.service.loan;

import com.lanou.model.loan.LoanInfo;
import com.lanou.model.vo.PaginationVO;

import java.util.List;
import java.util.Map; /**
 * ClassName : LoanInfoService
 * PackageName : com.lanou.service.loan
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/8/15 22:40
 * @Version : 1.0
 */
public interface LoanInfoService {

    /**
     * 获取历史平均年化收益率
     * @return
     */
    Double queryHistoricalAverageRate();

    /**
     * 根据产品类型查询产品信息列表
     * @param paramMap
     * @return
     */
    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap);

    /**
     * 分页查询产品信息列表
     * @param paramMap
     * @return
     */
    PaginationVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap);

    /**
     * 根据产品标识获取产品信息详情
     * @param id
     * @return
     */
    LoanInfo queryLoanInfoById(Integer id);
}
