package com.vamco.java.jar.jvgameEngine.plugin.base2d;

public class position {
    public int x,y;
    public position(int x,int y){
        this.x = x;
        this.y = y;
    }

    public void move(int x,int y){
        this.x += x;
        this.y += y;
    }

    public void move(int[] p){
        this.x += p[0];
        this.y += p[1];
    }

    public void moveTo(int x,int y){
        this.x = x;
        this.y = y;
    }

    public void moveTo(position p){
        this.x = p.x;
        this.y = p.y;
    }
}
