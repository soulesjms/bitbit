package bitbit;


import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author adam
 */
public class SmallImageSetup implements ImageStrategy {
    
    @Override
    public Node setupImageView(Bitmap bmp) {
        FlowPane imgViewBlocks = new FlowPane();
                        
        //spacing between pixels
        int gap = 0;

        imgViewBlocks.setVgap(gap);
        imgViewBlocks.setHgap(gap);
        //displayWidth
        int dWidth = bmp.getBiWidth();
        int wrapLength = 400;
        //enlarge view of image if space is left over
        while (dWidth < (wrapLength-bmp.getBiWidth())) {
            dWidth += bmp.getBiWidth();
        }
        imgViewBlocks.setPrefWrapLength(dWidth+gap*(bmp.getBiWidth()+1));
        imgViewBlocks.setMinWidth(dWidth+gap*(bmp.getBiWidth()+1));

        for (int perPix : bmp.getPix()) {

            //traverse colorTable of image and dereferrence proper colors
            Color co = Color.rgb(bmp.getColorTable().getColor(perPix).getRed()
                    , bmp.getColorTable().getColor(perPix).getGreen()
                    , bmp.getColorTable().getColor(perPix).getBlue());
            final Rectangle r = new Rectangle(dWidth/bmp.getBiWidth(), dWidth/bmp.getBiWidth(), co);

            // Configure rectangle and add to the imgViewBlocks
            imgViewBlocks.getChildren().add(r);
        }
        return imgViewBlocks;
    }
}
