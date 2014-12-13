/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbit;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class ColorTableUnifier {
    
    public ColorTable unify(List<ColorTable> tables) throws AWTException {
        ColorTable result = new ColorTable();
        for (ColorTable table : tables) {
            for (BmpColor color : table) {
                result.addColor(color);
            }
        }
        return result;
    }
    
    public static void main(String[] args) {
 
        try {
            Bitmap bmp1 = new Bitmap(bitbit.BitBit.defaultFileIn);
            Bitmap bmp2 = new Bitmap(bitbit.BitBit.defaultFileIn);
            
            List<ColorTable> tables = new ArrayList<>();
            tables.add(bmp1.getColorTable());
            tables.add(bmp2.getColorTable());
            
            ColorTable unified = new ColorTableUnifier().unify(tables);
            
            bmp1.replaceColorTable(unified);
            bmp2.replaceColorTable(unified);
            
            bmp1.exportBitmap("/home/adam/test3.bmp");
            bmp2.exportBitmap("/home/adam/test4.bmp");
            
        } catch (Exception ex) {
            Logger.getLogger(ColorTableUnifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
