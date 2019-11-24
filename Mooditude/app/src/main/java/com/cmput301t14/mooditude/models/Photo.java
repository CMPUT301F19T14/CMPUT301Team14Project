package com.cmput301t14.mooditude.models;

public class Photo {
    private String mImageUrl;
    public Photo(){

    }
    public Photo(String imageUrl){
        this.mImageUrl = imageUrl;

    }

    public String getmImageUrl(){
        return mImageUrl;
    }

    public void setmImageUrl(String imageUrl){
        mImageUrl = imageUrl;
    }
}
