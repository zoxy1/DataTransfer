package iao.ru.dataTransfer;

import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import jssc.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

/**
 * Created by Zoxy1 on 20.07.17.
 */
public class DataTransfer {
    private static final long serialVersion = 2347162171234712347L;
    private JTextField fieldInText = new JTextField(10);
    static String comPortName;
    private JButton sendPicture = new JButton("Send picture");
    private JButton sendText = new JButton("Send text");
    private JLabel comPortExeption = new JLabel();
    SerialPort serialPortOpen = new SerialPort("COM1");
    private int portSpeed = 115200;
    private ArrayList<JRadioButtonMenuItem> jRadioButtonSpeedMenuItems = new ArrayList<JRadioButtonMenuItem>();
    private JLabel pictureLabel = new JLabel("Open picture(BMP)");
    private JLabel picture = new JLabel("Picture");
    private File file;
    private ImagePanel pp = new ImagePanel();
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
                openMenuItem.addActionListener(new OpenActionListener());
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

                JMenu portMenu = new JMenu("Ports available");
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

                ArrayList<JMenuItem> comPortItems = new ArrayList<JMenuItem>();
                for (int i = 0; i < portNames.length; i++) {
                    comPortItems.add(new JMenuItem(portNames[i]));
                    comPortItems.get(i).setFont(font);
                    portMenu.add(comPortItems.get(i));
                    comPortItems.get(i).addActionListener(new ActionListenerSelectComPort(portNames[i]));
                }

                menuBar.add(settingsMenu);
                frame.setJMenuBar(menuBar);
                frame.setPreferredSize(new Dimension(500, 500));
                frame.setLayout(new GridBagLayout());
                JPanel filePathPanel = new JPanel();
                filePathPanel.add(pictureLabel);
                //pictureLabel.setLayout(new GridBagLayout());
                filePathPanel.setLayout(new GridBagLayout());
                frame.add(filePathPanel, new GridBagConstraints(0,0, 2,1 ,0.0,0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1,1,1,1), 0,0));

                //JPanel picturePanel = new JPanel();

                //picturePanel1.setBackground(new Color(255,121,232));

               /* pp.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.gray, 2),
                        BorderFactory.createEmptyBorder(1, 1, 1, 1)));*/
                //picturePanel.setVisible(true);

                GridBagConstraints  gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 0; // расположение элемента по х
                gridBagConstraints.gridy = 1; // расположение элемента по y
                gridBagConstraints.gridwidth = 2; // количество элементов, которое будет занимать по горизонтали
                gridBagConstraints.gridheight = 1; // количество элементов, которое будет занимать  по вертикали
                gridBagConstraints.weightx = 0.9; //как должна осуществляться растяжка компонента
                gridBagConstraints.weighty = 0.9;
                gridBagConstraints.anchor = GridBagConstraints.CENTER;
                gridBagConstraints.fill = GridBagConstraints.BOTH;
                gridBagConstraints.insets = new Insets(1, 1, 1 ,1); // отступы от компонета (top, left, down, right)
                gridBagConstraints.ipadx = 0; // говорят о том на сколько будут увеличены минимальные размеры компонента
                gridBagConstraints.ipady = 0;
                frame.add(pp, gridBagConstraints);


                sendPicture.setLayout(new GridBagLayout());
                frame.add(sendPicture, new GridBagConstraints(0,2, 1,1 ,0.0,0.0, GridBagConstraints.LAST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(1,1,1,1), 0,0));

                JProgressBar progressBar = new JProgressBar();
                progressBar.setMinimum(0);
                progressBar.setMaximum(100);
                progressBar.setValue(50);
                progressBar.setStringPainted(true);
                progressBar.setLayout(new GridBagLayout());
                progressBar.setMinimumSize(new Dimension(100,20));
                progressBar.setPreferredSize(new Dimension(300,20));

                progressBar.setForeground(Color.green);
                frame.add(progressBar, new GridBagConstraints(1,2, 1,1 ,0.9,0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(1,1,1,1), 0,0));

                sendText.setLayout(new GridBagLayout());
                sendText.addActionListener(new SendTextButtonActionListener());
                frame.add(sendText, new GridBagConstraints(0,3, 1,1 ,0.0,0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1,1,1,1), 0,0));

                fieldInText.setLayout(new GridBagLayout());
                frame.add(fieldInText, new GridBagConstraints(1,3, 1,1 ,0.0,0.0, GridBagConstraints.LAST_LINE_START, GridBagConstraints.BOTH, new Insets(1,1,1,1), 0,0));
                comPortExeption.setText("Select COM port");
                comPortExeption.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.gray, 2),
                        BorderFactory.createEmptyBorder(1, 1, 1, 1)));
                //comPortExeption
                frame.add(comPortExeption, new GridBagConstraints(0,4, 2,1 ,0.0,0.0, GridBagConstraints.LAST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(1,1,1,1), 0,0));
                //frame.add(fieldInText);
                //frame.add(sendPicture);
               // frame.add(comPortExeption);
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

    public class SendTextButtonActionListener implements ActionListener {

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

    public class BaundRateActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            for (JRadioButtonMenuItem speedItem : jRadioButtonSpeedMenuItems) {

                if (speedItem.isSelected()) {
                    portSpeed = Integer.parseInt(speedItem.getText());
                    comPortExeption.setText("Select speed = " + portSpeed);
                }
            }
        }
    }
    public class OpenActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            JFileChooser fileopen = new JFileChooser();

            fileopen.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            int ret = fileopen.showDialog(null, "Open");

            if (ret == JFileChooser.APPROVE_OPTION) {

                file = fileopen.getSelectedFile();

                if (file.isDirectory()) {
                    comPortExeption.setText(file.getAbsolutePath());
                    System.out.println("Path to file: " + file.getAbsolutePath());

                }
                if (file.isFile()) {
                    pictureLabel.setText(file.getAbsolutePath());

                    ImagePanel pp = new ImagePanel();
                    pp.setLayout(new BorderLayout());
                    try {
                        pp.setImage(ImageIO.read(file));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    pp.setPreferredSize(new Dimension(10000, 10000));

                }

            } else {
                System.out.println("���� ��� ���������� �� �������");


            }
        }
    }


}





