<%-- 
    Document   : register
    Created on : Oct 11, 2025, 4:50:08 PM
    Author     : QUYDAM
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register - Pizza Store</title>
    </head>
    <body>
        <h1>Đăng ký tài khoản</h1>
        
        <!-- Hiển thị error message -->
        <c:if test="${not empty requestScope.error}">
            <p style="color:red;">${requestScope.error}</p>
        </c:if>
        
        <!-- Hiển thị success message từ URL parameter -->
        <c:if test="${not empty param.message}">
            <p style="color:green;">${param.message}</p>
        </c:if>
        
        <form action="register" method="post">
            <label for="username">Tên đăng nhập:</label><br>
            <input type="text" id="username" name="username" value="${requestScope.username}" required><br><br>
            
            <label for="password">Mật khẩu:</label><br>
            <input type="password" id="password" name="password" required><br><br>
            
            <label for="confirmPassword">Xác nhận mật khẩu:</label><br>
            <input type="password" id="confirmPassword" name="confirmPassword" required><br><br>
            
            <label for="fullName">Họ và tên:</label><br>
            <input type="text" id="fullName" name="fullName" value="${requestScope.fullName}" required><br><br>
            
            <input type="submit" value="Đăng ký">
        </form>
        
        <p>Đã có tài khoản? <a href="login.jsp">Đăng nhập ngay</a></p>
    </body>
</html>
