package com.bookshopweb.servlet.admin.statiscal;

import com.bookshopweb.beans.Product;
import com.bookshopweb.beans.StatisticalProduct;
import com.bookshopweb.dao.CategoryDAO;
import com.bookshopweb.dao.StatiscalProductDAO;
import com.bookshopweb.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;




@WebServlet(name = "StatiscalProManagerServlet", value = "/admin/statiscalManager/product")
public class StatiscalProManagerServlet extends HttpServlet {
    private final StatiscalProductDAO statiscalProduct = new StatiscalProductDAO();

    private static final int STATISCAL_PRODUCT_PER_PAGE = 10;

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
        String orderBy = orderParam.map(statiscalProduct::getFirst).orElse("total_sold");
        String orderDir = orderParam.map(statiscalProduct::getLast).orElse("DESC");


        // Nếu không có tiêu chí lọc
        if (startYear.isEmpty() ||endYear.isEmpty()) {
            totalProducts = Protector.of(() -> statiscalProduct.count()).get(0);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startYear, formatter);
            LocalDate endDate = LocalDate.parse(endYear, formatter);

            String sqlStartDate = startDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String sqlEndDate = endDate.atTime(LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            totalProducts = Protector.of(() -> statiscalProduct.countProductsSoldByTime(sqlStartDate, sqlEndDate)).get(0);
        }

        // Tính tổng số trang (= tổng số sản phẩm / số sản phẩm trên mỗi trang)
        int totalPages = totalProducts / STATISCAL_PRODUCT_PER_PAGE;
        if (totalProducts % STATISCAL_PRODUCT_PER_PAGE != 0) {
            totalPages++;
        }

        // Lấy trang hiện tại, gặp ngoại lệ (chuỗi không phải số, nhỏ hơn 1, lớn hơn tổng số trang) thì gán bằng 1
        String pageParam = Optional.ofNullable(req.getParameter("page")).orElse("1");
        int page = Protector.of(() -> Integer.parseInt(pageParam)).get(1);
        if (page < 1 || page > totalPages) {
            page = 1;
        }

        // Tính mốc truy vấn (offset)
        int offset = (page - 1) * STATISCAL_PRODUCT_PER_PAGE;
        List<StatisticalProduct> products;

        // Nếu không có tiêu chí lọc
        if (startYear.isEmpty() ||endYear.isEmpty()) {
            products = Protector.of(() -> statiscalProduct.getUnsold(
                    STATISCAL_PRODUCT_PER_PAGE, offset)).get(ArrayList::new);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(startYear, formatter);
            LocalDate endDate = LocalDate.parse(endYear, formatter);

            String sqlStartDate = startDate.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String sqlEndDate = endDate.atTime(LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            products = Protector.of(() -> statiscalProduct.getUnsoldOrdered(
                    STATISCAL_PRODUCT_PER_PAGE, offset, sqlStartDate, sqlEndDate, orderBy,orderDir
            )).get(ArrayList::new);
        }
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("page", page);
        req.setAttribute("productsAll", products);
        req.setAttribute("startYear", startYear);
        req.setAttribute("endYear", endYear);
        req.setAttribute("order", orderParam.orElse("total_sold-DESC"));
        req.getRequestDispatcher("/WEB-INF/views/statiscalProManagerView.jsp").forward(req, resp);
    }
}
