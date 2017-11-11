package com.yxf.chat.home.bean;

/**
 * Created by xiong on 2017/9/9.
 */

public class TokenBody {

    private String userId;
    private String name;
    private String portraitUri;

    public TokenBody(String userId, String name, String portraitUri) {
        this.userId = userId;
        this.name = name;
        this.portraitUri = portraitUri;
    }

    public TokenBody() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    @Override
    public String toString() {
        return "TokenBody{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", portraitUri='" + portraitUri + '\'' +
                '}';
    }
}
