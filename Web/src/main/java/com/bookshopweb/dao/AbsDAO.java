package com.bookshopweb.dao;

import com.bookshopweb.beans.AbsModel;
import com.bookshopweb.beans.Log;
import com.bookshopweb.service.SendMail;
import com.bookshopweb.utils.JDBCUtils;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public abstract class AbsDAO<T extends AbsModel> implements IDAO<T> {
    protected Connection conn = JDBCUtils.getConnection();
    private LogDAO logDAO = new LogDAO();

    @Override
    public abstract T selectPrevalue(Long id) ;
    //Class Abstract DAO
    @Override
    public int insert(T t, String ip) {
        Log log = new Log(ip, 2,"Insert on table "+ t.getResource(),
                "null", t.toJson(), t.getCreateAt());
        logDAO.insert(log);
        return 0;
    }

    @Override
    public int update(T t, String ip) {
        T preValue = selectPrevalue(t.getId());
        Log log = new Log(ip, 3, "Update on table "+t.getResource(),
                preValue.toJson(), t.toJson(), t.getCreateAt());
        logDAO.insert(log);
        return 0;
    }

    @Override
    public int delete(T t, String ip) {
        Log log = new Log(ip, 4, "Delete on table "+t.getResource(),
                t.toJson(), "null", t.getCreateAt());
        logDAO.insert(log);
        //Gửi mail thông báo đến admin
        SendMail.sendLogForAdmin(log);
        return  0;
    }

    @Override
    public List<T> getAll() {
        return null;
    }

    @Override
    public List<T> getPart(int limit, int offset) {
        return null;
    }

    @Override
    public List<T> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return null;
    }
}