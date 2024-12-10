package com.bookshopweb.jdbiInterface;

import com.bookshopweb.beans.OrderDetail;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(OrderDetail.class)
public interface OrderDetailJDBI {
    @SqlQuery("select * from order_detail")
    List<OrderDetail> getAll();
    @SqlQuery("select * from order_detail where orderId=:orderId")
    OrderDetail getByOrderId(@Bind("orderId") long orderId);

    @SqlUpdate("insert into order_detail(orderId, addressId, shipVoucherId, shipVoucherDecrease, productVoucherId, productVoucherDecrease, totalPrice) " +
            "values(:orderId, :addressId, :shipVoucherId, :shipVoucherDecrease, :productVoucherId, :productVoucherDecrease, :totalPrice)")
    int addVoucherDetail(@BindBean OrderDetail orderDetail);

    @SqlUpdate("insert into order_detail(orderId, addressId, shipVoucherDecrease, productVoucherDecrease, totalPrice) " +
            "values(:orderId, :addressId, :shipVoucherDecrease, :productVoucherDecrease, :totalPrice)")
    int addVoucherDetailNoVoucher(@BindBean OrderDetail orderDetail);

    @SqlUpdate("update order_detail set shipVoucherId=:shipVoucherId where orderId=:orderId")
    int updateShipVoucherId(@Bind("shipVoucherId") long shipVoucherId, @Bind("orderId") long orderId);
    @SqlUpdate("update order_detail set productVoucherId=:productVoucherId where orderId=:orderId")
    int updateProductVoucherId(@Bind("productVoucherId") long productVoucherId, @Bind("orderId") long orderId);

}
