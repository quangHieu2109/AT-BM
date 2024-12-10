package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.Address;
import com.bookshopweb.beans.User;
import com.bookshopweb.dao.UserDAO;
import com.bookshopweb.utils.IPUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "SettingServlet", value = "/setting")
public class SettingServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        request.setAttribute("user", user);
        request.getRequestDispatcher("WEB-INF/views/settingView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        Map<String, String> values = new HashMap<>();
        values.put("username", request.getParameter("username"));
        values.put("fullname", request.getParameter("fullname"));
        values.put("email", request.getParameter("email"));
        values.put("phoneNumber", request.getParameter("phoneNumber"));
        values.put("gender", request.getParameter("gender"));


        User newUser = new User(
                user.getId(),
                values.get("username"),
                user.getPassword(),
                values.get("fullname"),
                values.get("email"),
                values.get("phoneNumber"),
                Integer.parseInt(values.get("gender")),

                "CUSTOMER",
                Timestamp.from(Instant.now())
        );

        String successMessage = "Cập nhật thành công!";
        String errorMessage = "Cập nhật không thành công!";

        Optional<User> userWithNewUsername = userDAO.getByUsername(values.get("username"));

        if (!user.getUsername().equals(values.get("username")) && userWithNewUsername.isPresent()) {
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("user", user);
        } else {
            userDAO.update(newUser, IPUtils.getIP(request));

            request.setAttribute("successMessage", successMessage);
            request.setAttribute("user", newUser);
            request.getSession().setAttribute("currentUser", newUser);
        }

        request.getRequestDispatcher("WEB-INF/views/settingView.jsp").forward(request, response);
    }
}
