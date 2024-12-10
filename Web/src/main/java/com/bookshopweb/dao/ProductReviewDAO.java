//package com.bookshopweb.dao;
//
//import com.bookshopweb.beans.ProductReview;
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
//@RegisterBeanMapper(ProductReview.class)
//public interface ProductReviewIDAO extends IDAO<ProductReview> {
//    @Override
//    @SqlUpdate("INSERT INTO product_review VALUES (default, :userId, :productId, :ratingScore, :content, :isShow, " +
//               ":createdAt, :updatedAt)")
//    @GetGeneratedKeys("id")
//    long insert(@BindBean ProductReview productReview);
//
//    @Override
//    @SqlUpdate("UPDATE product_review SET ratingScore = :ratingScore, content = :content, updatedAt = NOW() WHERE id = :id")
//    void update(@BindBean ProductReview productReview);
//
//    @Override
//    @SqlUpdate("DELETE FROM product_review WHERE id = :id")
//    void delete(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM product_review WHERE id = :id")
//    Optional<ProductReview> getById(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM product_review")
//    List<ProductReview> getAll();
//
//    @Override
//    @SqlQuery("SELECT * FROM product_review LIMIT :limit OFFSET :offset")
//    List<ProductReview> getPart(@Bind("limit") int limit, @Bind("offset") int offset);
//
//    @Override
//    @SqlQuery("SELECT * FROM product_review ORDER BY <orderBy> <orderDir> LIMIT :limit OFFSET :offset")
//    List<ProductReview> getOrderedPart(@Bind("limit") int limit, @Bind("offset") int offset,
//                                       @Define("orderBy") String orderBy, @Define("orderDir") String orderDir);
//
//    @SqlQuery("SELECT pr.*, u.fullname user_fullname " +
//              "FROM product_review pr " +
//              "JOIN user u ON pr.userId = u.id " +
//              "WHERE productId = :productId " +
//              "ORDER BY <orderBy> <orderDir> " +
//              "LIMIT :limit OFFSET :offset")
//    List<ProductReview> getOrderedPartByProductId(@Bind("limit") int limit, @Bind("offset") int offset,
//                                                  @Define("orderBy") String orderBy, @Define("orderDir") String orderDir,
//                                                  @Bind("productId") long productId);
//
//    @SqlQuery("SELECT COUNT(id) FROM product_review WHERE productId = :productId")
//    int countByProductId(@Bind("productId") long productId);
//
//    @SqlQuery("SELECT SUM(ratingScore) FROM product_review WHERE productId = :productId")
//    int sumRatingScoresByProductId(@Bind("productId") long productId);
//
//    @SqlQuery("SELECT COUNT(id) FROM product_review")
//    int count();
//
//    @SqlUpdate("UPDATE product_review SET isShow = 0, updatedAt = NOW() WHERE id = :id")
//    void hide(@Bind("id") long id);
//
//    @SqlUpdate("UPDATE product_review SET isShow = 1, updatedAt = NOW() WHERE id = :id")
//    void show(@Bind("id") long id);
//}
package com.bookshopweb.dao;

import com.bookshopweb.beans.Product;
import com.bookshopweb.beans.ProductReview;
import com.bookshopweb.beans.User;
import com.bookshopweb.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductReviewDAO extends AbsDAO<ProductReview> {
    Connection conn = JDBCUtils.getConnection();

    public ProductReview selectPrevalue(Long id){
        ProductReview result = null;
        try {
            String sql = "select * from product_review where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                long userId = rs.getLong("userId");
                long productId = rs.getLong("productId");
                int ratingScore = rs.getInt("ratingScore");
                String content = rs.getString("content");
                int isShow = rs.getInt("isShow");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                result = new ProductReview(id, userId, productId, ratingScore, content, isShow, createdAt, updatedAt);

            }
            rs.close();
            st.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public ProductReview selectByProduct(Product product){
        ProductReview result = null;
        try {
            String sql = "select * from product_review where productId=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, product.getId());
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                long id = rs.getLong("id");
                long userId = rs.getLong("userId");
                long productId = rs.getLong("productId");
                int ratingScore = rs.getInt("ratingScore");
                String content = rs.getString("content");
                int isShow = rs.getInt("isShow");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                result = new ProductReview(id, userId, productId, ratingScore, content, isShow, createdAt, updatedAt);
                rs.close();
                st.close();
            }
            st.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    @Override
    public int insert(ProductReview productReview, String ip) {

        int result = 0;
        try {
            String sql = "insert into product_review (id, userId, productId,ratingScore,content,isShow ,createdAt,updatedAt)" +
                    " values(?,?,?,?,?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1,productReview.getId());
            st.setLong(2, productReview.getUserId());
            st.setLong(3, productReview.getProductId());
            st.setInt(4, productReview.getRatingScore());
            st.setString(5, productReview.getContent());
            st.setInt(6, productReview.getIsShow());
            st.setTimestamp(7, productReview.getCreatedAt());
            st.setTimestamp(8, productReview.getUpdatedAt());
            result = st.executeUpdate();
            st.close();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.insert(productReview, ip);
        return result;
    }

    @Override
    public int update(ProductReview productReview, String ip) {
        super.update(productReview, ip);
        int result = 0;
        try {
            String sql = "update product_review set id=?, userId=?, productId=?, ratingScore=?, content=?, isShow=?, createdAt=?, updatedAt=?" +
                    " where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1,productReview.getId());
            st.setLong(2, productReview.getUserId());
            st.setLong(3, productReview.getProductId());
            st.setInt(4, productReview.getRatingScore());
            st.setString(5, productReview.getContent());
            st.setInt(6, productReview.getIsShow());
            st.setTimestamp(7, productReview.getCreatedAt());
            st.setTimestamp(8, productReview.getUpdatedAt());
            st.setLong(9, productReview.getProductId());
            result = st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int delete(ProductReview productReview, String ip) {

        int result = 0;
        try {
            String sql = "delete from product_review where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, productReview.getId());
            result = st.executeUpdate();
            st.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.delete(productReview, ip);
        return result;
    }
    public int deleteByUserId(long userId) {

        int result = 0;
        try{
            String sql = "delete from product_review where userId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,userId);
            result = ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public List<ProductReview> getAll() {
        List<ProductReview> reviews = new ArrayList<>();
        String query = "SELECT * FROM product_review";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ProductReview review = mapResultSetToProductReview(resultSet);
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return reviews;
    }

    public List<ProductReview> getPart(int limit, int offset) {
        List<ProductReview> reviews = new ArrayList<>();
        String query = "SELECT * FROM product_review LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ProductReview review = mapResultSetToProductReview(resultSet);
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return reviews;
    }

    public List<ProductReview> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        List<ProductReview> reviews = new ArrayList<>();
        String query = "SELECT * FROM product_review ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ProductReview review = mapResultSetToProductReview(resultSet);
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return reviews;
    }

    public List<ProductReview> getOrderedPartByProductId(int limit, int offset, String orderBy, String orderDir, long productId) {
        List<ProductReview> reviews = new ArrayList<>();
        String query = "SELECT pr.*, u.fullname user_fullname " +
                "FROM product_review pr " +
                "JOIN user u ON pr.userId = u.id " +
                "WHERE productId = ? " +
                "ORDER BY " + orderBy + " " + orderDir + " " +
                "LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, productId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ProductReview review = mapResultSetToProductReview(resultSet);
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return reviews;
    }

    public int countByProductId(long productId) {
        int count = 0;
        String query = "SELECT COUNT(id) FROM product_review WHERE productId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return count;
    }

    public int sumRatingScoresByProductId(long productId) {
        int sum = 0;
        String query = "SELECT SUM(ratingScore) FROM product_review WHERE productId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    sum = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return sum;
    }

    public int count() {
        int count = 0;
        String query = "SELECT COUNT(id) FROM product_review";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return count;
    }

    public void hide(long id) {
        String query = "UPDATE product_review SET isShow = 0, updatedAt = NOW() WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void show(long id) {
        String query = "UPDATE product_review SET isShow = 1, updatedAt = NOW() WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private ProductReview mapResultSetToProductReview(ResultSet resultSet) throws SQLException {
        ProductReview review = new ProductReview();
        review.setId(resultSet.getLong("id"));
        review.setUserId(resultSet.getLong("userId"));
        review.setProductId(resultSet.getLong("productId"));
        review.setRatingScore(resultSet.getInt("ratingScore"));
        review.setContent(resultSet.getString("content"));
        review.setIsShow(resultSet.getInt("isShow"));
        review.setCreatedAt(resultSet.getTimestamp("createdAt"));
        review.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        UserDAO userDao = new UserDAO();
        review.setUser(userDao.selectPrevalue(resultSet.getLong("userId")));

        ProductDAO productDAO = new ProductDAO();
        review.setProduct( productDAO.selectPrevalue(resultSet.getLong("productId")));

        return review;
    }
}