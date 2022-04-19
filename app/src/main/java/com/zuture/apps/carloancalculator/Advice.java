package com.zuture.apps.carloancalculator;

public class Advice {

    private String title;
    private  String desc;
    private int  imageRsc;

    public  Advice(String title, String desc, int imageRsc){
        this.title=title;
        this.desc= desc;
        this.imageRsc= imageRsc;

    }


    public void setImageRscId(int imageRscId) {
        this.imageRsc= imageRscId;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public int getImageRsc() {
        return imageRsc;
    }
}
