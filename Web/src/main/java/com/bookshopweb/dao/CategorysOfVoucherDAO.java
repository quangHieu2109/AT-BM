package com.bookshopweb.dao;

import com.bookshopweb.beans.CategorysOfVoucher;
import com.bookshopweb.jdbiInterface.CategorysOfVoucherJDBI;
import com.bookshopweb.utils.JDBIUltis;

import java.util.List;

public class CategorysOfVoucherDAO {
    private CategorysOfVoucherJDBI jdbi = JDBIUltis.getJDBI().onDemand(CategorysOfVoucherJDBI.class);
    public List<CategorysOfVoucher> getByVoucherId(long voucherId){
        return jdbi.getByVoucherId(voucherId);
    }
    public int addCategoryOfVoucher(CategorysOfVoucher categorysOfVoucher){
        return jdbi.addCategoryOfVoucher(categorysOfVoucher);
    }


}
