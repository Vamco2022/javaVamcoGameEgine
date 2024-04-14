package com.vamco.java.jar.jvgameEngine.object.sprites;

import com.vamco.java.jar.jvgameEngine.engine.basePaint;
import com.vamco.java.jar.jvgameEngine.plugin.base2d.position;
import com.vamco.java.jar.jvgameEngine.plugin.sprite.selfBody;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class sprite {
    public selfBody self;
    public position position;
    public File img;
    public basePaint p;
    public Color color;
    public String spriteId;
    public boolean isHind;
    public Map<String,String> spriteValue;

    public abstract void init();
    public abstract void loop();

    public sprite(int x, int y, File img, int wight, int height, String id){
        super();
        this.self = new selfBody(wight,height);
        this.position = new position(x,y);
        this.img = img;
        this.spriteId = id;
        this.isHind = false;
        this.spriteValue = new HashMap<>();
        runNewThread();
    }

    public sprite(int x, int y, basePaint p, Color color, int wight, int height, String id){
        super();
        this.self = new selfBody(wight,height);
        this.position = new position(x,y);
        this.img = null;
        this.p = p;
        this.color = color;
        this.spriteId = id;
        this.isHind = false;
        this.spriteValue = new HashMap<>();
        runNewThread();
    }

    public void runNewThread(){
        init();
//        new Thread(
//                () -> {
//                    while (true){
//                        loop();
//                    }
//                }
//        ).start();
    }
}
