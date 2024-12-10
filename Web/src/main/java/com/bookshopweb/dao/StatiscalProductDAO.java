package com.bookshopweb.dao;

import com.bookshopweb.beans.Category;
import com.bookshopweb.beans.StatisticalCustomer;
import com.bookshopweb.beans.StatisticalProduct;
import com.bookshopweb.utils.JDBCUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StatiscalProductDAO {
    private Connection conn = JDBCUtils.getConnection();

    public List<StatisticalProduct> getAll() {
        List<StatisticalProduct> result = new ArrayList<>();
        try {
        String sql = 
         "SELECT pd.id, pd.name, " +
         "     COALESCE((SELECT SUM(product_import.quanlity) " +
         "         FROM product_import  " +
         "         WHERE product_import.productId=pd.id), 0) " +
         "         AS import_quatity, " +
         "      COALESCE((SELECT SUM(order_item.quantity)  " +
         "         FROM order_item  " +
         "         INNER JOIN orders ON order_item.orderId = orders.id  " +
         "         WHERE order_item.productId = pd.id AND orders.status != 3 ), 0) " +
         "         AS sell_quantityitem,  " +
         "      COALESCE((SELECT SUM(order_item.quantity)  " +
         "         FROM order_item  " +
         "         INNER JOIN orders ON order_item.orderId = orders.id  " +
         "         WHERE order_item.productId = pd.id AND orders.status = 3 ), 0) " +
         "         AS refund_quantityitem, " +
         "      COALESCE((SELECT ROUND(SUM(order_item.price) ,0) " +
         "         FROM order_item " +
         "         INNER JOIN orders ON order_item.orderId = orders.id  " +
         "         WHERE order_item.productId = pd.id AND orders.status != 3  " +
         "         AND orders.status != 4), 0) AS total_sell_price, " +
         "      COALESCE((SELECT ROUND(SUM(product_import.quanlity * product_import.price),0)  " +
         "         FROM product_import " +
         "         WHERE product_import.productId=pd.id), 0) AS total_import_price " +
         "         FROM product pd;";
        PreparedStatement st = conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        result = getFromResultset(rs);

        } catch (Exception e) {
        throw new RuntimeException(e);
        }
        return result;
    }
    public List<StatisticalProduct> getPart(int limit, int offset) {
        List<StatisticalProduct> result = new ArrayList<>();
        String sql =
                "SELECT pd.id, pd.name, " +
                        "     COALESCE((SELECT SUM(product_import.quanlity) " +
                        "         FROM product_import  " +
                        "         WHERE product_import.productId=pd.id), 0) " +
                        "         AS import_quatity, " +
                        "      COALESCE((SELECT SUM(order_item.quantity)  " +
                        "         FROM order_item  " +
                        "         INNER JOIN orders ON order_item.orderId = orders.id  " +
                        "         WHERE order_item.productId = pd.id AND orders.status != 3 ), 0) " +
                        "         AS sell_quantityitem,  " +
                        "      COALESCE((SELECT SUM(order_item.quantity)  " +
                        "         FROM order_item  " +
                        "         INNER JOIN orders ON order_item.orderId = orders.id  " +
                        "         WHERE order_item.productId = pd.id AND orders.status = 3 ), 0) " +
                        "         AS refund_quantityitem, " +
                        "      COALESCE((SELECT ROUND(SUM(order_item.price) ,0) " +
                        "         FROM order_item " +
                        "         INNER JOIN orders ON order_item.orderId = orders.id  " +
                        "         WHERE order_item.productId = pd.id AND orders.status != 3  " +
                        "         AND orders.status != 4), 0) AS total_sell_price, " +
                        "      COALESCE((SELECT ROUND(SUM(product_import.quanlity * product_import.price),0)  " +
                        "         FROM product_import " +
                        "         WHERE product_import.productId=pd.id), 0) AS total_import_price " +
                        "         FROM product pd " +
                        "         LIMIT ? OFFSET ?;"; // thêm LIMIT và OFFSET
        try {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, limit);
            st.setInt(2, offset);
            ResultSet rs = st.executeQuery();
            result = getFromResultset(rs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // Lấy số lượng tồn kho và sô lượng bán , số lượng nhập
    // để biết số lượng tồn kho có đủ để bán hay không => có cần nhập thêm không
    public List<StatisticalProduct> getUnsold(int limit, int offset) {
        List<StatisticalProduct> result = new ArrayList<>();
        try {
            String sql = "SELECT " +
                    "    p.id, " +
                    "    p.name, " +
                    "    COALESCE(SUM(oi.quantity), 0) AS total_sold, " +
                    "    COALESCE(SUM(pi.quanlity), 0) AS total_import, " +
                    "    (COALESCE(SUM(pi.quanlity), 0) - COALESCE(SUM(oi.quantity), 0)) AS quantity_remaining " +
                    "FROM " +
                    "    product p " +
                    "LEFT JOIN (" +
                    "    SELECT " +
                    "        oi.productId, " +
                    "        SUM(oi.quantity) AS quantity " +
                    "    FROM " +
                    "        order_item oi " +
                    "    INNER JOIN orders o ON oi.orderId = o.id " +
                    "    WHERE " +
                    "        o.status != 3 " +
                    "    GROUP BY " +
                    "        oi.productId " +
                    ") oi ON p.id = oi.productId " +
                    "LEFT JOIN (" +
                    "    SELECT " +
                    "        productId, " +
                    "        SUM(quanlity) AS quanlity " +
                    "    FROM " +
                    "        product_import " +
                    "    GROUP BY " +
                    "        productId " +
                    ") pi ON p.id = pi.productId " +
                    "WHERE EXISTS (" +
                    "    SELECT 1 FROM order_item WHERE productId = p.id AND quantity > 0" +
                    ") " +
                    "GROUP BY " +
                    "    p.id, p.name " +
                    "HAVING " +
                    "    quantity_remaining > 0" +
                    " ORDER BY "+
                    " total_sold DESC " +
                    "LIMIT ? OFFSET ?";

            PreparedStatement st = conn.prepareStatement(sql);

            st.setInt(1, limit);
            st.setInt(2, offset);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                int quantityRemaining = rs.getInt("quantity_remaining");
                int totalQuantityImport = rs.getInt("total_import");
                int totalQuantitySell = rs.getInt("total_sold");
                double saleRate = totalQuantityImport > 0 ? (double) totalQuantitySell / totalQuantityImport : 1.0;
                saleRate = Double.parseDouble(String.format("%.3f", saleRate));
                result.add(new StatisticalProduct(id, name, totalQuantityImport, totalQuantitySell, quantityRemaining, saleRate));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn cơ sở dữ liệu", e);
        }
        return result;
    }



    public List<StatisticalProduct> getUnsoldOrdered(int limit, int offset, String start, String end, String orderBy, String orderDir) {
        List<StatisticalProduct> result = new ArrayList<>();
        try {
            String sql = "SELECT " +
                    "    p.id, " +
                    "    p.name, " +
                    "    COALESCE(SUM(oi.quantity), 0) AS total_sold, " +
                    "    COALESCE(SUM(pi.quanlity), 0) AS total_import, " +
                    "    (COALESCE(SUM(pi.quanlity), 0) - COALESCE(SUM(oi.quantity), 0)) AS quantity_remaining " +
                    "FROM " +
                    "    product p " +
                    "LEFT JOIN (" +
                    "    SELECT " +
                    "        oi.productId, " +
                    "        SUM(oi.quantity) AS quantity " +
                    "    FROM " +
                    "        order_item oi " +
                    "    INNER JOIN orders o ON oi.orderId = o.id " +
                    "    WHERE " +
                    "        o.status != 3 " +
                    "        AND o.createdAt BETWEEN ? AND ? " +
                    "    GROUP BY " +
                    "        oi.productId " +
                    ") oi ON p.id = oi.productId " +
                    "LEFT JOIN (" +
                    "    SELECT " +
                    "        productId, " +
                    "        SUM(quanlity) AS quanlity " +
                    "    FROM " +
                    "        product_import " +
                    "    WHERE " +
                    "        createAt BETWEEN ? AND ? " +
                    "    GROUP BY " +
                    "        productId " +
                    ") pi ON p.id = pi.productId " +
                    "WHERE EXISTS (" +
                    "    SELECT 1 FROM order_item WHERE productId = p.id AND quantity > 0" +
                    ") " +
                    "GROUP BY " +
                    "    p.id, p.name " +
                    "HAVING " +
                    "    quantity_remaining > 0 AND total_sold > 0 " +
                    "ORDER BY " + orderBy + " " + orderDir + " " +
                    "LIMIT ? OFFSET ?";

            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, start);
            st.setString(2, end);
            st.setString(3, start);
            st.setString(4, end);
            st.setInt(5, limit);
            st.setInt(6, offset);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                int quantityRemaining = rs.getInt("quantity_remaining");
                int totalQuantityImport = rs.getInt("total_import");
                int totalQuantitySell = rs.getInt("total_sold");
                double saleRate = totalQuantityImport > 0 ? (double) totalQuantitySell / totalQuantityImport : 1.0;
                saleRate = Double.parseDouble(String.format("%.3f", saleRate));
                result.add(new StatisticalProduct(id, name, totalQuantityImport, totalQuantitySell, quantityRemaining, saleRate));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn cơ sở dữ liệu", e);
        }
        return result;
    }


    // Thống kê tỉ lệ nhập so với tỉ lệ bán, số lượng tồn kho
    public List<StatisticalProduct> getSaleRate(String start, String end) {
        List<StatisticalProduct> result = new ArrayList<>();
        try {
        String sql =
         "SELECT pd.id, pd.name,   " +
          "COALESCE((SELECT SUM(order_item.quantity) " +
          "       FROM order_item  " +
          "       INNER JOIN orders ON order_item.orderId = orders.id " +
          "       WHERE order_item.productId = pd.id AND orders.status != 3  " +
          "       AND (orders.createdAt BETWEEN ? " +
          "       AND ?)), 0) AS sell_quantityitem, " +
          "COALESCE((SELECT SUM(order_item.quantity) " +
          "       FROM order_item  " +
          "       INNER JOIN orders ON order_item.orderId = orders.id " +
          "       WHERE order_item.productId = pd.id " +
          "       AND orders.status != 3 ), 0) " +
          "       AS total_sell_quantityitem, " +
          "COALESCE((SELECT SUM(product_import.quanlity)  " +
          "        FROM product_import " +
          "        WHERE product_import.productId=pd.id  " +
          "        AND (product_import.importAt BETWEEN ? AND ?)), 0) " +
          "        AS import_quatity, "+
          "COALESCE((SELECT SUM(product_import.quanlity)  " +
          "        FROM product_import " +
          "        WHERE product_import.productId=pd.id ), 0) " +
          "        AS total_import_quatity " +
          "FROM product pd ";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, start);
        st.setString(2, end);
        st.setString(3, start);
        st.setString(4, end);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
         long id = rs.getLong("id");
         String name = rs.getString("name");
         int quantitySell = rs.getInt("sell_quantityitem");
         int totalQuantityImport = rs.getInt("total_import_quatity");
         int totalQuantitySell = rs.getInt("total_sell_quantityitem");
         int quantityImport = rs.getInt("import_quatity");
         double saleRate = (quantityImport >0)?quantitySell/quantityImport:1;
         result.add(new StatisticalProduct(id, name, quantityImport, quantitySell, totalQuantityImport-totalQuantitySell, saleRate));

        }

        } catch (Exception e) {
        throw new RuntimeException(e);
        }
        return result;
    }
    // Thống kê thông tin về số lượng nhập, số lượng tồn kho của những sản phẩm không bán được trong 1 khoảng thời gian
    public List<StatisticalProduct> getCanntSale(String start, String end) {
        List<StatisticalProduct> result = new ArrayList<>();
        try {
        String sql =
         "SELECT pd.id, pd.name, " +
          "COALESCE((SELECT SUM(order_item.quantity) " +
          "       FROM order_item  INNER JOIN orders " +
          "       ON order_item.orderId = orders.id " +
          "       WHERE order_item.productId = pd.id " +
          "       AND orders.status != 3  " +
          "       AND (orders.createdAt BETWEEN ? AND ?)), 0) " +
          "       AS sell_quantityitem, " +
          "COALESCE((SELECT SUM(order_item.quantity) " +
          "       FROM order_item  INNER JOIN orders " +
          "       ON order_item.orderId = orders.id " +
          "       WHERE order_item.productId = pd.id " +
          "       AND orders.status != 3 ), 0) " +
          "       AS total_sell_quantityitem, " +
          "COALESCE((SELECT SUM(product_import.quanlity)  " +
          "        FROM product_import " +
          "        WHERE product_import.productId=pd.id  " +
          "        AND (product_import.importAt BETWEEN ? AND ?)), 0) AS import_quatity, "+
          " COALESCE((SELECT SUM(product_import.quanlity)  " +
          "        FROM product_import " +
          "        WHERE product_import.productId=pd.id ), 0) " +
          "        AS total_import_quatity " +
          "FROM product pd " +
          "HAVING sell_quantityitem=0";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, start);
        st.setString(2, end);
        st.setString(3, start);
        st.setString(4, end);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
         long id = rs.getLong("id");
         String name = rs.getString("name");
         int quantitySell = rs.getInt("sell_quantityitem");
         int totalQuantityImport = rs.getInt("total_import_quatity");
         int totalQuantitySell = rs.getInt("total_sell_quantityitem");
         int quantityImport = rs.getInt("import_quatity");
         double saleRate = (quantityImport >0)?quantitySell/quantityImport:1;
         result.add(new StatisticalProduct(id, name, quantityImport, quantitySell, totalQuantityImport-totalQuantitySell, saleRate));

        }

        } catch (Exception e) {
        throw new RuntimeException(e);
        }
        return result;
    }
    // THống kê doanh thu của từng sản phẩm trong một khoảng thời gian
    public List<StatisticalProduct> getRevenue(String start, String end) {
        List<StatisticalProduct> result = new ArrayList<>();
        try {
        String sql =
         "SELECT pd.id, pd.name,  " +
          "COALESCE((SELECT SUM(order_item.quantity) " +
          "       FROM order_item  " +
          "       INNER JOIN orders ON order_item.orderId = orders.id " +
          "       WHERE order_item.productId = pd.id " +
          "       AND orders.status != 3  " +
          "       AND orders.status != 4 " +
          "       AND (orders.createdAt BETWEEN ? " +
          "       AND ?)), 0) AS sell_quantityitem, " +
          " COALESCE((SELECT ROUND(SUM(order_item.price * order_item.quantity) ,0) " +
          "       FROM order_item  " +
          "       INNER JOIN orders ON order_item.orderId = orders.id " +
          "       WHERE order_item.productId = pd.id " +
          "       AND orders.status != 3 " +
          "       AND orders.status != 4 " +
          "       AND (orders.createdAt BETWEEN ? AND ?)), 0) AS total_sell_price " +
          "FROM product pd " +
          "HAVING total_sell_price>0";
        PreparedStatement st = conn.prepareStatement(sql);
        st.setString(1, start);
        st.setString(2, end);
        st.setString(3, start);
        st.setString(4, end);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
         long id = rs.getLong("id");
         String name = rs.getString("name");
         int quantity = rs.getInt("sell_quantityitem");
         double totalPrice = rs.getDouble("total_sell_price");
         result.add(new StatisticalProduct(id, name, quantity, totalPrice));

        }

        } catch (Exception e) {
        throw new RuntimeException(e);
        }
        return result;
    }
    //Thống kê những khách hàng đã từng mua hàng nhưng không quay lại trong 1 khoảng thời gian
    public List<StatisticalCustomer> getCustomersNotReorder(int limit, int offset, int numOfMonth, String orderBy, String orderDir){
        List<StatisticalCustomer> result = new ArrayList<>();

        try {
            String sql ="SELECT userId, username, lastOrderId, lastOrderTime  " +
                    "    FROM (SELECT  " +
                    "    user.id AS userId, " +
                    "    user.username,  " +
                    "    orders.id AS lastOrderId, " +
                    "    MAX(orders.createdAt) AS lastOrderTime " +
                    "FROM user " +
                    "INNER JOIN orders ON user.id = orders.userId " +
                    "WHERE orders.`status` = 2 " +
                    "GROUP BY user.id, user.username) AS tmp " +
                    "WHERE TIMESTAMPADD(MONTH, ?, tmp.lastOrderTime) < CURRENT_TIMESTAMP()"+
                    "ORDER BY " + orderBy + " " + orderDir + " " +
                    "LIMIT ? OFFSET ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, numOfMonth);
            st.setInt(2, limit);
            st.setInt(3, offset);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                long userId = rs.getLong("userId");
                long lastOrderId = rs.getLong("lastOrderId");
                String userName = rs.getString("username");
                Timestamp lastOrderTime = rs.getTimestamp("lastOrderTime");
                result.add(new StatisticalCustomer(userId, userName, lastOrderId, lastOrderTime));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public List<StatisticalProduct> getFromResultset(ResultSet rs) {
        List<StatisticalProduct> result = new ArrayList<>();
        try {
        while (rs.next()) {
         long productId = rs.getLong("id");
         String name = rs.getString("name");
         int importQuantity = rs.getInt("import_quatity");
         int sellQuantity = rs.getInt("sell_quantityitem");
         double totalSellPrice = rs.getDouble("total_sell_price");
         double totalImportPrice = rs.getDouble("total_import_price");
         int totalRefund = rs.getInt("refund_quantityitem");
         int remaining = importQuantity - sellQuantity;
         double rating = sellQuantity >0?importQuantity/sellQuantity:0;
         result.add(new StatisticalProduct(productId, name, importQuantity, sellQuantity, totalImportPrice, totalSellPrice, remaining, rating, totalRefund));
        }
        } catch (Exception e) {
        throw new RuntimeException(e);
        }

        return result;
    }
    public int count() {
        int count = 0;
        try {
            String sql = "SELECT COUNT(DISTINCT oi.productId) AS count " +
                    "FROM order_item oi " +
                    "INNER JOIN orders o ON oi.orderId = o.id " ;
            PreparedStatement st = conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đếm số lượng sản phẩm đã bán trong khoảng thời gian", e);
        }
        return count;

    }
    public int countProductsSoldByTime(String start, String end) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(DISTINCT oi.productId) AS count " +
                    "FROM order_item oi " +
                    "INNER JOIN orders o ON oi.orderId = o.id " +
                    "WHERE o.createdAt BETWEEN ? AND ?";

            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, start);
            st.setString(2, end);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi đếm số lượng sản phẩm đã bán trong khoảng thời gian", e);
        }
        return count;
    }
    public int countUser(int months) {
        int userCount = 0;

        try {
            String sql = "SELECT COUNT(*) AS userCount " +
                    "FROM (SELECT user.id AS userId, " +
                    "             MAX(orders.createdAt) AS lastOrderTime " +
                    "      FROM user " +
                    "      INNER JOIN orders ON user.id = orders.userId " +
                    "      WHERE orders.status = 2 " +
                    "      GROUP BY user.id) AS tmp " +
                    "WHERE TIMESTAMPADD(MONTH, ?, tmp.lastOrderTime) < CURRENT_TIMESTAMP()";

            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, months);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                userCount = rs.getInt("userCount");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userCount;
    }
public int getRemainingAmount(long productId){
        int result =0;

        return result;
}

    public String getFirst(String twopartString) {
        return twopartString.contains("-") ? twopartString.split("-")[0] : "";
    }

    public String getLast(String twopartString) {
        return twopartString.contains("-") ? twopartString.split("-")[1] : "";
    }

    public static void main(String[] args) {
        StatiscalProductDAO p = new StatiscalProductDAO();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
//        LocalDate startDate = LocalDate.parse("1-2-2024", formatter);
//        LocalDate endDate = LocalDate.parse("1-1-2025", formatter);
//
//// Format lại thành định dạng yêu cầu của SQL
//        String sqlStartDate = startDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        String sqlEndDate = endDate.atTime(LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        System.out.println(p.getUnsoldOrdered(10,1,sqlStartDate,sqlEndDate,"total_sold","DESC"));
        System.out.println(p.countUser(3));
    }
}
