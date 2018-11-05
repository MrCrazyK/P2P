package com.lanou.service.loan;

import com.lanou.constant.Constants;
import com.lanou.mapper.loan.LoanInfoMapper;
import com.lanou.model.loan.LoanInfo;
import com.lanou.model.vo.PaginationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName : LoanInfoServiceImpl
 * PackageName : com.lanou.service.loan
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/8/15 22:48
 * @Version : 1.0
 */
@Service("loanInfoServiceImpl")
public class LoanInfoServiceImpl implements LoanInfoService {

    //注入dao，如果redis缓存中没有，还是需要到数据库中查询
    @Autowired
    private LoanInfoMapper loanInfoMapper;

    //使用redis
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public Double queryHistoricalAverageRate() {
        //修改redis中key值序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //首先去redis查询，判断是否有值，有：直接使用，没有：去数据库查询并存放到redis缓存中，设置失效时间
        //好处：减少对数据库的访问，提升系统性能

        //从redis缓存中获取该值
        Double historicalAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORICAL_AVERAGE_RATE);

        //判断是否为空
        if (historicalAverageRate == null) {
            //为空去数据库查询
            historicalAverageRate = loanInfoMapper.selectHistoricalAverageRate();
            //放到redis缓存中
            redisTemplate.opsForValue().set(Constants.HISTORICAL_AVERAGE_RATE,historicalAverageRate,15, TimeUnit.MINUTES);

        }
        return historicalAverageRate;
    }

    /**
     *分页查询产品信息列表
     * @param paramMap
     * @return
     */
    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap) {
        return loanInfoMapper.selectLoanfoByPage(paramMap);
    }

    @Override
    public PaginationVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap) {
        PaginationVO<LoanInfo> paginationVO = new PaginationVO<>();

        paginationVO.setTotal(loanInfoMapper.selectTotal(paramMap));
        paginationVO.setDataList(loanInfoMapper.selectLoanfoByPage(paramMap));

        return paginationVO;

    }

    @Override
    public LoanInfo queryLoanInfoById(Integer id) {
        return loanInfoMapper.selectByPrimaryKey(id);
    }

}
