package com.bookshopweb.jdbiInterface;

import com.bookshopweb.beans.CategorysOfVoucher;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(CategorysOfVoucher.class)
public interface CategorysOfVoucherJDBI {
    @SqlQuery("select * from categorys_of_voucher where voucherId = :voucherId")
    List<CategorysOfVoucher> getByVoucherId(@Bind("voucherId") long voucherId);

    @SqlUpdate("insert into categorys_of_voucher(id, voucherId, categoryId) " +
            "values (:id, :voucherId, :categoryId)")
    int addCategoryOfVoucher(@BindBean CategorysOfVoucher categorysOfVoucher);

}
