package com.yxf.chat.home;

import java.util.ArrayList;
import java.util.List;

/**
 * 卡片数据实体类
 *
 * @author xiong
 * @name CardDataBean
 * @date 2017/8/19
 */
public class CardDataBean {
    //图片Url集合
    private List<String> urlList;
    //名字
    private String name;
    //生日
    private String birthday;
    //性别
    private String sex;
    //职业
    private String profession;

    public CardDataBean() {
    }

    public CardDataBean(List<String> urlList, String name, String birthday, String sex, String
            profession) {
        this.urlList = urlList;
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
        this.profession = profession;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Override
    public String toString() {
        return "CardDataBean{" +
                "urlList=" + urlList +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", sex='" + sex + '\'' +
                ", profession='" + profession + '\'' +
                '}';
    }

    public static List<CardDataBean> initDatas() {
        List<CardDataBean> dataList = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        urls.add("http://i1.cfimg.com/591992/a7b875b9ee1344d0.jpg");
        urls.add("http://i4.cfimg.com/591992/c9361f410b35cd4c.png");
        urls.add("http://i4.cfimg.com/591992/5c9d04585402a224.jpg");
        urls.add("http://i4.cfimg.com/591992/2bb13517d53df406.jpg");
        urls.add("http://i4.cfimg.com/591992/3390f273d9a03833.jpg");
        dataList.add(new CardDataBean(urls, "孙允珠", "1996-01-21", "女", "歌手"));
        dataList.add(new CardDataBean(urls, "黄晓明", "1997-12-19", "男", "水军"));
        dataList.add(new CardDataBean(urls, "宋慧乔", "1995-05-24", "女", "明星"));
        dataList.add(new CardDataBean(urls, "新垣结衣", "1988-06-11", "女", "医生"));
        dataList.add(new CardDataBean(urls, "郑秀晶", "1994-08-29", "女", "模特"));
        return dataList;
    }
}
