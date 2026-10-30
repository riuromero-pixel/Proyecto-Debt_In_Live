package src.vista;

import java.awt.*;
import javax.swing.*;

public class VistaIntro extends JFrame {

    public VistaIntro(Runnable alTerminar) { 
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int anchoPantalla = pantalla.width;
        int altoPantalla  = pantalla.height;

        ImageIcon gif = new ImageIcon("images/introunpsjb.gif");

        JLabel label = new JLabel(gif);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 40)); 
        getContentPane().setBackground(new Color(30, 30, 40));
        panel.add(label);
        setContentPane(panel);

        setSize(anchoPantalla, altoPantalla);
        setLocationRelativeTo(null);

        Timer timer = new Timer(5000, e -> {
            alTerminar.run();
        });
        timer.setRepeats(false);
        timer.start();

        setVisible(true);
    }
}