package iao.ru.dataTransfer;

import sun.security.util.SecurityConstants;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Background data loader based on {@link SwingWorker}.
 *
 * @author Eugene Matyushkin aka Skipy
 * @version $Id: SwingWorkerLoader.java 416 2010-07-26 14:22:39Z skipy_ru $
 * @since 12.07.2010
 */
public class SwingWorkerLoader extends SwingWorker<String, Integer> {


    File fileText;
    /**
     * UI callback
     */
    private UICallback ui;

    /**
     * Creates data loader.
     *
     * @param ui     UI callback to use when publishing data and manipulating UI
     * //@param reader data source
     */
    public SwingWorkerLoader(UICallback ui, File fileText) {
        this.ui = ui;
        this.fileText = fileText;
        //this.reader = reader;
        // this operation is safe because
        // 1. SwingWorkerLoader is created in EDT
        // 2. Anyway - UICallback is proxied by EDTInvocationHandler  
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

            FileReader reader = null;
            try {
                reader = new FileReader(fileText);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            int countChar64 = 0;
            long countChar = 0;
            long sizeFile = fileText.length();
            System.out.print(sizeFile);
            try {
                while((reader.read())!=-1){
                    if(countChar64 > 63){
                        countChar64 = 0;
                        Thread.sleep(1000);
                    }
                    countChar64++;
                    //publish((int)((countChar*100)/sizeFile));
                    setProgress((int)((countChar*100)/sizeFile));
                    System.out.println((int) ((countChar*100)/sizeFile));
                    countChar++;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
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
