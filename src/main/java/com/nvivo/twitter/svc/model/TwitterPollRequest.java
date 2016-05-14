package com.nvivo.twitter.svc.model;

import lombok.Data;

import java.util.List;

/**
 * Created by DiegoT on 10/01/2016.
 */
@Data
public class TwitterPollRequest {
    private String title;
    private List<TwitterPollHashtag> hashtags;
}
