package com.nvivo.twitter.svc.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by DiegoT on 10/01/2016.
 */
@Data
public class TwitterPollResponse {
    private String startPollDateTime;
    private Map<String, TwitterPollResult> results;
    private long total;
}
