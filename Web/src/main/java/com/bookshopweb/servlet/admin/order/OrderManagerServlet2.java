package com.bookshopweb.servlet.admin.order;

import com.bookshopweb.beans.*;
import com.bookshopweb.dao.*;
import com.bookshopweb.utils.IPUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@WebServlet(value = "/orderManagerServlet2")
public class OrderManagerServlet2 extends HttpServlet {
    NumberFormat formatter = NumberFormat.getInstance();
    OrderDAO orderDAO = new OrderDAO();
    UserDAO userDAO = new UserDAO();
    OrderItemDAO orderItemDAO = new OrderItemDAO();
    OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        if (type.equalsIgnoreCase("order")) {
            getOrder(req, resp);
        } else {
            getDetail(req, resp);
        }


    }

    protected void getDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String baseName = "lang_vi_VN";

        Locale.setDefault(new Locale("vi_VN"));
        if (session.getAttribute("lang") != null && session.getAttribute("lang").toString().equals("en")) {
            baseName = "lang_en_US";
        }
        ResourceBundle resources = ResourceBundle.getBundle(baseName);
        long orderId = Long.parseLong(req.getParameter("orderId"));

        Order order = orderDAO.selectPrevalue(orderId);
        List<OrderItem> orderItems = orderItemDAO.getByOrderId(orderId);
        JsonArray jsonArray = new JsonArray();
        ProductDAO productDAO = new ProductDAO();
        int totalPrice = 0;


        for (OrderItem orderItem : orderItems) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", orderItem.getId());
            Product product = productDAO.getByIdProduct(orderItem.getProductId());
            totalPrice += orderItem.getQuantity() * (1 - product.getDiscount() / 100) * product.getPrice();
            product.setPrice(product.getPrice() * (1 - product.getDiscount() / 100));
            String imgSrc = req.getContextPath() + "/img/" + product.getImageName();
            jsonObject.addProperty("image", "<img width=\"80\"\n" +
                    "                                     height=\"80\" src=\"" + imgSrc + "\">");
            String info = "Name: " + product.getName() + "<br/>"
                    + "Author: " + product.getAuthor() + "<br>"
                    + "Pages: " + product.getPages() + "<br>"
                    + "YearPublishing: " + product.getYearPublishing();
            jsonObject.addProperty("info", info);
            jsonObject.addProperty("quantity", product.getQuantity());
            jsonObject.addProperty("price", formatter.format(product.getPrice()));
            jsonArray.add(jsonObject);
        }
        OrderDetail orderDetail = orderDetailDAO.getByOrderId(order.getId());
        double shipPrice = order.getDeliveryPrice();
        if (orderDetail != null) {
            System.out.println(orderDetail);
            totalPrice -= orderDetail.getProductVoucherDecrease();
            totalPrice -= orderDetail.getShipVoucherDecrease();
            shipPrice -= orderDetail.getShipVoucherDecrease();
        }
        User user = userDAO.selectPrevalue(order.getUserId());
        JsonObject jsonResopne = new JsonObject();
        String status = "";
        switch (order.getStatus()) {
            case 0:
                status = "dat_hang_thanh_cong";
                break;
            case 1:
                status = "dang_giao_hang";
                break;
            case 2:
                status = "giao_hang_thanh_cong";
                break;
            case 3:
                status = "da_huy";
                break;
            case 4:
                status = "tra_hang";
                break;

        }
        String deliveryMethod = order.getDeliveryMethod() == 0 ? "giao_hang_tieu_chuan" : "giao_hang_nhanh";
        jsonResopne.add("products", jsonArray);
        jsonResopne.addProperty("orderId", resources.getString("ma_don_hang") + " : " + order.getId());
        jsonResopne.addProperty("receiverInfo", resources.getString("thong_tin_nguoi_nhan"));
        jsonResopne.addProperty("paymentMethod", resources.getString("hinh_thuc_thanh_toan"));
        jsonResopne.addProperty("product", resources.getString("san_pham"));
        jsonResopne.addProperty("price", resources.getString("gia"));
        jsonResopne.addProperty("quantity", resources.getString("so_luong"));
        jsonResopne.addProperty("createdAt", resources.getString("ngay_mua") + " : " + order.getCreateAt().toString());
        jsonResopne.addProperty("status", resources.getString(status));
        jsonResopne.addProperty("fullname", user.getFullname());
        jsonResopne.addProperty("phoneNumber", resources.getString("so_dien_thoai") + " : " + user.getPhoneNumber());
        jsonResopne.addProperty("deliveryMethod", resources.getString(deliveryMethod));
        jsonResopne.addProperty("tempPrice", resources.getString("tam_tinh") + " : " + formatter.format((int) totalPrice));
        jsonResopne.addProperty("totalPrice", resources.getString("tong_cong") + " : " + formatter.format((int) totalPrice + shipPrice));
        jsonResopne.addProperty("deliveryPrice", resources.getString("phi_van_chuyen") + " : " + formatter.format((int) shipPrice));


        resp.setStatus(200);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().write(jsonResopne.toString());


    }

    protected void getOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println(1111);
        int status = Integer.parseInt(req.getParameter("status"));
        int start = Integer.parseInt(req.getParameter("start"));
        int length = Integer.parseInt(req.getParameter("length"));

        List<Order> orders = orderDAO.getByStatusLimit(status, start, length);
        JsonArray jsonArray = new JsonArray();
        for (Order order : orders) {
            JsonObject jsonObject = new JsonObject();
            List<OrderItem> orderItems = orderItemDAO.getByOrderId(order.getId());
            int totalPrice = 0;
            for (OrderItem item : orderItems) {
                Product product = productDAO.selectPrevalue(item.getProductId());
                totalPrice += item.getQuantity() * (1 - product.getDiscount() / 100) * product.getPrice();
            }
            OrderDetail orderDetail = orderDetailDAO.getByOrderId(order.getId());
            double shipPrice = order.getDeliveryPrice();
            if (orderDetail != null) {
                totalPrice -= orderDetail.getProductVoucherDecrease();
                totalPrice -= orderDetail.getShipVoucherDecrease();
                shipPrice -= orderDetail.getShipVoucherDecrease();

            }

            String deliveryMethod = (order.getDeliveryMethod() == 1) ? "Giao hàng nhanh" : "Giao hàng tiết kiệm";
            String updateStatus = "";
            switch (status) {
                case 0:
                    updateStatus = "<select class=\"form-select\" onchange=\"changeStatus(" + order.getId() + ", this.value)\" " +
                            "id=\"sl" + order.getId() + "\">" +
                            "<option value=\"0\" selected>Đặt hành thành công</option>" +
                            "<option value=\"1\" >Đang giao</option>" +
                            "<option value=\"3\" >Đã hủy</option>" +
                            "</select>";
                    break;
                case 1:
                    updateStatus = "<select class=\"form-select\" onchange=\"changeStatus(" + order.getId() + ", this.value)\" " +
                            "id=\"sl" + order.getId() + "\">" +
                            "<option value=\"1\" selected>Đang giao</option>" +
                            "<option value=\"2\" >Giao thành công</option>" +
                            "<option value=\"3\" >Đã hủy</option>" +
                            "</select>";
                    break;
                case 2:
                    updateStatus = "<select class=\"form-select\" onchange=\"changeStatus(" + order.getId() + ", this.value)\" " +
                            "id=\"sl" + order.getId() + "\">" +

                            "<option value=\"2\" selected>Giao hàng thành công</option>" +
                            "<option value=\"4\" >Trả hàng</option>" +

                            "</select>";
                    break;
                case 3:
                    updateStatus = "<select class=\"form-select\" onchange=\"changeStatus(" + order.getId() + ", this.value)\" " +
                            "id=\"sl" + order.getId() + "\">" +

                            "<option value=\"3\" selected>Đã hủy</option>" +
                            "</select>";
                    break;
                case 4:
                    updateStatus = "<select class=\"form-select\" onchange=\"changeStatus(" + order.getId() + ", this.value)\" " +
                            "id=\"sl" + order.getId() + "\">" +

                            "<option value=\"4\" selected>Trả hàng</option>" +
                            "</select>";
                    break;
            }
            String saveBtn = "<button style=\"width: 66px\" class=\"btn btn-secondary m-auto\" id=\"p" + order.getId() + "\">Save</button>";
            String detailBtn = "<button style=\"width: 66px\" class=\"btn btn-primary m-auto\" onclick=\"detail(" + order.getId() + ")\">Detail</button>";
            jsonObject.addProperty("id", order.getId());
            jsonObject.addProperty("idUser", order.getUserId());
            jsonObject.addProperty("deliveryMethod", deliveryMethod);
            jsonObject.addProperty("deliveryPrice", formatter.format((int) shipPrice));
            jsonObject.addProperty("productsPrice", formatter.format((int) totalPrice));
            jsonObject.addProperty("totalPrice", formatter.format((int) shipPrice + totalPrice));
            jsonObject.addProperty("createAt", order.getCreatedAt().toString());
            jsonObject.addProperty("updateAt", (order.getUpdatedAt() == null) ? "" : order.getUpdatedAt().toString());
            jsonObject.addProperty("updateStatus", updateStatus);
            jsonObject.addProperty("operation", "<div class=\"row\">" + saveBtn + detailBtn + "</div>");
            jsonArray.add(jsonObject);
//            System.out.println(jsonObject.toString());
        }
        JsonObject jsonResopne = new JsonObject();
        jsonResopne.add("data", jsonArray);
        jsonResopne.addProperty("recordsTotal", orders.size());
        jsonResopne.addProperty("recordsFiltered", orderDAO.getQuantity(status));
        resp.setStatus(200);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
//        System.out.println(jsonResopne.toString());
        resp.getWriter().write(jsonResopne.toString());


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println(121212);
        long id = Long.parseLong(req.getParameter("id"));
        int status = Integer.parseInt(req.getParameter("value"));
        OrderDAO orderDAO = new OrderDAO();

        resp.setCharacterEncoding("UTF-8");
        Order order = orderDAO.selectPrevalue(id);

        JsonObject jsonResponse = new JsonObject();
        if (order.getStatus() == status) {
            jsonResponse.addProperty("error_notifica", "Trạng thái mới bị trùng với trạng thái cũ, cập nhật thất bại!");
            resp.setStatus(400);
            resp.setContentType("application/json");

            resp.getWriter().write(jsonResponse.toString());
        } else {
            order.setStatus(status);
            order.setUpdatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            if (orderDAO.update(order, IPUtils.getIP(req)) > 0) {

                resp.setStatus(200);
            } else {
                jsonResponse.addProperty("error_notifica", "Cập nhật thất bại!");
                resp.setStatus(400);
                resp.setContentType("application/json");

                resp.getWriter().write(jsonResponse.toString());
            }
        }

    }
}
