package com.bookshopweb.servlet.admin.log;

import com.bookshopweb.beans.Log;
import com.bookshopweb.jdbiInterface.LogJDBI;
import com.bookshopweb.utils.JDBIUltis;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/logManagerServlet")
public class LogManagerServlet extends HttpServlet {
    LogJDBI logDAO = JDBIUltis.getJDBI().onDemand(LogJDBI.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.getRequestDispatcher("/WEB-INF/views/logManagerView.jsp").forward(req, resp);
        req.getRequestDispatcher("/WEB-INF/views/logManagerView.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = Integer.parseInt(req.getParameter("page"))-1;
        int level = Integer.parseInt(req.getParameter("level"));
        int start = 10*page;
        int length = 10;
        JsonArray jsonArray = new JsonArray();
        List<Log> logs = logDAO.selectByLevelLimit(level, start, length);
        for(Log log:logs){

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", log.getId());
            jsonObject.addProperty("ip", log.getIp());
            jsonObject.addProperty("levelLog", log.getLevelLog());
            jsonObject.addProperty("res", log.getRes());
            jsonObject.addProperty("preValue", log.getPreValue());
            jsonObject.addProperty("curValue", log.getCurValue());
            jsonObject.addProperty("createAt", log.getCreateAt().toString());
            jsonObject.addProperty("updateAt", log.getUpdateAt().toString());
            jsonArray.add(jsonObject);

        }
        int totalPage = (logDAO.getQuantityByLevel(level) %10 ==0)?logDAO.getQuantityByLevel(level)/10:(logDAO.getQuantityByLevel(level)/10)+1;
        JsonObject jsonRespone = new JsonObject();
        jsonRespone.add("data", jsonArray);
        jsonRespone.addProperty("totalPage", totalPage);
        resp.setStatus(200);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
//        System.out.println(jsonRespone.toString());
        resp.getWriter().write(jsonRespone.toString());
    }
}
