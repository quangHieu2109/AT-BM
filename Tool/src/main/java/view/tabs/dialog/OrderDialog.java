package view.tabs.dialog;

import api.API;
import api.ConnectException;

import model.Order;
import model.OrderItem;
import view.MainApp;
import view.tabs.OrderTab;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;
public class OrderDialog extends JDialog {
    Order order;
    List<OrderItem> orderItems;
    public OrderDialog(MainApp mainApp,Order order) throws ConnectException {
        super(mainApp, "Thông tin đơn hàng", true);
        this.order = order;
        this.orderItems = order.getOrderItems();
        this.setSize(new Dimension(700, 450));
        this.setLocationRelativeTo(mainApp);
        this.init();
        this.setVisible(true);
    }


    public void init() throws ConnectException {
        this.setLayout(new GridBagLayout());
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(650, 400));

        JPanel containInfo = new JPanel();
        containInfo.setLayout(new BoxLayout(containInfo, BoxLayout.X_AXIS));
        containInfo.add(new JLabel("Mã đơn hàng: "+order.getId()));
        containInfo.add(Box.createHorizontalGlue());
        containInfo.add(new JLabel("Ngày mua: "+order.getCreatedAt()));
        container.add(containInfo);

        JPanel containPayment = new JPanel();
        containPayment.setLayout(new BoxLayout(containPayment, BoxLayout.X_AXIS));
        containPayment.add(Box.createHorizontalGlue());
        containPayment.add(new JLabel("Tổng cộng: "+order.getTotalPrice()));
        container.add(containPayment);

        String[] columnNames = {"Thông tin Sản phẩm", "Giá", "Số lượng"};

        Object[][] data = new Object[orderItems.size()][3];
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem item = orderItems.get(i);
            data[i][0] = createProductInfo(item.getProductName(), item.getProduct().getAuthor(), String.valueOf(item.getProduct().getPages()), String.valueOf(item.getProduct().getYearPublishing()));

            data[i][1] = item.getPrice() + "₫";

            data[i][2] = item.getQuantity();
        }

        JTable table = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof JPanel) {
                    return (JPanel) value;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        table.setRowHeight(100);

        JScrollPane tableScroll = new JScrollPane(table);
        container.add(tableScroll);

        this.add(container);
    }

    private JPanel createProductInfo(String name, String author, String pages, String yearPublisher) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Tên: " + name), gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Tác giả: " + author), gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Số trang: " + pages), gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Năm xuất bản: " + yearPublisher), gbc);

        return panel;
    }

//    public static void main(String[] args) {
//        FlatCyanLightIJTheme.setup();
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame();
//            OrderDialog dialog = new OrderDialog(this);
//            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//            dialog.setVisible(true);
//        });
//    }
}
