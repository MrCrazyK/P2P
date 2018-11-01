package com.lanou.service.user;

import com.lanou.mapper.user.FinanceAccountMapper;
import com.lanou.model.user.FinanceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName : FinaceAccountServiceImpl
 * PackageName : com.lanou.service.user
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/1 16:34
 * @Version : 1.0
 */
@Service("financeAccountServiceImpl")
public class FinanceAccountServiceImpl implements FinanceAccountService{
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public FinanceAccount queryFinanceAccountByUid(Integer id) {
        return financeAccountMapper.selectByUid(id);
    }
}
