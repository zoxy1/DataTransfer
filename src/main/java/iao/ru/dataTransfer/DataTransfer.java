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
    //private static SerialPort serialPort;
    private JTextField fieldInText = new JTextField(10);
    //static String comPortName;
    private JButton startButton = new JButton("Start");

    void init() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Data Transfer");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setExtendedState(JFrame.NORMAL);
                frame.addWindowListener(new WindowListener() {
                    public void windowOpened(WindowEvent e) {

                    }

                    public void windowClosing(WindowEvent e) {
                        System.out.println("закрыли сом порт");
                    }

                    public void windowClosed(WindowEvent e) {

                    }

                    public void windowIconified(WindowEvent e) {

                    }

                    public void windowDeiconified(WindowEvent e) {

                    }

                    public void windowActivated(WindowEvent e) {

                    }

                    public void windowDeactivated(WindowEvent e) {

                    }
                });
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

                frame.setPreferredSize(new Dimension(270, 225));
                frame.setLayout(new GridBagLayout());


                startButton.addActionListener(new StartButtonActionListener());

                frame.add(fieldInText);
                frame.add(startButton);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);


                /*//Передаём в конструктор имя порта
                SerialPort serialPort = new SerialPort(comPortName);
                try {
                    DataTransfer.serialPort.set
                    //Открываем порт
                    serialPort.openPort();
                    //Выставляем параметры
                    serialPort.setParams(SerialPort.BAUDRATE_9600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    //Включаем аппаратное управление потоком
                    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                            SerialPort.FLOWCONTROL_RTSCTS_OUT);
                    //Устанавливаем ивент лисенер и маску
                    //serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);
                    //Отправляем запрос устройству
                    //serialPort.writeString("Get data");
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }*/




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

            System.out.println(fieldInText.getText());
        }
    }

    public class ActionListenerSelectComPort implements ActionListener {

        String portName;
        public ActionListenerSelectComPort(String portName) {
            this.portName = portName;
        }
        public void actionPerformed(ActionEvent e) {

            System.out.println(portName);
        }


    }


}





