package view;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import view.custom.ui.ProgressCircle;
import view.tabs.KeyTab;
import view.tabs.OrderTab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.Security;

public class MainApp extends JFrame implements BaseUI, DefaultPropertyUI {
    JTabbedPane tabbedPane;
    OrderTab orderTab;
    KeyTab keyTab;
    LoginPage loginPage;
    String username;
    String pass;
    JButton btnLogOut;
    JPanel header;
    ProgressCircle progressCircle;
    public MainApp() {
        this.setTitle("Tool");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        init();
        setOnClick();
    }
    public void displayProgress(){

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
        btnLogOut = new JButton("Đăng xuất", new ImageIcon(MainApp.class.getResource("/image/logout.png")));
        loginPage = new LoginPage(this);
        loginPage.setVisible(true);
        keyTab = new KeyTab(this);
        orderTab = new OrderTab(this);
        tabbedPane = new JTabbedPane();
        tabbedPane.setVisible(true);
        tabbedPane.addTab("Đơn hàng", new ImageIcon(MainApp.class.getResource("/image/package.png")), orderTab);
        tabbedPane.addTab("Khóa", new ImageIcon(MainApp.class.getResource("/image/key.png")), keyTab);
        header = new JPanel();
        header.setLayout(new FlowLayout(FlowLayout.RIGHT));
        header.add(btnLogOut);
        add(header,BorderLayout.NORTH);
        goToLoginPage();
        this.setVisible(true);
    }

    @Override
    public void setOnClick() {
        btnLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToLoginPage();
            }
        });
    }

    public void goToHomePage(String username, String pass) {
        this.username = username;
        this.pass = pass;
        header.setVisible(true);
        this.removeC(loginPage);
        orderTab.refresh();
        keyTab.refresh();
        add(tabbedPane, BorderLayout.CENTER);

    }
    public void goToLoginPage(){
        this.username = null;
        this.pass = null;
        header.setVisible(false);
        this.removeC(tabbedPane);
        add(loginPage, BorderLayout.CENTER);
    }
    private void removeC(Component component){
        this.remove(component);
        this.revalidate();
        this.repaint();
    }
    public String getUsername() {
        return username;
    }

    public String getPass() {
        return pass;
    }

}
