package com.theironyard.services;

import com.theironyard.entities.Photo;

import java.util.List;

/**
 * Created by EddyJ on 7/31/16.
 */
public class ExecutionerOfPhotos implements Runnable {
    private PhotoRepository photoRepository;
    private Photo photo;

    public ExecutionerOfPhotos(PhotoRepository photoRepository, Photo photo) {
        this.photoRepository = photoRepository;
        this.photo = photo;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(photo.getSleepTime() * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        photoRepository.delete(photo);
    }
}
