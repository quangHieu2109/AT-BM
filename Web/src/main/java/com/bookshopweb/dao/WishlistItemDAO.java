//package com.bookshopweb.dao;
//
//import com.bookshopweb.beans.WishlistItem;
//import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
//import org.jdbi.v3.sqlobject.customizer.Bind;
//import org.jdbi.v3.sqlobject.customizer.BindBean;
//import org.jdbi.v3.sqlobject.customizer.Define;
//import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
//import org.jdbi.v3.sqlobject.statement.SqlQuery;
//import org.jdbi.v3.sqlobject.statement.SqlUpdate;
//
//import java.util.List;
//import java.util.Optional;
//
//@RegisterBeanMapper(WishlistItem.class)
//public interface WishlistItemIDAO extends IDAO<WishlistItem> {
//    @Override
//    @SqlUpdate("INSERT INTO wishlist_item VALUES (default, :userId, :productId, NOW())")
//    @GetGeneratedKeys("id")
//    long insert(@BindBean WishlistItem wishlistItem);
//
//    @Override
//    @SqlUpdate("UPDATE wishlist_item SET userId = :userId, productId = :productId, createdAt = :createdAt WHERE id = :id")
//    void update(@BindBean WishlistItem wishlistItem);
//
//    @Override
//    @SqlUpdate("DELETE FROM wishlist_item WHERE id = :id")
//    void delete(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM wishlist_item WHERE id = :id")
//    Optional<WishlistItem> getById(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM wishlist_item")
//    List<WishlistItem> getAll();
//
//    @Override
//    @SqlQuery("SELECT * FROM wishlist_item LIMIT :limit OFFSET :offset")
//    List<WishlistItem> getPart(@Bind("limit") int limit, @Bind("offset") int offset);
//
//    @Override
//    @SqlQuery("SELECT * FROM wishlist_item ORDER BY <orderBy> <orderDir> LIMIT :limit OFFSET :offset")
//    List<WishlistItem> getOrderedPart(@Bind("limit") int limit, @Bind("offset") int offset,
//                                      @Define("orderBy") String orderBy, @Define("orderDir") String orderDir);
//
//    @SqlQuery("SELECT * FROM wishlist_item WHERE userId = :userId")
//    List<WishlistItem> getByUserId(@Bind("userId") long userId);
//
//    @SqlQuery("SELECT COUNT(id) FROM wishlist_item WHERE userId = :userId AND productId = :productId")
//    int countByUserIdAndProductId(@Bind("userId") long userId, @Bind("productId") long productId);
//}
package com.bookshopweb.dao;

import com.bookshopweb.beans.WishlistItem;
import com.bookshopweb.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WishlistItemDAO extends AbsDAO<WishlistItem>{
    Connection conn = JDBCUtils.getConnection();
    public int deleteByUserId(long userId) {

        int result = 0;
        try{
            String sql = "delete from wishlist_item where userId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,userId);
            result = ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    @Override
    public int delete(WishlistItem wishlistItem, String ip) {

        int result = 0;
        try{
            String sql = "delete from wishlist_item where id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,wishlistItem.getId());
            result = ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.delete(wishlistItem, ip);
        return result;
    }

    @Override
    public int update(WishlistItem wishlistItem, String ip) {
        super.update(wishlistItem, ip);        int result = 0;
        try{
            String sql = "update wishlist_item set id=?, userId=?, productId=?,createdAt=?" +
                    " where id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,wishlistItem.getId());
            ps.setLong(2, wishlistItem.getUserId());
            ps.setLong(3, wishlistItem.getProductId());
            ps.setTimestamp(4,wishlistItem.getCreatedAt());
            ps.setLong(5,wishlistItem.getId());
            result = ps.executeUpdate();
            ps.close();

        }   catch (Exception e) {

        }

        return result;
    }

    @Override
    public WishlistItem selectPrevalue(Long id) {
        WishlistItem result = null;
        try {
            String sql = "select * from wishlist_item where id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                long userId = rs.getLong("userId");
                long productId= rs.getLong("productId");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                result = new WishlistItem(id, userId, productId, createdAt);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int insert(WishlistItem wishlistItem, String ip) {

        int result = 0;
        try{
            String sql = "insert into product_review (id, userId, productId, createdAt)" +
                    " values(?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1,wishlistItem.getId());
            st.setLong(2, wishlistItem.getUserId());
            st.setLong(3, wishlistItem.getProductId());
            st.setTimestamp(4,wishlistItem.getCreatedAt());
            result = st.executeUpdate();
            st.close();

        }catch (Exception e) {
            e.printStackTrace();

        }
        super.insert(wishlistItem, ip);
        return result;
    }
    public Optional<WishlistItem> getById(long id) {
        Optional<WishlistItem> wishlistItem = Optional.empty();
        String query = "SELECT * FROM wishlist_item WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    wishlistItem = Optional.of(mapResultSetToWishlistItem(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
        return wishlistItem;
    }

    public List<WishlistItem> getAll() {
        List<WishlistItem> wishlistItems = new ArrayList<>();
        String query = "SELECT * FROM wishlist_item";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    wishlistItems.add(mapResultSetToWishlistItem(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
        return wishlistItems;
    }

    public List<WishlistItem> getPart(int limit, int offset) {
        List<WishlistItem> wishlistItems = new ArrayList<>();
        String query = "SELECT * FROM wishlist_item LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    wishlistItems.add(mapResultSetToWishlistItem(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
        return wishlistItems;
    }
    public List<WishlistItem> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        List<WishlistItem> wishlistItems = new ArrayList<>();
        String query = "SELECT * FROM wishlist_item ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    wishlistItems.add(mapResultSetToWishlistItem(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
        return wishlistItems;
    }

    public List<WishlistItem> getByUserId(long userId) {
        List<WishlistItem> wishlistItems = new ArrayList<>();
        String query = "SELECT * FROM wishlist_item WHERE userId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    wishlistItems.add(mapResultSetToWishlistItem(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
        return wishlistItems;
    }

    public int countByUserIdAndProductId(long userId, long productId) {
        int count = 0;
        String query = "SELECT COUNT(id) FROM wishlist_item WHERE userId = ? AND productId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, userId);
            statement.setLong(2, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
        return count;
    }
    private WishlistItem mapResultSetToWishlistItem(ResultSet resultSet) throws SQLException {
        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setId(resultSet.getLong("id"));
        wishlistItem.setUserId(resultSet.getLong("userId"));
        wishlistItem.setProductId(resultSet.getLong("productId"));
        wishlistItem.setCreatedAt(resultSet.getTimestamp("createdAt"));

        // Map product
        ProductDAO productDAO = new ProductDAO();
        wishlistItem.setProduct( productDAO.selectPrevalue(resultSet.getLong("productId")));

        return wishlistItem;
    }
}