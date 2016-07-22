package com.nvivo.twitter.svc.dao.impl;

import com.nvivo.twitter.svc.dao.TwitterDao;
import com.nvivo.twitter.svc.model.MediaDownloadJob;
import com.nvivo.twitter.svc.model.TweetPlaylistResource;
import com.nvivo.twitter.svc.model.TweetProfileImage;
import com.nvivo.twitter.svc.model.TweetResource;
import org.apache.log4j.Logger;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DiegoT on 13/05/2016.
 */
public class TwitterDaoImpl implements TwitterDao {
    private DBConnection dbConnection;

    final static Logger LOGGER = Logger.getLogger(TwitterDaoImpl.class);

    @Override
    public TweetPlaylistResource addPlaylist(List<TweetResource> tweets) {
        TweetPlaylistResource playlist = new TweetPlaylistResource();

        String insertQuery = String.format("INSERT INTO playlist (dateTime, title, description) " +
                "VALUES ('%s', '%s', '%s')", LocalDateTime.now(), "lista de tweets", "-----");
        final long playlistId;

        try {
            dbConnection.openConnection();

            tweets.stream().forEach(t -> {
                final String query = String.format("INSERT INTO tweets (id, hashtag, username, fullname, message, urlAvatar, localUrlAvatar, dateTime, isRetweet) " +
                        "VALUES (%s, '%s', '%s', '%s', '%s', '%s', '', '%s', %s)", t.getId().toString(), t.getHashtag(), t.getUserName(), t.getFullName(), t.getMessage(), t.getUrlAvatar(), t.getDateTime(), String.valueOf(t.isRetweet()));
                dbConnection.executeCommand(query);
                saveTweetProfileImages(t);
//                saveTweetLocalProfileImages(t);
            });

            dbConnection.executeCommand(insertQuery);
            ResultSet result = dbConnection.getQuery("SELECT IDENTITY() AS LASTID");
            result.first();
            playlistId = result.getLong("LASTID");

            tweets.stream().forEach(t -> {
                final String query = String.format("INSERT INTO playlistDetail (playlistId, tweetId) VALUES (%s, %s)", playlistId, t.getId());
                dbConnection.executeCommand(query);
            });

            playlist = getPlaylist(playlistId);

        } catch (Exception sqle) {
            LOGGER.error("No se pudo guardar el playlist");
            LOGGER.error(sqle);
        }

        return playlist;
    }

    private void saveTweetProfileImages(TweetResource tweet) {
        try {
            dbConnection.openConnection();

            String tweetId = tweet.getId();

            tweet.getProfileImages().forEach(t -> {
                final String query = String.format("INSERT INTO tweetsProfileImage (tweetId, profileImageType, profileImageUrl, localProfileImageUrl) " +
                        "VALUES (%s, '%s', '%s')", tweetId, t.getProfileImageType(), t.getProfileImageUrl(), t.getLocalProfileImageUrl());
                dbConnection.executeCommand(query);
            });

        } catch (Exception sqle) {
            LOGGER.error("No se pudieron guardar los profile images");
            LOGGER.error(sqle);
        }
    }

    @Override
    public TweetPlaylistResource getPlaylist(long id) {
        List<TweetPlaylistResource> list = new ArrayList<>();

        String query = "SELECT id, title, datetime, description FROM playlist WHERE id = " + id;

        try {
            dbConnection.openConnection();
            ResultSet result = dbConnection.getQuery(query);

            while (result.next()) {
                TweetPlaylistResource playlist = new TweetPlaylistResource();

                playlist.setId(result.getLong("id"));
                playlist.setDateTime(result.getTimestamp("datetime").toLocalDateTime().toString());
                playlist.setTitle(result.getString("title"));
                playlist.setDescription(result.getString("description"));
                playlist.setTweets(getPlaylistDetail(playlist.getId()));

                list.add(playlist);
            }
            result.close();

        } catch (SQLException sqlE) {
            LOGGER.error("No se pudo cargar el playlist " + id);
            LOGGER.error(sqlE);
        }

        return list.get(0);
    }

    @Override
    public void removePlaylist(long playlistId) {
        try {
            dbConnection.openConnection();

            String deleteQuery = String.format("DELETE playlist WHERE id = %s", playlistId);
            dbConnection.executeCommand(deleteQuery);
        } catch (Exception e) {
            LOGGER.error("No se pudo eliminar el playlist " + playlistId);
        }
    }



    @Override
    public List<TweetPlaylistResource> getPlaylists() {
        List<TweetPlaylistResource> list = new ArrayList<>();

        String query = "SELECT id, title, datetime, description FROM playlist";

        try {
            dbConnection.openConnection();
            ResultSet result = dbConnection.getQuery(query);

            while (result.next()) {
                TweetPlaylistResource playlist = new TweetPlaylistResource();

                playlist.setId(result.getLong("id"));
                playlist.setDateTime(result.getTimestamp("datetime").toLocalDateTime().toString());
                playlist.setTitle(result.getString("title"));
                playlist.setDescription(result.getString("description"));
                playlist.setTweets(getPlaylistDetail(playlist.getId()));

                list.add(playlist);
            }
            result.close();

        } catch (SQLException sqlE) {
            LOGGER.error("No se pudieron cargar los playlist de la BD.");
        }

        return list;
    }

    @Override
    public void updateUrlMedia(List<MediaDownloadJob> mediaDownloadJobs) {

        try {
            dbConnection.openConnection();

            String updateQuery = "UPDATE tweetsProfileImage SET localProfileImageUrl = '%s' WHERE tweetId = '%s' AND profileImageType = '%s'";

            mediaDownloadJobs.stream().forEach(mediaDownloadJob -> {
                dbConnection.executeCommand(String.format(updateQuery, mediaDownloadJob.getDestinationMediaUrl(), mediaDownloadJob.getMediaId(), mediaDownloadJob.getMediaName()));
            });

        } catch (Exception e) {
            LOGGER.error("No se pudo actualizar las url locales de los avatars.");
        }
    }

    private List<TweetResource> getPlaylistDetail(long playlistId) {
        List<TweetResource> tweets = new ArrayList<>();

        String query = "SELECT id, hashtag, username, fullname, message, urlAvatar, localUrlAvatar, dateTime, isRetweet ";
        query += "FROM tweets, playlistDetail WHERE playlistDetail.tweetId = tweets.id ";
        query += "AND playlistDetail.playlistId = " + playlistId;

        try {
            dbConnection.openConnection();
            ResultSet result = dbConnection.getQuery(query);

            while (result.next()) {
                TweetResource tw = new TweetResource();

                tw.setId(((Long)result.getLong("id")).toString());
                tw.setHashtag(result.getString("hashtag"));
                tw.setUserName(result.getString("username"));
                tw.setFullName(result.getString("fullname"));
                tw.setMessage(result.getString("message"));
                tw.setUrlAvatar(result.getString("urlAvatar"));
                tw.setLocalUrlAvatar(result.getString("localUrlAvatar"));
                tw.setDateTime(result.getTimestamp("datetime").toLocalDateTime().toString());
                tw.setRetweet(result.getBoolean("isRetweet"));
                tw.setProfileImages(getTweetProfileImages((Long)result.getLong("id")));

                tweets.add(tw);
            }
            result.close();

        } catch (SQLException sqlE) {
            LOGGER.error("No se pudo cargar el detalle del playlist " + playlistId);
        }

        return tweets;
    }

    private List<TweetProfileImage> getTweetProfileImages(long tweetId) {
        List<TweetProfileImage> images = new ArrayList<>();

        String query = "SELECT tweetId, profileImageType, profileImageUrl, localProfileImageUrl ";
        query += "FROM tweetsProfileImage WHERE tweetsProfileImage.tweetId = " + tweetId;

        try {
            dbConnection.openConnection();
            ResultSet result = dbConnection.getQuery(query);

            while (result.next()) {
                TweetProfileImage pimg = new TweetProfileImage();

                pimg.setProfileImageType(result.getString("profileImageType"));
                pimg.setProfileImageUrl(result.getString("profileImageUrl"));
                pimg.setLocalProfileImageUrl(result.getString("localProfileImageUrl"));

                images.add(pimg);
            }
            result.close();

        } catch (SQLException sqlE) {
            LOGGER.error("No se pudo cargar los profile images de " + tweetId);
        }

        return images;
    }

    @Override
    public String getDBStatus() {
        String result = "";
        try{

            dbConnection.openConnection();
            DatabaseMetaData meta = dbConnection.getMetaData();
            result = "Connected with " +
                    meta.getDriverName() + " " + meta.getDriverVersion()
                    + "{ " + meta.getDriverMajorVersion() + "," +
                    meta.getDriverMinorVersion() + " }" + " to " +
                    meta.getDatabaseProductName() + " " +
                    meta.getDatabaseProductVersion() + "\n";

            LOGGER.debug(result);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            result = "No database connection!!";
            LOGGER.error(result);
        }
        finally
        {
//            dbConnection.closeConnection();
        }
        return result;
    }

    public void setDbConnection(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
}
