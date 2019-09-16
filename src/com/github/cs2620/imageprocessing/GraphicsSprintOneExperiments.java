package com.github.cs2620.imageprocessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GraphicsSprintOneExperiments {
    public static void main(String args[]){
        try {
            BufferedImage img = ImageIO.read(new File("00.jpg"));
            BufferedImage colors = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
            BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
            Map<Integer, Long> counts = new HashMap<>();
            for (int y = 0; y < img.getHeight(); y++){
                for (int x = 0; x < img.getWidth(); x++){
                    int px = img.getRGB(x,y);
                    if (counts.get(px) == null){
                        counts.put(px, (long)1);
                    } else {
                        counts.replace(px, counts.get(px) + 1);
                    }
                }
            }
            int h = img.getHeight();
            int w = img.getWidth();
            List<Map.Entry<Integer, Long>> list = new LinkedList<Map.Entry<Integer, Long>>(counts.entrySet());
            List<Map.Entry<Integer, Long>> filtered = list.stream().filter(l -> l.getValue() > Math.min(h, w)).collect(Collectors.toList());
            Collections.sort(filtered, new Comparator<Map.Entry<Integer, Long>>(){public int compare(Map.Entry<Integer, Long> o1, Map.Entry<Integer, Long> o2){return o1.getValue().compareTo(o2.getValue());}});
            System.out.println(list.toString());
            System.out.println(list.size());
            for (int y = 0; y < img.getHeight(); y++){
                for (int x = 0; x < img.getWidth(); x++){
                    int px = img.getRGB(x, y);
                    int updated = findNearestMatch(px, filtered);
                    out.setRGB(x, y, updated);
                }
            }
            ImageIO.write(out, "PNG", new File("01.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    static boolean compareByHue(int px1, int px2){
        Color clr1 = new Color(px1);
        int px1r = clr1.getRed();
        int px1g = clr1.getGreen();
        int px1b = clr1.getBlue();
        float[] hsb1 = new float[3];
        Color.RGBtoHSB(px1r, px1g, px1b, hsb1);

        Color clr2 = new Color(px2);
        int px2r = clr2.getRed();
        int px2g = clr2.getGreen();
        int px2b = clr2.getBlue();
        float[] hsb2 = new float[3];
        Color.RGBtoHSB(px2r, px2g, px2b, hsb2);

        return (Math.abs(hsb1[0] - hsb2[0]) > .1);
    }

    static boolean compareByHSB(int prev, int px, int next){
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


        return ((difH1 > .2 || difS1 > .2 || difB1 > .2) && (difH2 < .2 && difS2 < .2 && difB2 < .2));
    }

    static int findNearestMatch(int px, List<Map.Entry<Integer, Long>> colors){
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

    static int toEightBit(int px){
        Color clr = new Color(px);
        int r = clr.getRed();
        int g = clr.getGreen();
        int b = clr.getBlue();

        clr = new Color((r/32)*32, (g/32)*32, (b/64)*64);
        return clr.getRGB();
    }
}
