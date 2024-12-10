//package com.bookshopweb.service;
//
//import com.bookshopweb.beans.ProductReview;
//import com.bookshopweb.dao.ProductReviewIDAO;
//
//import java.util.List;
//
//public class ProductReviewService extends Service<ProductReview, ProductReviewIDAO> implements ProductReviewIDAO {
//    public ProductReviewService() {
//        super(ProductReviewIDAO.class);
//    }
//
//    @Override
//    public List<ProductReview> getOrderedPartByProductId(int limit, int offset, String orderBy, String orderDir, long productId) {
//        return jdbi.withExtension(ProductReviewIDAO.class, dao -> dao.getOrderedPartByProductId(limit, offset, orderBy, orderDir, productId));
//    }
//
//    @Override
//    public int countByProductId(long productId) {
//        return jdbi.withExtension(ProductReviewIDAO.class, dao -> dao.countByProductId(productId));
//    }
//
//    @Override
//    public int sumRatingScoresByProductId(long productId) {
//        return jdbi.withExtension(ProductReviewIDAO.class, dao -> dao.sumRatingScoresByProductId(productId));
//    }
//
//    @Override
//    public int count() {
//        return jdbi.withExtension(ProductReviewIDAO.class, ProductReviewIDAO::count);
//    }
//
//    @Override
//    public void hide(long id) {
//        jdbi.useExtension(ProductReviewIDAO.class, dao -> dao.hide(id));
//    }
//
//    @Override
//    public void show(long id) {
//        jdbi.useExtension(ProductReviewIDAO.class, dao -> dao.show(id));
//    }
//}
