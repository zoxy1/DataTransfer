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

        //FileReader reader = null;
        FileInputStream fis = new FileInputStream(fileText);
        ArrayList<Byte> arrayByte = new ArrayList<Byte>();
        int byteRead;
        while ((byteRead = fis.read()) != -1) {
            arrayByte.add((byte)byteRead);
        }
        long countChar32 = 0;
        long countChar = 0;
        long sizeFile = fileText.length();
        for (byte byteTransfer : arrayByte) {
            if (countChar32 > 31) {
                countChar32 = 0;
                Thread.sleep(100);
            }
            serialPortOpen.writeByte(byteTransfer);
            System.out.println(byteTransfer);
            countChar32++;
            //publish((int)((countChar*100)/sizeFile));
            setProgress((int) ((countChar * 100) / sizeFile));
            countChar++;
        }








        //InputStreamReader isr = new InputStreamReader(fis, "Windows-1251");
        //System.out.println(isr.getEncoding());

        //String stringBuffer = new String();
        //int countChar64 = 0;
        //int byteRead;
        //long countChar = 0;
        //while((byteRead = isr.read())!=-1){
            //stringBuffer=stringBuffer + (char)byteRead;
            //System.out.println((char)byteRead);
            /*if(countChar64 > 31){
                countChar64 = 0;
                Thread.sleep(100);
            }
            serialPortOpen.writeInt(byteRead);
            System.out.println(byteRead);
            countChar64++;
            //publish((int)((countChar*100)/sizeFile));
            setProgress((int)((countChar*100)/sizeFile));
            countChar++;*/
        //}

        //System.out.println(stringBuffer);


       // Charset cset = Charset.forName("Windows-1251");
        //ByteBuffer buf = cset.encode(stringBuffer);
        //byte[] b = buf.array();
        //String str = new String(b);
        //System.out.println(str);
        //String stringDecode = new String(stringBuffer.getBytes("Windows-1251"),"UTF-8");
        //System.out.println(stringDecode);
        //BufferedReader br = new BufferedReader(isr);
        /*String line;
        StringBuffer stringBuffer = new StringBuffer();
        while((line = br.readLine()) != null){
            System.out.println(line);
            stringBuffer.append(line);
            //System.out.println(new String(line.getBytes("UTF-16"),"Windows-1251"));
        }
        System.out.println(stringBuffer.toString());
        br.close();*/



        /*while((byteRead = isr.read())!=-1){
            if(countChar64 > 63){
                countChar64 = 0;
                Thread.sleep(100);
            }
            serialPortOpen.writeInt(byteRead);
            System.out.println(byteRead);
            countChar64++;
            //publish((int)((countChar*100)/sizeFile));
            setProgress((int)((countChar*100)/sizeFile));
            countChar++;
        }*/





       /* try {
                reader = new FileReader(fileText);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }

            System.out.print(sizeFile);
            try {
                while((byteRead = reader.read())!=-1){
                    if(countChar64 > 63){
                        countChar64 = 0;
                        Thread.sleep(1000);
                    }
                    serialPortOpen.writeInt(byteRead);
                    System.out.println(byteRead);
                    countChar64++;
                    //publish((int)((countChar*100)/sizeFile));
                    setProgress((int)((countChar*100)/sizeFile));
                    countChar++;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }*/
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
