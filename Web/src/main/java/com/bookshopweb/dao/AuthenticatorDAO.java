package com.bookshopweb.dao;

import com.bookshopweb.beans.Authenticator;
import com.bookshopweb.jdbiInterface.AuthenticatorJDBI;
import com.bookshopweb.utils.JDBIUltis;
import com.google.gson.Gson;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class AuthenticatorDAO {
    private AuthenticatorJDBI authenticatorJDBI = JDBIUltis.getJDBI().onDemand(AuthenticatorJDBI.class);
    public List<Authenticator> getAll(){
        return authenticatorJDBI.getAll();
    }
    public List<Authenticator> getAllByUserId(long userId){
        return authenticatorJDBI.getAllByUserId(userId);
    }
    public Authenticator getById(long id){
        return authenticatorJDBI.getById(id);

    }
    public int getCountSignature(long authId){
        return authenticatorJDBI.getCountSignature(authId);
    }
    public Authenticator getByUserId(long userId){
        return authenticatorJDBI.getByUserId(userId);
    }
    public int updateStatus(Authenticator authenticator){
        return authenticatorJDBI.updateStatus(authenticator);
    }
    public int expireAll(long userId){
        return authenticatorJDBI.expireAll(userId);
    }
    // trả về id khi insert thành công
    public long addAuthenticator(Authenticator authenticator){
        expireAll(authenticator.getUserId());
        return JDBIUltis.getJDBI().withHandle(handle ->
                handle.createUpdate("INSERT INTO authenticator (userId, publicKey, createdAt, status)\n" +
                                "VALUES (:userId, :publicKey, :createdAt, :status)")
                        .bindBean(authenticator)
                        .executeAndReturnGeneratedKeys("id") // Lấy ID
                        .mapTo(Long.class) // Chuyển đổi thành Long
                        .findOne()
                        .orElse(0l)
        );
    }

    public static void main(String[] args) {
        for(Authenticator authenticator: new AuthenticatorDAO().getAllByUserId(1)){
            System.out.println(new AuthenticatorDAO().getCountSignature(authenticator.getId()));
        }
    }
}
