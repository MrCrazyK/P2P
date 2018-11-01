package com.lanou.config;

/**
 * ClassName : Config
 * PackageName : com.lanou.config
 * Description :
 *
 * @Autor : Administrator
 * @Date : 2018/8/20 23:12
 * @Version : 1.0
 */
public class Config {

    /**
     * 实名认证的appkey
     */
    private String realNameAppKey;

    /**
     * 实名认证的URL
     */
    private String realNameUrl;

    public String getRealNameAppKey() {
        return realNameAppKey;
    }

    public void setRealNameAppKey(String realNameAppKey) {
        this.realNameAppKey = realNameAppKey;
    }

    public String getRealNameUrl() {
        return realNameUrl;
    }

    public void setRealNameUrl(String realNameUrl) {
        this.realNameUrl = realNameUrl;
    }
}
