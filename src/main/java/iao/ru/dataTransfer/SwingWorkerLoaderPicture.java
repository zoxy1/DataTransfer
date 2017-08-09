package iao.ru.dataTransfer;

import jssc.SerialPort;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Zoxy1 on 09.08.17.
 */
public class SwingWorkerLoaderPicture extends SwingWorker<String, Integer> {

    private File file;
    /**
     * UI callback
     */
    private UICallback ui;
    private SerialPort serialPortOpen;
    private ImagePanel imagePanel;
    private final BufferedImage bufferedImage;
    /**
     * Creates data loader.
     *
     * @param ui     UI callback to use when publishing data and manipulating UI
     * //@param reader data source
     */
    public SwingWorkerLoaderPicture(UICallback ui, File file, SerialPort serialPortOpen, ImagePanel imagePanel, BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
        this.imagePanel = imagePanel;
        this.file = file;
        this.serialPortOpen = serialPortOpen;
        this.ui = ui;
        this.ui.startLoading();
    }

    /**
     * Background part of loader. This method is called in background thread. It reads data from data source and
     * places it to UI  by calling {@link SwingWorker#publish(Object[])}
     *
     * @return background execution result - all data loaded
     * @throws Exception if any error occures
     */
    @Override
    protected String doInBackground() throws Exception {
        BufferedImage scaleImage = new BufferedImage(imagePanel.getWidthRealViewImg(), imagePanel.getHeightRealViewImg(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = scaleImage.createGraphics();
        graphics.drawImage(bufferedImage, 0, 0, imagePanel.getWidthRealViewImg(), imagePanel.getHeightRealViewImg(), null);
        graphics.dispose();
        int height = scaleImage.getHeight();
        int width = scaleImage.getWidth();
        for(int i=0;i < height;i++ ) {
            for(int j=0;j < width;j++) {
                int rgba = scaleImage.getRGB(j, i);
                Color color = new Color(rgba, true);
                int r = color.getRed();
                System.out.print(r+" ");
            }
            System.out.println(" ");
        }
        //int g = color.getGreen();
        //int b = color.getBlue();
        //ArrayList<Byte> arrayByteLine = new ArrayList<Byte>();
        //ArrayList<ArrayList<Byte>> arrayByte = new ArrayList<ArrayList<Byte>>();


       /* int byteRead;
        scaleImage.
        while ((byteRead = fis.read()) != -1) {
            arrayByte.add((byte) byteRead);
        }
        long countByte32 = 0;
        long countByte = 0;
        long sizeFile = file.length();
        for (byte byteTransfer : arrayByte) {
            if (countByte32 > 31) {
                countByte32 = 0;
                Thread.sleep(100);
            }
            serialPortOpen.writeByte(byteTransfer);
            countByte32++;
            setProgress((int) ((countByte * 100) / sizeFile));
            countByte++;
        }*/
               /* int [] rgbMass = bufferedImageBMP.getRGB(0,0,imagePanel.getWidth(),imagePanel.getWidth(),null, 0, imagePanel.getWidth());
                int rgba = rgbMass[0];
                Color color = new Color(rgba, true);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                int length = rgbMass.length;*/
        Date currentData = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        System.out.println(format1.format(currentData));
        String path = "picture_" + format1.format(currentData) + ".bmp";
        try {
            File fileWrite = new File("Pictures is transmitted\\picture_" + format1.format(currentData) + ".bmp");
            fileWrite.mkdirs();
            ImageIO.write(scaleImage, "bmp", fileWrite);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //lineTextExeption.setText("Send picture...");

        return "";
    }

    /**
     * EDT part of loader. This method is called in EDT
     *
     * @param chunks data, that was passed to UI in {@link #doInBackground()} by calling
     *               {@link SwingWorker#publish(Object[])}
     */
    @Override
    protected void process(List<Integer> chunks) {
        /*for (Integer line : chunks) {
            ui.appendText(line + "\n");
        }*/

        ui.setProgress(getProgress());
    }

    /**
     * Cancels execution
     */
    public void cancel() {
        cancel(true);
    }

    /**
     * This method is called in EDT after {@link #doInBackground()} is finished.
     */
    @Override
    protected void done() {
        ui.stopLoading();
        ui.setText("File transmitted");
    }
}
