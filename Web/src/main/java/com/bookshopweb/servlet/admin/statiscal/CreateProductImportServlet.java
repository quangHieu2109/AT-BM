package com.bookshopweb.servlet.admin.statiscal;

import com.bookshopweb.beans.ImportProduct;
import com.bookshopweb.dao.ImportProductDAO;
import com.bookshopweb.utils.IPUtils;
import com.bookshopweb.utils.Protector;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(name = "CreateProductImportServlet", value = "/admin/statiscalManager/create")
public class CreateProductImportServlet extends HttpServlet {
    private final ImportProductDAO importProductDAO = new ImportProductDAO();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String successMessage = "Thêm thành công!";
        String errorMessage = "Thêm thất bại!";
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

                for (FileItem item : multiparts) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = Paths.get(request.getServletContext().getRealPath("/"),"img/")+ fileName;
                        File uploadFile = new File(filePath);
                        item.write(uploadFile);
                        // Đọc dữ liệu từ file Excel
                        try (InputStream fis = new FileInputStream(uploadFile)) {
                            Workbook workbook = new XSSFWorkbook(fis);
                            Sheet sheet = workbook.getSheetAt(0);

                            int startRow = 3; // Bỏ qua 3 dòng đầu tiên: dòng tiêu đề chính và 2 dòng tiêu đề phụ
                            for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
                                Row row = sheet.getRow(i);
                                if (row != null) {
                                    // Tìm cột đầu tiên có dữ liệu
                                    int firstDataColumn = -1;
                                    for (int j = 0; j < row.getLastCellNum(); j++) {
                                        Cell cell = row.getCell(j);
                                        if (cell != null && cell.getCellType() != CellType.BLANK) {
                                            firstDataColumn = j;
                                            break;
                                        }
                                    }
                                    if (firstDataColumn != -1) {
                                        long productId = (long) getNumericCellValue(row.getCell(firstDataColumn));
                                        long userId = (long) getNumericCellValue(row.getCell(firstDataColumn + 1));
                                        Timestamp importAt = getTimestampCellValue(row.getCell(firstDataColumn + 2));
                                        int quantity = (int) getNumericCellValue(row.getCell(firstDataColumn + 3));
                                        double price = getNumericCellValue(row.getCell(firstDataColumn + 4));
                                        Timestamp createAt = getTimestampCellValue(row.getCell(firstDataColumn + 5));

                                        Protector.of(() -> {
                                                    importProductDAO.insert(new ImportProduct(importProductDAO.getMaxId(), productId, userId,
                                                            importAt, quantity, price, createAt), IPUtils.getIP(request));
                                                })
                                                .done(r -> request.setAttribute("successMessage", successMessage))
                                                .fail(e -> request.setAttribute("errorMessage", errorMessage));
                                    }
                                }
                            }
                            workbook.close();
                        }
                    }
                }
                request.setAttribute("successMessage", successMessage);
            } catch (Exception ex) {
                request.setAttribute("errorMessage", "errorMessage: " + ex.getMessage());
            }
        } else {
            request.setAttribute("errorMessage", "errorMessage");
        }

        request.getRequestDispatcher("/WEB-INF/views/statiscalPIManagerView.jsp").forward(request, response);
    }

    private double getNumericCellValue(Cell cell) {
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue().replace(",", ""));
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private Timestamp getTimestampCellValue(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return new Timestamp(cell.getDateCellValue().getTime());
            }
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                return new Timestamp(dateFormat.parse(cell.getStringCellValue()).getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
