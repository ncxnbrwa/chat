package com.yxf.chat.home.bean;

/**
 * 获取Token返回数据实体类
 *
 * @author xiong
 * @name TokenBean
 * @date 2017/9/9
 */
public class TokenBean {

    /**
     * code : 200
     * userId : 12306
     * token : VMU8qYs+VMRUgpOLeYQ+0nQ7IcEQkssgzpDXBO4mDGx8heMUxZBui3qFBcpgKdjTHvncy0UZ3i5x2vCH
     * +IakFg==
     */

    //返回码
    private int code;
    //用户Token
    private String userId;
    //用户ID
    private String token;

    public TokenBean() {
    }

    public TokenBean(int code, String userId, String token) {
        this.code = code;
        this.userId = userId;
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "TokenBean{" +
                "code=" + code +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
