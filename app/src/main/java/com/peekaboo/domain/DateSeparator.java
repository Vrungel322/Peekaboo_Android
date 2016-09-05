package com.peekaboo.domain;

/**
 * Created by Nataliia on 05.09.2016.
 */

public class DateSeparator {
    private String date;
    private int mediaType;
    public static final int MEDIA_TYPE = 99;

    public DateSeparator(String date){
        this.date = date;
        this.mediaType = MEDIA_TYPE;
    }

    public String getDate(){ return  date;}

    public int getMediaType(){return mediaType; }

}
