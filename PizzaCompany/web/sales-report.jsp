<%-- 
    Document   : sales-report
    Created on : Oct 11, 2025, 3:58:47 PM
    Author     : QUYDAM
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Báo cáo doanh số - Admin</title>
        <style>
            .container { max-width: 1200px; margin: 20px auto; padding: 30px; background: white; }
            h1 { color: #333; border-bottom: 2px solid #007bff; padding-bottom: 10px; }
            .form-section { background: #f8f9fa; padding: 15px; margin: 20px 0; }
            .form-group { display: inline-block; margin-right: 15px; }
            input[type="date"] { padding: 8px; }
            .btn-submit { background: #007bff; color: white; padding: 10px 20px; border: none; cursor: pointer; }
            .alert { padding: 10px; margin: 10px 0; }
            .alert-error { background: #f8d7da; color: #721c24; }
            .alert-info { background: #d1ecf1; color: #0c5460; }
            table { width: 100%; border-collapse: collapse; margin-top: 20px; }
            th, td { border: 1px solid #ddd; padding: 10px; }
            th { background: #007bff; color: white; text-align: center; }
            .text-right { text-align: right; }
            .text-center { text-align: center; }
            .total-row { background: #e7f3ff !important; font-weight: bold; }
        </style>
    </head>
    <body>
        <jsp:include page="fragments/navbar.jsp" />
        
        <div class="container">
            <h1>Báo cáo doanh số bán hàng</h1>
            
            <!-- Form nhập liệu -->
            <div class="form-section">
                <form action="SalesReportController" method="get">
                    <div class="form-group">
                        <label for="startDate">Từ ngày:</label>
                        <input type="date" id="startDate" name="startDate" 
                               value="${requestScope.startDate}" required>
                    </div>
                    <div class="form-group">
                        <label for="endDate">Đến ngày:</label>
                        <input type="date" id="endDate" name="endDate" 
                               value="${requestScope.endDate}" required>
                    </div>
                    <button type="submit" class="btn-submit">Tạo báo cáo</button>
                </form>
            </div>
            
            <!-- Hiển thị lỗi -->
            <c:if test="${not empty requestScope.error}">
                <div class="alert alert-error">
                    ❌ ${requestScope.error}
                </div>
            </c:if>
            
            <!-- Hiển thị thông báo -->
            <c:if test="${not empty requestScope.message}">
                <div class="alert alert-info">
                    ℹ️ ${requestScope.message}
                </div>
            </c:if>
            
            <!-- Bảng kết quả -->
            <c:if test="${not empty requestScope.salesReports}">
                <h2>Kết quả từ <strong>${requestScope.startDate}</strong> đến <strong>${requestScope.endDate}</strong></h2>
                
                <table>
                    <thead>
                        <tr>
                            <th style="width: 80px;">STT</th>
                            <th>Tên sản phẩm</th>
                            <th style="width: 150px;">Số lượng bán</th>
                            <th style="width: 200px;">Tổng doanh số ($)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="report" items="${requestScope.salesReports}" varStatus="status">
                            <tr>
                                <td class="text-center">${status.index + 1}</td>
                                <td>${report.productName}</td>
                                <td class="text-center">${report.totalQuantity}</td>
                                <td class="text-right">
                                    <fmt:formatNumber value="${report.totalSales}" pattern="#,##0.00" />
                                </td>
                            </tr>
                        </c:forEach>
                        
                        <!-- Tổng doanh thu -->
                        <tr class="total-row">
                            <td colspan="3" class="text-right">
                                <strong>TỔNG DOANH THU:</strong>
                            </td>
                            <td class="text-right">
                                <strong>
                                    <fmt:formatNumber value="${requestScope.totalRevenue}" pattern="#,##0.00" /> $
                                </strong>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </c:if>
        </div>
    </body>
</html>
