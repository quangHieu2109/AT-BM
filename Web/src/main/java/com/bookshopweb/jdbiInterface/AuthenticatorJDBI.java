package com.bookshopweb.jdbiInterface;

import com.bookshopweb.beans.Authenticator;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Authenticator.class)
public interface AuthenticatorJDBI {
    @SqlQuery("SELECT * FROM authenticator")
    List<Authenticator> getAll();

    @SqlQuery("SELECT * FROM authenticator WHERE id = :id")
    Authenticator getById(@Bind("id") long id);

    @SqlQuery("SELECT * FROM authenticator WHERE userId = :userId\n" +
            "ORDER BY createdAt DESC \n" +
            "LIMIT 1")
    Authenticator getByUserId(@Bind("userId") long userId);

    @SqlUpdate("UPDATE authenticator SET status=:status " +
            "WHERE id = :id ")
    int updateStatus(@BindBean Authenticator authenticator);

    @SqlUpdate("INSERT INTO authenticator (userId, publicKey, createdAt, status)\n" +
            "VALUES (:userId, :publicKey, :createdAt, :status)")
    int addAuthenticator(@BindBean Authenticator authenticator);



}
