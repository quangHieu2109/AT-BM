package view.tabs;

import api.API;
import api.ConnectException;
import com.google.gson.Gson;
import controller.Observer;
import model.Order;
import okhttp3.Response;
import utils.HashUtils;
import utils.SignatureUtils;
import view.BaseUI;
import view.MainApp;
import view.tabs.dialog.OrderDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderTab extends JPanel implements BaseUI, Observer {
    final String[] columnNames = {"ID", "ID người dùng", "Ngày tạo", "Tổng giá", "Địa chỉ giao hàng", "Hành động"};
    Gson gson;
    SignatureUtils signatureUtils;
    Object[][] data = {};
    OrderDialog orderDialog;
    DefaultTableModel model;
    JTable table;
    JTextField txtPrivateKey;
    JButton btnRefresh, btnSign, btnSelectPrivateKey;
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
        gson = new Gson();
        ordersSign = new ArrayList<>();
        signatureUtils = new SignatureUtils();
        this.setLayout(new BorderLayout());
        model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {

                return column == 5;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.getColumnModel().getColumn(5).setCellRenderer(new JButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new JButtonEditor());
        table.setShowGrid(true);

        table.setRowHeight(30);
        table.setGridColor(Color.BLACK);
        table.setIntercellSpacing(new Dimension(1, 1));

        try {
            refresh();
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(mainApp, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        setSizeCol();
        JScrollPane scrollPane = new JScrollPane(table);

        txtPrivateKey = new JPasswordField(20);
        txtPrivateKey.setMaximumSize(new Dimension(350, 30));

        btnSelectPrivateKey = new JButton("Chọn khóa riêng tư");
        btnRefresh = new JButton("Làm mới");
        btnSign = new JButton("Ký đơn hàng");
        JPanel panelSouth = new JPanel();
        panelSouth.setPreferredSize(new Dimension(0, 50));
        panelSouth.setLayout(new BoxLayout(panelSouth, BoxLayout.X_AXIS));
        panelSouth.add(btnSelectPrivateKey);
        panelSouth.add(new JLabel("Khóa riêng tư: "));
        panelSouth.add(txtPrivateKey);
        panelSouth.add(Box.createHorizontalGlue());
        panelSouth.add(btnSign);
        panelSouth.add(btnRefresh);

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(panelSouth, BorderLayout.SOUTH);

    }

    private Map<Long, String> sign() {
        Map<Long, String> signatures = new HashMap<>();
        if (!signatureUtils.loadPrivateKey(txtPrivateKey.getText())) {
            JOptionPane.showMessageDialog(mainApp, "Không thể ký khóa riêng tư không hợp lệ!", "Ký đơn hàng", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        for (Order order : ordersSign) {
            try {
                String sign = signatureUtils.sign(HashUtils.hash(gson.toJson(order)));
//                System.out.println(gson.toJson(order));
//                System.out.println("hash: "+HashingUtils.hash(gson.toJson(order)));
                signatures.put(order.getId(), sign);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainApp, "Đơn hàng kí không thành công với id: " + order.getId(), "Ký đơn hàng", JOptionPane.ERROR_MESSAGE);
            }
        }
        return signatures;
    }

    @Override
    public void setOnClick() {
        btnSelectPrivateKey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choosePrivateKey();
            }
        });
        btnSign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map signatures = sign();
                if (signatures != null && signatures.size() > 0) {
                    try {
                        Response response = API.sendSignature(signatures);
                        if (response.isSuccessful()) {
                            JOptionPane.showMessageDialog(mainApp, "Đã ký đơn hàng thành công!", "Ký đơn hàng", JOptionPane.INFORMATION_MESSAGE);
                            refresh();
                        } else {
                            JOptionPane.showMessageDialog(mainApp, response.body().string(), "Ký đơn hàng", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainApp, "Đã xảy ra lỗi!", "Ký đơn hàng", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    refresh();
                } catch (ConnectException ex) {
                    JOptionPane.showMessageDialog(mainApp, ex.getMessage(), "Làm mới các đơn hàng", JOptionPane.ERROR_MESSAGE);
                }
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
                        Order order = getOrderByID(id);
                        if (order != null) {
                            ordersSign.add(order);
                        } else {
                            JOptionPane.showMessageDialog(mainApp, "Không tìm thấy id bạn chọn hãy làm mới lại trang!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private void refresh() throws ConnectException {
        List<Order> orders = API.getOrders(mainApp.getUsername());
        if (orders == null) return;
        model.setRowCount(0);
        for (Order order : orders) {
            model.addRow(new Object[]{order.getId(), order.getUserId(), order.getCreatedAt(), order.getTotalPrice(), order.getDelivery_address().getText(), "Chi tiết"}); // Thay đổi giá trị cột cuối cùng
        }
    }

    public Order getOrderByID(long id) {
        for (Order order : API.orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    private void choosePrivateKey() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn khóa riêng tư");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            String notification = "Tải khóa riêng tư thành công!";
            int status = JOptionPane.INFORMATION_MESSAGE;
            try {
                byte[] bytesPrivateKey = Files.readAllBytes(fileToOpen.toPath());
                String privateKey = new String(bytesPrivateKey);
                txtPrivateKey.setText(privateKey);
            } catch (IOException ex) {
                status = JOptionPane.ERROR_MESSAGE;
                notification = "File bạn chọn không tồn tại";
            }
            JOptionPane.showMessageDialog(mainApp, notification, "Tải khóa riêng tư", status);
        }
    }

    private void setSizeCol() {
        //"ID", "ID người dùng", "Ngày tạo", "Tổng giá", "Địa chỉ giao hàng"
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(300);
    }

    class JButtonRenderer extends DefaultTableCellRenderer {
        private JButton button;

        public JButtonRenderer() {
            button = new JButton("Xem chi tiết");
            button.setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel contain = new JPanel();
            contain.setLayout(new GridBagLayout());
            if (isSelected) {
                contain.setBackground(table.getSelectionBackground());
            } else {
                contain.setBackground(table.getBackground());
            }
            contain.add(button);
            return contain;
        }
    }

    class JButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private int row;
        private JPanel container;
        private JTable table;
        private int[] previousSelectedRows; // Lưu các row được chọn trước đó

        public JButtonEditor() {
            super(new JCheckBox());
            container = new JPanel(new GridBagLayout());
            container.setOpaque(true);

            button = new JButton("Xem chi tiết");
            button.setOpaque(true);
            container.add(button);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    e.consume();
                    clicked = true;

                    // Lưu selection hiện tại
                    previousSelectedRows = table.getSelectedRows();

                    // Xử lý logic click
                    long orderId = (long) table.getModel().getValueAt(row, 0);
                    Order order = getOrderByID(orderId);
                    if (order != null) {
                        //Dử dụng jdiaglog
                        JDialog dialog = new JDialog(mainApp, "Chi tiết đơn hàng", true);
                        dialog.setLocationRelativeTo(null);
                        dialog.setSize(new Dimension(250, 500));
                        dialog.setVisible(true);

                        try {
                            orderDialog = new OrderDialog(mainApp);
                            orderDialog.setOrderTab(OrderTab.this);  // Gán OrderTab vào OrderDialog
                            orderDialog.setOrderId(orderId);
                            orderDialog.init();
                            orderDialog.setVisible(true);
                        } catch (ConnectException ex) {
                            throw new RuntimeException(ex);
                        }




                    } else {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy đơn hàng!");
                    }

                    // Khôi phục selection
                    SwingUtilities.invokeLater(() -> {
                        table.clearSelection();
                        for (int selectedRow : previousSelectedRows) {
                            table.addRowSelectionInterval(selectedRow, selectedRow);
                        }
                    });

                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.table = table;
            this.row = row;
            clicked = false;

            // Lưu selection khi bắt đầu edit
            previousSelectedRows = table.getSelectedRows();

            if (isSelected) {
                container.setBackground(table.getSelectionBackground());
                button.setBackground(table.getSelectionBackground());
            } else {
                container.setBackground(table.getBackground());
                button.setBackground(table.getBackground());
            }

            return container;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {

                SwingUtilities.invokeLater(() -> {
                    table.clearSelection();
                    for (int selectedRow : previousSelectedRows) {
                        table.addRowSelectionInterval(selectedRow, selectedRow);
                    }
                });
            }
            return "";
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }


}
