package com.anim.chart;

/**
 * Created by zhanghao on 2016/12/2.
 */

public class HistogramBean {
    private String title;
    private String intro;
    private float num;

    public HistogramBean() {

    }

    public HistogramBean(float num) {
        this.num = num;
    }

    public HistogramBean(String title, float num) {
        this.title = title;
        this.num = num;
    }

    public HistogramBean(String title, String intro, float num) {
        this.title = title;
        this.intro = intro;
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }
}
