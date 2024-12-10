//package com.bookshopweb.dao;
//
//import com.bookshopweb.beans.Category;
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
//@RegisterBeanMapper(Category.class)
//public interface CategoryIDAO extends IDAO<Category> {
//    @Override
//    @SqlUpdate("INSERT INTO category VALUES (default, :name, :description, :imageName)")
//    @GetGeneratedKeys("id")
//    long insert(@BindBean Category category);
//
//    @Override
//    @SqlUpdate("UPDATE category SET name = :name, description = :description, imageName = :imageName WHERE id = :id")
//    void update(@BindBean Category category);
//
//    @Override
//    @SqlUpdate("DELETE FROM category WHERE id = :id")
//    void delete(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM category WHERE id = :id")
//    Optional<Category> getById(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM category")
//    List<Category> getAll();
//
//    @Override
//    @SqlQuery("SELECT * FROM category LIMIT :limit OFFSET :offset")
//    List<Category> getPart(@Bind("limit") int limit, @Bind("offset") int offset);
//
//    @Override
//    @SqlQuery("SELECT * FROM category ORDER BY <orderBy> <orderDir> LIMIT :limit OFFSET :offset")
//    List<Category> getOrderedPart(@Bind("limit") int limit, @Bind("offset") int offset,
//                                  @Define("orderBy") String orderBy, @Define("orderDir") String orderDir);
//
//    @SqlQuery("SELECT c.* FROM product_category pc JOIN category c ON pc.categoryId = c.id WHERE productId = :productId")
//    Optional<Category> getByProductId(@Bind("productId") long productId);
//
//    @SqlQuery("SELECT COUNT(id) FROM category")
//    int count();
//}
package com.bookshopweb.dao;

import com.bookshopweb.beans.Category;
import com.bookshopweb.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAO extends AbsDAO<Category>{
    Connection conn = JDBCUtils.getConnection();
    @Override
    public int delete(Category category, String ip) {

        int result = 0;
        try{
            String sql = "delete from category where id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,category.getId());
            result = ps.executeUpdate();

        }catch (Exception e) {
            e.printStackTrace();
        }
        super.delete(category, ip);
        return result;

    }
    @Override
    public int update(Category category, String ip) {
        super.update(category, ip);
        int result = 0;
        try{
            String sql = "update category " +
                    "set id=?, name=?, description=?,imageName=?" +
                    "where id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,category.getId());
            ps.setString(2,category.getName());
            ps.setString(3,category.getDescription());
            ps.setString(4,category.getImageName());
            result= ps.executeUpdate();

        }catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    @Override
    public Category selectPrevalue(Long id) {
        Category result = null;
        try {
            String sql = "select * from category where id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                String name = rs.getString("name");
                String description = rs.getString("description");
                String imageName = rs.getString("imageName");
                result = new Category(id, name, description, imageName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int insert(Category category, String ip) {

        int result = 0;
        try{
            String sql = "insert into category (id, name, description, imageName) values(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1,category.getId());
            ps.setString(2, category.getName());
            ps.setString(3, category.getDescription());
            ps.setString(4, category.getImageName());
            result = ps.executeUpdate();
            ps.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
        super.insert(category, ip);
        return result;

    }

    public int count() {
        String query = "SELECT COUNT(id) FROM category";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public List<Category> getAll() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Category category = mapResultSetToCategory(resultSet);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return categories;
    }

    public List<Category> getPart(int limit, int offset) {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Category category = mapResultSetToCategory(resultSet);
                    categories.add(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return categories;
    }

    public List<Category> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Category category = mapResultSetToCategory(resultSet);
                    categories.add(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return categories;
    }

    public Optional<Category> getByProductId(long productId) {
        String query = "SELECT c.* FROM product_category pc JOIN category c ON pc.categoryId = c.id WHERE productId = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Category category = mapResultSetToCategory(resultSet);
                    return Optional.of(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return Optional.empty();
    }

    public Optional<Category> getById(long id) {
        Optional<Category> category = Optional.empty();
        String query = "SELECT * FROM category WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    category = Optional.of(mapResultSetToCategory(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
        return category;
    }

    private Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setId(resultSet.getLong("id"));
        category.setName(resultSet.getString("name"));
        category.setDescription(resultSet.getString("description"));
        category.setImageName(resultSet.getString("imageName"));
        return category;
    }
}