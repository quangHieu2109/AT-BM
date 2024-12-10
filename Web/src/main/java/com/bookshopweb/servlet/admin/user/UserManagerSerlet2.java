package com.bookshopweb.servlet.admin.user;

import com.bookshopweb.beans.User;
import com.bookshopweb.dao.UserDAO;
import com.bookshopweb.jdbiInterface.UserJDBI;
import com.bookshopweb.mapper.UserMapper;
import com.bookshopweb.utils.IPUtils;
import com.bookshopweb.utils.JDBIUltis;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(value = "/userManagerServlet2")
public class UserManagerSerlet2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int draw = Integer.parseInt(req.getParameter("draw"));
        int start = Integer.parseInt(req.getParameter("start"));
        int length = Integer.parseInt(req.getParameter("length"));
        String searchValue = req.getParameter("search[value]");
        String orderColumn = req.getParameter("order[0][column]");
        String orderDir = req.getParameter("order[0][dir]");
        List<User> users = JDBIUltis.getJDBI().withHandle(handle ->
                handle.createQuery("select * from user limit " + start + ", " + length)
                        .map(new UserMapper()).list());
        int recordsFilterd = JDBIUltis.getJDBI().withHandle(handle ->
                handle.createQuery("select count(*) from user").mapTo(Integer.class).one());
        StringBuilder json = new StringBuilder();
        JsonArray jsonArray = new JsonArray();


        for (User u : users) {
            JsonObject jsonObject = new JsonObject();
            String gender = (u.getGender() == 0) ? "Nam" : "Nu";
            String deleteBtn = "<button class=\"btn btn-danger\" onclick=\"deleteUser(" + u.getId() + ")\">Delete</button>";
            jsonObject.addProperty("id", u.getId());
            jsonObject.addProperty("username", u.getUsername());
            jsonObject.addProperty("password", u.getPassword());
            jsonObject.addProperty("fullname", u.getFullname());
            jsonObject.addProperty("email", u.getEmail());

            jsonObject.addProperty("phoneNumber", u.getPhoneNumber());
            jsonObject.addProperty("gender", gender);
            jsonObject.addProperty("role", u.getRole());

            jsonObject.addProperty("deleteBtn", deleteBtn);
            jsonArray.add(jsonObject);


        }
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.add("data", jsonArray);


//        jsonResponse.addProperty("draw", draw);
        jsonResponse.addProperty("recordsTotal", users.size());
        jsonResponse.addProperty("recordsFiltered", recordsFilterd);
        resp.setContentType("aplication/json");
        resp.setCharacterEncoding("UTF-8");
//        System.out.println(jsonResponse.toString());

        resp.getWriter().write(jsonResponse.toString());

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long id = Long.parseLong(req.getParameter("id"));
        new UserDAO().delete(JDBIUltis.getJDBI().onDemand(UserJDBI.class).getById(id), IPUtils.getIP(req));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        UserDAO userDAO = new UserDAO();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String role = req.getParameter("role");
        String emailRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);
        String username_error="";
        String password_error ="";
        String email_error = "";
        JsonObject jsonObject = new JsonObject();
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$";
        Pattern passwprdPattern = Pattern.compile(passwordRegex);
        Matcher passwordMatcher = passwprdPattern.matcher(password);
        if(userDAO.selectByUserName(username) != null){
            username_error ="Tên đăng nhập đã tồn tại";
        }
        if(!passwordMatcher.matches() || password.length() <8){
            password_error = "Mật khẩu chưa đủ mạnh. Phải từ 8 ký tự trở lên và chưas kí tụ viết in hoa và số";
        }
        if(!emailMatcher.matches()){
            email_error ="Email không hợp lệ";
        }else if(userDAO.getUserByEmail(email) != null){
            email_error="Email đã được đăng ký, vui lòng chọn email khác";
        }
        if(username_error.length() >0 || password_error.length() >0 || email_error.length() >0) {
            jsonObject.addProperty("username_error", username_error);
            jsonObject.addProperty("password_error", password_error);
            jsonObject.addProperty("email_error", email_error);
//            JsonObject jsonResponse = new JsonObject();

            resp.setStatus(400);

            resp.getWriter().write(jsonObject.toString());
        }else{
            User user = new User();
            user.setId(Calendar.getInstance().getTimeInMillis());
            user.setUsername(username);
            user.setPassword(password);
            user.setFullname(fullname);
            user.setEmail(email);
            user.setRole(role);
           int rs = userDAO.insert(user, IPUtils.getIP(req));
            System.out.println(rs);
           if(rs>0){
               JsonObject jsonObject1 = new JsonObject();
               String gender = (user.getGender() == 0) ? "Nam" : "Nu";
               String deleteBtn = "<button class=\"btn btn-danger\" onclick=\"deleteUser(" + user.getId() + ")\">Delete</button>";
               jsonObject1.addProperty("id", user.getId());
               jsonObject1.addProperty("username", user.getUsername());
               jsonObject1.addProperty("password", user.getPassword());
               jsonObject1.addProperty("fullname", user.getFullname());
               jsonObject1.addProperty("email", user.getEmail());

               jsonObject1.addProperty("phoneNumber", user.getPhoneNumber());
               jsonObject1.addProperty("gender", gender);
               jsonObject1.addProperty("role", user.getRole());

               jsonObject1.addProperty("deleteBtn", deleteBtn);
               resp.setStatus(200);
               resp.getWriter().write(jsonObject1.toString());
           }else{
               resp.setStatus(400);

           }
        }


    }


}
