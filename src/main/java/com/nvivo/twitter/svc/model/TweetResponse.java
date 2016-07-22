package com.nvivo.twitter.svc.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created by DiegoT on 28/11/2015.
 */
@Data
public class TweetResponse {
    private Long id;
    private String hashtag;
    private String userName;
    private String fullName;
    private String message;
    private String urlAvatar;
    private String localUrlAvatar;
    private LocalDateTime dateTime;
    private String htmlMessage;
    private boolean isRetweet;
    private List<TweetProfileImage> profileImages;
}
