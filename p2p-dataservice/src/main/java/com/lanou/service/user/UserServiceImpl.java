package com.lanou.service.user;

import com.lanou.common.constant.Constants;
import com.lanou.mapper.user.FinanceAccountMapper;
import com.lanou.mapper.user.UserMapper;
import com.lanou.model.user.FinanceAccount;
import com.lanou.model.user.ResultObject;
import com.lanou.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * ClassName : UserServiceImpl
 * PackageName : com.lanou.service.user
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/8/17 10:26
 * @Version : 1.0
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {


    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    public Long queryUserCount() {
        //修改redis中key值序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //从redis缓存中查询该值，有：直接使用，没有：去数据库查询，并放到redis缓存中

        //获取指定key的操作对象
        BoundValueOperations<Object, Object> boundValueOps = redisTemplate.boundValueOps(Constants.USER_COUNT);

        //从该key的操作对象中获取该可以的对应value
        Long userCount = (Long) boundValueOps.get();

        //判断是否有值
        if (userCount == null) {
            //查询数据
            userCount = userMapper.selectUserCount();
            boundValueOps.set(userCount, 15, TimeUnit.MINUTES);
        }
        return userCount;
    }

    @Override
    public User queryUserByPhone(String phone) {
        return userMapper.selectUserByPhone(phone);
    }

    @Override
    public ResultObject register(String phone, String loginPassword) {
        ResultObject resultObject = new ResultObject();
        resultObject.setErrorCode(Constants.SUCCESS);
        resultObject.setErrorMessage("注册成功");

        //新增用户
        User user = new User();
        user.setPhone(phone);
        user.setLoginPassword(loginPassword);
        user.setAddTime(new Date());
        user.setLastLoginTime(new Date());
        int insertUserCount = userMapper.insertSelective(user);

        if(insertUserCount == 1) {
            //根据手机号查询用户信息
            Integer uid = userMapper.selectUserByPhone(phone).getId();
            //开立账户
            FinanceAccount financeAccount = new FinanceAccount();
            financeAccount.setUid(uid);
            financeAccount.setAvailableMoney(888.0);
            int insertFinaceAcountCount = financeAccountMapper.insertSelective(financeAccount);
            if(insertFinaceAcountCount != 1) {
                resultObject.setErrorCode(Constants.FAIL);
                resultObject.setErrorMessage("注册失败");
            }
        } else {
            resultObject.setErrorCode(Constants.FAIL);
            resultObject.setErrorMessage("注册失败");
        }

        return resultObject;
    }

    @Override
    public int modifyUserById(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User login(String phone, String loginPassword) {
        //根据用户手机号和密码查询用户信息
         User user = userMapper.selectByPhoneAndLoginPassword(phone,loginPassword);
         //判断用户是否存在
         if(null != user) {
            User updateUser = new User();
            updateUser.setId(user.getId());
            updateUser.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKeySelective(updateUser);
         }

         return user;
    }


}
