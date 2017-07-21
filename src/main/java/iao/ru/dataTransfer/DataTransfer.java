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
    private int portSpeed = 115200;
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
                JMenu fileMenu = new JMenu("File");
                fileMenu.setFont(font);
                JMenuItem openMenuItem = new JMenuItem("Open");
                openMenuItem.setFont(font);
                JMenuItem exitMenuItem = new JMenuItem("Exit");
                exitMenuItem.setFont(font);
                exitMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        System.exit(0);
                    }

                });

                fileMenu.add(openMenuItem);
                fileMenu.addSeparator();
                fileMenu.add(exitMenuItem);
                menuBar.add(fileMenu);

                JMenu settingsMenu = new JMenu("Settings");
                settingsMenu.setFont(font);
                String[] portNames = SerialPortList.getPortNames();

                JMenu portMenu = new JMenu("Port");
                portMenu.setFont(font);
                settingsMenu.add(portMenu);

                JMenu speedMenu = new JMenu("Speed");
                speedMenu.setFont(font);


                JRadioButtonMenuItem BAUDRATE_110 = new JRadioButtonMenuItem("110");
                JRadioButtonMenuItem BAUDRATE_300 = new JRadioButtonMenuItem("300");
                JRadioButtonMenuItem BAUDRATE_600 = new JRadioButtonMenuItem("600");
                JRadioButtonMenuItem BAUDRATE_1200 = new JRadioButtonMenuItem("1200");
                JRadioButtonMenuItem BAUDRATE_4800 = new JRadioButtonMenuItem("4800");
                JRadioButtonMenuItem BAUDRATE_9600 = new JRadioButtonMenuItem("9600");
                JRadioButtonMenuItem BAUDRATE_14400 = new JRadioButtonMenuItem("14400");
                JRadioButtonMenuItem BAUDRATE_19200 = new JRadioButtonMenuItem("19200");
                JRadioButtonMenuItem BAUDRATE_38400 = new JRadioButtonMenuItem("38400");
                JRadioButtonMenuItem BAUDRATE_57600 = new JRadioButtonMenuItem("57600");
                JRadioButtonMenuItem BAUDRATE_115200 = new JRadioButtonMenuItem("115200");
                JRadioButtonMenuItem BAUDRATE_128000 = new JRadioButtonMenuItem("128000");
                JRadioButtonMenuItem BAUDRATE_256000 = new JRadioButtonMenuItem("256000");
                BAUDRATE_115200.setSelected(true);

                ButtonGroup buttonGroupSpeed = new ButtonGroup();
                buttonGroupSpeed.add(BAUDRATE_110);
                buttonGroupSpeed.add(BAUDRATE_300);
                buttonGroupSpeed.add(BAUDRATE_600);
                buttonGroupSpeed.add(BAUDRATE_1200);
                buttonGroupSpeed.add(BAUDRATE_4800);
                buttonGroupSpeed.add(BAUDRATE_9600);
                buttonGroupSpeed.add(BAUDRATE_14400);
                buttonGroupSpeed.add(BAUDRATE_19200);
                buttonGroupSpeed.add(BAUDRATE_38400);
                buttonGroupSpeed.add(BAUDRATE_57600);
                buttonGroupSpeed.add(BAUDRATE_115200);
                buttonGroupSpeed.add(BAUDRATE_128000);
                buttonGroupSpeed.add(BAUDRATE_256000);

                speedMenu.add(BAUDRATE_110);
                speedMenu.add(BAUDRATE_300);
                speedMenu.add(BAUDRATE_600);
                speedMenu.add(BAUDRATE_1200);
                speedMenu.add(BAUDRATE_4800);
                speedMenu.add(BAUDRATE_9600);
                speedMenu.add(BAUDRATE_14400);
                speedMenu.add(BAUDRATE_19200);
                speedMenu.add(BAUDRATE_38400);
                speedMenu.add(BAUDRATE_57600);
                speedMenu.add(BAUDRATE_115200);
                speedMenu.add(BAUDRATE_128000);
                speedMenu.add(BAUDRATE_256000);

                settingsMenu.add(speedMenu);

                JMenuItem comPortItems[] = new JMenuItem[10];

                for (int i = 0; i < portNames.length; i++) {
                    comPortItems[i] = new JMenuItem(portNames[i]);
                    comPortItems[i].setFont(font);
                    portMenu.add(comPortItems[i]);
                    comPortItems[i].addActionListener(new ActionListenerSelectComPort(portNames[i]));
                }

                menuBar.add(settingsMenu);
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
                serialPort.setParams(portSpeed,
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
                        serialPort.setParams(portSpeed,
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





