package com.theironyard.controllers;

import com.theironyard.entities.Photo;
import com.theironyard.entities.User;
import com.theironyard.exceptions.FileNotAnImage;
import com.theironyard.exceptions.NotLoggedIn;
import com.theironyard.exceptions.UserNotFoundException;
import com.theironyard.services.ExecutionerOfPhotos;
import com.theironyard.services.PhotoRepository;
import com.theironyard.services.UserRepository;

import com.theironyard.exceptions.LoginFailedException;
import com.theironyard.utilities.PasswordStorage;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jeff on 7/28/16.
 */
@RestController
public class IronGramController {
    public static final String SESSION_USERNAME = "username";

    @Autowired
    UserRepository userRepository;

    @Autowired
    PhotoRepository photoRepository;

    Server dbui;

    @PostConstruct
    public void init() throws SQLException {
        dbui = Server.createWebServer().start();
    }

    @PreDestroy
    public void destroy(){
        dbui.stop();
    }



    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public User login(String username, String password,
                      HttpSession session, HttpServletResponse response) throws Exception {
        User user = userRepository.findFirstByName(username);
        if(user == null){
            user = new User(username, PasswordStorage.createHash(password));
            userRepository.save(user);
        }
        else if(!PasswordStorage.verifyPassword(password, user.getPassword())){
            throw new LoginFailedException();
        }
        session.setAttribute(SESSION_USERNAME, username);
        response.sendRedirect("/");
        return user;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {
        session.invalidate();
        response.sendRedirect("/");
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public User getUser(HttpSession session){
        String username = (String) session.getAttribute(SESSION_USERNAME);
        return userRepository.findFirstByName(username);
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public Photo upload(HttpSession session, MultipartFile photo,
                        String receiverName, HttpServletResponse response, String publicPhoto, Integer sleepTime) throws IOException {
        String username = (String) session.getAttribute(SESSION_USERNAME);
        if(username == null){
            throw new NotLoggedIn();
        }

        User sender = userRepository.findFirstByName(username);
        User receiver = userRepository.findFirstByName(receiverName);

        if(receiver == null){
            throw new UserNotFoundException();
        }

        File dir = new File("public/photos");
        if(!dir.exists()){
            dir.mkdirs();
        }

        if (!photo.getContentType().startsWith("image")){
            throw new FileNotAnImage();
        }

        File photoFile = File.createTempFile("photo", photo.getOriginalFilename(), dir);
        FileOutputStream fos = new FileOutputStream(photoFile);
        fos.write(photo.getBytes());
        Photo p = new Photo(sender, receiver, photoFile.getName());
        if(publicPhoto == null){
            p.setPublicPhoto(false);
        }else{
            p.setPublicPhoto(true);
        }
        if(sleepTime == null) {
            p.setSleepTime(10);
        }else{
            p.setSleepTime(sleepTime);
        }

        photoRepository.save(p);


        response.sendRedirect("/");
        return p;
    }

    @RequestMapping(path = "/photos", method = RequestMethod.GET)
    public List<Photo> showPhotos(HttpSession session){
        String username = (String) session.getAttribute(SESSION_USERNAME);
        if(username == null){
            throw new NotLoggedIn();
        }

        User user = userRepository.findFirstByName(username);
        List<Photo> photoList = photoRepository.findByRecipientOrderByIdAsc(user);

        ExecutorService service = Executors.newCachedThreadPool();
        for(Photo photo : photoList){
            service.submit(new ExecutionerOfPhotos(photoRepository, photo));
        }
        service.shutdown();
        return photoList;
    }

    @RequestMapping(path = "/public-photos", method = RequestMethod.GET)
    public List<Photo> showSentPhoto(HttpSession session, String username){
        username = (String) session.getAttribute(SESSION_USERNAME);
        if (session.getAttribute(username) == null){
            throw new NotLoggedIn();
        }
        User user = userRepository.findFirstByName(username);
        return photoRepository.findBySenderAndPublicPhotoTrue(user);
    }
}
