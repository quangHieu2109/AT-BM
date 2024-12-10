<%--
  Created by IntelliJ IDEA.
  User: dmx
  Date: 4/26/2024
  Time: 2:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<jsp:include page="_meta.jsp"></jsp:include>--%>
<html>
<head>
    <title>Quản lý người dùng</title>
    <link href="${pageContext.request.contextPath}/css/bootstrap.css" type="text/css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/img/favicon.ico" rel="shortcut icon" type="image/x-icon">

    <!-- Bootstrap v5.0.1 -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.css" type="text/css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.js" type="text/javascript"></script>

    <!-- Bootstrap Icons v1.5.0 -->
    <link href="${pageContext.request.contextPath}/css/bootstrap-icons.css" type="text/css" rel="stylesheet">

    <!-- Custom Styles -->
    <link href="${pageContext.request.contextPath}/css/styles.css" type="text/css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <script src="https://cdn.datatables.net/2.0.5/js/dataTables.js"></script>
    <link rel="stylesheet" href="https://cdn.datatables.net/2.0.5/css/dataTables.dataTables.css">
    <style>
        .add-user__btn{
            width: max-content;
            margin-right: 0;

        }
        .add-user__btn button{
            padding-left: 20px;
            padding-right: 20px;
        }
        .add-user__content{

            display: none;
            background-color: #fff;
            border: 1px solid #ddd;

        }
    </style>
</head>
<body>
<jsp:include page="_headerAdmin.jsp"/>
<div class="container">
    <div class="container add-user__btn"><button class="btn btn-success mr-0" id="addUser">Add user</button></div>
    <div class="container w-50 m-auto add-user__content" id="add-user__content">

            <div class="mb-3">
                <label for="username" class="form-lable">Tên đăng nhập:</label>
                <label  class="from-lable text-danger" id="username-error"></label>
                <input type="text" class="form-control" id="username" required>
            </div>
            <div class="mb-3">
                <label for="username" class="form-lable">Mật khẩu:</label>
                <label  class="from-lable text-danger" id="password-error"></label>
                <input type="password" class="form-control" id="password" required>
            </div>
            <div class="mb-3">
                <label for="fullname" class="form-lable">Họ tên:</label>
                <label  class="from-lable text-danger" id="fullname-error"></label>
                <input type="text" class="form-control" id="fullname" required>
            </div>
            <div class="mb-3">
                <label for="username" class="form-lable">Email:</label>
                <label  class="from-lable text-danger" id="email-error"></label>
                <input type="text" class="form-control" id="email" required>
            </div>
            <div class="mb-3">
                <label for="username" class="form-lable">Phân quyền:</label>
                <label class="from-lable"></label>
                <select name="" id="role">
                    <option value="CUSTOMER" selected>CUSTOMER</option>
                    <option value="EMPLOYEE">EMPLOYEE</option>
                    <option value="ADMIN">ADMIN</option>
                </select>
            </div>
            <div class="mb-3 justify-content-between" >
                <button id="add" class="btn btn-info ">Thêm</button>
                <button id="cancel" class="btn btn-info">Hủy</button>

            </div>


    </div>
    <div style="width: 80%; margin: auto" id="table-contaier">
        <table id="my_table" class="disabled table-bordered table-striped">
            <thead>
            <tr>
                <%--        <th>#</th>--%>
                <th>ID</th>
                <th>UserName</th>

                <th>FullName</th>
                <th>Email</th>
                <th>PhoneNumber</th>
                <th>Gender</th>
                <th>Role</th>
                <th>Operation</th>


            </tr>

            </thead>
            <tbody>

            </tbody>

        </table>

    </div>



</div>
<jsp:include page="_footerAdmin.jsp"/>


<script src="${pageContext.request.contextPath}/js/userManager.js" type="text/javascript" ></script>

</body>
</html>
