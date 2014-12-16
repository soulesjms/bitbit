/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbit;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author David
 */
public class ColorTable implements Iterable<BmpColor> {
    
    private List<BmpColor> colors = new ArrayList<>();
    
    public final int MAX_COLORS = 256;
    
    /**
     * Adds a color to the color table. Will not add duplicates.
     * @param color 
     * @throws java.awt.AWTException - if colortable is full.
     */
    public void addColor(BmpColor color) throws AWTException {
        if (colors.size() >= MAX_COLORS) {
            throw new AWTException("Maximum of 256 colors exceeded");
        }
        Boolean isNewColor = true;
        for (BmpColor c : colors) {
            if (c.equals(color)) {
                isNewColor = false;
            }
        }
        if (isNewColor) {
            colors.add(color);
        }
    }
    
    public BmpColor getColor(int index) {
        return colors.get(index);
    }
    
    /**
     * 
     * @param color
     * @return index of the given color, or -1 if not found.
     */
    public int getIndex(BmpColor color) {
        int i = 0;
        for (BmpColor c: colors) {
            if (c.equals(color)) {
                return i;
            }
            i++;
        }
        //this was the only line here:
        return -1;
    }
    
    public int getNumColors() {
        return colors.size();
    }
    
    public void swapColors(int index1, int index2) {
        BmpColor temp = colors.get(index1);
        colors.set(index1, colors.get(index2));
        colors.set(index2, temp);
    }
    
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("");
        for (BmpColor color : colors) {
            buf.append(color.toString());
            buf.append("\n");
        }
        
        return buf.toString();
    }
    
    @Override
    public Iterator<BmpColor> iterator() {
        return colors.iterator();
    }
    
    public static void main(String[] args) {
        ColorTable ct = new ColorTable();
        
        try {
            ct.addColor(new BmpColor(0, 0, 0));
            ct.addColor(new BmpColor(3, 3, 3));
            ct.addColor(new BmpColor(3, 3, 3));
            ct.addColor(new BmpColor(6, 6, 6));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        
        System.out.println("ColorTable:");
        System.out.println(ct);
        
        ct.swapColors(0, 1);
        
        System.out.println("ColorTable Swapped:");
        System.out.println(ct);
        
    }
    
}
