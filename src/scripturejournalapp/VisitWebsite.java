/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scripturejournalapp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author adam
 */
public class VisitWebsite implements Runnable {

    String url = "http://www.github.com/zvakanaka";

    public VisitWebsite(String webSite) {
        this.url = webSite;
    }

    @Override//run can't throw an exception
    public void run() {
        try {
            visit(this.url);
        } catch (InterruptedException ex) {
            Logger.getLogger(VisitWebsite.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VisitWebsite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void visit(String site) throws InterruptedException, IOException {

        java.awt.Desktop.getDesktop().browse(java.net.URI.create(site));
        Thread.sleep(500);//miliseconds

        System.out.println("Loading " + site + " in default browser.");

    }  
}
