package com.bookshopweb.dao;

import com.bookshopweb.beans.Authenticator;
import com.bookshopweb.jdbiInterface.AuthenticatorJDBI;
import com.bookshopweb.utils.JDBIUltis;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class AuthenticatorDAO {
    private AuthenticatorJDBI authenticatorJDBI = JDBIUltis.getJDBI().onDemand(AuthenticatorJDBI.class);
    public List<Authenticator> getAll(){
        return authenticatorJDBI.getAll();
    }
    public Authenticator getById(long id){
        return authenticatorJDBI.getById(id);

    }
    public Authenticator getByUserId(long userId){
        return authenticatorJDBI.getByUserId(userId);
    }
    public int updateStatus(Authenticator authenticator){
        return authenticatorJDBI.updateStatus(authenticator);
    }
    public int addAuthenticator(Authenticator authenticator){
        return addAuthenticator(authenticator);
    }
}
