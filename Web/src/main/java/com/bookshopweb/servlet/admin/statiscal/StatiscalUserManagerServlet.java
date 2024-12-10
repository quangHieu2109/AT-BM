package com.bookshopweb.servlet.admin.statiscal;

import com.bookshopweb.beans.StatisticalCustomer;
import com.bookshopweb.beans.StatisticalProduct;
import com.bookshopweb.dao.StatiscalProductDAO;
import com.bookshopweb.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "StatiscalUserManagerServlet", value = "/admin/statiscalManager/user")
public class StatiscalUserManagerServlet extends HttpServlet {
    private final StatiscalProductDAO statiscalUser = new StatiscalProductDAO();

    private static final int USER_PER_PAGE = 5;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int totalUser ;
        int months;
        String monthsString = Protector.of(() -> req.getParameter("months")).get("");
        Optional<String> orderParam = Optional.ofNullable(req.getParameter("order"));
        String orderBy = orderParam.map(statiscalUser::getFirst).orElse("lastOrderTime");
        String orderDir = orderParam.map(statiscalUser::getLast).orElse("ASC");
        if(monthsString.isEmpty()){
            totalUser = 0;
            months = 0;
        }else{
            months = Integer.parseInt(monthsString);
            System.out.println("so thang la " +months);
            totalUser =  Protector.of(() -> statiscalUser.countUser(months)).get(0);
            System.out.println("Số user là "+ totalUser);
        }
        int totalPages = totalUser / USER_PER_PAGE;
        System.out.println("tổng số page là "+totalPages);
        if (totalPages % USER_PER_PAGE != 0) {
            totalPages++;
        }
        if (totalPages == 0) {
            totalPages = 1;
        }
        String pageParam = Optional.ofNullable(req.getParameter("page")).orElse("1");
        int page = Protector.of(() -> Integer.parseInt(pageParam)).get(1);
        if (page < 1 || page > totalPages) {
            page = 1;
        }
        int offset = (page - 1) * USER_PER_PAGE;
        List<StatisticalCustomer> users;
        if(monthsString.isEmpty()){
            users = null;
        }else{
            users = Protector.of(()->statiscalUser.getCustomersNotReorder(USER_PER_PAGE,offset,months,orderBy,orderDir)).get(ArrayList::new);
        }
        req.setAttribute("totalPages", totalPages);
        System.out.println("totalPages"+totalPages);
        req.setAttribute("page", page);
        System.out.println("page"+page);
        req.setAttribute("users", users);
        System.out.println(users);
        req.setAttribute("months", monthsString);
        System.out.println("monthsString"+monthsString);
        req.setAttribute("order", orderParam.orElse("lastOrderTime-ASC"));
        System.out.println(orderParam);
        req.getRequestDispatcher("/WEB-INF/views/statiscalUserManagerView.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
