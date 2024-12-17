package view;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import view.tabs.KeyTab;
import view.tabs.OrderTab;

import javax.swing.*;
import java.awt.*;
import java.security.Security;

public class MainApp extends JFrame implements BaseUI, DefaultPropertyUI {
    JTabbedPane tabbedPane;
    OrderTab orderTab;
    KeyTab keyTab;
    LoginPage loginPage;
    String username;
    String pass;

    public MainApp() {
        this.setTitle("Tool");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        init();
    }

    public static void main(String[] args) {
        FlatCyanLightIJTheme.setup();
        UIManager.put("Button.arc", 12);
        UIManager.put("TextComponent.arc", 12);
        UIManager.put("ScrollBar.thumbArc", 12);
        Security.addProvider(new BouncyCastleProvider());
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainApp();
            }
        });
    }

    @Override
    public void init() {
        loginPage = new LoginPage(this);
        add(loginPage, BorderLayout.CENTER);
        this.setVisible(true);
    }

    @Override
    public void setOnClick() {
    }

    public void goToHomePage(String username, String pass) {
        this.username = username;
        this.pass = pass;
        loginPage.setVisible(false);
        keyTab = new KeyTab(this);
        orderTab = new OrderTab(this);
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Orders", new ImageIcon(MainApp.class.getResource("/image/package.png")), orderTab);
        tabbedPane.addTab("Keys", new ImageIcon(MainApp.class.getResource("/image/key.png")), keyTab);
        add(tabbedPane, BorderLayout.CENTER);
    }

    public String getUsername() {
        return username;
    }

    public String getPass() {
        return pass;
    }

}
