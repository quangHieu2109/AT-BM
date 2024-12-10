package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.CartItem;
import com.bookshopweb.beans.CategorysOfVoucher;
import com.bookshopweb.beans.Product;
import com.bookshopweb.beans.Voucher;
import com.bookshopweb.dao.*;
import com.bookshopweb.utils.VoucherUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@WebServlet("/voucherServlet")
@MultipartConfig
public class VoucherServlet extends HttpServlet {
    private VoucherDAO voucherDAO = new VoucherDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private CategorysOfVoucherDAO categorysOfVoucherDAO = new CategorysOfVoucherDAO();
    private CartItemDAO cartItemDAO = new CartItemDAO();
    private ProductDAO productDAO = new ProductDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        if(type == null){

        }else{
            int voucherType = Integer.parseInt(req.getParameter("voucherType"));
            List<Voucher> vouchers = voucherDAO.getByType(voucherType);
            JsonArray vouchersJson = new JsonArray();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            long now = Calendar.getInstance().getTimeInMillis();
            for(Voucher voucher: vouchers){
                if(now >= voucher.getStartAt().getTime() && now <= voucher.getEndAt().getTime() && voucher.getQuantity()>0){
                    JsonObject voucherJson = new JsonObject();
                    voucherJson.addProperty("id", voucher.getId());
                    voucherJson.addProperty("voucherName", voucher.getVoucherName());
                    voucherJson.addProperty("percentDecrease", voucher.getPercentDecrease());
                    voucherJson.addProperty("maxDecrease", voucher.getMaxDecrease());
                    voucherJson.addProperty("minPrice", voucher.getMinPrice());
                    voucherJson.addProperty("voucherImage", voucher.getVoucherImage());
                    voucherJson.addProperty("startAt", dateFormat.format(voucher.getStartAt()));
                    voucherJson.addProperty("endAt", dateFormat.format(voucher.getEndAt()));
                    List<CategorysOfVoucher> categorysOfVouchers = categorysOfVoucherDAO.getByVoucherId(voucher.getId());
                    String categories = "Mọi sản phẩm";
                    if(categorysOfVouchers.size() >0){
                        categories="";
                        for(CategorysOfVoucher categorysOfVoucher: categorysOfVouchers){
                            categories += categoryDAO.selectPrevalue(categorysOfVoucher.getCategoryId()).getName()+", ";
                        }
                        categories = categories.substring(0, categories.length()-2);
                    }
                    voucherJson.addProperty("categories", categories);
                    vouchersJson.add(voucherJson);
                }

            }
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.add("vouchers", vouchersJson);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.getWriter().write(jsonResponse.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("voucherId"+req.getParameter("voucherId"));
//        System.out.println("cartItemIdsString"+Arrays.toString(req.getParameterValues("cartItemIds")));
//        System.out.println("ship"+req.getParameter("ship"));
        long voucherId = Long.parseLong(req.getParameter("voucherId"));
        String[] cartItemIdsString = req.getParameterValues("cartItemIds");
        double ship = Double.parseDouble(req.getParameter("ship"));


        double decrease = VoucherUtils.getDecrease(voucherId, cartItemIdsString, ship);

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("decrease", decrease);
        resp.setContentType("application/json");
        resp.getWriter().write(jsonResponse.toString());

    }
}
