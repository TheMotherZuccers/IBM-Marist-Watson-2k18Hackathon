package com.themotherzuccers.gitefficient.GUI;
import javax.swing.*;
import java.awt.*;

public class Draw extends JPanel{


    public void drawing(){
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.RED);
        g.fillRect(10, 100, 10, 50);

    }



}
