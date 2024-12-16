package view;
import api.API;
import okhttp3.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginPage extends JPanel implements BaseUI {
    JTextField txtUsername;
    JPasswordField txtPassword;
    JButton btnLogin;
    MainApp mainApp;
    public LoginPage(MainApp mainApp){
        this.init();
        this.setOnClick();
        this.setVisible(true);
        this.mainApp = mainApp;
    }
    @Override
    public void init() {
        this.setLayout(new GridBagLayout());
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(280,250));
        JPanel containUsername = new JPanel();
        JPanel containPassword = new JPanel();
        containUsername.setLayout(new BoxLayout(containUsername, BoxLayout.X_AXIS));
        containPassword.setLayout(new BoxLayout(containPassword, BoxLayout.X_AXIS));
        txtPassword = new JPasswordField(15);
        txtPassword.setMaximumSize(new Dimension(150,35));
        containPassword.add(new JLabel("Mật khẩu:"));
        containPassword.add(Box.createHorizontalGlue());
        containPassword.add(txtPassword);
        txtUsername = new JTextField(15);
        txtUsername.setMaximumSize(new Dimension(150,35));
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
                String username = txtUsername.getText();
                char[] pass = txtPassword.getPassword();
                if (!(username.isBlank()||pass.length==0||username.isEmpty())) {
                    try {
                        Response response = API.login(username,new String(pass));
                        if (response.isSuccessful()) {

                        }else {
                            JOptionPane.showMessageDialog(mainApp,response.body().string(),"Đăng nhập",JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(mainApp,"Không thể kết nối đến server!","Đăng nhập",JOptionPane.ERROR_MESSAGE);
                    }
                }else {
                    JOptionPane.showMessageDialog(mainApp,"Vui lòng nhập đầy đủ tài khoản và mật khẩu","Đăng nhập",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
