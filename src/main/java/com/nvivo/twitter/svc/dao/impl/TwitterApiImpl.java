package com.nvivo.twitter.svc.dao.impl;

import com.nvivo.twitter.svc.dao.TwitterApi;
import com.nvivo.twitter.svc.model.*;
import org.springframework.util.StringUtils;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by DiegoT on 28/11/2015.
 */
public class TwitterApiImpl implements TwitterApi {
    private Twitter twitter;
    private List<Status> stream;
    private TwitterStream twitterStream;
    private Configuration configuration;
    private long streamCounter;
    private Map<String, Long> streamCounters;


    public TwitterApiImpl(ConfigurationBuilder configurationBuilder) {
        this.configuration = configurationBuilder.build();
        this.streamCounter = 0l;
        this.streamCounters = new HashMap<>();
    }

    @Override
    public Status getTweet(String id) {
        return null;
    }

    @Override
    public QueryResult getTweets() {
        if (twitter == null){
            twitter = new TwitterFactory(configuration).getInstance();
        }

        Query query = new Query("#CopaDavisEnTyC");
        query.setCount(100);
        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public QueryResult getTweets(TweetQuery tweetQuery) {
        if (twitter == null){
            twitter = new TwitterFactory(configuration).getInstance();
        }

        Query query = new Query(tweetQuery.getHashtag());
        query.setCount(100);
        QueryResult result = null;
        try {
            result = twitter.search(query);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void startTweeterStream(String[] hashtag) {
        streamCounter = 0l;
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                streamCounter++;
//                stream.add(status);

                System.out.println("Tweets count: " + streamCounter);
                StringUtils.containsWhitespace("");
                streamCounters.keySet().forEach(sc -> {
                    if (status.getText().contains(sc)) {
                        Long aux = streamCounters.get(sc);
                        aux++;
                        streamCounters.replace(sc, aux);
                    }

                });
                System.out.println(status.getUser().getScreenName() + ": " + status.getText());
                System.out.println("*****" + streamCounters);
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int i) {

            }

            @Override
            public void onScrubGeo(long l, long l1) {

            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

            }

            @Override
            public void onException(Exception e) {

            }
        };


        twitterStream = new TwitterStreamFactory(configuration).getInstance();
        if (stream == null) {
            stream = new ArrayList<>();
        }
        FilterQuery fq = new FilterQuery();

        String[] keywords = hashtag;

        fq.track(keywords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq);
//        twitterStream.sample();
        System.out.println("Stream start!!!!");
    }

    @Override
    public void stopTwitterStream() {
        twitterStream.shutdown();
        System.out.println("Stream stop!!!!");
    }

    @Override
    public List<Status> getTwitterStream() {
        return this.stream;
    }

    @Override
    public void clearTwitterStream() {
        stream = new ArrayList<>();
        streamCounter = 0l;
    }

    @Override
    public long streamCount() {
        return streamCounter;
    }

    @Override
    public String publish(TweetPublish publish) {
        if (twitter == null){
            twitter = new TwitterFactory(configuration).getInstance();
        }

        String response = "";
        String meta = "\nId: " + String.valueOf(publish.getId()) + "\nAutor: " + publish.getAuthor() + "\nFechaHora: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String msg = publish.getMessage();


        try {
            twitter.updateStatus(msg + meta);
            response = "OK";
        } catch (TwitterException te) {
            if (401 == te.getStatusCode()) {
                System.out.println("Unable to get the access token.");
            } else {
                te.printStackTrace();
                response = "ERROR";
            }
            System.out.println(response);
        }

        return response;
    }

    @Override
    public void startPoll(TwitterPollRequest poll) {
        System.out.println(poll);
        poll.getHashtags().forEach(ht -> streamCounters.put(ht.getHashtag(), 0l));
        System.out.println(streamCounters.keySet());
        String[] tags = streamCounters.keySet().stream().toArray(String[]::new);
        startTweeterStream(tags);
    }

    @Override
    public void stopPoll() {
        stopTwitterStream();
    }

    @Override
    public void clearPoll() {
        clearTwitterStream();
    }

    @Override
    public TwitterPollResponse getPollResult() {
        TwitterPollResponse response = new TwitterPollResponse();
        Map<String, TwitterPollResult> resultMap = new HashMap<>();
        streamCounters.entrySet().forEach(entry -> {
            TwitterPollResult aux = new TwitterPollResult();
            aux.setCounter(entry.getValue());
            resultMap.put(entry.getKey(), aux);
        });
        Long total = resultMap.values().stream().mapToLong(TwitterPollResult::getCounter).sum();
        response.setResults(resultMap);
        response.setTotal(total.longValue());

        return response;
    }

//    public void setConfigurationBuilder(ConfigurationBuilder config) {
//        this.configurationBuilder = config;
//    }
}
