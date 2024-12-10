<%--
  Created by IntelliJ IDEA.
  User: dmx
  Date: 5/1/2024
  Time: 1:44 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
  <fmt:setLocale value="vi_VN" />
  <c:if test="${sessionScope.lang!=null&&sessionScope.lang=='en'}">
    <fmt:setLocale value="en_US" />
  </c:if>
  <fmt:setBundle basename="lang" />

  <title><fmt:message key="quan_ly_log"/></title>
  <jsp:include page="_meta.jsp"/>

  <link href="${pageContext.request.contextPath}/css/bootstrap.css" type="text/css" rel="stylesheet">

  <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
  <script src="https://cdn.datatables.net/2.0.5/js/dataTables.js"></script>
  <link href="${pageContext.request.contextPath}/css/bootstrap-icons.css" type="text/css" rel="stylesheet">

  <link rel="stylesheet" href="https://cdn.datatables.net/2.0.5/css/dataTables.dataTables.css">

  <style>
     .limitLine-3{
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
         word-wrap: break-word;
         overflow: hidden;
         text-overflow: ellipsis;
         width: 200px;
    }
     tr:hover td p {
         display: block;
     }
    /*td{*/
    /*  max-width: 100px;*/
    /*  max-height: 100px;*/
    /*  word-wrap: break-word;*/
    /*}*/
    .level1{
      background-color: #fff !important;
      max-height: 50px !important;
      overflow-y: auto;
    }
    .level2{
      background-color: #a4a1a1 !important;
    }
    .level3{
      background-color: rgba(229, 158, 94, 0.76) !important;
    }
    .level4{
      background-color: rgba(243, 41, 41, 0.67) !important;
      color: #000000;
    }
    .w-10{
      width: 10%;

    }
    .w-20{
      width: 20%;
    }
    input[type="radio"]:checked + .page-link {
      background-color: #0d6efd;
      color: #fff;
    }
     input[type="radio"]:checked + label {
       background-color: #52e74c; /* Màu sắc bạn muốn áp dụng khi label được chọn */
     }
     a{
       text-decoration: none;
     }
  </style>
</head>
<body>
<jsp:include page="_headerAdmin.jsp"></jsp:include>
<%--Navbar chứa các button tương ứng với từng level log--%>
<div class="row justify-content-around">
  <div class="w-auto">
    <input type="radio" name="category" value="level1" id="level1" checked hidden>
    <label for="level1" class="btn p-3 pt-1 pb-1 mt-2" onclick="loadData(1)">Level 1</label>
  </div>
  <div class="w-auto">
    <input type="radio" name="category" value="level2" id="level2" hidden>
    <label for="level2" class="btn p-3 pt-1 pb-1 mt-2" onclick="loadData(2)">Level 2</label>
  </div>
  <div class="w-auto">
    <input type="radio" name="category" value="level3" id="level3" hidden>
    <label for="level3" class="btn p-3 pt-1 pb-1 mt-2" onclick="loadData(3)">Level  3</label>
  </div>
  <div class="w-auto">
    <input type="radio" name="category" value="level4" id="level4" hidden>
    <label for="level4" class="btn p-3 pt-1 pb-1 mt-2" onclick="loadData(4)">Level 4</label>
  </div>
</div>

<%--Table hiển thị các log--%>
<div class="container mt-4">
  <main class="table-responsive-xl mb-5">
  <table id="my_table" class="table table-bordered align-middle">
    <thead>
    <tr>
      <th scope="col" class="w-10">ID</th>
      <th scope="col" class="w-10">IP</th>
      <th scope="col" class="w-10">Level log</th>
      <th scope="col" class="w-10">Resource</th>
      <th scope="col" class="w-20">Previous value</th>
      <th scope="col" class="w-20">Current value</th>
      <th scope="col" class="w-10">Created At</th>
      <th scope="col" class="w-10">Updated At</th>

    </tr>
    </thead>
    <tbody id="table_body">

    </tbody>
  </table>
  </main>
  <div id="paging" class="d-flex justify-content-center">

  </div>
</div>
<script src="${pageContext.request.contextPath}/js/logManager.js"></script>
</body>
</html>
