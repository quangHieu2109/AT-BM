//package com.bookshopweb.service;
//
//import com.bookshopweb.beans.User;
//import com.bookshopweb.dao.UserIDAO;
//
//import java.util.Optional;
//
//public class UserService extends Service<User, UserIDAO> implements UserIDAO {
//    public UserService() {
//        super(UserIDAO.class);
//    }
//
//    @Override
//    public Optional<User> getByUsername(String username) {
//        return jdbi.withExtension(UserIDAO.class, dao -> dao.getByUsername(username));
//    }
//
//    @Override
//    public void changePassword(long userId, String newPassword) {
//        jdbi.useExtension(UserIDAO.class, dao -> dao.changePassword(userId, newPassword));
//    }
//
//    @Override
//    public Optional<User> getByEmail(String email) {
//        return jdbi.withExtension(UserIDAO.class, dao -> dao.getByEmail(email));
//    }
//
//    @Override
//    public Optional<User> getByPhoneNumber(String phoneNumber) {
//        return jdbi.withExtension(UserIDAO.class, dao -> dao.getByPhoneNumber(phoneNumber));
//    }
//
//    @Override
//    public int count() {
//        return jdbi.withExtension(UserIDAO.class, UserIDAO::count);
//    }
//}
