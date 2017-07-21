package iao.ru.dataTransfer;

import java.awt.*;
import javax.swing.*;

import jssc.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;

/**
 * Created by Zoxy1 on 20.07.17.
 */
public class DataTransfer {
    private static final long serialVersion = 2347162171234712347L;
    private JTextField fieldInText = new JTextField(10);
    static String comPortName;
    private JButton startButton = new JButton("Start");
    private JButton openPortButton = new JButton("Open port");
    private JButton closePortButton = new JButton("Close port");
    private JLabel comPortExeption = new JLabel();
    SerialPort serialPortOpen = new SerialPort("COM1");
    void init() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Data Transfer");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setExtendedState(JFrame.NORMAL);
                Font font = new Font("Verdana", Font.PLAIN, 11);
                JMenuBar menuBar = new JMenuBar();
                JMenu selectComPortMenu = new JMenu("Select COM port");
                selectComPortMenu.setFont(font);
                String[] portNames = SerialPortList.getPortNames();
                JMenuItem comPortItems[] = new JMenuItem[10];

                for (int i = 0; i < portNames.length; i++) {
                    System.out.println(portNames[i]);
                    comPortItems[i] = new JMenuItem(portNames[i]);
                    comPortItems[i].setFont(font);
                    selectComPortMenu.add(comPortItems[i]);
                    comPortItems[i].addActionListener(new ActionListenerSelectComPort(portNames[i]));
                }

                menuBar.add(selectComPortMenu);
                frame.setJMenuBar(menuBar);
                frame.setPreferredSize(new Dimension(600, 300));
                frame.setLayout(new GridBagLayout());
                startButton.addActionListener(new StartButtonActionListener());
                openPortButton.addActionListener(new OpenPortActionListener());
                closePortButton.addActionListener(new ClosePortActionListener());
                frame.add(fieldInText);
                frame.add(startButton);
                frame.add(openPortButton);
                frame.add(closePortButton);
                frame.add(comPortExeption);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

    /*private static class PortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    //Получаем ответ от устройства, обрабатываем данные и т.д.
                    String data = serialPort.readString(event.getEventValue());
                    System.out.println(data);
                    //И снова отправляем запрос
                    serialPort.writeString("Get data");
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }*/


            }
        });
    }

    public class StartButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if(serialPortOpen.isOpened()){
                try {
                    serialPortOpen.writeString(fieldInText.getText());
                } catch (SerialPortException e1) {
                    e1.printStackTrace();
                    comPortExeption.setText(e1.getExceptionType());
                }
            }else {
                comPortExeption.setText("Don`t send " + serialPortOpen.getPortName()+" is closed");
            }
            System.out.println(fieldInText.getText());
        }
    }

    public class ActionListenerSelectComPort implements ActionListener {

        String portName;

        public ActionListenerSelectComPort(String portName) {
            this.portName = portName;
        }

        public void actionPerformed(ActionEvent e) {
            comPortName = portName;
            if (serialPortOpen.isOpened()) {
                try {
                    serialPortOpen.closePort();
                } catch (SerialPortException e1) {
                    e1.printStackTrace();
                    comPortExeption.setText(e1.getExceptionType());
                }
            }


            SerialPort serialPort = new SerialPort(comPortName);
            try {
                //Открываем порт
                serialPort.openPort();
                //Выставляем параметры
                serialPort.setParams(SerialPort.BAUDRATE_9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                if (serialPort.isOpened()) {
                    comPortExeption.setText(serialPort.getPortName() + " is open");
                    serialPortOpen = serialPort;
                }
            } catch (SerialPortException ex) {
                System.out.println(ex);
                comPortExeption.setText(ex.getExceptionType());
            }
            System.out.println(comPortName);
        }

    }

    public class OpenPortActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (comPortName != null) {
                if (!(comPortName == serialPortOpen.getPortName() && serialPortOpen.isOpened())) {
                    SerialPort serialPort = new SerialPort(comPortName);
                    try {
                        //Открываем порт
                        serialPort.openPort();
                        //Выставляем параметры
                        serialPort.setParams(SerialPort.BAUDRATE_9600,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                        if (serialPort.isOpened()) {
                            comPortExeption.setText(serialPort.getPortName() + " is open");
                            serialPortOpen = serialPort;
                        }
                    } catch (SerialPortException ex) {
                        System.out.println(ex);
                        comPortExeption.setText(ex.getExceptionType());
                    }
                }else{
                    comPortExeption.setText(serialPortOpen.getPortName() + " already open");
                }
            } else {
                comPortExeption.setText("Com port don`t selected");
                System.out.println("Com port don`t selected");
            }
        }
    }

    public class ClosePortActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (comPortName != null) {
                try {
                    serialPortOpen.closePort();

                    if(!serialPortOpen.isOpened()){
                        comPortExeption.setText(serialPortOpen.getPortName() + " is closed");
                    }
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                    comPortExeption.setText(ex.getExceptionType());
                }

            } else {
                comPortExeption.setText("Com port don`t selected");
                System.out.println("Com port don`t selected");
            }
        }
    }
}





