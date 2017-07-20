package iao.ru.dataTransfer;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Zoxy1 on 20.07.17.
 */
public class ActionListenerSelectComPort implements ActionListener {

    private String comPortName;
    public ActionListenerSelectComPort(String comPortName) {
        this.comPortName = comPortName;
    }

    public void actionPerformed(ActionEvent e) {
        DataTransfer.comPortName = comPortName;
        System.out.println(DataTransfer.comPortName);
    }

    public String getComPortName() {
        return comPortName;
    }

    public void setComPortName(String comPortName) {
        this.comPortName = comPortName;
    }
}
