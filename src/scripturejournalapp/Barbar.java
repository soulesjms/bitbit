/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripturejournalapp;

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
    private int curNum = 0;
    private int maxCount = 100;

    DoubleProperty processProperty;

    public void setCurNum(int num) {
        this.curNum = num;
    }

    public int getCurNum() {
        return this.curNum;
    }

    public Barbar(Integer delayMili, int max) {
        this.delayInMili = delayMili;
        this.maxCount = max;
        processProperty = new SimpleDoubleProperty(this.curNum);
    }

    @Override
    public void run() {
        for (int i = 0; i <= this.maxCount; i++) {
            try {
                Thread.sleep(this.delayInMili);
            } catch (InterruptedException ex) {
                Logger.getLogger(Barbar.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.curNum = i;
            processProperty.set((double) (this.curNum) / (double) this.maxCount);
            System.out.println("counting: " + this.curNum);
        }
    }

    public static void main(String[] args) {

    }
}
