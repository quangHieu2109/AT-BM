package com.bookshopweb.servlet.client;

import com.bookshopweb.api.CaptchaApi;
import com.bookshopweb.beans.User;
import com.bookshopweb.dao.UserDAO;
import com.bookshopweb.utils.HashingUtils;
import com.bookshopweb.utils.IPUtils;
import com.bookshopweb.utils.Protector;
import com.bookshopweb.utils.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "SigninServlet", value = "/signin")
public class SigninServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/signinView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{



        System.out.println(IPUtils.getIPInfo(request));


        Map<String, String> values = new HashMap<>();
        values.put("username", request.getParameter("username"));
        values.put("password", request.getParameter("password"));

        Map<String, List<String>> violations = new HashMap<>();
        Optional<User> userFromServer = Protector.of(() -> userDAO.getByUsername(values.get("username")))
                .get(Optional::empty);
        violations.put("usernameViolations", Validator.of(values.get("username"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(25)
                .isExistent(userFromServer.isPresent(), "Tên đăng nhập")
                .toList());
        violations.put("passwordViolations", Validator.of(values.get("password"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(32)
                .changeTo(HashingUtils.hash(values.get("password")))
                .isEqualTo(userFromServer.map(User::getPassword).orElse(""), "Mật khẩu")
                .toList());
        String username = request.getParameter("username")==null?"":request.getParameter("username");
        Integer countWrongPass = (Integer) getServletContext().getAttribute(username);
        if(countWrongPass == null){
            countWrongPass = 0;
        }
        if(countWrongPass>=5){
            String responseCaptcha = request.getParameter("g-recaptcha-response");
            boolean checkCaptcha =  CaptchaApi.checkCaptcha(responseCaptcha);
            if(checkCaptcha == false){
                violations.put("captchaViolations", List.of("Captcha không đúng"));

            }
        }

        int sumOfViolations = violations.values().stream().mapToInt(List::size).sum();

        if (sumOfViolations == 0 && userFromServer.isPresent()) {
            request.getSession().setAttribute("countWrongPass",0);
            request.getSession().setAttribute("currentUser", userFromServer.get());
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            countWrongPass++;
            request.setAttribute("values", values);
            request.setAttribute("violations", violations);
            getServletContext().setAttribute(username, countWrongPass);
            request.getSession().setAttribute("countWrongPass", countWrongPass);
            request.getRequestDispatcher("/WEB-INF/views/signinView.jsp").forward(request, response);
        }
    }
}
