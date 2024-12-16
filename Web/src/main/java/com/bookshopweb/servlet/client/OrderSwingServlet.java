package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.*;
import com.bookshopweb.dao.*;
import com.bookshopweb.utils.HashUtils;
import com.bookshopweb.utils.IPUtils;
import com.bookshopweb.utils.SignatureUtils;
import com.bookshopweb.utils.VoucherUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@WebServlet(name = "OrderSwingServlet", value = "/orderSwing")
@MultipartConfig
public class OrderSwingServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderSignatureDAO orderSignatureDAO = new OrderSignatureDAO();
    private final UserDAO userDAO = new UserDAO();

    private static final int ORDERS_PER_PAGE = 3;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        User user = userDAO.selectByUserName(username);
        if (user == null) {
            response.setStatus(400);
            response.getWriter().write("Username is incorrect!");
        } else {
            List<Order> orders = orderDAO.getUnconfirmOrdersByUsername(username);
            JsonArray jsonArray = new JsonArray();
            for(Order order: orders){
                jsonArray.add(JsonParser.parseString(order.getInfo()));
            }
            response.getWriter().write(jsonArray.toString());
            response.setStatus(200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); // Thiết lập mã hóa cho yêu cầu
        resp.setContentType("application/json; charset=UTF-8"); // Thiết lập mã hóa cho phản hồi
        resp.setCharacterEncoding("UTF-8");

        String orderSignatures = req.getParameter("orderSignatures");
        System.out.println("orderSignatures "+orderSignatures);
        StringBuilder payload = new StringBuilder();
        String line;
        BufferedReader reader = req.getReader();
        while ((line = reader.readLine()) != null) {
            payload.append(line);
        }

        // In ra payload
        System.out.println("Payload: " + payload.toString());
        JSONArray jsonArray = new JSONArray(orderSignatures);
        SignatureUtils signatureUtils = new SignatureUtils();
        Authenticator authenticator;
        String error = "";
        List<OrderSignature> signatures = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        for (Object object : jsonArray) {
            JSONObject jsonObject = new JSONObject(object.toString());
            long id = jsonObject.getLong("id");
            String signature = jsonObject.getString("signature");
            Order order = orderDAO.selectPrevalue(id);
            // Kiểm tra orderId có đúng không
            if (order == null) {
                resp.setStatus(400);
                resp.getWriter().write("orderId " + id + " does not exist!");
                return;
            } else {

                authenticator = new AuthenticatorDAO().getByUserId(order.getUserId());
                // Kiểm tra người dùng đã có privateKey hợp lệ chưa
                if (authenticator == null | authenticator.getStatus() == 0) {
                    resp.setStatus(400);
                    resp.getWriter().write("You have not created a PrivateKey or your PrivateKey is locked, please create a new Key!");
                    return;
                }
                signatureUtils.loadPublicKey(authenticator.getPublicKey());
                // Kiểm tra chữ ký có đúng người đặt hàng ký không
                String hashOrder = HashUtils.hash(new OrderDAO().selectPrevalue(order.getId()).getInfo());
//                System.out.println(order.getInfo());
//                System.out.println(hashOrder);
                if (!signatureUtils.verify(hashOrder, signature)) {
                    resp.setStatus(400);
                    resp.getWriter().write("You signature is invalid!");
                    return;
                }
                OrderSignature orderSignature = orderSignatureDAO.getByOrderId(order.getId());
                orderSignature.setAuthId(authenticator.getId());
                orderSignature.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                orderSignature.setSignature(signature);
                signatures.add(orderSignature);
                orders.add(order);
            }
//
        }
        for (int i = 0; i < orders.size(); i++) {
            OrderSignature orderSignature = signatures.get(i);
            Order order = orders.get(i);
            order.setStatus(0);
            orderSignatureDAO.addOrderSignature(orderSignature);
            orderDAO.updateStatus(order.getStatus(), order.getId());
        }

        resp.setStatus(200);
        resp.getWriter().write("Orders signed successfully!");

    }

    private String check(List<String> list) {
        if (list.size() == 1) {
            return list.get(0);
        }

        return list.get(0) + " và " + (list.size() - 1) + " sản phẩm khác";
    }


}
