package com.bookshopweb.servlet.general;

import com.bookshopweb.api.ViettelPostApi;
import com.bookshopweb.beans.Address;
import com.bookshopweb.beans.User;
import com.bookshopweb.dao.AddressDAO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/address"})
public class AddressServlet extends HttpServlet {
    private static List<ViettelPostApi.Province> provinces = null;
    private static List<ViettelPostApi.District> districts = null;
    private static List<ViettelPostApi.Ward> wards = null;
    private static final ViettelPostApi vietelPostApi = new ViettelPostApi();

    static {

        provinces = vietelPostApi.getProvinces(null);
        districts = vietelPostApi.getDistricts(null);
        wards = vietelPostApi.getWards(null);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String provinceID = req.getParameter("provinceID");
        String districtID = req.getParameter("districtID");
        String wardID = req.getParameter("wardID");
        String provinceName = req.getParameter("provinceName");
        String districtName = req.getParameter("districtName");
        String wardName = req.getParameter("wardName");

        List<ViettelPostApi.Province> provincesName = new ArrayList<>();
        List<ViettelPostApi.District> districtsName = new ArrayList<>();
        List<ViettelPostApi.Ward> wardsName = new ArrayList<>();
        if (provinceID != null || provinceName != null) {
            if (provinceName != null) {

                for (ViettelPostApi.Province province : provinces) {
                    if (province.getPROVINCE_NAME().contains(provinceName)) {
                        provinceID = province.getPROVINCE_ID();
                        break;
                    }
                }
            }
            for (ViettelPostApi.District district : districts) {
                if (district.getPROVINCE_ID().equals(provinceID)) {
                    districtsName.add(district);
                }
            }

        } else if (districtID != null || districtName != null) {
            if (districtName != null) {
                for (ViettelPostApi.District district : districts) {
                    if (district.getDISTRICT_NAME().contains(districtName)) {
                        districtID = district.getDISTRICT_ID();
                        break;
                    }
                }
            }
            for (ViettelPostApi.Ward ward : wards) {
                if (ward.getDISTRICT_ID().equals(districtID)) {
                    wardsName.add(ward);
                }
            }

        } else if (provinceID == null && districtID == null && wardID == null) {
            for (ViettelPostApi.Province province : provinces) {
                provincesName.add(province);
            }
        }

        Gson gson = new Gson();
        String jsonProvinces = gson.toJson(provincesName);
        String jsonDistricts = gson.toJson(districtsName);
        String jsonWards = gson.toJson(wardsName);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().print("{\"provinces\":" + jsonProvinces + ",\"districts\":" + jsonDistricts + ",\"wards\":" + jsonWards + "}");
        resp.getWriter().flush();
        resp.getWriter().close();
        resp.setStatus(200);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type =req.getParameter("type");
        if(type.equals("add")){
            addAddress(req,resp);
        }else{
            getAddress(req,resp);
        }
    }
    protected void getAddress(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AddressDAO addressDAO = new AddressDAO();
        User user = (User) req.getSession().getAttribute("currentUser");
        List<Address> addresses = addressDAO.selectByUser(user.getId());
        JsonArray jsonArray = new JsonArray();
        int index =0;
        for(Address address: addresses){
            String checked = (index++ ==0)?"checked":"";

            JsonObject jsonObject = new JsonObject();
            String radio = "<div class=\"row_address col-12\">"+
                    "<input type=\"radio\""+checked+" onchange=\"setShipInfo()\" name=\"address\" id=\""+address.getId()+"\" value=\""+address.getProvince()+"-"+
                    address.getDistrict()+"-"+address.getWard()+"\">"+
                    "<label for=\""+address.getId()+"\" class=\"form-label\">"+address.getHouseNumber()+"-"+address.getWard()+"-"+address.getDistrict()+"-"+address.getProvince()+"</label>" +
                    "</div>";
            jsonObject.addProperty("address", radio);
            jsonArray.add(jsonObject);
        }
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.add("data",jsonArray);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonResponse.toString());
    }
    protected void addAddress(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        AddressDAO addressDAO = new AddressDAO();
        String province = req.getParameter("province");
        String district = req.getParameter("district");
        String ward = req.getParameter("ward");
        String houseNumber = req.getParameter("house_number");
        String provinceID = "";
        for (ViettelPostApi.Province prov : provinces) {
            if (prov.getPROVINCE_NAME().contains(province)) {
                provinceID = prov.getPROVINCE_ID();
                System.out.println(provinceID);
                break;
            }
        }
        List<ViettelPostApi.District> districts1 = vietelPostApi.getDistricts(provinceID);
        boolean checkDistrict = false;
        boolean checkWard = false;
        String districtID = "";
        for (ViettelPostApi.District distr : districts1) {
            if (distr.getDISTRICT_NAME().contains(district)) {
                districtID = distr.getDISTRICT_ID();
                checkDistrict = true;
                break;
            }
        }

        for (ViettelPostApi.Ward w : vietelPostApi.getWards(districtID)) {
            if (w.getWARDS_NAME().contains(ward)) {

                checkWard = true;
                break;
            }
        }
        Address address = addressDAO.selectByAllInfo(user.getId(), province, district, ward, houseNumber);
        JsonObject jsonResponse = new JsonObject();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        if (checkDistrict && checkWard) {
            if (address != null) {
                resp.setStatus(400);
                jsonResponse.addProperty("error", "Địa chỉ đã được thêm trước đó, không thể thêm lần nữa!");
                resp.getWriter().write(jsonResponse.toString());
            } else {
                address = new Address(0L, user.getId(), province, district, ward, houseNumber);
                int rs = addressDAO.insertAddress(address);
                if (rs > 0) {
                    resp.setStatus(200);
                    jsonResponse.addProperty("msg", "Thêm địa chỉ thành công");
                    resp.getWriter().write(jsonResponse.toString());

                } else {
                    resp.setStatus(400);
                    jsonResponse.addProperty("error", "Thêm địa chỉ thất bại, vui lòng thử lại sau!");
                    resp.getWriter().write(jsonResponse.toString());

                }
            }
        } else {
            resp.setStatus(400);
            jsonResponse.addProperty("error", "Địa chỉ không hợp lệ!");
            resp.getWriter().write(jsonResponse.toString());
        }

    }

    public static List<ViettelPostApi.Province> getProvinces() {
        return provinces;
    }

    public static List<ViettelPostApi.District> getDistricts() {
        return districts;
    }

    public static List<ViettelPostApi.Ward> getWards() {
        return wards;
    }
}
