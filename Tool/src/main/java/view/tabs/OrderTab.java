package view.tabs;

import api.API;
import controller.Observer;
import model.Order;
import view.BaseUI;
import view.MainApp;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderTab extends JPanel implements BaseUI, Observer {
    String[] columnNames = {"ID", "ID người dùng", "Ngày tạo", "Tổng giá", "Địa chỉ giao hàng"};
    Object[][] data = {};
    DefaultTableModel model;
    JTable table;
    JButton btnRefresh,btnSign;
    MainApp mainApp;
    List<Order> ordersSign;

    public OrderTab(MainApp mainApp) {
        this.mainApp = mainApp;
        init();
        setOnClick();
    }


    @Override
    public void update() {

    }

    @Override
    public void init() {
        ordersSign = new ArrayList<>();
        this.setLayout(new BorderLayout());
        model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setShowGrid(true);
        table.setGridColor(Color.BLACK);
        table.setIntercellSpacing(new Dimension(1,1));

        setSizeCol();
        refresh();
        JScrollPane scrollPane = new JScrollPane(table);

        btnRefresh = new JButton("Làm mới");
        btnSign = new JButton("Ký đơn hàng");
        JPanel panelSouth = new JPanel();
        panelSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelSouth.add(btnSign);
        panelSouth.add(btnRefresh);

        this.add(scrollPane,BorderLayout.CENTER);
        this.add(panelSouth, BorderLayout.SOUTH);
    }
    private Map<Long,String> sign() {
        System.out.println("Bắt đầu ký");
        for (Order order : ordersSign) {
            // ký ở đây
        }
        return null;
    }
    @Override
    public void setOnClick() {
        btnSign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map signatures = sign();
                if (signatures!=null) {
                    try {
                        API.sendSignature(signatures);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainApp,"Đã xảy ra lỗi!","Lỗi",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Kiểm tra xem giá trị có đang điều chỉnh không
                    int[] selectedRows = table.getSelectedRows();
                    ordersSign.removeAll(ordersSign);
                    for (int row : selectedRows) {
                        long id = (long) model.getValueAt(row, 0);
//                        System.out.println("Chọn id: " + id);
                        Order order = getOrderByID(id);
                        if (order != null) {
                            ordersSign.add(order);
                        }else {
                            JOptionPane.showMessageDialog(mainApp,"Không tìm thấy id bạn chọn hãy refresh lại trang!","Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }
    public void refresh(){
        List<Order> orders = API.getOrders();
        if (orders==null) return;
        model.setRowCount(0); // Xóa tất cả hàng
        for (Order order : orders) {
            model.addRow(new Object[]{order.getId(), order.getUserId(), order.getCreateAt(), order.getTotalPrice(), order.getDelivery_address().getText()}); // Thêm hàng mới
        }
    }
    public Order getOrderByID(long id){
        for (Order order : API.orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }
    private void setSizeCol(){
        //"ID", "ID người dùng", "Ngày tạo", "Tổng giá", "Địa chỉ giao hàng"
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(300);
    }
}
