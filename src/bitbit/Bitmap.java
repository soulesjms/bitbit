package bitbit;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.image.*;

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
        case 4:  extract4BitData(is); 
        System.out.println("8-bit Image"); break;
        case 8:  extract8BitData(is); 
        System.out.println("8-bit Image"); break;
        case 24:
            throw new AWTException("24-bit is Unhandled bits/pixel: " + biBitCount);
        default:
            throw new AWTException("Invalid bits per pixel: " + biBitCount);
        }
    }

    public ColorTable getColorTable() {
        return colorTable;
    }
    
    /**
     * Describe the image as a string
     * @return string representation of image
     */
    @Override
    public String toString()
    {
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
            System.out.println("Output:\n" + im);
        }
        catch ( Exception e )
        {
            System.out.println(e);
        }
    }
}
