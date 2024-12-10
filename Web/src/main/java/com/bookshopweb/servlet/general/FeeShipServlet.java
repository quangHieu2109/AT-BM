package com.bookshopweb.servlet.general;

import com.bookshopweb.api.GHNApi;
import com.bookshopweb.api.ViettelPostApi;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/feeship"})
public class FeeShipServlet extends HttpServlet {
    int shopProvinceID = 0;
    int shopDistrictID = 0;
    String shopWardID = "";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String province = req.getParameter("province");
        String district = req.getParameter("district");
        String ward = req.getParameter("ward");
        String provinceID = "";
        String districtID = "";
        String wardID = "";
        int quantity = Integer.valueOf(req.getParameter("quantity"));// số lượng sản phẩm
        int weight = Integer.valueOf(req.getParameter("weight")) * quantity;// trọng lượng sản phẩm (kg)
        int length = Integer.valueOf(req.getParameter("length"));// chiều dài sản phẩm (cm)
        int width = Integer.valueOf(req.getParameter("width"));// chiều rộng sản phẩm (cm)
        int height = Integer.valueOf(req.getParameter("height")) * quantity;// chiều cao sản phẩm (cm)
        int unitShipping = Integer.valueOf(req.getParameter("unitShip"));// đơn vị vận chuyển
        Gson gson = new Gson();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        if (unitShipping == 0) {
            GHNApi ghnApi = new GHNApi();
            shopDistrictID = 3695;
            shopWardID = "90737";

            List<GHNApi.Province> provincesGHN = ghnApi.getProvinces();
            List<GHNApi.District> districtsGHN = new ArrayList<>();
            List<GHNApi.Ward> wardsGHN = new ArrayList<>();
            for (GHNApi.Province oProvince : provincesGHN){
                if(oProvince.getProvinceName().toLowerCase().contains(province.toLowerCase())){
                    provinceID = oProvince.getProvinceID()+"";
                    districtsGHN = ghnApi.getDistricts(Integer.valueOf(provinceID));

                    for(GHNApi.District oDistrict : districtsGHN){
                        if(oDistrict.getDistrictName().toLowerCase().contains(district.toLowerCase())){
                            districtID = oDistrict.getDistrictID()+"";
                            wardsGHN = ghnApi.getWards(districtID);
//                            System.out.println(wardsGHN);
                            for(GHNApi.Ward oWard : wardsGHN){
                                if(oWard.getWardName().toLowerCase().contains(ward.toLowerCase())){
                                    wardID = oWard.getWardCode();
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }
//            System.out.println(wardID);
//            System.out.println(districtID);
            //Giao hàng nhanh
            List<GHNApi.InfoShip> infoShips = ghnApi.getInfoShip(shopDistrictID, shopWardID,
                    Integer.valueOf(districtID), wardID, weight, height, length, width);
            String json = "{\"infoShips\":" + gson.toJson(infoShips) + "}";
            resp.getWriter().write(json);
            resp.getWriter().close();
            resp.setStatus(200);
        } else {
            shopProvinceID = 2;
            shopDistrictID = 1231;
            ViettelPostApi vietelPostApi = new ViettelPostApi();
            List<ViettelPostApi.District> districts = AddressServlet.getDistricts();
            List<ViettelPostApi.Province> provinces = AddressServlet.getProvinces();
            for (ViettelPostApi.Province oProvince : provinces) {
                if (oProvince.getPROVINCE_NAME().toLowerCase().contains(province.toLowerCase())) {
                    provinceID = oProvince.getPROVINCE_ID() + "";
                    break;
                }
            }
            for (ViettelPostApi.District oDistrict : districts) {
                if (oDistrict.getDISTRICT_NAME().toLowerCase().contains(district.toLowerCase())) {
                    districtID = oDistrict.getDISTRICT_ID() + "";
                    break;
                }
            }
            //ViettelPost
            List<ViettelPostApi.InfoShip> infoShips = vietelPostApi.getInfoShips(weight,
                    0, 0,
                    shopProvinceID + "", shopDistrictID + "",
                    provinceID, districtID, null);
            String jsonInfoShips = "{\"infoShips\":" + gson.toJson(infoShips) + "}";
            resp.getWriter().write(jsonInfoShips);
            resp.getWriter().flush();
            resp.getWriter().close();
            resp.setStatus(200);
        }

    }
}
