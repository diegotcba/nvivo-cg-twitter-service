package com.nvivo.twitter.svc.model;

import lombok.Data;

/**
 * Created by DiegoT on 13/05/2016.
 */
@Data
public class MediaDownloadJob {
    private String sourceMediaUrl;
    private Long mediaId;
    private String mediaName;
    private String destinationMediaUrl;
}
