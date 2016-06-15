package com.lukaszbaran.starter.services;

import com.lukaszbaran.starter.domain.User;
import com.lukaszbaran.starter.processing.EmailPictureProcessor;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);


    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>();
    }

    @Override
    public User getById(int id) {
        LOGGER.info("getById(" + id + ")");

        return new User();
    }

    @Override
    public User createNewUser(User user) {
        return new User();
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public int getNumberOfUsers() {
        LOGGER.info("getNumberOfUsers()");
        return 13;
    }
}
