/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author David
 */
public class ColorTable implements Iterable<BmpColor> {
    
    private List<BmpColor> colors = new ArrayList<>();
    
    public void addColor(BmpColor color) {
        colors.add(color);
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
        return colors.indexOf(color);
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
        ct.addColor(new BmpColor(0, 0, 0));
        ct.addColor(new BmpColor(3, 3, 3));
        ct.addColor(new BmpColor(6, 6, 6));
        
        System.out.println("ColorTable:");
        System.out.println(ct);
        
        ct.swapColors(0, 1);
        
        System.out.println("ColorTable Swapped:");
        System.out.println(ct);
        
    }
    
}
