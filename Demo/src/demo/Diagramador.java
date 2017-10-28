package demo;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class Diagramador extends JFrame {

    JLabel statusbar;


    public Diagramador() {

        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);
        Tablero tablero = new Tablero(this);
        add(tablero);
        tablero.start();

        setSize(200, 400);
        setTitle("Tetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
   }

   public JLabel getStatusBar() {
       return statusbar;
   }

    public static void main(String[] args) {

    	Diagramador game = new Diagramador();
        game.setLocationRelativeTo(null);
        game.setVisible(true);

    } 
}
