package com.nvivo.twitter.svc.dao;

import com.nvivo.twitter.svc.model.TweetPublish;
import twitter4j.QueryResult;
import twitter4j.Status;

import java.util.List;

/**
 * Created by DiegoT on 28/11/2015.
 */
public interface FacebookApi {
    public String publish(TweetPublish publish);
}
