package com.theironyard.services;

import com.theironyard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jeff on 7/28/16.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findFirstByName(String name);
}
