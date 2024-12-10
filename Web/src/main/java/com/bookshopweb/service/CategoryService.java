//package com.bookshopweb.service;
//
//import com.bookshopweb.beans.Category;
//import com.bookshopweb.dao.CategoryIDAO;
//
//import java.util.Optional;
//
//public class CategoryService extends Service<Category, CategoryIDAO> implements CategoryIDAO {
//    public CategoryService() {
//        super(CategoryIDAO.class);
//    }
//
//    @Override
//    public Optional<Category> getByProductId(long productId) {
//        return jdbi.withExtension(CategoryIDAO.class, dao -> dao.getByProductId(productId));
//    }
//
//    @Override
//    public int count() {
//        return jdbi.withExtension(CategoryIDAO.class, CategoryIDAO::count);
//    }
//}
