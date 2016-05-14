package com.nvivo.twitter.svc.model;

import lombok.Data;

/**
 * Created by DiegoT on 27/04/2016.
 */
@Data
public class TweetQuery {
    private String hashtag;
    private String user;
    private String startDateTime;
    private String endDateTime;
    private String location;
}
