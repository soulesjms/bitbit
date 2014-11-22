package bitbit;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.image.*;

/**
 *
 * @author Jonny
 */
public class Bitmap {
    String bfName;
    boolean imageProcessed;
    boolean windowsStyle;
    ColorModel colorModel = null;
    int pix[];

    byte bfType[];
    int bfSize;
    int bfOffset;
    int biSize;
    int biWidth;
    int biHeight;
    int biPlanes;
    int biBitCount;
    int biCompression;
    int biSizeImage;
    int biXPelsPerMeter;
    int biYPelsPerMeter;
    int biClrUsed;
    int biClrImportant;

    public Bitmap(String name) {
        bfName = name;
        bfType = new byte[2];
        imageProcessed = false;
    }
    
    private void extractFileHeader(DataInputStream is) throws IOException, AWTException {
        is.read(bfType);
        if ( bfType[0] != 'B' || bfType[1] != 'M' )
            throw new AWTException("Not BMP format");
        bfSize = pullVal(is, 4);
        is.skipBytes(4);
        bfOffset = pullVal(is, 4);
    }
    
    private int pullVal(DataInputStream is, int len) throws IOException {
        int value = 0;
        int temp = 0;

        for ( int x = 0; x < len; x++ )
        {
            temp = is.readUnsignedByte();
            value += (temp << (x * 8));
        }
        return value;
    }
    
    public static void main( String args[] )
    {
        try
        {
            String fileInput;
            if (args.length < 1) {
               fileInput = "/home/adam/Desktop/fun.bmp";
            }
            else {
                 fileInput = args[0];
            }
            FileInputStream inFile = new FileInputStream(fileInput);
            DataInputStream is = new DataInputStream( new BufferedInputStream(inFile) );
            
            BmpImage im = new BmpImage(fileInput);
            ImageProducer img = im.extractImage(is);
            System.out.println("Output:\n" + im);
        }
        catch ( Exception e )
        {
            System.out.println(e);
        }
    }
}
