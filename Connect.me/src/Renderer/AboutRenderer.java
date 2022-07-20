package Renderer;

import javax.swing.*;
import java.awt.*;

public class AboutRenderer extends JDialog {

    public AboutRenderer(Component component) {
        setSize(new Dimension(300,100));
        JPanel jp_contentPane = new JPanel();
        setContentPane(jp_contentPane);
        setTitle("Sobre!");
        setLocationRelativeTo(component);
        setModal(true);


        JLabel email1 = new JLabel("Augusto Rustick: gameeend00@gmail.com");
        JLabel email2 = new JLabel("Denis Zickuhr: deniszickuhr@gmail.com");

        add(email1);
        add(email2);

        setVisible(true);
    }
}
