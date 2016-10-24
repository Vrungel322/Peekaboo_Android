package com.peekaboo.presentation.pojo;

import android.support.annotation.Nullable;

/**
 * Created by Nikita on 10.10.2016.
 */
public class PhoneContactPOJO {
    private String name;
    private String phone;
    @Nullable
    private String photoThumbnail;

    public PhoneContactPOJO(String name, String phone, String photoThumbnail) {
        this.name = name;
        this.phone = phone;
        this.photoThumbnail = photoThumbnail;
    }

    @Nullable
    public String getPhotoThumbnail() {
        return photoThumbnail;
    }

    public void setPhotoThumbnail(@Nullable String photoThumbnail) {
        this.photoThumbnail = photoThumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneContactPOJO that = (PhoneContactPOJO) o;

        return phone != null ? phone.equals(that.phone) : that.phone == null;

    }

    @Override
    public int hashCode() {
        return phone != null ? phone.hashCode() : 0;
    }
}
