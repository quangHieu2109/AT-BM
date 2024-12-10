package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.AccurancyUser;
import com.bookshopweb.beans.Order;
import com.bookshopweb.beans.OrderItem;
import com.bookshopweb.beans.User;
import com.bookshopweb.dao.AccurancyDAO;
import com.bookshopweb.dao.CartDAO;
import com.bookshopweb.dao.OrderDAO;
import com.bookshopweb.dao.OrderItemDAO;
import com.bookshopweb.dto.ErrorMessage;
import com.bookshopweb.dto.OrderRequest;
import com.bookshopweb.dto.SuccessMessage;
import com.bookshopweb.service.SendMail;
import com.bookshopweb.utils.JsonUtils;
import com.bookshopweb.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "AccuracyServlet", value = "/accuracy")
public class AccuracyServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("currentUser");
        AccurancyUser accurancyUser = new AccurancyUser(user.getUsername());
        new AccurancyDAO().insertAccurancy(accurancyUser);
        boolean send =  SendMail.sendEmail(user.getEmail(),accurancyUser.getAccurancyCode(), user.getUsername());
//        int status =200;
//        if(!send){
//            status = 404;
//        }
//        new AccurancyDAO().delete(accurancyUser.getUserName());
        response.sendRedirect(request.getContextPath()+"/views/accuracyView.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy đối tượng orderRequest từ JSON trong request
        String verificationCode = request.getParameter("verificationCode");
        User user = (User) request.getSession().getAttribute("currentUser");
        AccurancyDAO accurancyDAO = new AccurancyDAO();
        AccurancyUser accurancyUser = accurancyDAO.getByUserName(user.getUsername());
        System.out.println("input "+verificationCode);
        System.out.println(accurancyUser.getAccurancyCode());
        if(verificationCode.equals(accurancyUser.getAccurancyCode())){
            accurancyDAO.delete(user.getUsername());
            user.setAccuracy(true);

            response.sendRedirect(request.getContextPath()+"/");
        }else{
            request.setAttribute("error","Mã xác nhận không chính xác!");
            request.getRequestDispatcher("/WEB-INF/views/accuracyView.jsp").forward(request, response);
        }
    }
}
