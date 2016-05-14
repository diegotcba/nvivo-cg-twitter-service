package com.nvivo.twitter.svc.service;

import com.nvivo.twitter.svc.model.*;

import java.util.List;

/**
 * Created by DiegoT on 28/11/2015.
 */
public interface TwitterService {

    public String getCount(String hashtag);
    public TweetResponse getTweet(String id);
    public List<TweetResponse> getTweets();
    public List<TweetResponse> getTweets(TweetQuery query);
    public void startStream(String hashtag);
    public void stopStream();
    public List<TweetResponse> getStream();
    public void clearStream();
    public long streamCount();
    public String publish(TweetPublish publish);
    public void startPoll(TwitterPollRequest poll);
    public void stopPoll();
    public void clearPoll();
    public TwitterPollResponse getPollResult();
    public TweetPlaylistResource getPlaylist(long playlistId);
    public List<TweetPlaylistResource> getPlaylists();
    public TweetPlaylistResource createPlaylist(List<TweetResource> tweetPlaylist);
    public void removePlaylist(long playlistId);
    public  String dbStatus();
}
