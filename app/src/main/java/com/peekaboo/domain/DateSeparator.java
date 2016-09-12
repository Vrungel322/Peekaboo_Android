package com.peekaboo.domain;

/**
<<<<<<< Updated upstream
 * Created by Nataliia on 05.09.2016.
=======
<<<<<<< HEAD
 * Created by st1ch on 05.09.2016.
>>>>>>> Stashed changes
 */

public class DateSeparator {
    private String date;
    private int mediaType;
    public static final int MEDIA_TYPE = 99;

<<<<<<< Updated upstream
    public DateSeparator(String date){
        this.date = date;
        this.mediaType = MEDIA_TYPE;
    }

    public String getDate(){ return  date;}
=======
    public DateSeparator(String date) {
        this.date = date;
        mediaType = MEDIA_TYPE;
    }

    public String getDate() {
        return date;
    }
>>>>>>> Stashed changes

    public int getMediaType(){return mediaType; }

}
