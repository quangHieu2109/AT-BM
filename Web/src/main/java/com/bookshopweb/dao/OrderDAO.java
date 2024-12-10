//package com.bookshopweb.dao;
//
//import com.bookshopweb.beans.Order;
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
//@RegisterBeanMapper(Order.class)
//public interface OrderIDAO extends IDAO<Order> {
//    @Override
//    @SqlUpdate("INSERT INTO orders VALUES (default, :userId, :status, :deliveryMethod, :deliveryPrice, :createdAt, :updatedAt)")
//    @GetGeneratedKeys("id")
//    long insert(@BindBean Order order);
//
//    @Override
//    @SqlUpdate("UPDATE orders SET userId = :userId, status = :status, deliveryMethod = :deliveryMethod, " +
//               "deliveryPrice = :deliveryPrice, createdAt = :createdAt, updatedAt = :updatedAt WHERE id = :id")
//    void update(@BindBean Order order);
//
//    @Override
//    @SqlUpdate("DELETE FROM orders WHERE id = :id")
//    void delete(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM orders WHERE id = :id")
//    Optional<Order> getById(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM orders")
//    List<Order> getAll();
//
//    @Override
//    @SqlQuery("SELECT * FROM orders LIMIT :limit OFFSET :offset")
//    List<Order> getPart(@Bind("limit") int limit, @Bind("offset") int offset);
//
//    @Override
//    @SqlQuery("SELECT * FROM orders ORDER BY <orderBy> <orderDir> LIMIT :limit OFFSET :offset")
//    List<Order> getOrderedPart(@Bind("limit") int limit, @Bind("offset") int offset,
//                               @Define("orderBy") String orderBy, @Define("orderDir") String orderDir);
//
//    @SqlQuery("SELECT * FROM orders WHERE userId = :userId ORDER BY orders.createdAt DESC LIMIT :limit OFFSET :offset ")
//    List<Order> getOrderedPartByUserId(@Bind("userId") long userId, @Bind("limit") int limit, @Bind("offset") int offset);
//
//    @SqlQuery("SELECT COUNT(orders.id) FROM orders WHERE userId = :userId")
//    int countByUserId(@Bind("userId") long userId);
//
//    @SqlUpdate("UPDATE orders SET status = 3, updatedAt = NOW() WHERE id = :id")
//    void cancelOrder(@Bind("id") long id);
//
//    @SqlQuery("SELECT COUNT(id) FROM orders")
//    int count();
//
//    @SqlUpdate("UPDATE orders SET status = 2, updatedAt = NOW() WHERE id = :id")
//    void confirm(@Bind("id") long id);
//
//    @SqlUpdate("UPDATE orders SET status = 3, updatedAt = NOW() WHERE id = :id")
//    void cancel(@Bind("id") long id);
//
//    @SqlUpdate("UPDATE orders SET status = 1, updatedAt = NOW() WHERE id = :id")
//    void reset(@Bind("id") long id);
//}
package com.bookshopweb.dao;

import com.bookshopweb.beans.Order;
import com.bookshopweb.jdbiInterface.OrderJDBI;
import com.bookshopweb.utils.JDBCUtils;
import com.bookshopweb.utils.JDBIUltis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAO extends AbsDAO<Order> {
    Connection conn = JDBCUtils.getConnection();
    static OrderJDBI orderJDBI = JDBIUltis.getJDBI().onDemand(OrderJDBI.class);
    public List<Order> getByStatus(int status){
        return orderJDBI.getByStatus(status);
    }
    public int getQuantity(int status){
        return orderJDBI.getQuantityByStatus(status);
    }
    public List<Order> getByStatusLimit(int status, int start, int length){
        return orderJDBI.getByStatusLimit(status, start, length);
    }
    public int updateStatus(int status, long id){
        return orderJDBI.updateStatus(status, id);
    }
    public int deleteByUserId(long userId) {

        int result = 0;
        try{
            if(getOrderByUserId(userId).size() >0) {
                for (Order order : getOrderByUserId(userId)) {
                    new OrderItemDAO().deleteByOrderId(order.getId());
                }
            }
            String sql = "delete from orders where userId=?";
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
    public Order selectPrevalue(Long id) {
        Order result = null;
        try {
            String sql = "select * from orders where id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                long userId = rs.getLong("userId");
                int status = rs.getInt("status");
                int deliveryMethod = rs.getInt("deliveryMethod");
                double deliveryPrice = rs.getDouble("deliveryPrice");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                result = new Order(id, userId, status, deliveryMethod, deliveryPrice, createdAt, updatedAt);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int insert(Order order, String ip) {

        int result = 0;
        try {
            String sql = "insert into orders (id, userId, status, deliveryMethod, deliveryPrice, createdAt, updatedAt) " +
                    "values(?,?,?,?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, order.getId());
            st.setLong(2, order.getUserId());
            st.setInt(3, order.getStatus());
            st.setInt(4, order.getDeliveryMethod());
            st.setDouble(5, order.getDeliveryPrice());
            st.setTimestamp(6, order.getCreatedAt());
            st.setTimestamp(7, order.getUpdatedAt());
            result = st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.insert(order, ip);
        return result;

    }

    @Override
    public int update(Order order, String ip) {
        super.update(order, ip);
        int result = 0;
        try {
            String sql = "update orders set id=?, userId=?, status=?, deliveryMethod=?, deliveryPrice=?, createdAt=?, updatedAt=? " +
                    "where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, order.getId());
            st.setLong(2, order.getUserId());
            st.setInt(3, order.getStatus());
            st.setInt(4, order.getDeliveryMethod());
            st.setDouble(5, order.getDeliveryPrice());
            st.setTimestamp(6, order.getCreatedAt());
            st.setTimestamp(7, order.getUpdatedAt());
            st.setLong(8, order.getId());
            result = st.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int delete(Order order, String ip) {

        int result = 0;
        try {
            String sql = "delete from orders where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, order.getId());
            result = st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.delete(order, ip);
        return result;
    }
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Order order = mapResultSetToOrder(resultSet);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return orders;
    }

    public List<Order> getPart(int limit, int offset) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = mapResultSetToOrder(resultSet);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return orders;
    }

    public List<Order> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = mapResultSetToOrder(resultSet);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return orders;
    }
    public List<Order> getOrderByUserId(long userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE userId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = mapResultSetToOrder(resultSet);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return orders;
    }
    public List<Order> getOrderedPartByUserId(long userId, int limit, int offset) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE userId = ? ORDER BY orders.createdAt DESC LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, userId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = mapResultSetToOrder(resultSet);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return orders;
    }
    public Optional<Order> getById(long id) {
        Optional<Order> order = Optional.empty();
        String query = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    order = Optional.of(mapResultSetToOrder(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
        return order;
    }
    public int countByUserId(long userId) {
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

    public int count() {
        int count = 0;
        String query = "SELECT COUNT(id) FROM orders";
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

    public void cancelOrder(long id) {
        String query = "UPDATE orders SET status = 3, updatedAt = NOW() WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void confirm(long id) {
        String query = "UPDATE orders SET status = 2, updatedAt = NOW() WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void cancel(long id) {
        String query = "UPDATE orders SET status = 3, updatedAt = NOW() WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void reset(long id) {
        String query = "UPDATE orders SET status = 1, updatedAt = NOW() WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }



    private Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setUserId(resultSet.getLong("userId"));
        order.setStatus(resultSet.getInt("status"));
        order.setDeliveryMethod(resultSet.getInt("deliveryMethod"));
        order.setDeliveryPrice(resultSet.getDouble("deliveryPrice"));
        order.setCreatedAt(resultSet.getTimestamp("createdAt"));
        order.setUpdatedAt(resultSet.getTimestamp("updatedAt"));


        return order;
    }
}