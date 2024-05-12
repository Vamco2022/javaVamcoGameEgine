package com.vamco.java.jar.jvgameEngine.engine;

import com.vamco.java.jar.jvgameEngine.object.sprites.sprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public abstract class gameMain extends Thread{
    public static gameWindow g;
    static gameDraw d;
    public static ArrayList<sprite> spritesMap;
    public static long maxSprites = 5000; //the max number of sprite in the game
    public static long tickSpeed = 500; //the tick per second

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
        g.addKeyListener(
                new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        inputListener.keyboard.isClick = true;
                    }

                    @Override
                    public void keyTyped(KeyEvent e) {
                        inputListener.keyboard.isClick = true;
                        inputListener.keyboard.keyCode = e.getKeyCode();
                        inputListener.keyboard.keyChar = e.getKeyChar();
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                        inputListener.keyboard.isClick = false;
                        inputListener.keyboard.keyCode = null;
                        inputListener.keyboard.keyChar = null;
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
                    Thread.sleep(1000 / tickSpeed);
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
                if (!a.isHind) {
                    if (a.img == null) {
                        switch (a.p) {
                            case square -> {
                                g.setColor(a.color);
                                if (a.degree % 90 == 0) {
                                    g.fillRect((a.position.x - (a.self.wight / 2)), (a.position.y - (a.self.height / 2)), a.self.wight, a.self.height);
                                }else{
                                    //Because JPanel no able to paint object with its degree
                                    //So we try to use something to virtual it
                                    //Since the draw will use lots of time, the window thread can't run, so we put it into a new thread
                                    new Thread(() -> {
                                            int[] leftAbovePoint = new int[]{(int) Math.floor((a.position.x - Math.cos(Math.toRadians(a.degree)) * a.self.wight / 2)), (int) Math.floor(a.position.y - Math.sin(Math.toRadians(a.degree)) * a.self.height / 2)};
                                            int[] rightAbovePoint = new int[]{(int) Math.ceil((a.position.x + Math.cos(Math.toRadians(a.degree)) * a.self.wight / 2)), (int) Math.ceil((a.position.y - Math.sin(Math.toRadians(a.degree)) * a.self.height / 2))};
                                            int[] leftDownPoint = new int[]{(int) Math.floor((a.position.x - Math.cos(Math.toRadians(a.degree)) * a.self.wight / 2)), (int) Math.floor((a.position.y + Math.sin(Math.toRadians(a.degree)) * a.self.height / 2))};
                                            int[] rightDownPoint = new int[]{(int) Math.ceil((a.position.x + Math.cos(Math.toRadians(a.degree)) * a.self.wight / 2)), (int) Math.ceil((a.position.y + Math.sin(Math.toRadians(a.degree)) * a.self.height / 2))};
                                            int upLineLeft;
                                            int downLineLeft;
                                            if (a.degree > 0) {
                                                upLineLeft = (Math.abs(leftAbovePoint[1] - leftDownPoint[1])) / (Math.abs(leftAbovePoint[0] - leftDownPoint[0]));
                                                //upLineRight = (Math.abs(rightAbovePoint[1] - leftAbovePoint[1])) / (Math.abs(rightAbovePoint[0] - leftAbovePoint[0]));
                                                downLineLeft = (Math.abs(leftDownPoint[1] - rightDownPoint[1])) / (Math.abs(leftDownPoint[0] - rightDownPoint[0]));
                                                //downLineRight = (Math.abs(rightAbovePoint[1] - rightDownPoint[1])) / (Math.abs(rightAbovePoint[0] - rightDownPoint[0]));
                                            } else {
                                                upLineLeft = (Math.abs(rightAbovePoint[1] - leftAbovePoint[1])) / (Math.abs(rightAbovePoint[0] - leftAbovePoint[0]));
                                                //upLineRight = (Math.abs(rightAbovePoint[1] - rightDownPoint[1])) / (Math.abs(rightAbovePoint[0] - rightDownPoint[0]));
                                                downLineLeft = (Math.abs(leftAbovePoint[1] - leftDownPoint[1])) / (Math.abs(leftAbovePoint[0] - leftDownPoint[0]));
                                                //downLineRight = (Math.abs(rightDownPoint[1] - leftDownPoint[1])) / (Math.abs(rightDownPoint[0] - leftDownPoint[0]));
                                            }
                                            //paint cube for degree
                                            int s = (int) Math.sqrt(Math.pow(a.self.wight, 2) + Math.pow(a.self.height, 2));
                                            int drawArea = 1;
                                            int localAbs = s;
                                            while (drawArea <= s) {
                                                g.fillRect(a.position.x - drawArea, a.position.y - localAbs, drawArea, 1);
                                                drawArea += upLineLeft;
                                                System.out.println("Paint up");
                                                localAbs--;
                                            }
                                            while (drawArea >= 1) {
                                                g.fillRect(a.position.x - drawArea, a.position.y - localAbs, drawArea, 1);
                                                drawArea -= downLineLeft;
                                                System.out.println("Paint down");
                                                localAbs--;
                                            }
                                        }
                                    ).start();
                                }
                            }
                        }
                    }else{
                        Image img = Toolkit.getDefaultToolkit().getImage(a.img.getPath());
                        g.drawImage(img,a.position.x - (a.self.wight / 2), a.position.y - (a.self.height / 2),this);
                    }
                }
            }
        }
    }

    public static class inputListener{
        public static class mouse{
            public static boolean leftClick;
            public static boolean rightClick;
            public static int mouseX;
            public static int mouseY;
        }

        public static class keyboard{
            public static boolean isClick;
            public static Integer keyCode;
            public static Character keyChar;
        }
    }

    public static class event{
        public static boolean isTouch(sprite sprite1, sprite sprite2){
            int xLength = Math.abs(sprite1.position.x - sprite2.position.x);
            int yLength = Math.abs(sprite1.position.y - sprite2.position.y);
            return (xLength <= (sprite1.self.wight / 2 + sprite2.self.wight / 2))
                    && (yLength <= (sprite1.self.height / 2 + sprite2.self.height / 2));
        }

        public static boolean isTouch(String id1,String id2){
            ArrayList<sprite> sprites1 = new ArrayList<>();
            ArrayList<sprite> sprites2 = new ArrayList<>();
            for (sprite i : spritesMap){
                if (i.spriteId.equals(id1)){
                    sprites1.add(i);
                }else if (i.spriteId.equals(id2)){
                    sprites2.add(i);
                }
            }
            for (sprite i : sprites1){
                for (sprite a : sprites2){
                    if (isTouch(i,a)){
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class gameSystem{
        public static void setMaxSprite(int maxSprite){
            maxSprites = maxSprite;
        }

        public static void setMaxTickPerSecond(long tickHz){
            tickSpeed = tickHz;
        }

        //public static void setMaxThread(int maxThread);
    }

    public static class spriteConsole{
        public static ArrayList<sprite> getSpriteList(String id){
            ArrayList<sprite> out = new ArrayList<>();
            for (sprite i : spritesMap){
                if (i.spriteId.equals(id)) out.add(i);
            }
            return out;
        }
    }
}
