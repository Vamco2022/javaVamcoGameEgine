package com.vamco.java.jar.jvgameEngine.object.sprites;

import com.vamco.java.jar.jvgameEngine.engine.basePaint;
import com.vamco.java.jar.jvgameEngine.plugin.base2d.position;
import com.vamco.java.jar.jvgameEngine.plugin.sprite.selfBody;

import java.awt.*;
import java.io.File;

public abstract class sprite {
    public selfBody self;
    public position pos;
    public File img;
    public basePaint p;
    public Color color;


    public abstract void init();
    public abstract void loop();

    public sprite(int x, int y, File img, int wight, int height){
        super();
        this.self = new selfBody(wight,height);
        this.pos = new position(x,y);
        this.img = img;
        runNewThread();
    }

    public sprite(int x, int y, basePaint p, Color color, int wight, int height){
        super();
        this.self = new selfBody(wight,height);
        this.pos = new position(x,y);
        this.img = null;
        this.p = p;
        this.color = color;
        runNewThread();
    }

    public void runNewThread(){
        init();
        new Thread(
                () -> {
                    while (true){
                        loop();
                    }
                }
        ).start();
    }
}
