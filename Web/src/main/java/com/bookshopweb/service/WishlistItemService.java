//package com.bookshopweb.service;
//
//import com.bookshopweb.beans.WishlistItem;
//import com.bookshopweb.dao.WishlistItemIDAO;
//
//import java.util.List;
//
//public class WishlistItemService extends Service<WishlistItem, WishlistItemIDAO> implements WishlistItemIDAO {
//    public WishlistItemService() {
//        super(WishlistItemIDAO.class);
//    }
//
//    @Override
//    public List<WishlistItem> getByUserId(long userId) {
//        return jdbi.withExtension(WishlistItemIDAO.class, dao -> dao.getByUserId(userId));
//    }
//
//    @Override
//    public int countByUserIdAndProductId(long userId, long productId) {
//        return jdbi.withExtension(WishlistItemIDAO.class, dao -> dao.countByUserIdAndProductId(userId, productId));
//    }
//}
