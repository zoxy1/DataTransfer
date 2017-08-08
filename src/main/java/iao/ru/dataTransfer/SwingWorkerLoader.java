package iao.ru.dataTransfer;

import jssc.SerialPort;
import sun.security.util.SecurityConstants;

import javax.swing.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
/**
 * Background data loader based on {@link SwingWorker}.
 *
 * @author Eugene Matyushkin aka Skipy
 * @version $Id: SwingWorkerLoader.java 416 2010-07-26 14:22:39Z skipy_ru $
 * @since 12.07.2010
 */
public class SwingWorkerLoader extends SwingWorker<String, Integer> {


    private File fileText;
    /**
     * UI callback
     */
    private UICallback ui;

    private SerialPort serialPortOpen;
    /**
     * Creates data loader.
     *
     * @param ui     UI callback to use when publishing data and manipulating UI
     * //@param reader data source
     */
    public SwingWorkerLoader(UICallback ui, File fileText, SerialPort serialPortOpen) {
        this.ui = ui;
        this.fileText = fileText;
        this.serialPortOpen = serialPortOpen;
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

        FileInputStream fis = new FileInputStream(fileText);
        ArrayList<Byte> arrayByte = new ArrayList<Byte>();
        int byteRead;
        while ((byteRead = fis.read()) != -1) {
            arrayByte.add((byte) byteRead);
        }
        long countByte32 = 0;
        long countByte = 0;
        long sizeFile = fileText.length();
        for (byte byteTransfer : arrayByte) {
            if (countByte32 > 31) {
                countByte32 = 0;
                Thread.sleep(100);
            }
            serialPortOpen.writeByte(byteTransfer);
            countByte32++;
            setProgress((int) ((countByte * 100) / sizeFile));
            countByte++;
        }
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
        ui.setText("File transferred");
    }
}
