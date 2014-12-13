package bitbit;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Jonny
 * @author David
 */
public class Bitmap {
    String bfName;
    boolean windowsStyle;
    ColorTable colorTable = new ColorTable();
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

    
    /**
     * Constructs a Bitmap object, extracts bmp image data from a given file,
     *   stores it in the object
     * @param filename
     * @throws AWTException if an error occurs
     */
    public Bitmap(String filename) throws AWTException {
        bfName = filename;
        bfType = new byte[2];       
        
        try
        {
            DataInputStream is = new DataInputStream( new BufferedInputStream(new FileInputStream(filename)) );
            extractFileHeader(is);
            extractBitmapHeader(is);
            extractImageData(is);
        }
        catch (IOException ioe )
        {
            throw new AWTException(ioe.toString());
        }
        
    }

    /**
     * A private method for extracting little endian
     * quantities from a input stream.
     * @param is contains the input stream
     * @param len is the number of bytes in the quantity
     * @returns the result as an integer
     */
    private int pullVal(DataInputStream is, int len)
        throws IOException
    {
        int value = 0;
        int temp = 0;

        for ( int x = 0; x < len; x++ )
        {
            temp = is.readUnsignedByte();
            value += (temp << (x * 8));
        }
        return value;
    }

    /**
     * A private method for extracting the file header
     * portion of a BMP file.
     * @param is contains the input stream
     */
    private void extractFileHeader(DataInputStream is)
        throws IOException, AWTException
    {
        is.read(bfType);
        if ( bfType[0] != 'B' || bfType[1] != 'M' )
            throw new AWTException("Not BMP format");
        bfSize = pullVal(is, 4);
        is.skipBytes(4);
        bfOffset = pullVal(is, 4);
    }

    /**
     * A private method for extracting the color table from
     * a BMP type file.
     * @param is contains the input stream
     * @param numColors contains the biClrUsed (for Windows) or zero
     */
        private void extractColorMap(DataInputStream is, int numColors)
        throws IOException, AWTException
    {
        int blues[], reds[], greens[];

        // if passed count is zero, then determine the
        // number of entries from bits per pixel.
        if ( numColors == 0 )
        {
            switch ( biBitCount )
            {
            case 1:  numColors =   2; break;
            case 4:  numColors =  16; break;
            case 8:  numColors = 256; break;
            default: numColors =  -1; break;
            }
        }
        if ( numColors == -1)
            throw new AWTException("Invalid bits per pixel: " + biBitCount);
        else
        {
            reds = new int[numColors];
            blues = new int[numColors];
            greens = new int[numColors];
            
            for ( int x = 0; x < numColors; x++ )
            {
                blues[x] = is.readUnsignedByte();
                greens[x] = is.readUnsignedByte();
                reds[x] = is.readUnsignedByte();
                if ( windowsStyle )
                    is.skipBytes(1);
     
                colorTable.addColor(new BmpColor(reds[x], greens[x], blues[x]));
            }
        }
    }

    /**
     * A private method for extracting an OS/2 style
     * bitmap header.
     * @param is contains the input stream
     */
    private void extractOS2Style(DataInputStream is)
        throws IOException, AWTException
    {
        windowsStyle = false;
        biWidth = pullVal(is, 2);
        biHeight = pullVal(is, 2);
        biPlanes = pullVal(is, 2);
        biBitCount = pullVal(is, 2);
        extractColorMap(is, 0);
    }

    /**
     * A private method for extracting a Windows style
     * bitmap header.
     * @param is contains the input stream
     */
    private void extractWindowsStyle(DataInputStream is)
        throws IOException, AWTException
    {
        windowsStyle = true;
        biWidth = pullVal(is, 4);
        biHeight = pullVal(is, 4);
        biPlanes = pullVal(is, 2);
        biBitCount = pullVal(is, 2);
        biCompression = pullVal(is, 4);
        biSizeImage = pullVal(is, 4);
        biXPelsPerMeter = pullVal(is, 4);
        biYPelsPerMeter = pullVal(is, 4);
        biClrUsed = pullVal(is, 4);
        biClrImportant = pullVal(is, 4);
        extractColorMap(is, biClrUsed);
    }

    /**
     * A private method for extracting the bitmap header.
     * This method determines the header type (OS/2 or Windows)
     * and calls the appropriate routine.
     * @param is contains the input stream
     */
    private void extractBitmapHeader(DataInputStream is)
        throws IOException, AWTException
    {
        biSize = pullVal(is, 4);
        if ( biSize == 12 )
            extractOS2Style(is);
        else
            extractWindowsStyle(is);
    }

    /**
     * A private method for extracting 4 bit per pixel
     * image data.
     * @param is contains the input stream
     */
    private void extract4BitData( DataInputStream is )
        throws IOException
    {
        int index, temp = 0;

        if ( biCompression == 0 )
        {
            int padding = 0;
            int overage = ((biWidth + 1)/ 2) % 4;
            if ( overage != 0 )
                padding = 4 - overage;
            pix = new int[biHeight * biWidth];
            for ( int y = biHeight - 1; y >= 0; y-- )
            {
                index = y * biWidth;
                for ( int x = 0; x < biWidth; x++ )
                {
                    // if on an even byte, read new 8 bit quantity
                    // use low nibble of previous read for odd bytes
                    if ( (x % 2) == 0 )
                    {
                        temp = is.readUnsignedByte();
                        pix[index++] = temp >> 4;
                    }
                    else
                        pix[index++] = temp & 0x0f;
                }
                if ( padding != 0 ) is.skipBytes(padding);
            }
        }
        else
        {
            throw new IOException("Compressed images not supported");
        }
    }

    /**
     * A private method for extracting 8 bit per pixel
     * image data.
     * @param is contains the input stream
     */
    private void extract8BitData( DataInputStream is )
        throws IOException
    {
        int index;

        if ( biCompression == 0 )
        {
            int padding = 0;
            int overage = biWidth % 4;
            if ( overage != 0 )
                padding = 4 - overage;
            pix = new int[biHeight * biWidth];
            for ( int y = biHeight - 1; y >= 0; y-- )
            {
                index = y * biWidth;
                for ( int x = 0; x < biWidth; x++ )
                {
                    pix[index++] = is.readUnsignedByte();
                }
                if ( padding != 0 ) is.skipBytes(padding);
            }
        }
        else
        {
            throw new IOException("Compressed images not supported");
        }
    }

    /**
     * A private method for extracting the image data from
     * a input stream.
     * @param is contains the input stream
     */
    private void extractImageData( DataInputStream is )
        throws IOException, AWTException
    {
        switch ( biBitCount )
        {
        case 1:
            throw new AWTException("Unhandled bits/pixel: " + biBitCount);
        case 4:
            extract4BitData(is); 
            break;
        case 8:  
            extract8BitData(is); 
            break;
        case 24:
            throw new AWTException("24-bit is Unhandled bits/pixel: " + biBitCount);
        default:
            throw new AWTException("Invalid bits per pixel: " + biBitCount);
        }
    }

    /**
     * getter for the ColorTable
     * @return this bitmap's color table
     */
    public ColorTable getColorTable() {
        return colorTable;
    }
    
    /**
     * Exports this bitmap's current data to a Windows-style bitmap file
     * @param filename 
     * @throws java.io.IOException 
     */
    public void exportBitmap(String filename) throws IOException {
        DataOutputStream out = new DataOutputStream(new FileOutputStream(filename));
        writeFileHeader(out);
        writeBitmapHeader(out);
        writeColorTable(out);
        writeImageData(out);
    }

    /**
     * Private method to write a bitmap file header
     * @param out Data output stream
     */
    private void writeFileHeader(DataOutputStream out) throws IOException {
        //Bitmap signature
        out.writeByte(0x42);
        out.writeByte(0x4D);
        //File size
        writeValLE(out, 4, bfSize);
        //reserved
        out.writeInt(0);
        //offset to pixel data
        writeValLE(out, 4, bfOffset);
    }

    /**
     * Private method to write this bmp's DIB bitmap header to an output stream.
     * Writes Windows-style
     * @param out 
     */
    private void writeBitmapHeader(DataOutputStream out) throws IOException {
        writeValLE(out, 4, biSize); //bitmap header size
        writeValLE(out, 4, biWidth);
        writeValLE(out, 4, biHeight);
        writeValLE(out, 2, biPlanes);
        writeValLE(out, 2, biBitCount); //bits per pixel
        writeValLE(out, 4, biCompression);
        writeValLE(out, 4, biSizeImage); //image size
        writeValLE(out, 4, biXPelsPerMeter);
        writeValLE(out, 4, biYPelsPerMeter);
        writeValLE(out, 4, biClrUsed);
        writeValLE(out, 4, biClrImportant);
    }

    /**
     * Private method to write this bitmap's color table
     * to an output stream
     * @param out 
     */
    private void writeColorTable(DataOutputStream out) throws IOException {
        for (BmpColor color : colorTable) {
            writeColor(out, color);
        }
    }

    /**
     * Private method for writing this bmp's image data to an output stream
     * @param out 
     */
    private void writeImageData(DataOutputStream out) throws IOException {
        int index;
        int padding = 0;
        int overage = biWidth % 4;
        if ( overage != 0 )
            padding = 4 - overage;
        for ( int y = biHeight - 1; y >= 0; y-- )
        {
            index = y * biWidth;
            for ( int x = 0; x < biWidth; x++ )
            {
                out.writeByte(pix[index++]);
            }
            if ( padding != 0 ) {
                for ( int i = 0; i < padding; i++) {
                    out.writeByte(0);
                }
            }
        }
        
    }
    
    /**
     * A private method for writing little endian quantities to an output stream
     * @param out
     * @param len
     * @param value 
     */
    private void writeValLE(DataOutputStream out, int len, int value) throws IOException {
        for (int x = 0; x < len; x++) {
            out.writeByte(value >> (x * 8) & 0xFF);
        }
    }
    
    /**
     * A private method for writing a Windows-style RGB color to an outputstream
     * @param out 
     */
    private void writeColor(DataOutputStream out, BmpColor color) throws IOException {
        out.writeByte(color.getBlue());
        out.writeByte(color.getGreen());
        out.writeByte(color.getRed());
        out.writeByte(0);
    }
    
    /**
     * Replaces this bitmap's color table with another table,
     * changing the pixel data to match the new color table.
     * Precondition:
     *   The new table must contain at least the colors that are currently
     *   contained in this bitmap's color table
     * @param newTable 
     * @throws AWTException if the new table doesn't contain all the colors
     *                      contained in the current table
     */
    public void replaceColorTable(ColorTable newTable) throws AWTException {
        boolean[] changedPixels = new boolean[pix.length];
        for (BmpColor color : colorTable) {
            int indexInNewTable = newTable.getIndex(color);
            int indexInThisTable = colorTable.getIndex(color);
            if (indexInNewTable != -1) {
                if (indexInThisTable != indexInNewTable) {
                    for (int i = 0; i < pix.length; i++) {
                        if (!changedPixels[i] && pix[i] == indexInThisTable) {
                            pix[i] = indexInNewTable;
//                            System.out.println("Swapping pixel " + i + "(" + changedPixels[i] + "): " + 
//                                    indexInThisTable + " to " + indexInNewTable);
                            changedPixels[i] = true;
                        }
                    }
                }
            }
            else {
                throw new AWTException("Colors in new table do not match the old");
            }
        }
        
        if (colorTable.getNumColors() != newTable.getNumColors()) {
            int diffNumColors = newTable.getNumColors() - colorTable.getNumColors();
            bfSize += diffNumColors * 4;
            bfOffset += diffNumColors * 4;
            biClrUsed += diffNumColors;
        }
            colorTable = newTable;
    }
    
    @Override
    public String toString() {
        return bfName;
    }
    /**
     * Describe the image as a string
     * @return string representation of image
     */
public String debug() {
        StringBuilder buf = new StringBuilder("");
        buf.append("       name: " + bfName + "\n");
        buf.append("       size: " + bfSize + "\n");
        buf.append(" img offset: " + bfOffset + "\n");
        buf.append("header size: " + biSize + "\n");
        buf.append("      width: " + biWidth + "\n");
        buf.append("     height: " + biHeight + "\n");
        buf.append(" clr planes: " + biPlanes + "\n");
        buf.append(" bits/pixel: " + biBitCount + "\n");
        if ( windowsStyle )
        {
            buf.append("compression: " + biCompression + "\n");
            buf.append(" image size: " + biSizeImage + "\n");
            buf.append("Xpels/meter: " + biXPelsPerMeter + "\n");
            buf.append("Ypels/meter: " + biYPelsPerMeter + "\n");
            buf.append("colors used: " + biClrUsed + "\n");
            buf.append("primary clr: " + biClrImportant + "\n");
        }
        
        buf.append("Color Table" + "\n" + colorTable.toString() + "\n");
        buf.append("Pixel data:\n");
        for (int y = 0; y < biHeight; y++) {
            int index = y * biWidth;
            for (int x = 0; x < biWidth; x++) {
                buf.append(pix[index++] + " ");
            }
            buf.append("\n");
        }
        
        return buf.toString();
        
    }
    
    public static void main( String args[] )
    {
        try
        {
            String fileInput;
            if (args.length < 1) {
               fileInput = "C:/Users/David/Desktop/test.bmp";
            }
            else {
                 fileInput = args[0];
            }
            
            Bitmap im = new Bitmap(fileInput);
            System.out.println("Before swap:\n" + im);
            System.out.println();
            
            System.out.println("Swapping colors");
            ColorTable clrTable = new ColorTable();
            for (BmpColor clr : im.getColorTable()) {
                clrTable.addColor(clr);
            }
            clrTable.swapColors(0, 1);
            clrTable.addColor(new BmpColor(0,0,0));
            im.replaceColorTable(clrTable);
            
            System.out.println("After swap:\n" + im);
            System.out.println();
            
            System.out.println("Writing to a file...");
            im.exportBitmap("C:/Users/David/Desktop/test2.bmp");
            System.out.println("Success");
        }
        catch ( Exception e )
        {
            System.out.println(e);
        }
    }

    
}