package com.example.graduatesystem.entities;

import android.graphics.Bitmap;

import java.util.Date;

import javax.annotation.Nullable;

public class Post {
    public static final String AUTHOR_ID = "author";
    public static final String TEXT = "text";
    public static final String POSTED_AT = "postedAt";
    public static final String VALID_UNTIL = "validUntil";

    public static final int TEXT_TYPE = 0;
    public static final int IMAGE_TYPE = 1;

    public int type;
    private String authorId;
    private String text;
    private Date postedAt;
    private Date validUntil;
    @Nullable
    private String authorFullName;
    @Nullable
    private Bitmap authorProfilePicture;
    @Nullable
    private Bitmap postPicture;

    public Post() {
    }

    public Post(int type, String authorId, String text, Date postedAt, Date validUntil) {
        this.type = type;
        this.authorId = authorId;
        this.text = text;
        this.postedAt = postedAt;
        this.validUntil = validUntil;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    @Nullable
    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(@Nullable String authorFullName) {
        this.authorFullName = authorFullName;
    }

    @Nullable
    public Bitmap getAuthorProfilePicture() {
        return authorProfilePicture;
    }

    public void setAuthorProfilePicture(@Nullable Bitmap authorProfilePicture) {
        this.authorProfilePicture = authorProfilePicture;
    }

    @Nullable
    public Bitmap getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(@Nullable Bitmap postPicture) {
        this.postPicture = postPicture;
    }
}
