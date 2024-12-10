//package com.bookshopweb.dao;
//
//import com.bookshopweb.beans.OrderItem;
//import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
//import org.jdbi.v3.sqlobject.customizer.Bind;
//import org.jdbi.v3.sqlobject.customizer.BindBean;
//import org.jdbi.v3.sqlobject.customizer.Define;
//import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
//import org.jdbi.v3.sqlobject.statement.SqlBatch;
//import org.jdbi.v3.sqlobject.statement.SqlQuery;
//import org.jdbi.v3.sqlobject.statement.SqlUpdate;
//
//import java.util.List;
//import java.util.Optional;
//
//@RegisterBeanMapper(OrderItem.class)
//public interface OrderItemIDAO extends IDAO<OrderItem> {
//    @Override
//    @SqlUpdate("INSERT INTO order_item VALUES (default, :orderId, :productId, :price, :discount, :quantity, :createdAt, :updatedAt)")
//    @GetGeneratedKeys("id")
//    long insert(@BindBean OrderItem orderItem);
//
//    @Override
//    @SqlUpdate("UPDATE order_item SET orderId = :orderId, productId = :productId, price = :price, discount = :discount, " +
//               "quantity = :quantity, createdAt = :createdAt, updatedAt = :updatedAt WHERE id = :id")
//    void update(@BindBean OrderItem orderItem);
//
//    @Override
//    @SqlUpdate("DELETE FROM order_item WHERE id = :id")
//    void delete(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM order_item WHERE id = :id")
//    Optional<OrderItem> getById(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM order_item")
//    List<OrderItem> getAll();
//
//    @Override
//    @SqlQuery("SELECT * FROM order_item LIMIT :limit OFFSET :offset")
//    List<OrderItem> getPart(@Bind("limit") int limit, @Bind("offset") int offset);
//
//    @Override
//    @SqlQuery("SELECT * FROM order_item ORDER BY <orderBy> <orderDir> LIMIT :limit OFFSET :offset")
//    List<OrderItem> getOrderedPart(@Bind("limit") int limit, @Bind("offset") int offset,
//                                   @Define("orderBy") String orderBy, @Define("orderDir") String orderDir);
//
//    @SqlBatch("INSERT INTO order_item VALUES (default, :orderId, :productId, :price, :discount, :quantity, :createdAt, :updatedAt)")
//    void bulkInsert(@BindBean List<OrderItem> orderItems);
//
//    @SqlQuery("SELECT name FROM product p JOIN order_item o ON p.id = o.productId WHERE o.orderId = :orderId")
//    List<String> getProductNamesByOrderId(@Bind("orderId") long orderId);
//
//    @SqlQuery("SELECT * FROM order_item WHERE orderId = :orderId")
//    List<OrderItem> getByOrderId(@Bind("orderId") long orderId);
//}
package com.bookshopweb.dao;

import com.bookshopweb.beans.Order;
import com.bookshopweb.beans.OrderItem;
import com.bookshopweb.beans.Product;
import com.bookshopweb.jdbiInterface.OrderItemJDBI;
import com.bookshopweb.mapper.OrderItemMapper;
import com.bookshopweb.utils.JDBCUtils;
import com.bookshopweb.utils.JDBIUltis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO extends AbsDAO<OrderItem> {
    Connection conn = JDBCUtils.getConnection();
    static OrderItemJDBI orderItemJDBI = JDBIUltis.getJDBI().onDemand(OrderItemJDBI.class);

    public List<OrderItem> getByOrderId(long orderId){
        List<OrderItem> result = new ArrayList<>();
        result = JDBIUltis.getJDBI().withHandle(handel ->
                handel.createQuery("select * from order_item where orderId="+orderId)
                        .map(new OrderItemMapper()).list());
        return result;
    }
    public List<OrderItem> getByOrderIdLimit(long orderId, int start, int length){
        return JDBIUltis.getJDBI().withHandle(handle ->
                handle.createQuery("select * from order_item where orderId="+orderId+" limit "+start+","+length)
                .map(new OrderItemMapper())
                .list());
    }
    public int getQuantityByOrderId(long orderID){
        return orderItemJDBI.getQuantityByOrderId(orderID);
    }
    public int getTotalPriceByOrderId(long orderId){
        return orderItemJDBI.getTotalPriceByOrderId(orderId);
    }
    public int deleteByOrderId(long orderId) {

        int result = 0;
        try{
            String sql = "delete from order_item where orderId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,orderId);
            result = ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public OrderItem selectPrevalue(Long id){
        OrderItem result = null;
        try {
            String sql = "select * from order_item where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                long orderId = rs.getLong("orderId");
                long productId = rs.getLong("productId");
                double price = rs.getDouble("price");
                double discount = rs.getDouble("discount");
                int quantity = rs.getInt("quantity");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                result=(new OrderItem(id, orderId, productId, price, discount, quantity, createdAt, updatedAt));
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  result;
    }




    public List<OrderItem> selectByOrder(Order order){
        List<OrderItem> result = new ArrayList<>();
        try {
            String sql = "select * from order_item where orderId=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, order.getId());
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                long id = rs.getLong("id");
                long orderId = rs.getLong("orderId");
                long productId = rs.getLong("productId");
                double price = rs.getDouble("price");
                double discount = rs.getDouble("discount");
                int quantity = rs.getInt("quantity");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                result.add(new OrderItem(id, orderId, productId, price, discount, quantity, createdAt, updatedAt));
            }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  result;
    }

    @Override
    public int insert(OrderItem orderItem, String ip) {

        int result =0;
        try {
            String sql="insert into order_item (id, orderId, productId, price, discount, quantity, createdAt, updatedAt) " +
                    "values(?,?,?,?,?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, orderItem.getId());
            st.setLong(2, orderItem.getOrderId());
            st.setLong(3, orderItem.getProductId());
            st.setDouble(4, orderItem.getPrice());
            st.setDouble(5, orderItem.getDiscount());
            st.setInt(6, orderItem.getQuantity());
            st.setTimestamp(7, orderItem.getCreatedAt());
            st.setTimestamp(8, orderItem.getUpdatedAt());
            result = st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.insert(orderItem, ip);
        return result;
    }

    @Override
    public int update(OrderItem orderItem, String ip) {
        super.update(orderItem, ip);
        int result =0;
        try {
            String sql = "update order_item set id=?, orderId=?, productId=?, price=?, discount=?, quantity=?, createdAt=?, updatedAt=?" +
                    " where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, orderItem.getId());
            st.setLong(2, orderItem.getOrderId());
            st.setLong(3, orderItem.getProductId());
            st.setDouble(4, orderItem.getPrice());
            st.setDouble(5, orderItem.getDiscount());
            st.setInt(6, orderItem.getQuantity());
            st.setTimestamp(7, orderItem.getCreatedAt());
            st.setTimestamp(8, orderItem.getUpdatedAt());
            st.setLong(9, orderItem.getId());
            result = st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int delete(OrderItem orderItem, String ip) {

        int result =0;
        try {
            String sql = "delete from order_item where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, orderItem.getId());
            result = st.executeUpdate();
            st.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.delete(orderItem, ip);
        return result;
    }

    @Override
    public List<OrderItem> getAll() {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT * FROM order_item";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                OrderItem orderItem = mapResultSetToOrderItem(resultSet);
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return orderItems;
    }

    @Override
    public List<OrderItem> getPart(int limit, int offset) {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT * FROM order_item LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    OrderItem orderItem = mapResultSetToOrderItem(resultSet);
                    orderItems.add(orderItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return orderItems;
    }

    @Override
    public List<OrderItem> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT * FROM order_item ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    OrderItem orderItem = mapResultSetToOrderItem(resultSet);
                    orderItems.add(orderItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return orderItems;
    }
    public List<String> getProductNamesByOrderId(long orderId) {
        List<String> productNames = new ArrayList<>();
        String query = "SELECT p.name FROM product p JOIN order_item o ON p.id = o.productId WHERE o.orderId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    productNames.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return productNames;
    }
//    public List<OrderItem> getByOrderId(long orderId) {
//        List<OrderItem> orderItems = new ArrayList<>();
//        String query = "SELECT * FROM order_item WHERE orderId = ?";
//        try (PreparedStatement statement = conn.prepareStatement(query)) {
//            statement.setLong(1, orderId);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                while (resultSet.next()) {
//                    OrderItem orderItem = mapResultSetToOrderItem(resultSet);
//                    orderItems.add(orderItem);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // Handle exception
//        }
//        return orderItems;
//    }
    public void bulkInsert(List<OrderItem> orderItems) {
        String query = "INSERT INTO order_item (orderId, productId, price, discount, quantity, createdAt, updatedAt) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (OrderItem orderItem : orderItems) {
                System.out.println(orderItem);
                statement.setLong(1, orderItem.getOrderId());
                statement.setLong(2, orderItem.getProductId());
                statement.setDouble(3, orderItem.getPrice());
                statement.setDouble(4, orderItem.getDiscount());
                statement.setInt(5, orderItem.getQuantity());
                statement.setTimestamp(6, orderItem.getCreatedAt());
                statement.setTimestamp(7, orderItem.getUpdatedAt());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
    }
    private OrderItem mapResultSetToOrderItem(ResultSet resultSet) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(resultSet.getLong("id"));
        orderItem.setOrderId(resultSet.getLong("orderId"));
        orderItem.setProductId(resultSet.getLong("productId"));
        orderItem.setPrice(resultSet.getDouble("price"));
        orderItem.setDiscount(resultSet.getDouble("discount"));
        orderItem.setQuantity(resultSet.getInt("quantity"));
        orderItem.setCreatedAt(resultSet.getTimestamp("createdAt"));
        orderItem.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        Product product = new Product();
        product.setId(resultSet.getLong("productId"));
        // Thực hiện các thao tác khác để map dữ liệu từ ResultSet vào đối tượng Product
        orderItem.setProduct(product);
        return orderItem;
    }

    public static void main(String[] args) {
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        System.out.println(orderItemDAO.getQuantityByOrderId(3));
    }
}