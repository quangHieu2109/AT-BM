package view.tabs.dialog;

import view.BaseUI;
import view.MainApp;

import javax.swing.*;
import java.awt.*;

public class GenKeyDialog extends JDialog implements BaseUI {
    MainApp mainApp;
    JTextField txtOTP;
    JPasswordField txtPrivateKey,txtPublicKey;
    JButton btnSendOTP,btnExportKey,btnConfirm;

    public GenKeyDialog(MainApp mainApp){
        super(mainApp,"Tạo khóa",true);
        this.setSize(new Dimension(400,350));
        this.setLocationRelativeTo(null);
        this.init();
        this.setOnClick();
    }

    @Override
    public void init() {
        this.setLayout(new GridBagLayout());
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(350,250));
        JPanel containOTP = new JPanel();
        JPanel containPublicKey = new JPanel();
        JPanel containPrivateKey = new JPanel();
        JPanel containAction = new JPanel();
        containOTP.setLayout(new BoxLayout(containOTP, BoxLayout.X_AXIS));
        containPublicKey.setLayout(new BoxLayout(containPublicKey, BoxLayout.X_AXIS));
        containPrivateKey.setLayout(new BoxLayout(containPrivateKey, BoxLayout.X_AXIS));
        containAction.setLayout(new BoxLayout(containAction, BoxLayout.X_AXIS));
        txtOTP = new JTextField(18);
        txtOTP.setMaximumSize(new Dimension(150,35));
        containOTP.add(new JLabel("OTP:"));
        containOTP.add(Box.createHorizontalGlue());
        containOTP.add(txtOTP);
        txtPublicKey = new JPasswordField(18);
        txtPublicKey.setMaximumSize(new Dimension(150,35));
        containPublicKey.add(new JLabel("Khóa công khai:"));
        containPublicKey.add(Box.createHorizontalGlue());
        containPublicKey.add(txtPublicKey);

        txtPrivateKey = new JPasswordField(18);
        txtPrivateKey.setMaximumSize(new Dimension(150,35));
        containPrivateKey.add(new JLabel("Khóa công khai:"));
        containPrivateKey.add(Box.createHorizontalGlue());
        containPrivateKey.add(txtPrivateKey);

        btnSendOTP = new JButton("Gửi OTP");
        btnExportKey = new JButton("Xuất tệp khóa riêng tư");
        btnConfirm = new JButton("Xác nhận");
        btnSendOTP.setBackground(new Color(255,193,7));
        btnExportKey.setBackground(new Color(255,193,7));
        btnConfirm.setBackground(new Color(13,110,253));
        btnConfirm.setForeground(Color.WHITE);
        btnExportKey.setEnabled(false);
        containAction.add(btnSendOTP);
        containAction.add(btnExportKey);
        containAction.add(btnConfirm);

        txtPublicKey.setEnabled(false);
        txtPrivateKey.setEnabled(false);

        container.add(containOTP);
        container.add(Box.createVerticalStrut(15));
        container.add(containPublicKey);
        container.add(Box.createVerticalStrut(15));
        container.add(containPrivateKey);
        container.add(Box.createVerticalStrut(15));
        container.add(containAction);

        this.add(container);
    }

    @Override
    public void setOnClick() {

    }
}
