package com.bookshopweb.filter;

import com.bookshopweb.beans.AccurancyUser;
import com.bookshopweb.beans.User;
import com.bookshopweb.dao.AccurancyDAO;
import com.bookshopweb.service.SendMail;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@WebFilter(filterName = "AccurancyFilter", value = "/*")
public class AccurancyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        List<String> filter = new ArrayList<>();
        filter.add("/signout");
        filter.add("/accuracy");
        filter.add("/accuracyView.jsp");
//        filter.add("/signout");
//        System.out.println(request.getRequestURI());
        if(session != null && !filter.contains(request.getRequestURI())){
            User currentUser = (User) session.getAttribute("currentUser");
            if(currentUser == null){
                chain.doFilter(request, response);
            }else{
                //Kiểm tra xem tài khoản đã xác thực hay chưa
                if(currentUser.isAccuracy()){
                    //Đã xác thực sẽ chuyển đến trang chủ
                    if(request.getRequestURI().equals("/accuracyView.jsp")){
                        response.sendRedirect(request.getContextPath()+"/");
                    }else {

                        chain.doFilter(request, response);
                    }
                }else{
                    // Tài khoản chưa xác thực thì sẽ gửi 1 mã xác thực đến email
                    // Và chuyển người dùng đến trang xác thực
                    AccurancyUser accurancyUser = new AccurancyDAO().getByUserName(currentUser.getUsername());
                    if(Calendar.getInstance().getTimeInMillis() > accurancyUser.getEndAt().getTime()){

                        accurancyUser = new AccurancyUser(currentUser.getUsername());
                        new AccurancyDAO().insertAccurancy(accurancyUser);
                        SendMail.sendEmail(currentUser.getEmail(),accurancyUser.getAccurancyCode(), currentUser.getUsername());
                    }
//                    response.sendRedirect (request.getContextPath()+"/accuracyView.jsp");
                    request.getRequestDispatcher("/WEB-INF/views/accuracyView.jsp").forward(request, response);
                }
            }
        }else{
            chain.doFilter(request, response);
        }

    }
}
