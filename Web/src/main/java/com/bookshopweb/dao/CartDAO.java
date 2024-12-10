//package com.bookshopweb.dao;
//
//import com.bookshopweb.beans.Cart;
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
//@RegisterBeanMapper(Cart.class)
//public interface CartIDAO extends IDAO<Cart> {
//    @Override
//    @SqlUpdate("INSERT INTO cart VALUES (default, :userId, :createdAt, :updatedAt)")
//    @GetGeneratedKeys("id")
//    long insert(@BindBean Cart cart);
//
//    @Override
//    @SqlUpdate("UPDATE cart SET userId = :userId, createdAt = :createdAt, updatedAt = :updatedAt WHERE id = :id")
//    void update(@BindBean Cart cart);
//
//    @Override
//    @SqlUpdate("DELETE FROM cart WHERE id = :id")
//    void delete(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM cart WHERE id = :id")
//    Optional<Cart> getById(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM cart")
//    List<Cart> getAll();
//
//    @Override
//    @SqlQuery("SELECT * FROM cart LIMIT :limit OFFSET :offset")
//    List<Cart> getPart(@Bind("limit") int limit, @Bind("offset") int offset);
//
//    @Override
//    @SqlQuery("SELECT * FROM cart ORDER BY <orderBy> <orderDir> LIMIT :limit OFFSET :offset")
//    List<Cart> getOrderedPart(@Bind("limit") int limit, @Bind("offset") int offset,
//                              @Define("orderBy") String orderBy, @Define("orderDir") String orderDir);
//
//    @SqlQuery("SELECT * FROM cart WHERE userId = :userId")
//    Optional<Cart> getByUserId(@Bind("userId") long userId);
//
//    @SqlQuery("SELECT SUM(ci.quantity) FROM cart c JOIN cart_item ci ON c.id = ci.cartId WHERE c.userId = :userId")
//    int countCartItemQuantityByUserId(@Bind("userId") long userId);
//
//    @SqlQuery("SELECT COUNT(orders.id) FROM orders WHERE userId = :userId")
//    int countOrderByUserId(@Bind("userId") long userId);
//
//    @SqlQuery("SELECT COUNT(orders.id) FROM orders WHERE userId = :userId AND status = 1")
//    int countOrderDeliverByUserId(@Bind("userId") long userId);
//
//    @SqlQuery("SELECT COUNT(orders.id) FROM orders WHERE userId = :userId AND status = 2")
//    int countOrderReceivedByUserId(@Bind("userId") long userId);
//}
package com.bookshopweb.dao;

import com.bookshopweb.beans.AbsModel;
import com.bookshopweb.beans.Cart;
import com.bookshopweb.beans.CartItem;
import com.bookshopweb.beans.User;
import com.bookshopweb.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartDAO extends AbsDAO<Cart> {

    Connection conn = JDBCUtils.getConnection();
    public int deleteByUserId(long userId) {

        int result = 0;
        try{
            if(selectByUserId(userId) != null) {
                new CartItemDAO().deleteByCartId(selectByUserId(userId).getId());
            }
            String sql = "delete from cart where userId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,userId);
            result = ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public Cart selectPrevalue(Long id){
        Cart result = null;
        CartItemDAO dao = new CartItemDAO();
        try {
            String sql="select * from cart where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()){

                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                long userId = rs.getLong("userId");
                result = new Cart(id, userId, createdAt, updatedAt);
                result.addCartItem(dao.selectByCart(result));

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public Cart selectByUserId(long userId){
        Cart result = null;
        CartItemDAO dao = new CartItemDAO();
        try {
            String sql="select * from cart where userId=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                long id = rs.getLong("id");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                result = new Cart(id, userId, createdAt, updatedAt);
//                result.addCartItem(dao.selectByCart(result));

            }
            rs.close();
            st.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public Cart selectByUser(User user){
        Cart result = null;
        CartItemDAO dao = new CartItemDAO();
        try {
            String sql="select * from cart where userId=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, user.getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                long id = rs.getLong("id");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                result = new Cart(id, user.getId(), createdAt, updatedAt);
                result.addCartItem(dao.selectByCart(result));

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    @Override
    public int insert(Cart cart, String ip) {

        int result = 0;

        try {
            String sql = "insert into cart (id, userId, createdAt, updatedAt) " +
                    "values (?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, cart.getId());
            st.setLong(2,cart.getUserId());
            st.setTimestamp(3, cart.getCreatedAt());
            st.setTimestamp(4, cart.getUpdatedAt());

            result = st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.insert(cart, ip);
        return result;

    }

    @Override
    public int update(Cart cart, String ip) {
        super.update(cart, ip);
        int result =0;
        try {
            String sql = "update cart set updatedAt=? where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setTimestamp(1, cart.getUpdatedAt());
            st.setLong(2, cart.getId());
            result = st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int delete(Cart cart, String ip) {

        int result =0;
        try {
            String sql = "delete from cart where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, cart.getId());
            result = st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.delete(cart, ip);
        return result;
    }
    public Optional<Cart> getById(long id) {
        Optional<Cart> cart = Optional.empty();
        String query = "SELECT * FROM cart WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    cart = Optional.of(mapResultSetToCart(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
        return cart;
    }
    public List<Cart> getAll() {
        List<Cart> carts = new ArrayList<>();
        String query = "SELECT * FROM cart";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Cart cart = mapResultSetToCart(resultSet);
                carts.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return carts;
    }

    public List<Cart> getPart(int limit, int offset) {
        List<Cart> carts = new ArrayList<>();
        String query = "SELECT * FROM cart LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Cart cart = mapResultSetToCart(resultSet);
                    carts.add(cart);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return carts;
    }

    public List<Cart> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        List<Cart> carts = new ArrayList<>();
        String query = "SELECT * FROM cart ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Cart cart = mapResultSetToCart(resultSet);
                    carts.add(cart);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return carts;
    }

    public Optional<Cart> getByUserId(long userId) {
        Optional<Cart> cart = Optional.empty();
        String query = "SELECT * FROM cart WHERE userId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    cart = Optional.of(mapResultSetToCart(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return cart;
    }

    public int countCartItemQuantityByUserId(long userId) {
        int quantity = 0;
        String query = "SELECT SUM(ci.quantity) FROM cart c JOIN cart_item ci ON c.id = ci.cartId WHERE c.userId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    quantity = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return quantity;
    }

    public int countOrderByUserId(long userId) {
        int count = 0;
        String query = "SELECT COUNT(orders.id) FROM orders WHERE userId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, userId);
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

    public int countOrderDeliverByUserId(long userId) {
        int count = 0;
        String query = "SELECT COUNT(orders.id) FROM orders WHERE userId = ? AND status = 1";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, userId);
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

    public int countOrderReceivedByUserId(long userId) {
        int count = 0;
        String query = "SELECT COUNT(orders.id) FROM orders WHERE userId = ? AND status = 2";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, userId);
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


    private Cart mapResultSetToCart(ResultSet resultSet) throws SQLException {
        Cart cart = new Cart();
        cart.setId(resultSet.getLong("id"));
        cart.setUserId(resultSet.getLong("userId"));
        cart.setCreatedAt(resultSet.getTimestamp("createdAt"));
        cart.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        CartItemDAO cartItemDAO = new CartItemDAO();

        cart.addCartItem(cartItemDAO.getByCartId(resultSet.getLong("id")));
        return cart;
    }
}
