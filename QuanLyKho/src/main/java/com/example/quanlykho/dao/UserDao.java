package com.example.quanlykho.dao;

import com.example.quanlykho.entity.User;
import com.example.quanlykho.util.UserContext;

import java.util.List;

public class UserDao {
    private final GenericDao<User, Integer> genericDao;

    public UserDao() {
        genericDao = new GenericDao<>(User.class);
    }

    public boolean login(User user) {
        String hql = "FROM User WHERE username = ?1";
        List<User> userFound = genericDao.findWithQuery(hql, user.getUsername());

        if (userFound.isEmpty()) {
            return false;
        }

        UserContext.getInstance().setUsername(userFound.get(0).getUsername());
        UserContext.getInstance().setFullName(userFound.get(0).getFullName());
        UserContext.getInstance().setUserId(userFound.get(0).getId());

        return userFound.get(0).getPassword().equals(user.getPassword());
    }

    public boolean register(User newUser) {
        boolean existedUser = (Long) genericDao.findSingleResult("select count(u) from User u where u.username = ?1", newUser.getUsername()) > 0;

        if (existedUser) {
            return false;
        }

        genericDao.save(newUser);
        return true;
    }

    public List<User> getAllUsers() {
        return genericDao.findAll();
    }



}
