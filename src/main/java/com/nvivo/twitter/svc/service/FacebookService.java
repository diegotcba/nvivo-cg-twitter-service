package com.nvivo.twitter.svc.service;

import com.nvivo.twitter.svc.model.TweetPublish;
import com.nvivo.twitter.svc.model.TweetResponse;

import java.util.List;

/**
 * Created by DiegoT on 28/11/2015.
 */
public interface FacebookService {

    public String getCount(String hashtag);
    public String publish(TweetPublish publish);
}
