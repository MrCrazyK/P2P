package com.lanou.service.user;

import com.lanou.model.user.ResultObject;
import com.lanou.model.user.User;

/**
 * ClassName : UserService
 * PackageName : com.lanou.service.user
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/8/17 10:21
 * @Version : 1.0
 */
public interface UserService {
    /**
     * 获取平台注册总人数
     * @return
     */
    Long queryUserCount();

    /**
     * 根据用户手机号查询用户手机
     * @param phone
     * @return
     */
    User queryUserByPhone(String phone);


    /**
     * 用户注册
     * @param phone
     * @param loginPassword
     * @return
     */
    ResultObject register(String phone, String loginPassword);

    /**
     * 根据用户标识更新用户信息
     * @param user
     * @return
     */
    int modifyUserById(User user);

    /**
     * 根据手机号和密码查询用户，更新最近登录时间
     * @param phone
     * @param loginPassword
     */
    User login(String phone, String loginPassword);
}
