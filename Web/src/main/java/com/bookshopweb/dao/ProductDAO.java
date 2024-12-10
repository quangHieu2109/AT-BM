
package com.bookshopweb.dao;

import com.bookshopweb.beans.Product;
import com.bookshopweb.mapper.ProductMapper;
import com.bookshopweb.utils.JDBCUtils;
import com.bookshopweb.utils.JDBIUltis;
import com.bookshopweb.utils.Protector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductDAO extends AbsDAO<Product>{
    Connection conn = JDBCUtils.getConnection();
    public Product getByIdProduct(long id){

       return JDBIUltis.getJDBI().withHandle(handel ->
               handel.createQuery("select * from product where id="+id)
                       .map(new ProductMapper()).one());
    }
    public long getCategpryId(long productId){

        return JDBIUltis.getJDBI().withHandle(handle ->
                handle.createQuery("SELECT categoryId FROM product_category WHERE productId = :productId")
                        .bind("productId", productId)
                        .mapTo(Long.class)
                        .one());
    }

    public Product selectPrevalue(Long id){
        Product result = null;
        try {
            String sql = "select * from product where id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            while(rs.next()){

                String name = rs.getString("name");
                double price = rs.getDouble("price");
                double discount = rs.getDouble("discount");
                int quantity = selectQuantity(id);
                int totalBuy = rs.getInt("totalBuy");
                String author = rs.getString("author");
                int pages = rs.getInt("pages");
                String publisher = rs.getString("publisher");
                int yearPublishing = rs.getInt("yearPublishing");
                String description = rs.getString("description");
                String imageName = rs.getString("imageName");
                int shop = rs.getInt("shop");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                Timestamp startsAt = rs.getTimestamp("startsAt");
                Timestamp endsAt = rs.getTimestamp("endsAt");

                result = new Product(id, name, price, discount, quantity, totalBuy, author, pages, publisher,
                        yearPublishing, description, imageName, shop, createdAt, updatedAt, startsAt, endsAt);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public int selectQuantity(long id){
        int ressult =0;
        try {
            String sql ="SELECT COALESCE((SELECT SUM(product_import.quanlity)\n" +
                    "     FROM product_import\n" +
                    "     WHERE product_import.productId = ?),0)\n" +
                    "      - \n" +
                    "    COALESCE((SELECT SUM(order_item.quantity)\n" +
                    "              FROM order_item \n" +
                    "              INNER JOIN orders ON order_item.orderId = orders.id\n" +
                    "              WHERE order_item.productId = ? AND orders.status != 3 " +
                    "              ), 0) AS conLai";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            st.setLong(2, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                ressult = rs.getInt("conLai");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ressult;
    }
    public Product selectById(Long id){
        Product result = null;
        try {
            String sql = "select * from product where id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            while(rs.next()){

                String name = rs.getString("name");
                double price = rs.getDouble("price");
                double discount = rs.getDouble("discount");
                int quantity = selectQuantity(id);
                int totalBuy = rs.getInt("totalBuy");
                String author = rs.getString("author");
                int pages = rs.getInt("pages");
                String publisher = rs.getString("publisher");
                int yearPublishing = rs.getInt("yearPublishing");
                String description = rs.getString("description");
                String imageName = rs.getString("imageName");
                int shop = rs.getInt("shop");
                Timestamp createdAt = rs.getTimestamp("createdAt");
                Timestamp updatedAt = rs.getTimestamp("updatedAt");
                Timestamp startsAt = rs.getTimestamp("startsAt");
                Timestamp endsAt = rs.getTimestamp("endsAt");

                result = new Product(id, name, price, discount, quantity, totalBuy, author, pages, publisher,
                        yearPublishing, description, imageName, shop, createdAt, updatedAt, startsAt, endsAt);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    @Override
    public int insert(Product product, String ip) {


        int result = 0;
        try {
            String sql = "insert into product (id, name, price, discount, quantity, totalBuy, author, pages" +
                    ", publisher, yearPublishing, description, imageName, shop, createdAt, updatedAt, startsAt, endsAt) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, product.getId());
            st.setString(2, product.getName());
            st.setDouble(3, product.getPrice());
            st.setDouble(4, product.getDiscount());
            st.setInt(5, product.getQuantity());
            st.setInt(6, product.getTotalBuy());
            st.setString(7, product.getAuthor());
            st.setInt(8, product.getPages());
            st.setString(9, product.getPublisher());
            st.setInt(10, product.getYearPublishing());
            st.setString(11, product.getDescription());
            st.setString(12, product.getImageName());
            st.setInt(13, product.getShop());
            st.setTimestamp(14, product.getCreatedAt());
            st.setTimestamp(15, product.getUpdatedAt());
            st.setTimestamp(16, product.getStartsAt());
            st.setTimestamp(17, product.getEndsAt());
            result = st.executeUpdate();
            st.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.insert(product, ip);
        return result;
    }

    @Override
    public int update(Product product, String ip) {
        super.update(product, ip);
        int result = 0;
        try {
            String sql="update product set " +
                    "id=?, name=?, price=?, discount=?, quantity=?, totalBuy=?, author=?, pages=?,publisher=?," +
                    "yearPublishing=?, description=?, imageName=?, shop=?, createdAt=?, updatedAt=?, startsAt=?, endsAt=? " +
                    "where id=?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, product.getId());
            st.setString(2, product.getName());
            st.setDouble(3, product.getPrice());
            st.setDouble(4, product.getDiscount());
            st.setInt(5, product.getQuantity());
            st.setInt(6, product.getTotalBuy());
            st.setString(7, product.getAuthor());
            st.setInt(8, product.getPages());
            st.setString(9, product.getPublisher());
            st.setInt(10, product.getYearPublishing());
            st.setString(11, product.getDescription());
            st.setString(12, product.getImageName());
            st.setInt(13, product.getShop());
            st.setTimestamp(14, product.getCreatedAt());
            st.setTimestamp(15, product.getUpdatedAt());
            st.setTimestamp(16, product.getStartsAt());
            st.setTimestamp(17, product.getEndsAt());
            st.setLong(18, product.getId());
            result = st.executeUpdate();
            st.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;

    }

    @Override
    public int delete(Product product, String ip) {

        int result = 0;
        try {
            String sql = "delete from product where id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, product.getId());
            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.delete(product, ip);
        return result;
    }
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Product product = mapResultSetToProduct(resultSet);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return products;
    }

    public List<Product> getPart(int limit, int offset) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapResultSetToProduct(resultSet);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return products;
    }

    public List<Product> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM product ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapResultSetToProduct(resultSet);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return products;
    }

    public List<Product> getOrderedPartByCategoryId(int limit, int offset, String orderBy, String orderDir, long categoryId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.* " +
                "FROM product_category pc " +
                "JOIN product p ON pc.productId = p.id " +
                "WHERE pc.categoryId = ? " +
                "ORDER BY p." + orderBy + " " + orderDir + " " +
                "LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, categoryId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapResultSetToProduct(resultSet);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return products;
    }

    public int countByCategoryId(long categoryId) {
        int count = 0;
        String query = "SELECT COUNT(productId) FROM product_category WHERE categoryId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, categoryId);
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

    public List<Product> getRandomPartByCategoryId(int limit, int offset, long categoryId) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.* FROM product_category pc " +
                "JOIN product p ON pc.productId = p.id " +
                "WHERE pc.categoryId = ? " +
                "ORDER BY RAND() " +
                "LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, categoryId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapResultSetToProduct(resultSet);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return products;
    }

    public List<String> getPublishersByCategoryId(long categoryId) {
        List<String> publishers = new ArrayList<>();
        String query = "SELECT DISTINCT p.publisher " +
                "FROM product_category pc " +
                "JOIN product p ON pc.productId = p.id " +
                "WHERE pc.categoryId = ? " +
                "ORDER BY p.publisher";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, categoryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String publisher = resultSet.getString("publisher");
                    publishers.add(publisher);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return publishers;
    }
    public int countByCategoryIdAndFilters(long categoryId, String filters) {
        int count = 0;
        String query = "SELECT COUNT(p.id) " +
                "FROM product_category pc " +
                "JOIN product p ON pc.productId = p.id " +
                "WHERE pc.categoryId = ? " +
                "AND " + filters;
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, categoryId);
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

    public List<Product> getOrderedPartByCategoryIdAndFilters(int limit, int offset, String orderBy, String orderDir, long categoryId, String filters) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT p.* " +
                "FROM product_category pc " +
                "JOIN product p ON pc.productId = p.id " +
                "WHERE pc.categoryId = ? " +
                "AND " + filters + " " +
                "ORDER BY p." + orderBy + " " + orderDir + " " +
                "LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, categoryId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapResultSetToProduct(resultSet);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return products;
    }
    public Optional<Product> getById(long id) {
        Optional<Product> product = Optional.empty();
        String query = "SELECT * FROM product WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    product = Optional.of(mapResultSetToProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
        return product;
    }

    public int count() {
        int count = 0;
        String query = "SELECT COUNT(id) FROM product";
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

    public void insertProductCategory(long productId, long categoryId) {
        String query = "INSERT product_category VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, productId);
            statement.setLong(2, categoryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void updateProductCategory(long productId, long categoryId) {
        String query = "UPDATE product_category SET categoryId = ? WHERE productId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, categoryId);
            statement.setLong(2, productId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void deleteProductCategory(long productId, long categoryId) {
        String query = "DELETE FROM product_category WHERE productId = ? AND categoryId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, productId);
            statement.setLong(2, categoryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public List<Product> getByQuery(String query, int limit, int offset) {
        List<Product> products = new ArrayList<>();
        String sqlQuery = "SELECT * FROM product WHERE name LIKE CONCAT('%', ?, '%') LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, query);
            statement.setInt(2, limit);
            statement.setInt(3, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = mapResultSetToProduct(resultSet);
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return products;
    }

    public int countByQuery(String query) {
        int count = 0;
        String sqlQuery = "SELECT COUNT(id) FROM product WHERE name LIKE CONCAT('%', ?, '%')";
        try (PreparedStatement statement = conn.prepareStatement(sqlQuery)) {
            statement.setString(1, query);
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

    private Product mapResultSetToProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getLong("id"));
        product.setName(resultSet.getString("name"));
        product.setPrice(resultSet.getDouble("price"));
        product.setDiscount(resultSet.getDouble("discount"));
        product.setQuantity(selectQuantity(product.getId()));
        product.setTotalBuy(resultSet.getInt("totalBuy"));
        product.setAuthor(resultSet.getString("author"));
        product.setPages(resultSet.getInt("pages"));
        product.setPublisher(resultSet.getString("publisher"));
        product.setYearPublishing(resultSet.getInt("yearPublishing"));
        product.setDescription(resultSet.getString("description"));
        product.setImageName(resultSet.getString("imageName"));
        product.setShop(resultSet.getInt("shop"));
        product.setCreatedAt(resultSet.getTimestamp("createdAt"));
        product.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
        product.setStartsAt(resultSet.getTimestamp("startsAt"));
        product.setEndsAt(resultSet.getTimestamp("endsAt"));

        return product;
    }
    public String getFirst(String twopartString) {
        return twopartString.contains("-") ? twopartString.split("-")[0] : "";
    }

    public String getLast(String twopartString) {
        return twopartString.contains("-") ? twopartString.split("-")[1] : "";
    }

    private int getMinPrice(String priceRange) {
        return Protector.of(() -> Integer.parseInt(getFirst(priceRange))).get(0);
    }

    private int getMaxPrice(String priceRange) {
        return Protector.of(() -> {
            String maxPriceString = getLast(priceRange);
            if (maxPriceString.equals("infinity")) {
                return Integer.MAX_VALUE;
            }
            return Integer.parseInt(maxPriceString);
        }).get(0);
    }

    public String filterByPublishers(List<String> publishers) {
        String publishersString = publishers.stream().map(p -> "'" + p + "'").collect(Collectors.joining(", "));
        return "p.publisher IN (" + publishersString + ")";
    }

    public String filterByPriceRanges(List<String> priceRanges) {
        String priceRangeConditions = priceRanges.stream().map(
                priceRange -> "p.price BETWEEN " + getMinPrice(priceRange) + " AND " + getMaxPrice(priceRange)
        ).collect(Collectors.joining(" OR "));
        return "(" + priceRangeConditions + ")";
    }

    public String createFiltersQuery(List<String> filters) {
        return String.join(" AND ", filters);
    }

    public static void main(String[] args) {
        System.out.println(new ProductDAO().getCategpryId(1));
    }
}

