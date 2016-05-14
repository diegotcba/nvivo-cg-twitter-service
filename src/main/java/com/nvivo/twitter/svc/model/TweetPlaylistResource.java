package com.nvivo.twitter.svc.model;

import lombok.Data;

import java.util.List;

/**
 * Created by DiegoT on 13/05/2016.
 */
@Data
public class TweetPlaylistResource {
    private long id;
    private String dateTime;
    private String title;
    private String description;
    private List<TweetResource> tweets;
}
