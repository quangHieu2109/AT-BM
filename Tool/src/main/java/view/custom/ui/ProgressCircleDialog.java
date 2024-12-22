package view.custom.ui;

import view.BaseUI;
import view.MainApp;

import javax.swing.*;
import java.awt.*;

public class ProgressCircleDialog extends JDialog implements BaseUI {
        private JProgressBar progressCircle;
        public ProgressCircleDialog(JFrame frame){
            super(frame,true);
            this.setUndecorated(true);
            this.setSize(new Dimension(100,100));
            this.setLocationRelativeTo(frame);
            init();
        }

        @Override
        public void init() {

            progressCircle = new JProgressBar();
            progressCircle.setUI(new ProgressCircleUI());
            progressCircle.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
            progressCircle.setStringPainted(false);
            add(progressCircle);
        }

        public void showProgress(boolean isRun){
            if (isRun) {
                (new Timer(30, e -> {
                    int iv = progressCircle.getValue() + 2;
                    progressCircle.setValue(iv>100?0:iv);
                })).start();
                this.setVisible(true);


            } else {
                this.setVisible(false);
            }
        }
        @Override
        public void setOnClick() {

        }

    public class RoundedPanel extends JPanel {
        private int cornerRadius;

        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false); // Để cho JPanel có thể hiển thị hình dạng bo tròn
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // 30 là bán kính bo tròn
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            paintComponent(g); // Gọi phương thức vẽ hình tròn
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(100, 100); // Kích thước tùy chỉnh cho JPanel
        }
    }
}
