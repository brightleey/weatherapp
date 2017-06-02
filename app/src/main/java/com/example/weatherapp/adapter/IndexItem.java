package com.example.weatherapp.adapter;

/**
 * Created by brightleey on 2017/5/21.
 */

public class IndexItem {
    private String indexName;
    private String txt;
    private int imageId;
    private String desc;

    public IndexItem(String indexName, String shortDesc, String detail, int imageId){
        this.indexName = indexName;
        this.txt = shortDesc;
        this.desc = detail;
        this.imageId = imageId;
    }
    public void setIndexName(String indexName){
        this.indexName = indexName;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIndexName(){
        return indexName;
    }

    public String getTxt() {
        return txt;
    }

    public int getImageId() {
        return imageId;
    }

    public String getDesc() {
        return desc;
    }
}
