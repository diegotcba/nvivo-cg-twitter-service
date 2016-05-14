package com.nvivo.twitter.svc.service;

import com.nvivo.twitter.svc.dao.TwitterDao;
import com.nvivo.twitter.svc.model.MediaDownloadJob;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by DiegoT on 14/05/2016.
 */
public class MediaDownloader implements Callable {
    private TwitterDao daoService;
    private List<MediaDownloadJob> mediaJob;

    private static Logger LOGGER = Logger.getLogger(MediaDownloader.class);

    @Override
    public Object call() throws Exception {
        mediaJob.stream().forEach(mediaDownloadJob -> {
            try {
                 mediaDownloadJob.setDestinationMediaUrl(downloadMediaFile(new URL(mediaDownloadJob.getSourceMediaUrl()), mediaDownloadJob));
            } catch (MalformedURLException e) {
                LOGGER.error("Url para descargar media mal formada: " + mediaDownloadJob.getSourceMediaUrl());
            }
        });

        updateMediaRecord(mediaJob);
        LOGGER.trace("Tarea de descarga de medias terminada.");
        return null;
    }

    private String downloadMediaFile(URL mediaUrl, MediaDownloadJob mediaDownloadJob) {
        String result = mediaUrl.toString();

        try {
            LOGGER.trace("Comenzando descarga de media.");
            InputStream input = mediaUrl.openStream();
            LOGGER.trace("Descarga de media completada.");
            result = writeStreamToFile(input, mediaDownloadJob);
        } catch (IOException e) {
            LOGGER.error("Error al descargar media en: " + mediaUrl.getPath());
        }

        return result;
    }

    private String writeStreamToFile(InputStream mediaStream, MediaDownloadJob mediaDownloadJob) {
        String result = "";
        String fileName = mediaDownloadJob.getMediaName() + "-" + mediaDownloadJob.getMediaId() + ".jpg";

        FileOutputStream file = null;
        try {
            file = new FileOutputStream(fileName);
            file.write(IOUtils.toByteArray(mediaStream));
            LOGGER.trace("Archivo creado: " + fileName);
            result = fileName;
            file.close();
            mediaStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void updateMediaRecord(List<MediaDownloadJob> mediaDownloadJob) {
        daoService.updateUrlMedia(mediaDownloadJob);
    }

    public void setDaoService(TwitterDao daoService) {
        this.daoService = daoService;
    }

    public void setMediaJob(List<MediaDownloadJob> mediaJob) {
        this.mediaJob = mediaJob;
    }
}
