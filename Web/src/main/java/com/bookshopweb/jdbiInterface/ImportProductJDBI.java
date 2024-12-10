package com.bookshopweb.jdbiInterface;

import com.bookshopweb.beans.ImportProduct;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(ImportProduct.class)
public interface ImportProductJDBI {
    @SqlQuery("select * from product_import")
    List<ImportProduct> getAll();

    @SqlQuery("select * from product_import where id=:id")
    ImportProduct getById(@Bind("id") long id);

    @SqlUpdate("insert into product_import (id, productId, userId, importAt, quanlity, price, createAt) " +
            "values (:id, :productId, :userId, :importAt, :quantity, :price, :createAt)")
    int addProductImport(@BindBean ImportProduct product);
    @SqlQuery("SELECT COALESCE(MAX(id), 0) FROM product_import")
    int getMaxId();
}
