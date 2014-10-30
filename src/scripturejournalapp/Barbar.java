/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripturejournalapp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author adam
 */
public class Barbar implements Runnable {

    //private vars

    private Integer delayInMili = 1000;
    private int currNum = 0;
    private int maxCount = 100;

    DoubleProperty processProperty;

    public Barbar(Integer delayMili, int max) {
        this.delayInMili = delayMili;
        this.maxCount = max;
        processProperty = new SimpleDoubleProperty(this.currNum);
    }

    @Override
    public void run() {
        for (int i = 0; i < this.maxCount; i++) {
            try {
                Thread.sleep(this.delayInMili);
            } catch (InterruptedException ex) {
                Logger.getLogger(Barbar.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.currNum = i;
            processProperty.set((double) (this.currNum) / (double) this.maxCount);
            System.out.println("counting: " + this.currNum);
        }
    }

    public static void main(String[] args) {

    }
}
