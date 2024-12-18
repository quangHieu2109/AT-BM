package view;

import api.API;
import model.PublicKeyItem;
import okhttp3.Response;
import utils.HashingUtils;
import view.custom.ui.ProgressCircle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class LoginPage extends JPanel implements BaseUI {
    JTextField txtUsername;
    JPasswordField txtPassword;
    String username;
    String password;
    JButton btnLogin;
    List<PublicKeyItem> publicKeyItems;
    MainApp mainApp;

    public LoginPage(MainApp mainApp) {
        this.init();
        this.setOnClick();
        this.setVisible(true);
        this.mainApp = mainApp;
    }

    @Override
    public void init() {
        this.setLayout(new GridBagLayout());
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(280, 250));
        JPanel containUsername = new JPanel();
        JPanel containPassword = new JPanel();
        containUsername.setLayout(new BoxLayout(containUsername, BoxLayout.X_AXIS));
        containPassword.setLayout(new BoxLayout(containPassword, BoxLayout.X_AXIS));
        txtPassword = new JPasswordField(15);
        txtPassword.setMaximumSize(new Dimension(150, 35));
        containPassword.add(new JLabel("Mật khẩu:"));
        containPassword.add(Box.createHorizontalGlue());
        containPassword.add(txtPassword);
        txtUsername = new JTextField(15);
        txtUsername.setMaximumSize(new Dimension(150, 35));
        containUsername.add(new JLabel("Tên người dùng:"));
        containUsername.add(Box.createHorizontalGlue());
        containUsername.add(txtUsername);
        btnLogin = new JButton("Đăng nhập");

        container.add(containUsername);
        container.add(Box.createVerticalStrut(15));
        container.add(containPassword);
        container.add(Box.createVerticalStrut(15));
        container.add(btnLogin);

        this.add(container);
    }

    @Override
    public void setOnClick() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = txtUsername.getText();
                char[] pass = txtPassword.getPassword();
                if (!(username.isBlank() || pass.length == 0 || username.isEmpty())) {
                    password = HashingUtils.hash(new String(pass));
                   callAPILogin(username,password);
                } else {
                    JOptionPane.showMessageDialog(mainApp, "Vui lòng nhập đầy đủ tài khoản và mật khẩu", "Đăng nhập", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void callAPILogin(String username,String password){

       ProgressCircle progressCircle = new ProgressCircle(mainApp) {
            @Override
            protected Response doInBackground() throws Exception {
                return API.login(username, password);
            }

            @Override
            protected void done() {
                super.done();
                try {
                    Response response = get();
                    if (response.isSuccessful()) {
                        mainApp.goToHomePage(username, password);
                    } else {
                        JOptionPane.showMessageDialog(mainApp, response.body().string(), "Đăng nhập", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(mainApp, "Không thể kết nối đến server!", "Đăng nhập", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        progressCircle.execute();
        progressCircle.progressCircleDialog.showProgress(true);

    }
}
