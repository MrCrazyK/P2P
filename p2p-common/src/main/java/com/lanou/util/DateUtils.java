package com.lanou.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ClassName : DateUtils
 * PackageName : com.lanou.util
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/6 2:32
 * @Version : 1.0
 */
public class DateUtils {

    /**
     * 通过制定日期添加天数，返回一个日期值
     * @param date
     * @param days
     * @return
     */

    public static Date getDateByAddDays(Date date, Integer days) {
        //创建日期处理类对象
        Calendar calendar = Calendar.getInstance();
        //设置日期处理类对象的时间
        calendar.setTime(date);
        //在日期处理类对象上添加天数
        calendar.add(Calendar.DAY_OF_MONTH,days);
        return calendar.getTime();
    }

    public static void main(String[] args) throws ParseException{
        System.out.println(getDateByAddDays(new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-9"),1));
    }

    public static Date getDateByAddMonths(Date date, Integer months) {
        //创建日期处理类对象
        Calendar calendar = Calendar.getInstance();
        //设置日期处理类对象的时间
        calendar.setTime(date);
        //在日期处理类对象上添加天数
        calendar.add(Calendar.MONTH,months);
        return calendar.getTime();
    }

    /**
     * 获取指定年份天数
     * @param year
     * @return
     */
    public static int getDaysByYear(int year) {
        //默认为365天，平年
        int days = 365;

        //闰年：能被4整除且不能被100整除或者能被400整除
        if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            days = 366;
        }
        return days;
    }

    public static int getDistanceBetweenDates(Date endDate, Date startDate) {
        int distance = (int) ((endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000));
        if((int) ((endDate.getTime() - startDate.getTime()) % (24 * 60 * 60 * 1000)) > 0) {
            distance ++;
        }
        return distance;
    }
}
