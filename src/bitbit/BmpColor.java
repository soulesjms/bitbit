/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbit;

/**
 *
 * @author David
 */
public class BmpColor implements Cloneable {
    
    private int red;
    private int green;
    private int blue;
    
    public BmpColor(int r, int g, int b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(byte blue) {
        this.blue = blue;
    }
    
    @Override
    public String toString() {
        return red + " " + green + " " + blue;
    }
    
    public boolean equals(BmpColor clr) {
        return this.getRed() == clr.getRed() && this.getGreen() == clr.getGreen()
                && this.getBlue() == clr.getBlue();
    }
    
    @Override
    public BmpColor clone() throws CloneNotSupportedException {
        return (BmpColor) super.clone();
    }
}
