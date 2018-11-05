package com.lanou.service.loan;

import com.lanou.constant.Constants;
import com.lanou.mapper.loan.BidInfoMapper;
import com.lanou.mapper.loan.LoanInfoMapper;
import com.lanou.mapper.user.FinanceAccountMapper;
import com.lanou.model.loan.BidInfo;
import com.lanou.model.loan.LoanInfo;
import com.lanou.model.user.ResultObject;
import com.lanou.model.vo.PaginationVO;
import com.lanou.model.vo.UserBid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * ClassName : BidInfoServiceImpl
 * PackageName : com.lanou.service.loan
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/8/17 11:33
 * @Version : 1.0
 */
@Service("bidInfoServiceImpl")
public class BidInfoServiceImpl implements BidInfoService{

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public Double queryBidMoneySum() {
        //修改redis中key值序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //获取平台累计投资金额
        Double bidMoneySum = (Double) redisTemplate.opsForValue().get(Constants.BID_MONEY_SUM);

        //判断是否为空
        if (bidMoneySum == null) {
            //去数据库查询
            bidMoneySum = bidInfoMapper.selectBidMoneySum();
            //将查询的结果存放redis缓存当中
            redisTemplate.opsForValue().set(Constants.BID_MONEY_SUM,bidMoneySum,15, TimeUnit.MINUTES);
        }
        return bidMoneySum;
    }

    @Override
    public List<BidInfo> queryBidInfoByLoanId(Integer loanId) {
        return bidInfoMapper.selectBidInfoListByLoanId(loanId);
    }

    @Override
    public List<BidInfo> queryBidInfoByUid(Map<String,Object> paramMap) {

        return bidInfoMapper.selectBidInfoByPage(paramMap);
    }

    @Override
    public PaginationVO<BidInfo> queryBidInfoByPage(Map<String, Object> paramMap) {
        PaginationVO<BidInfo> paginationVO = new PaginationVO<>();

        paginationVO.setDataList(bidInfoMapper.selectBidInfoByPage(paramMap));
        paginationVO.setTotal(bidInfoMapper.selecTotal(paramMap));

        return paginationVO;
    }

    @Override
    public ResultObject invest(Map<String, Object> paramMap) {
        ResultObject resultObject = new ResultObject();
        resultObject.setErrorCode(Constants.SUCCESS);
        resultObject.setErrorMessage("投资成功");

        //更新产品剩余金额
        //超卖现象：实际销售数量超过库存数量
        //解决方案：数据库乐观锁机制
        //获取产品版本号
        LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey((Integer) paramMap.get("loanId"));

        paramMap.put("version",loanInfo.getVersion());

        int updateLeftProductMoneyCount = loanInfoMapper.updateLeftProductMoneyByLoanId(paramMap);
        if(updateLeftProductMoneyCount > 0) {
            //更新账号可用余额
            int updateFinanceCount = financeAccountMapper.updateFinanceAccountByUid(paramMap);
            if(updateFinanceCount > 0) {
                //新增投资记录
                BidInfo bidInfo = new BidInfo();
                bidInfo.setUid((Integer) paramMap.get("uid"));
                bidInfo.setLoanId((Integer) paramMap.get("loanId"));
                bidInfo.setBidMoney((Double) paramMap.get("bidMoney"));
                bidInfo.setBidTime(new Date());
                bidInfo.setBidStatus(1);
                int insertBidCount = bidInfoMapper.insertSelective(bidInfo);
                if(insertBidCount > 0) {
                    loanInfo = loanInfoMapper.selectByPrimaryKey((Integer) paramMap.get("loanId"));
                    //判断产品是否满标
                    if(0 == loanInfo.getLeftProductMoney()) {
                        //更新产品的状态及满标时间
                        loanInfo.setProductStatus(1);
                        loanInfo.setProductFullTime(new Date());
                        int updateLoanInfoCount = loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
                        if(updateLoanInfoCount <= 0) {
                            resultObject.setErrorCode(Constants.FAIL);
                            resultObject.setErrorMessage("投资失败");
                        }
                    }
                    redisTemplate.opsForZSet().incrementScore(Constants.INVEST_TOP,paramMap.get("phone"),(Double) paramMap.get("bidMoney"));

                } else {
                    resultObject.setErrorCode(Constants.FAIL);
                    resultObject.setErrorMessage("投资失败");
                }
            } else {
                resultObject.setErrorCode(Constants.FAIL);
                resultObject.setErrorMessage("投资失败");
            }
        } else {
            resultObject.setErrorCode(Constants.FAIL);
            resultObject.setErrorMessage("投资失败");
        }

        return resultObject;
    }

    @Override
    public List<UserBid> queryUserBidTop() {
        List<UserBid> userBidList = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(Constants.INVEST_TOP,0,9);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();
        while(iterator.hasNext()) {
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            UserBid userBid = new UserBid();
            userBid.setPhone((String) next.getValue());
            userBid.setScore(next.getScore());
            userBidList.add(userBid);
        }
        return userBidList;
    }
}
