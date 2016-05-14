package com.nvivo.twitter.svc.comverter;

import com.nvivo.twitter.svc.model.TweetResponse;
import com.nvivo.twitter.svc.model.TweetResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DiegoT on 29/11/2015.
 */
public class TweetResourceConverter {
    public static TweetResource convert(TweetResponse tweet) {
        TweetResource result = new TweetResource();

        result.setId(tweet.getId().toString());
        result.setUserName(tweet.getUserName());
        result.setFullName(tweet.getFullName());
        result.setHashtag(tweet.getHashtag());
        result.setMessage(tweet.getMessage());
        result.setUrlAvatar(tweet.getUrlAvatar());
        result.setDateTime(tweet.getDateTime().toString());
        result.setRetweet(tweet.isRetweet());

        return result;
    }

    public static List<TweetResource> convert(List<TweetResponse> tweets) {
        List<TweetResource> result = new ArrayList<>();
        tweets.stream().forEach(t -> result.add(convert(t)));
        return result;
    }
}
