package com.yxf.chat.userPair;

/**
 * 用户配对实体类
 *
 * @author xiong
 * @name PairUserBean
 * @date 2017/9/6
 */
public class PairUserBean {

    private String urlIcon;
    private String name;
    private String describe;

    public PairUserBean(String urlIcon, String name, String describe) {
        this.urlIcon = urlIcon;
        this.name = name;
        this.describe = describe;
    }

    public String getUrlIcon() {

        return urlIcon;
    }

    public void setUrlIcon(String urlIcon) {
        this.urlIcon = urlIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
