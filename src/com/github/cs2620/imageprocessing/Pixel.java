package com.github.cs2620.imageprocessing;

import java.awt.Color;


public class Pixel {

    private int r, g, b;
    
    public Pixel(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public Pixel(int i){  
        
        
        
        int r = (i >> 16) & 0xff;
        int g = (i >> 8) & 0xff;
        int b = i & 0xff;
        
        this.r = r;
        this.g = g;
        this.b = b;
    }

    
    

    public Pixel toGrayscale() {
        int gray = (r + g + b) / 3;
        grayscale(gray);
        return this;

    }
    
    public void lessSaturated(){
        
        float[] hsb = new float[3];
        Color.RGBtoHSB(r, g, b, hsb);
        
        float h = hsb[0];//All values 0-1
        float s = hsb[1];
        float v = hsb[2];
        
        s = .5f;
        
        int i = Color.HSBtoRGB(h, s, v);
        
        int r = (i >> 16) & 0xff;
        int g = (i >> 8) & 0xff;
        int b = i & 0xff;
        
        this.r = r;
        this.g = g;
        this.b = b;
        
        
    }
    
    public void moreSaturated(){
        
        float[] hsb = new float[3];
        Color.RGBtoHSB(r, g, b, hsb);
        
        float h = hsb[0];//All values 0-1
        float s = hsb[1];
        float v = hsb[2];
        
        s = 1;
        
        int i = Color.HSBtoRGB(h, s, v);
        
        int r = (i >> 16) & 0xff;
        int g = (i >> 8) & 0xff;
        int b = i & 0xff;
        
        this.r = r;
        this.g = g;
        this.b = b;
        
        
    }

    public void toGrayscaleRed() {
        grayscale(r);
    }

    public void toGrayscaleGreen() {
        grayscale(g);
    }

    public void toGrayscaleBlue() {
        grayscale(b);
    }

    public void toEightBit(){
        r -= r % 32;
        g -= g % 32;
        b -= b % 64;
    }

    private void grayscale(int i) {
        r = i;
        g = i;
        b = i;
    }

    public Color getColor() {
        return new Color(r, g, b);
    }
    
    public int getRed(){
        return r;
    }
    
    public int getGreen(){
        return g;
    }
    
    public int getBlue(){
        return b;
    }
    
    private float[] getHSB(){
        float[] hsb = new float[3];
        Color.RGBtoHSB(r, g, b, hsb);
        return hsb;
    }
    
    public float getHue(){
        float[] hsb = getHSB();
        return hsb[0];
    }
    
    public float getSaturation(){
        float[] hsb = getHSB();
        return hsb[1];
    }
    
    public float getValue(){
        float[] hsb = getHSB();
        return hsb[2];
    }
    
    public int getRGB(){
        int toReturn = (r << 16) + (g << 8) + b;
        return toReturn;
    }

}
