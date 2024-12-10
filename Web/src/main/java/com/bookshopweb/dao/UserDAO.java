//package com.bookshopweb.dao;
//
//import com.bookshopweb.beans.User;
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
//@RegisterBeanMapper(User.class)
//public interface UserIDAO extends IDAO<User> {
//    @Override
//    @SqlUpdate("INSERT INTO user VALUES (default, :username, :password, :fullname, " +
//               ":email, :phoneNumber, :gender, :address, :role)")
//    @GetGeneratedKeys("id")
//    long insert(@BindBean User user);
//
//    @Override
//    @SqlUpdate("UPDATE user SET username = :username, password = :password, fullname = :fullname, " +
//               "email = :email, phoneNumber = :phoneNumber, gender = :gender, address = :address, role = :role " +
//               "WHERE id = :id")
//    void update(@BindBean User user);
//
//    @Override
//    @SqlUpdate("DELETE FROM user WHERE id = :id")
//    void delete(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM user WHERE id = :id")
//    Optional<User> getById(@Bind("id") long id);
//
//    @Override
//    @SqlQuery("SELECT * FROM user")
//    List<User> getAll();
//
//    @Override
//    @SqlQuery("SELECT * FROM user LIMIT :limit OFFSET :offset")
//    List<User> getPart(@Bind("limit") int limit, @Bind("offset") int offset);
//
//    @Override
//    @SqlQuery("SELECT * FROM user ORDER BY <orderBy> <orderDir> LIMIT :limit OFFSET :offset")
//    List<User> getOrderedPart(@Bind("limit") int limit, @Bind("offset") int offset,
//                              @Define("orderBy") String orderBy, @Define("orderDir") String orderDir);
//
//    @SqlQuery("SELECT * FROM user WHERE username = :username")
//    Optional<User> getByUsername(@Bind("username") String username);
//
//    @SqlUpdate("UPDATE user SET password = :newPassword  WHERE id = :userId")
//    void changePassword(@Bind("userId") long userId, @Bind("newPassword") String newPassword);
//
//    @SqlQuery("SELECT * FROM user WHERE email = :email")
//    Optional<User> getByEmail(@Bind("email") String email);
//
//    @SqlQuery("SELECT * FROM user WHERE phoneNumber = :phoneNumber")
//    Optional<User> getByPhoneNumber(@Bind("phoneNumber") String phoneNumber);
//
//    @SqlQuery("SELECT COUNT(id) FROM user")
//    int count();
//}
package com.bookshopweb.dao;

import com.bookshopweb.beans.Address;
import com.bookshopweb.beans.GoogleUser;
import com.bookshopweb.beans.User;
import com.bookshopweb.beans.WishlistItem;
import com.bookshopweb.jdbiInterface.UserJDBI;
import com.bookshopweb.utils.JDBCUtils;
import com.bookshopweb.utils.JDBIUltis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO extends AbsDAO<User> {
    Connection conn = JDBCUtils.getConnection();
    public User getUserByPhoneNumber(String phoneNumber){
        return JDBIUltis.getJDBI().onDemand(UserJDBI.class).getByPhoneNumber(phoneNumber);
    }
    public User selectPrevalue(Long id){
        User result = null;
        try {
            String sql = "select * from user where id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                String username = rs.getString("username");
                String password = rs.getString("password");
                String fullname = rs.getString("fullname");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                int gender = rs.getInt("gender");
                String role = rs.getString("role");
                Timestamp createAt = rs.getTimestamp("createAt");


                result = new User(id, username, password, fullname, email, phoneNumber, gender, role, createAt);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    public User selectByUserName(String userName){
        User result = null;
        try {
            String sql = "select * from user where username =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, userName);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Long id = rs.getLong("id");
                String password = rs.getString("password");
                String fullname = rs.getString("fullname");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                int gender = rs.getInt("gender");

                String role = rs.getString("role");
                Timestamp createAt = rs.getTimestamp("createAt");

                result = new User(id, userName, password, fullname, email, phoneNumber, gender, role, createAt);
                result.setAccuracy(new AccurancyDAO().getByUserName(userName) == null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    @Override
    public int delete(User user, String ip) {
        int result =0;
        try {
            new WishlistItemDAO().deleteByUserId(user.getId());
            new OrderDAO().deleteByUserId(user.getId());
            new GoogleUserDAO().deleteByUserId(user.getId());
            new CartDAO().deleteByUserId(user.getId());
            new AddressDAO().deleteByUserId(user.getId());
            new AccurancyDAO().deleteByUserId(user.getId());


            String sql = "delete from user where id =?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, user.getId());
            result = st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.delete(user, ip);
        return result;
    }

    @Override
    public int update(User user, String ip) {
        super.update(user,ip);
        int result  = 0;
        try {
            String sql = "update user " +
                    "set password=?, fullname=?, email=?, phoneNumber=?,gender=?, role=?" +
                    "where id=?";

            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, user.getPassword());
            st.setString(2, user.getFullname());
            st.setString(3, user.getEmail());
            st.setString(4, user.getPhoneNumber());
            st.setInt(5, user.getGender());
            st.setString(6, user.getRole());
            st.setLong(7, user.getId());
            result = st.executeUpdate();
            st.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public int insert(User user, String ip) {

        int result = 0;
        try {
            String sql = "insert into user(id, username, password, fullname, email, phoneNumber, gender, role ) " +
                    "values(?,?,?,?,?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setLong(1, user.getId());
            st.setString(2, user.getUsername());
            st.setString(3, user.getPassword());
            st.setString(4, user.getFullname());
            st.setString(5, user.getEmail());
            st.setString(6, user.getPhoneNumber());
            st.setInt(7, user.getGender());
            st.setString(8, user.getRole());
            result = st.executeUpdate();
            st.close();
//            UserDAO userDAO = new UserDAO();
//            if (user.getId() == 0L) {
//                userDAO.getByUsername(user.getUsername()).ifPresent(u -> {
//                    user.setId(u.getId());
//                });
//            }
//
//
//            user.getAddress().setUserId(user.getId());
//            new AddressDAO().insertAddress(user.getAddress());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        UserJDBI userJDBI = JDBIUltis.getJDBI().onDemand(UserJDBI.class);
//        userJDBI.addUsser(user);
        super.insert(user, ip);
        return result;
    }
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = mapResultSetToUser(resultSet);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return users;
    }

    public List<User> getPart(int limit, int offset) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = mapResultSetToUser(resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return users;
    }

    public List<User> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user ORDER BY " + orderBy + " " + orderDir + " LIMIT ? OFFSET ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = mapResultSetToUser(resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return users;
    }

    public Optional<User> getByUsername(String username) {
        Optional<User> user = Optional.empty();
        String query = "SELECT * FROM user WHERE username = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = Optional.of(mapResultSetToUser(resultSet));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return user;
    }

    public void changePassword(long userId, String newPassword) {
        String query = "UPDATE user SET password = ? WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, newPassword);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public Optional<User> getByEmail(String email) {
        Optional<User> user = Optional.empty();
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = Optional.of(mapResultSetToUser(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return user;
    }

    public User getUserByEmail(String email) {
        User user = null;
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = mapResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return user;
    }

    public Optional<User> getByPhoneNumber(String phoneNumber) {
        Optional<User> user = Optional.empty();
        String query = "SELECT * FROM user WHERE phoneNumber = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, phoneNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = Optional.of(mapResultSetToUser(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return user;
    }

    public int count() {
        int count = 0;
        String query = "SELECT COUNT(id) FROM user";
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
    public Optional<User> getById(long id) {
        Optional<User> userOptional = Optional.empty();
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = mapResultSetToUser(resultSet);
                    userOptional = Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
        return userOptional;
    }
    public User getByGoogle(GoogleUser googleUser) {
        User userOptional = null;
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setLong(1, googleUser.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    userOptional = mapResultSetToUser(resultSet);
                    userOptional.setGoogleUser(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ
        }
        return userOptional;
    }
    public List<User> getByRole(String role) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user WHERE role = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, role);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = mapResultSetToUser(resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return users;
    }


    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setFullname(resultSet.getString("fullname"));
        user.setEmail(resultSet.getString("email"));
        user.setPhoneNumber(resultSet.getString("phoneNumber"));
        user.setGender(resultSet.getInt("gender"));
        user.setRole(resultSet.getString("role"));
        user.setCreateAt(resultSet.getTimestamp("createAt"));
        user.setAccuracy(new AccurancyDAO().getByUserName(resultSet.getString("username"))==null);


        return user;
    }

}