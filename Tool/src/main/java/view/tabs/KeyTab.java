package view.tabs;

import api.API;
import api.ConnectException;
import model.PublicKeyItem;
import view.BaseUI;
import view.MainApp;
import view.tabs.dialog.GenKeyDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class KeyTab extends JPanel implements BaseUI {
    MainApp mainApp;
    GenKeyDialog genKeyDialog;
    final String[] columnNames = {"ID", "Khóa công khai", "Ngày tạo", "Trạng thái", "Số đơn hàng xác thực"};
    Object[][] data = {};
    DefaultTableModel model;
    JTable table;
    JButton btnGenKey,btnReportKey;

    public KeyTab(MainApp mainApp) {
        this.mainApp = mainApp;
        this.init();
        this.setOnClick();
    }
    @Override
    public void init() {
        genKeyDialog = new GenKeyDialog(mainApp);
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
        table.setIntercellSpacing(new Dimension(1,1));

        try {
            refresh();
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(mainApp,e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        setSizeCol();
        btnGenKey = new JButton("Tạo khóa");
        btnReportKey = new JButton("Báo cáo khóa");
        btnGenKey.setForeground(Color.WHITE);
        btnReportKey.setForeground(Color.WHITE);
        btnGenKey.setBackground(new Color(13,110,253));
        btnReportKey.setBackground(new Color(250,53,69));

        JPanel panelSouth = new JPanel();
        panelSouth.setPreferredSize(new Dimension(0,50));
        panelSouth.setLayout(new BoxLayout(panelSouth, BoxLayout.X_AXIS));
        panelSouth.add(btnGenKey);
        panelSouth.add(Box.createHorizontalGlue());
        panelSouth.add(btnReportKey);

        this.add(scrollPane,BorderLayout.CENTER);
        this.add(panelSouth, BorderLayout.SOUTH);

    }
    public void refresh() throws ConnectException{
        List<PublicKeyItem> orders = API.getPublicKeys(mainApp.getUsername());
        if (orders == null) return;
        model.setRowCount(0); // Xóa tất cả hàng
        for (PublicKeyItem publicKeyItem : orders) {
            model.addRow(new Object[]{publicKeyItem.getId(), publicKeyItem.getPublicKey(), publicKeyItem.getCreatedAt(), publicKeyItem.getStatus(), publicKeyItem.getCountOrderSignature()}); // Thêm hàng mới
        }
    }

    @Override
    public void setOnClick() {
        btnGenKey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genKeyDialog.setVisible(true);
            }
        });
    }

    private void setSizeCol(){
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
            String mess ;
            if ((int)value == 0) {
                mess = "Đã dừng";
                fg = Color.WHITE;
                bg = new Color(220,53,69);
            }else {
                mess = "Đang sử dụng";
                fg = Color.BLACK;
                bg = new Color(13,202,240);
            }
            JButton label = new JButton(mess);
            label.setPreferredSize(new Dimension(110,22));
            label.setForeground(fg);
            label.setBackground(bg);
            contain.add(label);
            label.setOpaque(true);
            if (isSelected) {
                contain.setBackground(table.getSelectionBackground());
            }else {
                contain.setBackground(table.getBackground());
            }

            return contain;
        }
    }
}
