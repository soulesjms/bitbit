/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbit;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author adam
 */
public class LargeImageSetup implements ImageStrategy {

    @Override
    public Node setupImageView(Bitmap bmp) {
        ImageView imgView = new ImageView();
        Image image = new Image("file:" + bmp.getBfName());
        imgView.setImage(image);
        imgView.setFitWidth(400);
        imgView.setPreserveRatio(true);
        imgView.setSmooth(false);
        return imgView;
    }
    
}
