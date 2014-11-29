/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import bitbit.BitBit;
import static bitbit.PicTerm.hexToInt;
import static bitbit.PicTerm.intToHex;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author adam
 */
public class BmpTest {

    public BmpTest() {
    }

    @Test
    //test if content is set right
    public void colorTest() {
        /*        int[][] bitmap = null;
        
        String temp = null;
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        temp = "/home/adam/Desktop/HomeMade.bmp";
        try {
        bitmap = new BitBit().seeBMPImage(temp);
        } catch (IOException ex) {
        Logger.getLogger(BmpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        BitBit.displayArray2D(bitmap, 4, 4);
        Assert.assertEquals(intToHex(bitmap[1][1]), intToHex(-1));*/
        System.out.println(intToHex(-1));
        Assert.assertEquals("ffffffff", intToHex(-1));

    }

}
