package view;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import view.tabs.OrderTab;


import javax.swing.*;
import java.awt.*;
import java.security.Security;

public class MainApp extends JFrame implements BaseUI, DefaultPropertyUI {
    JTabbedPane tabbedPane;
    OrderTab orderTab;
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
        tabbedPane = new JTabbedPane();
        orderTab = new OrderTab(this);

        tabbedPane.addTab("Orders", new ImageIcon(MainApp.class.getResource("/image/package.png")), orderTab);

        add(tabbedPane, BorderLayout.CENTER);

    }

    @Override
    public void setOnClick() {

    }

}
