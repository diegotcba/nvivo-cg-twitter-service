package com.nvivo.twitter.svc.comverter;

import com.nvivo.twitter.svc.model.TweetProfileImage;
import com.nvivo.twitter.svc.model.TweetResponse;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.time.ZoneId;
import java.util.*;

import static java.time.LocalDateTime.ofInstant;

/**
 * Created by DiegoT on 29/11/2015.
 */
public class TweetResponseConverter {
    public static TweetResponse convert(Status status) {
        TweetResponse result = new TweetResponse();
        result.setId(status.getId());
        result.setFullName(status.getUser().getName());
        result.setUserName(status.getUser().getScreenName());
        result.setMessage(status.getText());
        result.setDateTime(ofInstant(status.getCreatedAt().toInstant(), ZoneId.systemDefault()));
        result.setUrlAvatar(status.getUser().getMiniProfileImageURL());
        result.setRetweet(status.isRetweet());

        List<TweetProfileImage> avatars = new ArrayList<>();

        TweetProfileImage tpimg = new TweetProfileImage();
        tpimg.setProfileImageType("profile");
        tpimg.setProfileImageUrl(status.getUser().getProfileImageURL());
        avatars.add(tpimg);

        tpimg = new TweetProfileImage();
        tpimg.setProfileImageType("mini");
        tpimg.setProfileImageUrl(status.getUser().getMiniProfileImageURL());
        avatars.add(tpimg);

//        avatars.put("original", status.getUser().getOriginalProfileImageURL());
//        avatars.put("bigger", status.getUser().getBiggerProfileImageURL());

        result.setProfileImages(avatars);

        List<HashtagEntity> hashtags = Arrays.asList(status.getHashtagEntities());

        //tweet.setHashtag(Arrays.toString(s.getHashtagEntities()));
        result.setHashtag((hashtags.isEmpty())? "":hashtags.get(0).getText());
        //result.setHashtag(hashtags.get(0).getText());

        return result;
    }

    public static List<TweetResponse> convert(List<Status> tweets) {
        List<TweetResponse> result = new ArrayList<>();
        tweets.stream().forEach(t -> result.add(convert(t)));
        return result;
    }
}
