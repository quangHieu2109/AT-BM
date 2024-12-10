////package com.bookshopweb.service;
////
////import com.bookshopweb.dao.IDAO;
////import com.bookshopweb.utils.JdbiUtils;
////import org.jdbi.v3.core.Jdbi;
////
////import java.util.List;
////import java.util.Optional;
////
////public abstract class Service<T, D extends IDAO<T>> implements IDAO<T> {
////    private final Class<D> daoClass;
////    protected final Jdbi jdbi = JdbiUtils.createInstance();
////
////    public Service(Class<D> daoClass) {
////        this.daoClass = daoClass;
////    }
////
////    @Override
////    public long insert(T t) {
////        return jdbi.withExtension(daoClass, dao -> dao.insert(t));
////    }
////
////    @Override
////    public void update(T t) {
////        jdbi.useExtension(daoClass, dao -> dao.update(t));
////    }
////
////    @Override
////    public void delete(long id) {
////        jdbi.useExtension(daoClass, dao -> dao.delete(id));
////    }
////
////    @Override
////    public Optional<T> getById(long id) {
////        return jdbi.withExtension(daoClass, dao -> dao.getById(id));
////    }
////
////    @Override
////    public List<T> getAll() {
////        return jdbi.withExtension(daoClass, IDAO::getAll);
////    }
////
////    @Override
////    public List<T> getPart(int limit, int offset) {
////        return jdbi.withExtension(daoClass, dao -> dao.getPart(limit, offset));
////    }
////
////    @Override
////    public List<T> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
////        return jdbi.withExtension(daoClass, dao -> dao.getOrderedPart(limit, offset, orderBy, orderDir));
////    }
////}
//package com.bookshopweb.service;
//
//import com.bookshopweb.dao.IDAO;
//import com.bookshopweb.utils.JDBCUtils;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public abstract class Service<T, D extends IDAO<T>> implements IDAO<T> {
//    private final D dao;
//    protected final Connection connection = JDBCUtils.getConnection();
//
//    public Service(D d, D dao) {
//        this.dao = dao;
//    }
//
//    @Override
//    public int insert(T t,String ip) {
//      return 0;
//    }
//
//    @Override
//    public int update(T t, String ip) {
//        return 0;
//    }
//
//    @Override
//    public int delete(T t, String ip) {
//        return 0;
//    }
//    public Optional<T> getById(long id) {
//        // Implement getById operation using JDBC
//        return Optional.empty();
//    }
//
//    @Override
//    public List<T> getAll() {
//        // Implement getAll operation using JDBC
//        return new ArrayList<>();
//    }
//
//    @Override
//    public List<T> getPart(int limit, int offset) {
//        // Implement getPart operation using JDBC
//        return new ArrayList<>();
//    }
//
//    @Override
//    public List<T> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
//        return new ArrayList<>();
//    }
//}
