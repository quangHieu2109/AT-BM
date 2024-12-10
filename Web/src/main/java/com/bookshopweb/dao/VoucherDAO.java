package com.bookshopweb.dao;

import com.bookshopweb.beans.Voucher;
import com.bookshopweb.jdbiInterface.VoucherJDBI;
import com.bookshopweb.utils.JDBIUltis;

import java.util.List;

public class VoucherDAO extends AbsDAO<Voucher> {
    VoucherJDBI voucherJDBI = JDBIUltis.getJDBI().onDemand(VoucherJDBI.class);
    @Override
    public Voucher selectPrevalue(Long id) {
        return voucherJDBI.getById(id);
    }
    public Voucher getByVoucherCode(String code) {
        return voucherJDBI.getByVoucherCode(code);
    }
    public List<Voucher> getAll(){
        return voucherJDBI.getAll();
    }
    public List<Voucher> getByType(int type){
        return voucherJDBI.getByType(type);
    }
    //Class kế thừa
    @Override
    public int insert(Voucher voucher, String ip) {
        int rs = voucherJDBI.addVoucher(voucher);
        super.insert(voucher, ip);
        return rs;
    }

    @Override
    public int delete(Voucher voucher, String ip) {
        int rs = voucherJDBI.removeVoucher(voucher.getId());
        super.delete(voucher, ip);
        return rs;
    }
    public int decreaseQuantity(long voucherId){
        Voucher voucher = selectPrevalue(voucherId);
        voucher.setQuantity(voucher.getQuantity()-1);
        return voucherJDBI.decreaseQuantity(voucher);
    }
}
