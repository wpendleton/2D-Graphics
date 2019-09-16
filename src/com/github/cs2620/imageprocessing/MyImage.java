/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.cs2620.imageprocessing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

public class MyImage {

    BufferedImage bufferedImage;

    /**
     * Create a new image instance from the given file
     *
     * @param filename The file to load
     */
    public MyImage(String filename) {
        try {
            bufferedImage = ImageIO.read(new File(filename));
        } catch (IOException ex) {
            Logger.getLogger(MyImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public MyImage(BufferedImage img) {
        bufferedImage = img;
    }
    
    public MyImage(int w, int h){
        bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
    }

    /**
     * Run a pixel operation on each pixel in the image
     *
     * @param pixelInterface The pixel operation to run
     */
    public void all(PixelInterface pixelInterface) {

        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int color_int = bufferedImage.getRGB(x, y);

                Pixel p = new Pixel(color_int);

                pixelInterface.PixelMethod(p);

                bufferedImage.setRGB(x, y, p.getColor().getRGB());

            }
        }

    }

    /**
     * Save the file to the given location
     *
     * @param filename The location to save to
     */
    public void save(String filename) {

        try {
            ImageIO.write(bufferedImage, "PNG", new File(filename));
        } catch (IOException ex) {
            Logger.getLogger(MyImage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public InputStream getInputStream() throws IOException {
        
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            return is;
        
    }
    
    public int[] getGrayscaleHistogram(){
        int[] histogram = new int[256];
        
        
        //Bin each pixel in the histogram
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int color_int = bufferedImage.getRGB(x, y);

                Pixel p = new Pixel(color_int);

                int grayscale = (int) (p.getValue() * 255);
                histogram[grayscale]++;
               

            }
        }
        
        
        
        return histogram;
    }
    
    public MyImage getGrayscaleHistogramImage(){
        int[] histogram = getGrayscaleHistogram();
        //Find the biggest bin
        int max = 0;
        for(int h = 0; h < 256; h++){
            if(histogram[h] > max){
                max = histogram[h];
            }
        }
        
        System.out.println("The biggest histogram value is " + max);
        
        MyImage toReturn = new MyImage(256, 50);
        
        
        //Go across and create the histogram
        for(int x = 0; x < 256; x++){
            int localMax = histogram[x]*50/max;
            for(int y = 0; y < 50; y++){
                int localY = 50-y;
                
                if(localY < localMax)
                    toReturn.bufferedImage.setRGB(x, y, new Pixel(x, x,x).getColor().getRGB());
                else
                    toReturn.bufferedImage.setRGB(x, y, Color.BLACK.getRGB());
            }
        }
        return toReturn;
    }

    public MyImage edgeDetection(){
        BufferedImage edges = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < bufferedImage.getHeight(); y++){
            for (int x = 1; x < bufferedImage.getWidth() - 1; x++){
                int prev = bufferedImage.getRGB(x-1, y);
                int px = bufferedImage.getRGB(x, y);
                int next = bufferedImage.getRGB(x+1, y);
                if (compareByHSB(prev, px, next))
                {
                    edges.setRGB(x, y, Color.YELLOW.getRGB());
                }
                else
                {
                    edges.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return new MyImage(edges);
    }

    private boolean compareByHSB(int prev, int px, int next){
        Color clr1 = new Color(prev);
        int px1r = clr1.getRed();
        int px1g = clr1.getGreen();
        int px1b = clr1.getBlue();
        float[] hsb1 = new float[3];
        Color.RGBtoHSB(px1r, px1g, px1b, hsb1);

        Color clr2 = new Color(px);
        int px2r = clr2.getRed();
        int px2g = clr2.getGreen();
        int px2b = clr2.getBlue();
        float[] hsb2 = new float[3];
        Color.RGBtoHSB(px2r, px2g, px2b, hsb2);

        Color clr3 = new Color(next);
        int px3r = clr3.getRed();
        int px3g = clr3.getGreen();
        int px3b = clr3.getBlue();
        float[] hsb3 = new float[3];
        Color.RGBtoHSB(px3r, px3g, px3b, hsb3);

        float difH1 = Math.abs(hsb1[0] - hsb2[0]);
        float difS1 = Math.abs(hsb1[1] - hsb2[1]);
        float difB1 = Math.abs(hsb1[2] - hsb2[2]);

        float difH2 = Math.abs(hsb2[0] - hsb3[0]);
        float difS2 = Math.abs(hsb2[1] - hsb3[1]);
        float difB2 = Math.abs(hsb2[2] - hsb3[2]);


        return ((difH1 > .1 || difS1 > .1 || difB1 > .1) && (difH2 < .1 && difS2 < .1 && difB2 < .1));
    }

    public MyImage reduceColor(){
        Map<Integer, Long> counts = new HashMap<>();
        for (int y = 0; y < bufferedImage.getHeight(); y++){
            for (int x = 0; x < bufferedImage.getWidth(); x++){
                int px = bufferedImage.getRGB(x,y);
                if (counts.get(px) == null){
                    counts.put(px, (long)1);
                } else {
                    counts.replace(px, counts.get(px) + 1);
                }
            }
        }
        List<Map.Entry<Integer, Long>> list = new LinkedList<Map.Entry<Integer, Long>>(counts.entrySet());
        List<Map.Entry<Integer, Long>> filtered = list.stream().filter(l -> l.getValue() > Math.min(bufferedImage.getHeight(), bufferedImage.getWidth())).collect(Collectors.toList());
        Collections.sort(filtered, new Comparator<Map.Entry<Integer, Long>>(){public int compare(Map.Entry<Integer, Long> o1, Map.Entry<Integer, Long> o2){return o1.getValue().compareTo(o2.getValue());}});
        for (int y = 0; y < bufferedImage.getHeight(); y++){
            for (int x = 0; x < bufferedImage.getWidth(); x++){
                int px = bufferedImage.getRGB(x, y);
                int updated = findNearestMatch(px, filtered);
                bufferedImage.setRGB(x, y, updated);
            }
        }
        return this;
    }

    private int findNearestMatch(int px, List<Map.Entry<Integer, Long>> colors){
        int result = 0;
        float resultDif = 255;
        Color clr = new Color(px);
        int r = clr.getRed();
        int g = clr.getGreen();
        int b = clr.getBlue();
        for(Map.Entry<Integer, Long> e : colors){
            int candidate = e.getKey();
            Color candClr = new Color(candidate);
            int cr = candClr.getRed();
            int cg = candClr.getGreen();
            int cb = candClr.getBlue();
            float dif = (Math.abs(r - cr) + Math.abs(g - cg) + Math.abs(b - cb)) / 3;
            if (dif < resultDif){
                result = e.getKey();
                resultDif = dif;
            }
        }
        return result;
    }
}
