package com.nvivo.twitter.svc.dao;

import com.nvivo.twitter.svc.model.MediaDownloadJob;
import com.nvivo.twitter.svc.model.TweetPlaylistResource;
import com.nvivo.twitter.svc.model.TweetResource;

import java.util.List;

/**
 * Created by DiegoT on 13/05/2016.
 */
public interface TwitterDao {
    public String getDBStatus();
    public TweetPlaylistResource addPlaylist(List<TweetResource> tweets);
    public TweetPlaylistResource getPlaylist(long id);
    public void removePlaylist(long playlistId);
    public List<TweetPlaylistResource> getPlaylists();
    public void updateUrlMedia(List<MediaDownloadJob> mediaDownloadJobs);
}
