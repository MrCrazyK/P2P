package com.lanou.service.user;

import com.lanou.model.user.FinanceAccount;

/**
 * ClassName : FinanceAccountService
 * PackageName : com.lanou.service.loan
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/1 15:54
 * @Version : 1.0
 */
public interface FinanceAccountService {
    FinanceAccount queryFinanceAccountByUid(Integer id);
}
