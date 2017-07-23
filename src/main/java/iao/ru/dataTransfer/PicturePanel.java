package iao.ru.dataTransfer;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class PicturePanel extends javax.swing.JPanel {
    // Храним 2 изображения: оригинальное и текущее.
    // Оригинальное используется для получения текущего в зависимости от размеров панели.
    // Текущее непосредственно прорисовывается на панели.
    private BufferedImage originalImage = null;
    private Image image = null;
    public PicturePanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(null);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
    }

    // Реакция на изменение размеров панели - изменение размера изображения.
    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        int w = this.getWidth();
        int h = this.getHeight();
        if ((originalImage != null) && (w > 0) && (h > 0)) {
            image = originalImage.getScaledInstance(w, h, Image.SCALE_DEFAULT);
            this.repaint();
        }
    }
    // Берем прорисовку в свои руки.
    public void paint(Graphics g) {
        // Рисуем картинку
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }

        // Рисуем подкомпоненты.
        super.paintChildren(g);
        // Рисуем рамку
        super.paintBorder(g);
    }

    // Методы для настройки картинки.
    public BufferedImage getImage() {
        return originalImage;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public void setImageFile(File imageFile) {
        try {
            if (imageFile == null) {
                originalImage = null;
            }
            BufferedImage bi = ImageIO.read(imageFile);
            originalImage = bi;
        } catch (IOException ex) {
            System.err.println("Неудалось загрузить картинку!");
            ex.printStackTrace();
        }
        repaint();
    }
}
