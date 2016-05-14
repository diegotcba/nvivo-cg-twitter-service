package com.nvivo.twitter.svc.service.impl;

import com.nvivo.twitter.svc.dao.FacebookApi;
import com.nvivo.twitter.svc.model.TweetPublish;
import com.nvivo.twitter.svc.service.FacebookService;

/**
 * Created by DiegoT on 10/04/2016.
 */
public class FacebookServiceImpl implements FacebookService {
    private FacebookApi daoService;

    @Override
    public String getCount(String hashtag) {
        return null;
    }

    @Override
    public String publish(TweetPublish publish) {
        return daoService.publish(publish);
    }

    public void setDaoService(FacebookApi daoService) {
        this.daoService = daoService;
    }
}
