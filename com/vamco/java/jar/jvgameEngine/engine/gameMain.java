package com.vamco.java.jar.jvgameEngine.engine;

import com.vamco.java.jar.jvgameEngine.object.sprites.sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class gameMain extends Thread{
    public static gameWindow g;
    static gameDraw d;
    public static ArrayList<sprite> spritesMap;

    public abstract void init();
    public abstract void loop();
    public static void initWindow(String title,int[] screenSize){
        g = new gameWindow();
        spritesMap = new ArrayList<sprite>();
        d = new gameDraw();
        d.setBounds(0,0,screenSize[0],screenSize[1]);
        g.setTitle(title);
        g.setBounds(0,0,screenSize[0],screenSize[1]);
        g.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        g.add(d);
        g.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        listener.mouse.leftClick = (e.getButton() == MouseEvent.BUTTON1);
                        listener.mouse.rightClick = (e.getButton() == MouseEvent.BUTTON2);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        listener.mouse.leftClick = !(e.getButton() == MouseEvent.BUTTON1);
                        listener.mouse.rightClick = !(e.getButton() == MouseEvent.BUTTON2);
                    }
                }
        );
        g.addMouseMotionListener(
                new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        listener.mouse.mouseX = e.getX() - 8; //I don't why that always has 8 bigger than true x
                        listener.mouse.mouseY = e.getY() - 31; //I don't why that always has 31 bigger than true y
                    }

                    @Override
                    public void mouseDragged(MouseEvent e){
                        listener.mouse.mouseX = e.getX() - 8; //I don't why that always has 8 bigger than true x
                        listener.mouse.mouseY = e.getY() - 31; //I don't why that always has 31 bigger than true y
                    }
                }
        );
        g.setVisible(true);
    }

    public static void addSprite(sprite sprite){
        spritesMap.add(sprite);
    }

    public void runMain(){
        new Thread(() -> {
            init();
            while (true) {
                loop();
                //every sprite
                for (sprite q : spritesMap){
                    q.loop();
                }
                //repaint
                d.repaint();
            }
        }).start();
    }

    static class gameDraw extends JPanel{
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            for (sprite a : spritesMap){
                if (a.img == null){
                    switch (a.p){
                        case square -> {
                            g.setColor(a.color);
                            g.fillRect(a.pos.x,a.pos.y,a.self.wight,a.self.height);
                        }
                    }
                }
            }
        }
    }

    public static class listener{
        public static class mouse extends MouseAdapter{
            public static boolean leftClick;
            public static boolean rightClick;
            public static int mouseX;
            public static int mouseY;
        }

        public static class keyboard{

        }
    }
}
