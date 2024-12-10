<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<footer class="section-footer">
  <fmt:setLocale value="vi_VN" />
  <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
    <fmt:setLocale value="en_US" />
  </c:if>
  <fmt:setBundle basename="lang" />

  <section class="footer-top py-5 bg-light">
    <div class="container">
      <div class="row">
        <aside class="col-sm-6 col-lg-3">
          <h6 class="pb-2"><fmt:message key="gioi_thieu"/></h6>
          <ul class="list-unstyled">
            <li><a href="#"><fmt:message key="ve_shop"/></a></li>
            <li><a href="#"><fmt:message key="tuyen_dung"/></a></li>
            <li><a href="#"><fmt:message key="chinh_sach_thanh_toan"/></a></li>
            <li><a href="#"><fmt:message key="chinh_sach_bao_mat"/></a></li>
            <li><a href="#"><fmt:message key="giai_quyet_khieu_nai"/></a></li>
            <li><a href="#"><fmt:message key="hop_tac"/></a></li>
          </ul>
        </aside>
        <aside class="col-sm-6 col-lg-3">
          <h6 class="pb-2"><fmt:message key="ho_tro_khach_hang"/></h6>
          <ul class="list-unstyled">
            <li>Hotline: 1900-80xx</li>
            <li><a href="#"><fmt:message key="cau_hoi_thuong_gap"/></a></li>
            <li><a href="#"><fmt:message key="huong_dan_dat_hang"/></a></li>
            <li><a href="#"><fmt:message key="phuong_thuc_van_chuyen"/></a></li>
            <li><a href="#"><fmt:message key="chinh_sach_doi_tra"/></a></li>
          </ul>
        </aside>
        <aside class="col-lg-5">
          <h6 class="pb-2"><fmt:message key="dang_ki_nhan_tin"/></h6>
          <form action="#">
            <div class="input-group w-100">
              <input type="text" class="form-control" placeholder="<fmt:message key="email_cua_ban"/> ...">
              <button class="btn btn-primary" type="button">
                <a href="#"><fmt:message key="dang_ki"/></a>
              </button>
            </div>
          </form>
        </aside>
      </div> <!-- row.// -->
    </div> <!-- container.// -->
  </section> <!-- footer-top.// -->

  <section class="footer-bottom text-center bg-light border-top py-3">
    <div class="container-fluid">© 2022 — <fmt:message key="shop_ban_sach"/></div> <!-- container-fluid.// -->
  </section> <!-- footer-bottom.// -->
</footer> <!-- section-footer.// -->
