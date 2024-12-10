package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "PaymentReturnServlet", value = "/vnpay_return")
public class PaymentReturnServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map fields = new HashMap();
        for (Enumeration params = req.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = (String) params.nextElement();
            String fieldValue = req.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }
        String responseCode = req.getParameter("vnp_ResponseCode");
        String stringResponseCode = "";

        String vnp_SecureHash = req.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = "";
        try {
            signValue= Constants.hashAllFields(fields);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (signValue.equals(vnp_SecureHash)) {
switch (responseCode) {
            case "00":
                stringResponseCode = "Giao dịch thành công";
                break;
            case "07":
                stringResponseCode = "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).";
                break;
            case "09":
                stringResponseCode = "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.";
                break;
            case "10":
                stringResponseCode = "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần";
                break;
            case "11":
                stringResponseCode = "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.";
                break;
            case "12":
                stringResponseCode = "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.";
                break;
            case "13":
                stringResponseCode = "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.";
                break;
            case "24":
                stringResponseCode = "Giao dịch không thành công do: Khách hàng hủy giao dịch";
                break;
            case "51":
                stringResponseCode = "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.";
                break;
            case "65":
                stringResponseCode = "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.";
                break;
            case "75":
                stringResponseCode = "Ngân hàng thanh toán đang bảo trì.";
                break;
            case "79":
                stringResponseCode = "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch";
                break;

            default:
                stringResponseCode = "Lỗi không xác định";
                break;
        }


        } else {
            stringResponseCode = "Chữ ký không hợp lệ";

        }
        req.setAttribute("stringResponseCode", stringResponseCode);
        req.getRequestDispatcher("/WEB-INF/views/paymentResult.jsp").forward(req, resp);
    }
}
