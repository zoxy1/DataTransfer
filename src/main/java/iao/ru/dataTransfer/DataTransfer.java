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
import java.util.ArrayList;

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
    private ArrayList<JRadioButtonMenuItem> jRadioButtonSpeedMenuItems = new ArrayList<JRadioButtonMenuItem>();

    void init() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Data Transfer");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("110"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("300"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("600"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("1200"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("4800"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("9600"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("14400"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("1920"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("38400"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("57600"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("115200"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("128000"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("256000"));

                jRadioButtonSpeedMenuItems.get(10).setSelected(true);

                ButtonGroup buttonGroupSpeed = new ButtonGroup();

                for (JRadioButtonMenuItem speedItem : jRadioButtonSpeedMenuItems) {
                    speedItem.setFont(font);
                    buttonGroupSpeed.add(speedItem);
                    speedMenu.add(speedItem);
                    speedItem.addActionListener(new BaundRateActionListener());
                }

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
            if (serialPortOpen.isOpened()) {
                try {
                    serialPortOpen.writeString(fieldInText.getText());
                } catch (SerialPortException e1) {
                    e1.printStackTrace();
                    comPortExeption.setText(e1.getExceptionType());
                }
            } else {
                comPortExeption.setText("Don`t send " + serialPortOpen.getPortName() + " is closed");
            }
            System.out.println(fieldInText.getText());
        }
    }

    public class ActionListenerSelectComPort implements ActionListener {

        private String portName;

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
                if (!(comPortName.equals(serialPortOpen.getPortName()) && serialPortOpen.isOpened())) {
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
                    } catch (SerialPortException ex1) {
                        System.out.println(ex1);
                        comPortExeption.setText(ex1.getExceptionType());
                    }
                } else {
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

                    if (!serialPortOpen.isOpened()) {
                        comPortExeption.setText(serialPortOpen.getPortName() + " is closed");
                    }
                } catch (SerialPortException ex2) {
                    System.out.println(ex2);
                    comPortExeption.setText(ex2.getExceptionType());
                }

            } else {
                comPortExeption.setText("Com port don`t selected");
                System.out.println("Com port don`t selected");
            }
        }
    }

    public class BaundRateActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            for (JRadioButtonMenuItem speedItem : jRadioButtonSpeedMenuItems) {

                if (speedItem.isSelected()) {
                    portSpeed = Integer.parseInt(speedItem.getText());
                    comPortExeption.setText("Select " + serialPortOpen.getPortName() + " speed = " + portSpeed);
                }
            }
        }
    }
}





