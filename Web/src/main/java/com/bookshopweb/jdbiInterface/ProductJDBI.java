package com.bookshopweb.jdbiInterface;

import com.bookshopweb.beans.Product;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@RegisterBeanMapper(Product.class)
public interface ProductJDBI {
@SqlQuery("Select * from product where id =:id")
Product getByProductId(@Bind("id") long id);
}
