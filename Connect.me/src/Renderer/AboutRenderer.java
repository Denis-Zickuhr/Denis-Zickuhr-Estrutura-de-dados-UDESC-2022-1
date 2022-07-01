package Renderer;

import javax.swing.*;
import java.awt.*;

public class AboutRenderer extends JDialog {

    private JPanel jp_contentPane;

    public AboutRenderer(Component component) {
        setSize(new Dimension(300,400));
        jp_contentPane = new JPanel();
        setContentPane(jp_contentPane);
        setTitle("Sobre!");
        setLocationRelativeTo(component);
        setModal(true);

        JTextArea txt = new JTextArea();

        txt.setEditable(false);
        txt.append("Augusto Rustick" + "\n");
        txt.append("Email: AugustoRustick@email.com"+ "\n");
        txt.append("Denis Zickuhr"+ "\n");
        txt.append("Email: DenisZickuhr@email.com"+ "\n");

        add(txt);

        setVisible(true);
    }
}
