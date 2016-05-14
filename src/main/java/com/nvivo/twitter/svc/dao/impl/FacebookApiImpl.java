package com.nvivo.twitter.svc.dao.impl;

import com.nvivo.twitter.svc.dao.FacebookApi;
import com.nvivo.twitter.svc.model.TweetPublish;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.PostUpdate;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;
import twitter4j.TwitterException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by DiegoT on 10/04/2016.
 */
public class FacebookApiImpl implements FacebookApi {
    private Configuration configuration;
    private Facebook facebook;

    public FacebookApiImpl(ConfigurationBuilder configurationBuilder) {
        this.configuration = configurationBuilder.build();
    }

    @Override
    public String publish(TweetPublish publish) {
        if (facebook == null) {
            facebook = new FacebookFactory(configuration).getInstance();
        }

        String response = "";
        String meta = "\nId: " + String.valueOf(publish.getId()) + "\nAutor: " + publish.getAuthor() + "\nFechaHora: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String msg = publish.getMessage();


        try {
            PostUpdate post = new PostUpdate(msg + meta);
            facebook.postFeed(post);
            response = "OK";
        } catch (FacebookException fe) {
            if (401 == fe.getStatusCode()) {
                System.out.println("Unable to get the access token.");
            } else {
                fe.printStackTrace();
                response = "ERROR";
            }
            System.out.println(response);
        }


        return response;
    }
}
