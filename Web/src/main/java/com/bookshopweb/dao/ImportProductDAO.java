package com.bookshopweb.dao;

import com.bookshopweb.beans.ImportProduct;
import com.bookshopweb.beans.Product;
import com.bookshopweb.jdbiInterface.ImportProductJDBI;
import com.bookshopweb.utils.JDBCUtils;
import com.bookshopweb.utils.JDBIUltis;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImportProductDAO extends  AbsDAO<ImportProduct>{
    private ImportProductJDBI importProductJDBI = JDBIUltis.getJDBI().onDemand(ImportProductJDBI.class);
    private Connection conn = JDBCUtils.getConnection();
    @Override
    public ImportProduct selectPrevalue(Long id) {
        return importProductJDBI.getById(id);
    }

    @Override
    public int insert(ImportProduct product, String ip) {
         super.insert(product, ip);
         return importProductJDBI.addProductImport(product);
    }
    public int getMaxId(){
        return importProductJDBI.getMaxId()+1;
    }

    public int countTotalImports() {
        int totalCount = 0;
        try (
                PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS total FROM product_import");
                ResultSet rs = stmt.executeQuery()
        ) {
            if (rs.next()) {
                totalCount = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalCount;
    }
    public int countByTime(String start, String end) {
        int totalCount = 0;
        try (
                PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS total FROM product_import WHERE createAt BETWEEN ? AND ?");
        ) {
            stmt.setString(1, start);
            stmt.setString(2, end);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalCount = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalCount;
    }
    public List<ImportProduct> getPart(int limit, int offset) {
        List<ImportProduct> importProducts = new ArrayList<>();

        try (

                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM product_import LIMIT ? OFFSET ?");
        ) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Map each row to an ImportProduct object
                    ImportProduct importProduct = new ImportProduct();
                    importProduct.setId(rs.getLong("id"));
                    importProduct.setProductId(rs.getLong("productId"));
                    importProduct.setUserId(rs.getLong("userId"));
                    importProduct.setImportAt(rs.getTimestamp("importAt"));
                    importProduct.setQuantity(rs.getInt("quanlity"));
                    importProduct.setPrice(rs.getFloat("price"));
                    importProduct.setCreateAt(rs.getTimestamp("createAt"));

                    // Add ImportProduct object to the list
                    importProducts.add(importProduct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions or connection issues
        }

        return importProducts;
    }
    public List<ImportProduct> getPartByOrdered(int limit, int offset, String start, String end, String orderBy, String orderDir) {
        List<ImportProduct> importProducts = new ArrayList<>();

        try (
                // Establish connection to the database

                // Prepare SQL statement with parameters for limit, offset, start, end, orderBy, and orderDir
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * FROM product_import " +
                                "WHERE createAt BETWEEN ? AND ? " +
                                "ORDER BY " + orderBy + " " + orderDir + " " +
                                "LIMIT ? OFFSET ?"
                );
        ) {

            stmt.setString(1, start);
            stmt.setString(2, end);
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);

            // Execute query and obtain the result set
            try (ResultSet rs = stmt.executeQuery()) {
                // Process each row in the result set
                while (rs.next()) {
                    // Map each row to an ImportProduct object
                    ImportProduct importProduct = new ImportProduct();
                    importProduct.setId(rs.getLong("id"));
                    importProduct.setProductId(rs.getLong("productId"));
                    importProduct.setUserId(rs.getLong("userId"));
                    importProduct.setImportAt(rs.getTimestamp("importAt"));
                    importProduct.setQuantity(rs.getInt("quanlity"));
                    importProduct.setPrice(rs.getDouble("price"));
                    importProduct.setCreateAt(rs.getTimestamp("createAt"));

                    // Add ImportProduct object to the list
                    importProducts.add(importProduct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions or connection issues
        }

        return importProducts;
    }
    public String getFirst(String twopartString) {
        return twopartString.contains("-") ? twopartString.split("-")[0] : "";
    }

    public String getLast(String twopartString) {
        return twopartString.contains("-") ? twopartString.split("-")[1] : "";
    }

    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();
        ImportProductDAO importProductDAO = new ImportProductDAO();
//        List<Product> productList = productDAO.getAll();
//        Random rs = new Random();
//        for(Product product:productList){
//
//            ImportProduct importProduct = new ImportProduct(product.getId(), product.getId(), 1, product.getCreatedAt(), rs.nextInt(10)*50, 0.8*product.getPrice());
//            System.out.println(product);
////            importProductDAO.insert(importProduct, "");
//        }
        ImportProduct product = new ImportProduct();
        product.setId(importProductDAO.getMaxId());
        product.setProductId(1);
        product.setUserId(1);
        product.setImportAt(Timestamp.valueOf("2021-03-23 08:22:50"));
        product.setQuantity(50);
        product.setPrice(300.99);
        product.setCreateAt(Timestamp.valueOf(LocalDateTime.now()));
        importProductDAO.insert(product,"");
        System.out.println("Generated ID: " + product.getId());
    }
}
