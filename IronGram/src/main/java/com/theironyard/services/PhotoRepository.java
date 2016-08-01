package com.theironyard.services;

import com.theironyard.entities.Photo;
import com.theironyard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jeff on 7/28/16.
 */
public interface PhotoRepository extends JpaRepository<Photo, Integer>{
    List<Photo> findByRecipientOrderByIdAsc(User user);
    List<Photo> findBySenderAndPublicPhotoTrue(User user);
    Photo findByRecipient(User user);
}
