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
    public static long maxSprites; //the max number of sprite in the game
    public static long tickSpeed; //n tick per second

    public abstract void init();
    public abstract void loop();
    public static void initWindow(String title,int[] screenSize){
        maxSprites = 5000;
        tickSpeed = 500;
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
                        inputListener.mouse.leftClick = (e.getButton() == MouseEvent.BUTTON1);
                        inputListener.mouse.rightClick = (e.getButton() == MouseEvent.BUTTON2);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        inputListener.mouse.leftClick = !(e.getButton() == MouseEvent.BUTTON1);
                        inputListener.mouse.rightClick = !(e.getButton() == MouseEvent.BUTTON2);
                    }
                }
        );
        g.addMouseMotionListener(
                new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        inputListener.mouse.mouseX = e.getX() - 8; //I don't why that always has 8 bigger than true x
                        inputListener.mouse.mouseY = e.getY() - 31; //I don't why that always has 31 bigger than true y
                    }

                    @Override
                    public void mouseDragged(MouseEvent e){
                        inputListener.mouse.mouseX = e.getX() - 8; //I don't why that always has 8 bigger than true x
                        inputListener.mouse.mouseY = e.getY() - 31; //I don't why that always has 31 bigger than true y
                    }
                }
        );
        g.setVisible(true);
    }

    public static void addSprite(sprite sprite){
        if (spritesMap.size() < maxSprites) spritesMap.add(sprite);
    }

    public void runMain(){
        new Thread(() -> {
            try {
                init();
                while (true) {
                    loop();
                    //every sprite
                    for (sprite q : spritesMap) {
                        q.loop();
                    }
                    //repaint
                    d.repaint();
                    Thread.sleep(1 / tickSpeed);
                }
            }catch (InterruptedException ignored){

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
                            g.fillRect(a.pos.x - (a.self.wight / 2),a.pos.y - (a.self.height / 2),a.self.wight,a.self.height);
                        }
                    }
                }
            }
        }
    }

    public static class inputListener{
        public static class mouse extends MouseAdapter{
            public static boolean leftClick;
            public static boolean rightClick;
            public static int mouseX;
            public static int mouseY;
        }

        public static class keyboard{

        }
    }

    public static class event{
        public static boolean isTouch(sprite sprite1, sprite sprite2){
            int xLength = Math.abs(sprite1.pos.x - sprite2.pos.x);
            int yLength = Math.abs(sprite1.pos.y - sprite2.pos.y);
            return (xLength <= (sprite1.self.wight / 2 + sprite2.self.wight / 2))
                    && (yLength <= (sprite1.self.height / 2 + sprite2.self.height / 2));
        }
    }
}
