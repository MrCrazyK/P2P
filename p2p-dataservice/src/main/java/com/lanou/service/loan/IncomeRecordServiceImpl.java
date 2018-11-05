package com.lanou.service.loan;

import com.lanou.constant.Constants;
import com.lanou.mapper.loan.BidInfoMapper;
import com.lanou.mapper.loan.IncomeRecordMapper;
import com.lanou.mapper.loan.LoanInfoMapper;
import com.lanou.mapper.user.FinanceAccountMapper;
import com.lanou.model.loan.BidInfo;
import com.lanou.model.loan.IncomeRecord;
import com.lanou.model.loan.LoanInfo;
import com.lanou.model.vo.PaginationVO;
import com.lanou.util.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * ClassName : IncomeRecordServiceImpl
 * PackageName : com.lanou.service.loan
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/1 20:52
 * @Version : 1.0
 */
@Service("incomeRecordServiceImpl")
public class IncomeRecordServiceImpl implements IncomeRecordService{
    private Logger logger = Logger.getLogger(IncomeRecordServiceImpl.class);

    @Autowired
    private IncomeRecordMapper incomeRecordMapper;

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public List<IncomeRecord> queryIncomeRecordTopByUid(Map<String, Object> paramMap) {
        return incomeRecordMapper.selectIncomeRecordByPage(paramMap);
    }

    @Override
    public PaginationVO<IncomeRecord> queryIncomeRecordByPage(Map<String, Object> paramMap) {
        PaginationVO<IncomeRecord> paginationVO = new PaginationVO<>();
        paginationVO.setDataList(incomeRecordMapper.selectIncomeRecordByPage(paramMap));
        paginationVO.setTotal(incomeRecordMapper.selectTotal(paramMap));

        return paginationVO;
    }

    @Override
    public void generateIncomePlan() {
        //获取到已满标的产品 -> 返回List<产品列表>
        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoByProductStatus(1);

        //循环遍历每一个产品，获取该产品的所有投资记录 -> 返回List<投资记录列表>
        for (LoanInfo loanInfo : loanInfoList) {
            Integer productType = loanInfo.getProductType();
            Date productFullTime = loanInfo.getProductFullTime();
            Integer cycle = loanInfo.getCycle();
            Double rate = loanInfo.getRate();

            //获取该产品的投资记录
            List<BidInfo> bidInfoList = bidInfoMapper.selectBidInfoByLoanId(loanInfo.getId());

            //循环遍历每一条投资记录，将它生成对应的收益记录
            for (BidInfo bidInfo: bidInfoList) {
                //生成收益记录
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setUid(bidInfo.getUid());//用户标识
                incomeRecord.setLoanId(loanInfo.getId());//投资产品标识
                incomeRecord.setBidId(bidInfo.getId());//投资记录标识
                incomeRecord.setBidMoney(bidInfo.getBidMoney());//投资金额
                incomeRecord.setIncomeStatus(0);//投资状态：0：未返还
                //收益时间（Date） = 满标时间 （Date）+ 产品周期（Integer）（单位分类：天【新手宝】，月【优选，散标】）
                Date incomeDate = null;

                //收益金额 = 投资金额 * 天利率 * 投资天数
                double incomeMoney ;

                //判断产品类型
                if(Constants.PRODUCT_TYPE_X == productType) {
                    //新手宝
                    incomeDate = DateUtils.getDateByAddDays(productFullTime,cycle);
                    incomeMoney = bidInfo.getBidMoney() * (rate / 100 / DateUtils.getDaysByYear(Calendar.getInstance().YEAR)) * cycle;
                    incomeMoney = Math.round(incomeMoney * Math.pow(10,2) / Math.pow(10,2));
                }
                    else {
                    //优选或散标
                    incomeDate = DateUtils.getDateByAddMonths(productFullTime,cycle);
                    incomeMoney = bidInfo.getBidMoney() * (rate / 100 / DateUtils.getDaysByYear(Calendar.getInstance().YEAR)) * cycle * DateUtils.getDistanceBetweenDates(incomeDate,productFullTime);
                    incomeMoney = Math.round(incomeMoney * Math.pow(10,2) / Math.pow(10,2));
                }

                incomeMoney = Math.round(incomeMoney * Math.pow(10,2) / Math.pow(10,2));
                incomeRecord.setIncomeDate(incomeDate);
                incomeRecord.setIncomeMoney(incomeMoney);

                int insertCount = incomeRecordMapper.insertSelective(incomeRecord);

                if(insertCount > 0) {
                    logger.info("用户标识为：" + bidInfo.getUid() + ",投资记录标识为：" + bidInfo.getId() + ",生成收益计划成功。");
                } else {
                    logger.info("用户标识为：" + bidInfo.getUid() + ",投资记录标识为：" + bidInfo.getId() + ",生成收益计划失败。");
                }

            }
            //将当前产品的状态更新为2满标且生成收益计划
            LoanInfo updateLoanInfo = new LoanInfo();
            updateLoanInfo.setId(loanInfo.getId());
            updateLoanInfo.setProductStatus(2);
            int updateCount = loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);
            if(updateCount > 0) {
                logger.info("产品标识为" + loanInfo.getId() + ",状态更新成功。");
            } else {
                logger.info("产品标识为" + loanInfo.getId() + ",状态更新失败。");
            }
        }
    }

    @Override
    public void generateIncomeBack() {
        //查询收益记录状态为0且收益时间与当前时间相同的收益记录
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectIncomeRecordByIncomeStatusAndIncomeDate(0);
        //循环遍历收益记录，将收益记录返还给对应的用户
        for (IncomeRecord incomeRecord : incomeRecordList) {
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("uid",incomeRecord.getUid());
            paramMap.put("incomeMoney",incomeRecord.getIncomeMoney());
            paramMap.put("bidMoney",incomeRecord.getBidMoney());
            //更新用户可用余额
            int updateCount = financeAccountMapper.updateFinaceAccountByIncomeBack(paramMap);

            if(updateCount > 0) {
                //更新当前收益状态为1
                IncomeRecord updateIncomeRecord = new IncomeRecord();
                updateIncomeRecord.setId(incomeRecord.getId());
                updateIncomeRecord.setIncomeStatus(1);
                int updateIncomeCount = incomeRecordMapper.updateByPrimaryKeySelective(updateIncomeRecord);
                if(updateIncomeCount <= 0) {
                    logger.info("收益标识为" + incomeRecord.getId() + "，更新失败");
                }
            } else {
                logger.info("收益标识为" + incomeRecord.getId() + "，更新账户余额失败");
            }
        }
        }


    }

