//package com.bookshopweb.service;
//
//import com.bookshopweb.beans.Product;
//import com.bookshopweb.dao.ProductIDAO;
//import com.bookshopweb.utils.Protector;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class ProductService extends Service<Product, ProductIDAO> implements ProductIDAO {
//    public ProductService() {
//        super(ProductIDAO.class);
//    }
//
//    @Override
//    public List<Product> getOrderedPartByCategoryId(int limit, int offset, String orderBy, String orderDir, long categoryId) {
//        return jdbi.withExtension(ProductIDAO.class, dao -> dao.getOrderedPartByCategoryId(limit, offset, orderBy, orderDir, categoryId));
//    }
//
//    @Override
//    public int countByCategoryId(long categoryId) {
//        return jdbi.withExtension(ProductIDAO.class, dao -> dao.countByCategoryId(categoryId));
//    }
//
//    @Override
//    public List<Product> getRandomPartByCategoryId(int limit, int offset, long categoryId) {
//        return jdbi.withExtension(ProductIDAO.class, dao -> dao.getRandomPartByCategoryId(limit, offset, categoryId));
//    }
//
//    @Override
//    public List<String> getPublishersByCategoryId(long categoryId) {
//        return jdbi.withExtension(ProductIDAO.class, dao -> dao.getPublishersByCategoryId(categoryId));
//    }
//
//    @Override
//    public int countByCategoryIdAndFilters(long categoryId, String filters) {
//        return jdbi.withExtension(ProductIDAO.class, dao -> dao.countByCategoryIdAndFilters(categoryId, filters));
//    }
//
//    @Override
//    public List<Product> getOrderedPartByCategoryIdAndFilters(int limit, int offset, String orderBy, String orderDir, long categoryId, String filters) {
//        return jdbi.withExtension(ProductIDAO.class, dao -> dao.getOrderedPartByCategoryIdAndFilters(limit, offset, orderBy, orderDir, categoryId, filters));
//    }
//
//    @Override
//    public int count() {
//        return jdbi.withExtension(ProductIDAO.class, ProductIDAO::count);
//    }
//
//    @Override
//    public void insertProductCategory(long productId, long categoryId) {
//        jdbi.useExtension(ProductIDAO.class, dao -> dao.insertProductCategory(productId, categoryId));
//    }
//
//    @Override
//    public void updateProductCategory(long productId, long categoryId) {
//        jdbi.useExtension(ProductIDAO.class, dao -> dao.updateProductCategory(productId, categoryId));
//    }
//
//    @Override
//    public void deleteProductCategory(long productId, long categoryId) {
//        jdbi.useExtension(ProductIDAO.class, dao -> dao.deleteProductCategory(productId, categoryId));
//    }
//
//    @Override
//    public List<Product> getByQuery(String query, int limit, int offset) {
//        return jdbi.withExtension(ProductIDAO.class, dao -> dao.getByQuery(query, limit, offset));
//    }
//
//    @Override
//    public int countByQuery(String query) {
//        return jdbi.withExtension(ProductIDAO.class, dao -> dao.countByQuery(query));
//    }
//}
//