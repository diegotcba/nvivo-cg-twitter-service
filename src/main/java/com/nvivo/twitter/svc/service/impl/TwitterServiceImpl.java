package com.nvivo.twitter.svc.service.impl;

import com.nvivo.twitter.svc.comverter.TweetResponseConverter;
import com.nvivo.twitter.svc.dao.TwitterApi;
import com.nvivo.twitter.svc.dao.TwitterDao;
import com.nvivo.twitter.svc.model.*;
import com.nvivo.twitter.svc.service.MediaDownloader;
import com.nvivo.twitter.svc.service.TwitterService;
import org.apache.log4j.Logger;
import twitter4j.QueryResult;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.time.LocalDateTime.parse;

/**
 * Created by DiegoT on 28/11/2015.
 */
public class TwitterServiceImpl implements TwitterService {
    private TwitterApi apiService;
    private TwitterDao daoService;
    private List<TweetResponse> tweets;

    private static Logger LOGGER = Logger.getLogger(TwitterServiceImpl.class);

    @Override
    public String getCount(String hashtag) {
        QueryResult results = apiService.getTweets();
        return String.valueOf(results.getCount());
    }

    @Override
    public TweetResponse getTweet(String id) {
        return TweetResponseConverter.convert(apiService.getTweet(id));
    }

    @Override
    public List<TweetResponse> getTweets() {
        QueryResult result = apiService.getTweets();

        List<Status> statuses = result.getTweets();

        if (tweets == null || tweets.size() == 0) {
            tweets = new ArrayList<>();
            tweets = TweetResponseConverter.convert(statuses);
        }

        return tweets;
    }

    @Override
    public List<TweetResponse> getTweets(TweetQuery query) {
        QueryResult result = apiService.getTweets(query);

        List<Status> statuses = result.getTweets();

        if (tweets == null || tweets.size() == 0) {
            tweets = new ArrayList<>();
            tweets = TweetResponseConverter.convert(statuses);
        }

        return tweets;
    }

    @Override
    public void startStream(String hashtag) {
        String[] aux = {hashtag};
        apiService.startTweeterStream(aux);
    }

    @Override
    public void stopStream() {
        apiService.stopTwitterStream();
    }

    @Override
    public List<TweetResponse> getStream() {
        return TweetResponseConverter.convert(apiService.getTwitterStream());
    }

    @Override
    public void clearStream() {
        apiService.clearTwitterStream();
    }

    @Override
    public long streamCount() {
        return apiService.streamCount();
    }

    @Override
    public String publish(TweetPublish publish) {
        return apiService.publish(publish);
    }

    @Override
    public void startPoll(TwitterPollRequest poll) {
        apiService.startPoll(poll);
    }

    @Override
    public void stopPoll() {
        apiService.stopPoll();
    }

    @Override
    public void clearPoll() {
        apiService.clearPoll();
    }

    @Override
    public TwitterPollResponse getPollResult() {
        return apiService.getPollResult();
    }

    @Override
    public TweetPlaylistResource getPlaylist(long playlistId) {
        return daoService.getPlaylist(playlistId);
    }

    @Override
    public List<TweetPlaylistResource> getPlaylists() {
        return daoService.getPlaylists();
    }

    @Override
    public TweetPlaylistResource createPlaylist(List<TweetResource> tweetPlaylist) {
        TweetPlaylistResource result = daoService.addPlaylist(tweetPlaylist);
        //TODO: add downloading media code
        //TODO: add update call to db for local urls
        downloadMediasByUrl(tweetPlaylist);

        return result;
    }

    private void downloadMediasByUrl(List<TweetResource> tweetResources) {
        List<MediaDownloadJob> jobList = new ArrayList<>();

        tweetResources.stream().forEach(tweetResource -> {
            MediaDownloadJob mediaJob = new MediaDownloadJob();

            mediaJob.setMediaId(Long.valueOf(tweetResource.getId()));
            mediaJob.setMediaName("avatar");
            mediaJob.setSourceMediaUrl(tweetResource.getUrlAvatar());

            jobList.add(mediaJob);
        });

        mediaJob(jobList);
    }
    private void mediaJob(List<MediaDownloadJob> mediaJobs) {
        //TODO: create a runnable class fir the downloading, write files and update db
        ExecutorService executor = Executors.newSingleThreadExecutor();
        MediaDownloader task = new MediaDownloader();
        task.setDaoService(daoService);
        task.setMediaJob(mediaJobs);

        LOGGER.trace("Iniciando tarea de descarga medias.");
        executor.submit(task);
    }

    @Override
    public void removePlaylist(long playlistId) {
        daoService.removePlaylist(playlistId);
    }

    @Override
    public String dbStatus() {
        return daoService.getDBStatus();
    }

    public void setApiService(TwitterApi apiService) {
        this.apiService = apiService;
    }

    public void setDaoService(TwitterDao daoService) {
        this.daoService = daoService;
    }
}
