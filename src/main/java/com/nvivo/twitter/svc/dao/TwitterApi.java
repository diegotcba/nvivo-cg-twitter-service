package com.nvivo.twitter.svc.dao;

import com.nvivo.twitter.svc.model.TweetPublish;
import com.nvivo.twitter.svc.model.TweetQuery;
import com.nvivo.twitter.svc.model.TwitterPollRequest;
import com.nvivo.twitter.svc.model.TwitterPollResponse;
import twitter4j.QueryResult;
import twitter4j.Status;

import java.util.List;

/**
 * Created by DiegoT on 28/11/2015.
 */
public interface TwitterApi {
    public Status getTweet(String id);
    public QueryResult getTweets();
    public QueryResult getTweets(TweetQuery query);
    public void startTweeterStream(String[] hashtag);
    public void stopTwitterStream();
    public List<Status> getTwitterStream();
    public void clearTwitterStream();
    public long streamCount();
    public String publish(TweetPublish publish);
    public void startPoll(TwitterPollRequest poll);
    public void stopPoll();
    public void clearPoll();
    public TwitterPollResponse getPollResult();

}
