package com.bookshopweb.servlet.admin.statiscal;

import com.bookshopweb.beans.ImportProduct;
import com.bookshopweb.beans.StatisticalProduct;
import com.bookshopweb.dao.ImportProductDAO;
import com.bookshopweb.dao.StatiscalProductDAO;
import com.bookshopweb.dao.UserDAO;
import com.bookshopweb.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "StatiscalPIManagerServlet", value = "/admin/statiscalManager/productImport")
public class StatiscalPIManagerServlet extends HttpServlet {
    private final ImportProductDAO importProductDAO = new ImportProductDAO();
    private static final int PRODUCT_IMPORT_PER_PAGE = 10;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int totalProducts ;
        String startYear = Protector.of(() -> req.getParameter("startYear")).get("");
        String   endYear =  Protector.of(() -> req.getParameter("endYear")).get("");
        Optional<String> orderParam = Optional.ofNullable(req.getParameter("order"));
        String orderBy = orderParam.map(importProductDAO::getFirst).orElse("createAt");
        String orderDir = orderParam.map(importProductDAO::getLast).orElse("DESC");


        // Nếu không có tiêu chí lọc
        if (startYear.isEmpty() ||endYear.isEmpty()) {
            totalProducts = Protector.of(() -> importProductDAO.countTotalImports()).get(0);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startYear, formatter);
            LocalDate endDate = LocalDate.parse(endYear, formatter);

            String sqlStartDate = startDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String sqlEndDate = endDate.atTime(LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            totalProducts = Protector.of(() -> importProductDAO.countByTime(sqlStartDate, sqlEndDate)).get(0);
        }


        int totalPages = totalProducts / PRODUCT_IMPORT_PER_PAGE;
        if (totalProducts % PRODUCT_IMPORT_PER_PAGE != 0) {
            totalPages++;
        }

        String pageParam = Optional.ofNullable(req.getParameter("page")).orElse("1");
        int page = Protector.of(() -> Integer.parseInt(pageParam)).get(1);
        if (page < 1 || page > totalPages) {
            page = 1;
        }

        int offset = (page - 1) * PRODUCT_IMPORT_PER_PAGE;
        List<ImportProduct> products;


        if (startYear.isEmpty() ||endYear.isEmpty()) {
            products = Protector.of(() -> importProductDAO.getPart(
                    PRODUCT_IMPORT_PER_PAGE, offset)).get(ArrayList::new);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startYear, formatter);
            LocalDate endDate = LocalDate.parse(endYear, formatter);

            String sqlStartDate = startDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String sqlEndDate = endDate.atTime(LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            products = Protector.of(() -> importProductDAO.getPartByOrdered(
                    PRODUCT_IMPORT_PER_PAGE, offset, sqlStartDate, sqlEndDate, orderBy,orderDir
            )).get(ArrayList::new);
        }
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("page", page);
        req.setAttribute("productsImport", products);
        req.setAttribute("startYear", startYear);
        req.setAttribute("endYear", endYear);
        req.setAttribute("order", orderParam.orElse("createAt-DESC"));
        req.getRequestDispatcher("/WEB-INF/views/statiscalPIManagerView.jsp").forward(req, resp);
    }
}
