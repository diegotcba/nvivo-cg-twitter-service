package com.nvivo.twitter.svc.model;

import lombok.Data;

/**
 * Created by DiegoT on 28/11/2015.
 */
@Data
public class TweetResource {
    private String id;
    private String hashtag;
    private String userName;
    private String fullName;
    private String message;
    private String urlAvatar;
    private String localUrlAvatar;
    private String dateTime;
    private String htmlMessage;
    private boolean isRetweet;
}
