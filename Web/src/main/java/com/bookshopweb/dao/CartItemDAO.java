//package com.bookshopweb.dao;
//
//import com.bookshopweb.beans.CartItem;
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
//@RegisterBeanMapper(CartItem.class)
//public interface CartItemIDAO extends IDAO<CartItem> {
//    @Override
//    @SqlUpdate("INSERT INTO cart_item VALUES (default, :cartId, :productId, :quantity, :createdAt, :updatedAt)")
//    @GetGeneratedKeys("id")
//    long insert(@BindBean CartItem cartItem);
//
//    @Override
//    @SqlUpdate("UPDATE cart_item SET cartId = :cartId, productId = :productId, quantity = :quantity, " +
//               "createdAt = :createdAt, updatedAt = :updatedAt WHERE id = :id")
//    void update(@BindBean CartItem cartItem);
//
//    @Override
//    @SqlUpdate("DELETE FROM cart_item WHERE id = :id")
//    void delete(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM cart_item WHERE id = :id")
//    Optional<CartItem> getById(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM cart_item")
//    List<CartItem> getAll();
//
//    @Override
//    @SqlQuery("SELECT * FROM cart_item LIMIT :limit OFFSET :offset")
//    List<CartItem> getPart(@Bind("limit") int limit, @Bind("offset") int offset);
//
//    @Override
//    @SqlQuery("SELECT * FROM cart_item ORDER BY <orderBy> <orderDir> LIMIT :limit OFFSET :offset")
//    List<CartItem> getOrderedPart(@Bind("limit") int limit, @Bind("offset") int offset,
//                                  @Define("orderBy") String orderBy, @Define("orderDir") String orderDir);
//
//    @SqlQuery("SELECT ci.*, p.name product_name, p.price product_price, p.discount product_discount, " +
//              "p.quantity product_quantity, p.imageName product_imageName " +
//              "FROM cart_item ci " +
//              "JOIN product p on p.id = ci.productId " +
//              "WHERE cartId = :cartId " +
//              "ORDER BY createdAt DESC")
//    List<CartItem> getByCartId(@Bind("cartId") long cartId);
//
//    @SqlQuery("SELECT * FROM cart_item WHERE cartId = :cartId AND productId = :productId")
//    Optional<CartItem> getByCartIdAndProductId(@Bind("cartId") long cartId, @Bind("productId") long productId);
//
//    @SqlQuery("SELECT SUM(ci.quantity) FROM cart_item ci JOIN cart c on c.id = ci.cartId WHERE c.userId = :userId;")
//    int sumQuantityByUserId(@Bind("userId") long userId);
//}
package com.bookshopweb.dao;

import com.bookshopweb.beans.Cart;
import com.bookshopweb.beans.CartItem;
import com.bookshopweb.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartItemDAO extends AbsDAO<CartItem> {
    Connection conn = JDBCUtils.getConnection();
    public int deleteByCartId(long cartId) {

        int result = 0;
        try{
            String sql = "delete from cart_item where cartId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,cartId);
            result = ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public List<CartItem> selectByCart(Cart cart){
        List<CartItem> result = new ArrayList<>();
        Connection conn = JDBCUtils.getConnection();
        try {
            String sql = "select * from cart_item where cartId=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1,cart.getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                Long id = rs.getLong("id");
                Long productId = rs.getLong("productId");
                int quantity = rs.getInt("quantity");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                result.add(new CartItem(id, cart.getId(), productId, quantity, createdAt, updatedAt));

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public CartItem selectPrevalue(Long id){
        CartItem result = null;
        try {
            String sql = "select * from cart_item where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1,id);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                Long productId = rs.getLong("productId");
                int quantity = rs.getInt("quantity");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                long cartId = rs.getLong("cartId");
                result=(new CartItem(id, cartId, productId, quantity, createdAt, updatedAt));

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    @Override
    public int delete(CartItem cartItem, String ip) {

        int result =0;
        Connection conn = JDBCUtils.getConnection();

        try {
            String sql = "delete from cart_item where id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, cartItem.getId());
            result = st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.delete(cartItem, ip);
        return result;
    }

    @Override
    public int update(CartItem cartItem, String ip) {
        super.update(cartItem, ip);
        int result =0;
        Connection conn = JDBCUtils.getConnection();
        try {
            String sql = "update cart_item set " +
                    "quantity=?, updatedAt=? where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, cartItem.getQuantity());
            st.setTimestamp(2, cartItem.getUpdatedAt());
            st.setLong(3, cartItem.getId());
            result = st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int insert(CartItem cartItem, String ip) {

        int result = 0;
        Connection conn = JDBCUtils.getConnection();
        try {
            String sql = "insert into cart_item (id, cartId, productId, quantity, createdAt, updatedAt) " +
                    "values(?,?,?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, cartItem.getId());
            st.setLong(2, cartItem.getCartId());
            st.setLong(3, cartItem.getProductId());
            st.setInt(4, cartItem.getQuantity());
            st.setTimestamp(5, cartItem.getCreatedAt());
            st.setTimestamp(6, cartItem.getUpdatedAt());
            result = st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.insert(cartItem, ip);
        return result;
    }
    public Optional<CartItem> getById(long id) {
        Optional<CartItem> cartItem = Optional.empty();
        String query = "SELECT * FROM cart_item WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    cartItem = Optional.of(mapResultSetToCartItem(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return cartItem;
    }

    public List<CartItem> getAll() {
        List<CartItem> cartItems = new ArrayList<>();
        String query = "SELECT * FROM cart_item";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                CartItem cartItem = mapResultSetToCartItem(resultSet);
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return cartItems;
    }

    public List<CartItem> getPart(int limit, int offset) {
        List<CartItem> cartItems = new ArrayList<>();
        String query = "SELECT * FROM cart_item LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CartItem cartItem = mapResultSetToCartItem(resultSet);
                    cartItems.add(cartItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return cartItems;
    }

    public List<CartItem> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        List<CartItem> cartItems = new ArrayList<>();
        String query = "SELECT * FROM cart_item ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CartItem cartItem = mapResultSetToCartItem(resultSet);
                    cartItems.add(cartItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return cartItems;
    }

    public List<CartItem> getByCartId(long cartId) {
        List<CartItem> cartItems = new ArrayList<>();
        String query = "SELECT ci.*, p.name product_name, p.price product_price, p.discount product_discount, " +
                "p.quantity product_quantity, p.imageName product_imageName " +
                "FROM cart_item ci " +
                "JOIN product p on p.id = ci.productId " +
                "WHERE cartId = ? " +
                "ORDER BY createdAt DESC";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, cartId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CartItem cartItem = mapResultSetToCartItem(resultSet);
                    cartItems.add(cartItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return cartItems;
    }

    public Optional<CartItem> getByCartIdAndProductId(long cartId, long productId) {
        Optional<CartItem> cartItem = Optional.empty();
        String query = "SELECT * FROM cart_item WHERE cartId = ? AND productId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, cartId);
            statement.setLong(2, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    cartItem = Optional.of(mapResultSetToCartItem(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return cartItem;
    }

    public int sumQuantityByUserId(long userId) {
        int sum = 0;
        String query = "SELECT SUM(ci.quantity) FROM cart_item ci JOIN cart c on c.id = ci.cartId WHERE c.userId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, userId);
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

    // Other methods go here...

    private CartItem mapResultSetToCartItem(ResultSet resultSet) throws SQLException {
            CartItem cartItem = new CartItem();
            cartItem.setId(resultSet.getLong("id"));
            cartItem.setCartId(resultSet.getLong("cartId"));
            cartItem.setProduct(new ProductDAO().getById(resultSet.getLong("productId")).get());
            cartItem.setProductId(resultSet.getLong("productId"));
            cartItem.setQuantity(resultSet.getInt("quantity"));
            cartItem.setCreatedAt(resultSet.getTimestamp("createdAt"));
            cartItem.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        return cartItem;
    }
    }


