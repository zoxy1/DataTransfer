package iao.ru.dataTransfer;

import javax.swing.*;
import java.awt.*;
public class ImagePanel extends JPanel {
    private Image image;
    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {

            int imgWidth, imgHeight;
            double contRatio = (double) getWidth() / (double) getHeight();
            double imgRatio =  (double) image.getWidth(this) / (double) image.getHeight(this);

            //width limited
            if(contRatio < imgRatio){
                imgWidth = getWidth();
                imgHeight = (int) (getWidth() / imgRatio);

                //height limited
            }else{
                imgWidth = (int) (getHeight() * imgRatio);
                imgHeight = getHeight();
            }

            //to center
            int x = (int) (((double) getWidth() / 2) - ((double) imgWidth / 2));
            int y = (int) (((double) getHeight()/ 2) - ((double) imgHeight / 2));

            g.drawImage(image, x, y, imgWidth, imgHeight, this);
        }

    }
}
