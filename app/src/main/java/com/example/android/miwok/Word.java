package com.example.android.miwok;

import android.media.Image;

public class Word {

    //Miwok Translation
    private String mMiwokTranslation;

    //Default Translation
    private String mDefaultTranslation;

    //Image associated with the word
    private  int mImageResourceId;

    //Audio associated with the word
    private  int mAudioResourceId;

    // Public Constructor
    public Word(String miwokTranslation, String defaultTrasnslation, int audioResourceId){
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTrasnslation;
        mAudioResourceId = audioResourceId;
    }


    // Public Constructor considering audio input file
    public Word(String miwokTranslation, String defaultTrasnslation, int imageResourceId, int audioResourceId){
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTrasnslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }

    //method for obtaining default translation
    public String getDefaultTranslation(){
        return mDefaultTranslation;
    }

    //method for obtaining miwok translation
    public String getMiwokTranslation(){
        return mMiwokTranslation;
    }

    //method for obtaining image resource ID
    public int getImageResourceId(){
        return mImageResourceId;
    }

    //method for obtaining image resource ID
    public int getAudioResourceId(){
        return mAudioResourceId;
    }

    //toString method for logging


    @Override
    public String toString() {
        return "Word{" +
                "mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mImageResourceId=" + mImageResourceId +
                ", mAudioResourceId=" + mAudioResourceId +
                '}';
    }
}
