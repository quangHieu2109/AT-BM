package com.bookshopweb.servlet.client;

import com.bookshopweb.beans.Authenticator;
import com.bookshopweb.beans.User;
import com.bookshopweb.dao.AuthenticatorDAO;
import com.bookshopweb.dao.OTPDAO;
import com.bookshopweb.utils.SignatureUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.EOFException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.List;

@WebServlet(name = "AuthenticatorServlet", value = "/authenticator")
@MultipartConfig
public class AuthenticatorServlet extends HttpServlet {
    SignatureUtils signatureUtils = new SignatureUtils();
    AuthenticatorDAO authenticatorDAO = new AuthenticatorDAO();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("currentUser");
        List<Authenticator> authenticators = authenticatorDAO.getAllByUserId(user.getId());
        String responseString ="";
        for(Authenticator  authenticator: authenticators){
            responseString += convertAuthToHtml(authenticator);
        }
        resp.getWriter().write(responseString);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/views/authenticatorView.jsp").forward(req, resp);
    }
    protected String convertAuthToHtml(Authenticator authenticator){
        int countOrder = authenticatorDAO.getCountSignature(authenticator.getId());
        String status1 = authenticator.getStatus() ==1? "<span class=\"badge bg-info text-dark\">Đang sử dụng</span>":
        "<span class=\"badge bg-danger\">Đã dừng</span>";
     String ressult = String.format("<tr>\n" +
             "                                    <th>%d</th>\n" +
             "                                    <td style=\"width:200px\">\n" +
             "                                    <span title=\"%s\" class=\"truncate\">\n" +
             "                                     %s\n" +
             "                                    </span>\n" +
             "                                    </td>\n" +
             "                                    <td>%s</td>\n" +
             "                                    <td>\n" +
             "                                        %s"+
             "\n" +
             "                                    </td>\n" +
             "                                    <td>\n" +
             "                                        %d\n" +
             "                                    </td>\n" +
             "                                </tr>", authenticator.getId(), authenticator.getPublicKey(), authenticator.getPublicKey(),
             authenticator.getCreatedAt().toString(), status1, countOrder).toString();
     return ressult;
    }
}
