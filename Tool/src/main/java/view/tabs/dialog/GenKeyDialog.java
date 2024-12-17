package view.tabs.dialog;

import api.API;
import okhttp3.Response;
import utils.SignatureUtils;
import view.BaseUI;
import view.MainApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenKeyDialog extends JDialog implements BaseUI {
    SignatureUtils signatureUtils;
    MainApp mainApp;
    JTextField txtOTP;
    JPasswordField txtPrivateKey, txtPublicKey;
    JButton btnSendOTP, btnExportKey, btnConfirm;

    public GenKeyDialog(MainApp mainApp) {
        super(mainApp, "Tạo khóa", true);
        this.mainApp = mainApp;
        this.setSize(new Dimension(400, 350));
        this.setLocationRelativeTo(null);
        this.init();
        this.setOnClick();
    }

    @Override
    public void init() {
        signatureUtils = new SignatureUtils();
        this.setLayout(new GridBagLayout());
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(350, 250));
        JPanel containOTP = new JPanel();
        JPanel containPublicKey = new JPanel();
        JPanel containPrivateKey = new JPanel();
        JPanel containAction = new JPanel();
        containOTP.setLayout(new BoxLayout(containOTP, BoxLayout.X_AXIS));
        containPublicKey.setLayout(new BoxLayout(containPublicKey, BoxLayout.X_AXIS));
        containPrivateKey.setLayout(new BoxLayout(containPrivateKey, BoxLayout.X_AXIS));
        containAction.setLayout(new BoxLayout(containAction, BoxLayout.X_AXIS));
        txtOTP = new JTextField(18);
        txtOTP.setMaximumSize(new Dimension(150, 35));
        containOTP.add(new JLabel("OTP:"));
        containOTP.add(Box.createHorizontalGlue());
        containOTP.add(txtOTP);
        txtPublicKey = new JPasswordField(18);
        txtPublicKey.setMaximumSize(new Dimension(150, 35));
        containPublicKey.add(new JLabel("Khóa công khai:"));
        containPublicKey.add(Box.createHorizontalGlue());
        containPublicKey.add(txtPublicKey);

        txtPrivateKey = new JPasswordField(18);
        txtPrivateKey.setMaximumSize(new Dimension(150, 35));
        containPrivateKey.add(new JLabel("Khóa công khai:"));
        containPrivateKey.add(Box.createHorizontalGlue());
        containPrivateKey.add(txtPrivateKey);

        btnSendOTP = new JButton("Gửi OTP");
        btnExportKey = new JButton("Xuất tệp khóa riêng tư");
        btnConfirm = new JButton("Xác nhận");
        btnSendOTP.setBackground(new Color(255, 193, 7));
        btnExportKey.setBackground(new Color(255, 193, 7));
        btnConfirm.setBackground(new Color(13, 110, 253));
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
        btnExportKey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Chọn nơi lưu khóa riêng tư");
                int userSelection = fileChooser.showSaveDialog(mainApp);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    if (fileToSave.exists()) {
                        int response = JOptionPane.showConfirmDialog(mainApp,
                                "Tệp tin đã tồn tại. Bạn có muốn ghi đè?",
                                "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                        if (response != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }
                    try (FileWriter writer = new FileWriter(fileToSave)) {
                        writer.write(signatureUtils.getPrivateKeyBase64());
                        JOptionPane.showMessageDialog(mainApp, "Tệp tin đã được lưu thành công!", "Lưu tâ tin", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainApp, "Đã có lỗi khi lưu tệp tin!", "Lưu tệp tin", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String OTP = txtOTP.getText();
                if (!(OTP.isEmpty() || OTP.isBlank())) {
                    String mess;
                    int status;
                    try {
                        Response response = API.verifyOTP(mainApp.getUsername(), mainApp.getPass(), OTP);
                        if (response.isSuccessful()) {
                            signatureUtils.genKey();
                            String pubB64 = signatureUtils.getPublicKeyBase64();
                            Response responseSavePublicKey = API.savePublicKey(mainApp.getUsername(), mainApp.getPass(), pubB64);
                            if (responseSavePublicKey.isSuccessful()) {
                                status = JOptionPane.INFORMATION_MESSAGE;
                                txtPrivateKey.setText(pubB64);
                                txtPublicKey.setText(signatureUtils.getPublicKeyBase64());
                                btnExportKey.setEnabled(true);
                                mess = "Xác thực thành công!\nBạn có thể lưu khóa riêng tư!";
                            } else {
                                status = JOptionPane.ERROR_MESSAGE;
                                mess = "Lưu khóa công khai lên server không thành công!";
                            }

                        } else {
                            mess = response.body().string();
                            status = JOptionPane.ERROR_MESSAGE;
                        }
                        JOptionPane.showMessageDialog(mainApp, mess, "Xác thực OTP", status);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainApp, "Xác OTP không thành công!", "Xác thực OTP", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainApp, "Vui lòng nhập OTP!", "Xác thực OTP", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnSendOTP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Response response = API.sendOTP(mainApp.getUsername(), mainApp.getPass());
                    String mess;
                    int status;
                    if (response.isSuccessful()) {
                        status = JOptionPane.INFORMATION_MESSAGE;
                        mess = "Gửi OTP thành công vui lòng kiểm tra email";
                    } else {
                        status = JOptionPane.ERROR_MESSAGE;
                        mess = response.body().string();
                    }
                    JOptionPane.showMessageDialog(mainApp, mess, "Gửi OTP", status);

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(mainApp, "Gửi OTP không thành công!", "Gửi OTP", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void setEnable(boolean enable) {
        setVisible(enable);
        btnExportKey.setEnabled(false);

    }
}
