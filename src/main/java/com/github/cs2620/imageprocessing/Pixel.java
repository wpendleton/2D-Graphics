/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cs2620.imageprocessing;

import java.awt.Color;


public class Pixel {

  static Pixel interpolate(int colorOne, int colorTwo, float f) {
    Pixel one  = new Pixel(colorOne);
    Pixel two = new Pixel(colorTwo);
    
    return Pixel.interpolate(one, two, f);
  }

  static Pixel interpolate(Pixel one, Pixel two, float f) {
   //Now interpolate between the two colors based on f.
    //If f is close to 0, we want more of colorOne. 
    //If f is close to 1, we want more of colorTwo
    
    int r = (int) (one.r * (1 - f) + two.r * f);
    int g = (int) (one.g * (1 - f) + two.g * f);
    int b = (int) (one.b * (1 - f) + two.b * f);
    
    return new Pixel(r, g, b);
  }

    private int r, g, b;
    
    public Pixel(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
        
        clip();
    }
    
    public Pixel(int i){  
        
        
        
        int r = (i >> 16) & 0xff;
        int g = (i >> 8) & 0xff;
        int b = i & 0xff;
        
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    private void clip(){
        if(this.r > 255) this.r = 255;
        if(this.g > 255) this.g = 255;
        if(this.b > 255) this.b = 255;
        
        
        if(this.r < 0) this.r = -this.r;
        if(this.g < 0) this.g = -this.g;
        if(this.b < 0) this.b = -this.b;
        
        
    }

    
    

    public Pixel toGrayscale() {
        int gray = (r + g + b) / 3;
        grayscale(gray);
        return this;

    }
    
    public int getSlice(int bit){
       int gray = (r + g + b) / 3;
        
      int o;
      int power = (int)(Math.pow(2,bit));
      if((power& gray) > 0){
        o = 255;
      }
      else{
        o = 0;
      }
      return o;
    }
    
    public Pixel slice(int bit){
     int o = getSlice(bit);
      grayscale(o);
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
    
    public void setValue(float value){
        
        float[] hsb = new float[3];
        Color.RGBtoHSB(r, g, b, hsb);
        
        float h = hsb[0];//All values 0-1
        float s = hsb[1];
        float v = value;
        
        
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
        int toReturn = ((0xff) << 24) + (r << 16) + (g << 8) + b;
        
        return toReturn;
    }

}
