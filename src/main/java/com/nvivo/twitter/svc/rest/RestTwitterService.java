package com.nvivo.twitter.svc.rest;

import com.nvivo.twitter.svc.model.*;
import org.apache.cxf.jaxrs.ext.Nullable;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by DiegoT on 28/11/2015.
 */
@Path("/")
@Consumes("application/json")
@Produces("application/json")
public interface RestTwitterService {
    @GET
    @Path("/status/")
    @Produces("text/plain")
    public String getStatus();

    @POST
    @Path("/publish-tweet/")
    public String publish(TweetPublish publish);

    @POST
    @Path("/publish-facebook/")
    public String publishFacebook(TweetPublish publish);

    @POST
    @Path("/publish-social/")
    public String publishSocial(TweetPublish publish);

    @GET
    @Path("/tweets/count")
    public String getCount(@QueryParam("hashtag") String hashtag);

    @GET
    @Path("/tweets/")
    public List<TweetResource> getTweets();

    @POST
    @Path("/tweets/")
    public  List<TweetResource> getTweets(TweetQuery request);

    @GET
    @Path("/tweets/{tweetId}/")
    public TweetResource getTweet(@PathParam("tweetId") String tweetId);

    /* Playlist endpoints */
    @GET
    @Path("/tweets/playlist/")
    public List<TweetPlaylistResource> getPlaylists();

    @POST
    @Path("/tweets/playlist/")
    public Response createPlaylist(List<TweetResource> playlist);

    @GET
    @Path("/tweets/playlist/{playlistId}/")
    public TweetPlaylistResource getPlaylist(@PathParam("playlistId") int playlistId);

    @DELETE
    @Path("/tweets/playlist/{playlistId}")
    public Response removePlaylist(@PathParam("playlistId") int playlistId);

    @GET
    @Path("/media/{filename}")
    @Produces("image/jpg")
    public Response getMediaFile(@PathParam("filename") String fileName);

    /* Stream endpoints */
    @GET
    @Path("/stream/start/{hashtag}")
    public void startStream(@PathParam("hashtag") @Nullable String hashtag);

    @GET
    @Path("/stream/stop/")
    public void stopStream();

    @GET
    @Path("/stream/tweets/")
    public List<TweetResource> getStreamTweets();

    @GET
    @Path("/stream/clear/")
    public void clearStreamTweets();

    @GET
    @Path("/stream/count/")
    public Response getStreamCount();

    /* Poll endpoints */
    @POST
    @Path("/poll/start/")
    public Response startPoll(TwitterPollRequest request);

    @GET
    @Path("/poll/stop/")
    public void stopPoll();

    @GET
    @Path("/poll/clear/")
    public void clearPoll();

    @GET
    @Path("/poll/result/")
    public Response getPollResult();
}
