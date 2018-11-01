package com.lanou.test;

import com.lanou.model.user.ResultObject;
import com.lanou.service.loan.BidInfoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * ClassName : MyTest
 * PackageName : com.lanou.test
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/3 16:32
 * @Version : 1.0
 */
public class MyTest {
    public static void main(String[] args) {
        //获取spring容器
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        //获取指定bean
        BidInfoService bidInfoService = (BidInfoService) context.getBean("bidInfoServiceImpl");
        //投资参数
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("uid",32);
        paramMap.put("bidMoney",100);
        paramMap.put("loanId",14);

        //创建一个固定的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        for (int i = 0;i < 100;i ++) {
            executorService.submit(new Callable<ResultObject>() {

                @Override
                public ResultObject call() throws Exception {
                    return bidInfoService.invest(paramMap);
                }
            });
        }

        executorService.shutdown();
    }
}
