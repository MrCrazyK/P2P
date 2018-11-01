package com.lanou.model.vo;

import java.io.Serializable;

/**
 * ClassName : UserBid
 * PackageName : com.lanou.model.vo
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/10/3 22:54
 * @Version : 1.0
 */
public class UserBid implements Serializable{

    /**
     * 投资人手机号
     */
    private String phone;

    /**
     * 分数：用户累计投资金额
     */
    private Double score;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
