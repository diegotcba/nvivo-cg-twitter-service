package com.nvivo.twitter.svc.rest.impl;

import com.nvivo.twitter.svc.comverter.TweetResourceConverter;
import com.nvivo.twitter.svc.model.*;
import com.nvivo.twitter.svc.rest.RestTwitterService;
import com.nvivo.twitter.svc.service.FacebookService;
import com.nvivo.twitter.svc.service.TwitterService;

import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;

/**
 * Created by DiegoT on 28/11/2015.
 */
public class RestTwitterServiceImpl implements RestTwitterService {
    private TwitterService service;

    private FacebookService facebookService;

    @Override
    public String getStatus() {
        return "The service is up and running!!!\n" + service.dbStatus();
    }

    @Override
    public String publish(TweetPublish publish) {
        return service.publish(publish);
    }

    @Override
    public String publishFacebook(TweetPublish publish) {
        return facebookService.publish(publish);
    }

    @Override
    public String publishSocial(TweetPublish publish) {
        String response;
        response = service.publish(publish);
        response += " " + facebookService.publish(publish);
        return response;
    }

    @Override
    public String getCount(String hashtag) {
        return service.getCount("");
    }

    @Override
    public List<TweetResource> getTweets() {
        return TweetResourceConverter.convert(service.getTweets());
    }

    @Override
    public List<TweetResource> getTweets(TweetQuery request) {
        return TweetResourceConverter.convert(service.getTweets(request));
    }

    @Override
    public TweetResource getTweet(String tweetId) {
        return TweetResourceConverter.convert(service.getTweet(tweetId));
    }

    @Override
    public List<TweetPlaylistResource> getPlaylists() {
        return service.getPlaylists();
    }

    @Override
    public Response createPlaylist(List<TweetResource> playlist) {
        return Response.ok(service.createPlaylist(playlist)).build();
    }

    @Override
    public TweetPlaylistResource getPlaylist(int playlistId) {
        return service.getPlaylist(playlistId);
    }

    @Override
    public Response removePlaylist(int playlistId) {
        service.removePlaylist(playlistId);
        return Response.noContent().build();
    }

    @Override
    public Response getMediaFile(String fileName) {
        File file = new File(fileName);
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        return response.build();
    }

    @Override
    public Response getStreamCount() {
        return Response.status(Response.Status.OK).entity(String.valueOf(service.streamCount())).build();
    }

    @Override
    public void startStream(String hashtag) {
        service.startStream(hashtag);
    }

    @Override
    public void stopStream() {
        service.stopStream();
    }

    @Override
    public List<TweetResource> getStreamTweets() {
        return TweetResourceConverter.convert(service.getStream());
    }

    @Override
    public void clearStreamTweets() {
        service.clearStream();
    }

    @Override
    public Response startPoll(TwitterPollRequest request) {
        service.startPoll(request);
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public void stopPoll() {
        service.stopPoll();
    }

    @Override
    public void clearPoll() {
        service.clearPoll();
    }

    @Override
    public Response getPollResult() {
        return Response.status(Response.Status.OK).entity(service.getPollResult()).build();
    }

    public void setService(TwitterService service) {
        this.service = service;
    }

    public void setFacebookService(FacebookService facebookService) {
        this.facebookService = facebookService;
    }

}
