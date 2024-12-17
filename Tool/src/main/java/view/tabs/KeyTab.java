package view.tabs;

import api.API;
import api.ConnectException;
import model.PublicKeyItem;
import okhttp3.Response;
import view.BaseUI;
import view.MainApp;
import view.tabs.dialog.GenKeyDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class KeyTab extends JPanel implements BaseUI {
    final String[] columnNames = {"ID", "Khóa công khai", "Ngày tạo", "Trạng thái", "Số đơn hàng xác thực"};
    MainApp mainApp;
    GenKeyDialog genKeyDialog;
    Object[][] data = {};
    DefaultTableModel model;
    JTable table;
    JButton btnGenKey, btnReportKey, btnRefresh;

    public KeyTab(MainApp mainApp) {
        this.mainApp = mainApp;
        this.init();
        this.setOnClick();
    }

    @Override
    public void init() {
        genKeyDialog = new GenKeyDialog(mainApp);
        genKeyDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               super.windowClosing(e);
               refresh();
            }
        });
        this.setLayout(new BorderLayout());
        model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(new JLabelRenderer());
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setShowGrid(true);
        table.setRowHeight(30);
        table.setGridColor(Color.BLACK);
        table.setIntercellSpacing(new Dimension(1, 1));

            refresh();

        JScrollPane scrollPane = new JScrollPane(table);
        setSizeCol();
        btnRefresh = new JButton("Làm mới");
        btnGenKey = new JButton("Tạo khóa");
        btnReportKey = new JButton("Báo cáo khóa");
        btnGenKey.setForeground(Color.WHITE);
        btnReportKey.setForeground(Color.WHITE);
        btnGenKey.setBackground(new Color(13, 110, 253));
        btnReportKey.setBackground(new Color(250, 53, 69));

        JPanel panelSouth = new JPanel();
        panelSouth.setPreferredSize(new Dimension(0, 50));
        panelSouth.setLayout(new BoxLayout(panelSouth, BoxLayout.X_AXIS));
        panelSouth.add(btnGenKey);
        panelSouth.add(btnRefresh);
        panelSouth.add(Box.createHorizontalGlue());
        panelSouth.add(btnReportKey);

        this.add(scrollPane, BorderLayout.CENTER);
        this.add(panelSouth, BorderLayout.SOUTH);

    }

    public void refresh() {
        List<PublicKeyItem> orders = null;
        try {
            orders = API.getPublicKeys(mainApp.getUsername());
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(mainApp, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        if (orders == null) return;
        model.setRowCount(0); // Xóa tất cả hàng
        for (PublicKeyItem publicKeyItem : orders) {
            model.addRow(new Object[]{publicKeyItem.getId(), publicKeyItem.getPublicKey(), publicKeyItem.getCreatedAt(), publicKeyItem.getStatus(), publicKeyItem.getCountOrderSignature()}); // Thêm hàng mới
        }
    }

    @Override
    public void setOnClick() {
        btnRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    refresh();

            }
        });
        btnReportKey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Response response = API.reportKey(mainApp.getUsername(), mainApp.getPass());
                    String mess;
                    int status;
                    if (response.isSuccessful()) {
                        status = JOptionPane.INFORMATION_MESSAGE;
                        mess = "Báo cáo khóa thành công!";
                        refresh();
                    }else {
                        status = JOptionPane.ERROR_MESSAGE;
                        mess = response.body().string();
                    }
                    JOptionPane.showMessageDialog(mainApp,mess,"Báo cáo khóa",status);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainApp,"Đã có lôi xảy ra!","Báo cáo khóa",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnGenKey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genKeyDialog.setEnable(true);
            }
        });
    }

    private void setSizeCol() {
        //"ID", "khóa công khai", "Ngày tạo", "trạng thía", "số đơn"
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(300);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
    }

    static class JLabelRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel contain = new JPanel();
            contain.setLayout(new GridBagLayout());
            Color bg;
            Color fg;
            String mess;
            if ((int) value == 0) {
                mess = "Đã dừng";
                fg = Color.WHITE;
                bg = new Color(220, 53, 69);
            } else {
                mess = "Đang sử dụng";
                fg = Color.BLACK;
                bg = new Color(13, 202, 240);
            }
            JButton label = new JButton(mess);
            label.setPreferredSize(new Dimension(110, 22));
            label.setForeground(fg);
            label.setBackground(bg);
            contain.add(label);
            label.setOpaque(true);
            if (isSelected) {
                contain.setBackground(table.getSelectionBackground());
            } else {
                contain.setBackground(table.getBackground());
            }

            return contain;
        }
    }
}
