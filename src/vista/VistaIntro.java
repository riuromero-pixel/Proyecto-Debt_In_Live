package src.vista;

import java.awt.*;
import javax.swing.*;

public class VistaIntro extends JFrame {
public VistaIntro(Runnable alTerminar) {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         getContentPane().setBackground(new Color(30, 30, 40));

        ImageIcon gif = new ImageIcon("images/introunpsjb.gif");

           final Image imagenGif = gif.getImage();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Al pasarle getWidth() y getHeight(), le decimos que estire el GIF 
                // al tamaño de la ventana. El 'this' al final mantiene la animación.
                if (imagenGif != null) {
                    g.drawImage(imagenGif, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setBackground(new Color(30, 30, 40));
        setContentPane(panel);


        setContentPane(panel);

     setSize(800, 600);
     
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.toFront();
        this.requestFocusInWindow();

        Timer timer = new Timer(5000, e -> {
            dispose();
            alTerminar.run();
        });
        timer.setRepeats(false);
        timer.start();

    }
}