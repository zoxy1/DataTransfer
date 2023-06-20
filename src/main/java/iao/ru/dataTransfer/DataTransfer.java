package iao.ru.dataTransfer;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Zoxy1 on 20.07.17.
 */
public class DataTransfer extends JFrame {
    private static final long serialVersion = 2347162171234712347L;
    private JTextField fieldInText = new JTextField(10);
    private JLabel pathText = new JLabel();
    static String comPortName;
    private JButton sendPictureButton = new JButton("Send picture file");
    private JButton sendText = new JButton("Send text");
    private JButton sendFile = new JButton("Send text file");
    private JLabel lineTextExeption = new JLabel();
    private JLabel developedBy = new JLabel("Developed by Andrey Kudryavtsev");
    private SerialPort serialPortOpen = new SerialPort("COM1");
    private int portSpeed = 115200;
    private ArrayList<JRadioButtonMenuItem> jRadioButtonSpeedMenuItems = new ArrayList<JRadioButtonMenuItem>();
    private JLabel pictureLabel = new JLabel("");
    private File filePicture;
    private File fileText;
    private JFrame frame = new JFrame("Remote controller");
    private BufferedImage bufferedImage;
    private ImagePanel imagePanel = new ImagePanel(pictureLabel);
    private JProgressBar progressBar = new JProgressBar();
    private JPanel progressBarPanel = new JPanel();
    private SwingWorkerLoaderText loaderText = null;
    private SwingWorkerLoaderPicture loaderPicture = null;
    private JButton cancel = new JButton("Cancel");
    private JLabel pathPicture = new JLabel();
    private JPanel aChannelPanel = new JPanel();
    private JPanel bChannelPanel = new JPanel();
    private JPanel channelPanelTextA = new JPanel();
    private JPanel channelPanelTextB = new JPanel();
    private JLabel aChanelLabel = new JLabel("Amplifier");
    private JLabel bChanelLabel = new JLabel("Threshold");

    private JLabel labelA = new JLabel();
    ;
    private JLabel labelB = new JLabel();
    ;

    void init() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setExtendedState(JFrame.NORMAL);
                Font font = new Font("Verdana", Font.PLAIN, 11);

                JMenuBar menuBar = new JMenuBar();
                JMenu fileMenu = new JMenu("File");
                fileMenu.setFont(font);


                JMenuItem exitMenuItem = new JMenuItem("Exit");
                exitMenuItem.setFont(font);
                exitMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        System.exit(0);
                    }

                });


                fileMenu.addSeparator();
                fileMenu.add(exitMenuItem);
                //menuBar.add(fileMenu);

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
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("19200"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("38400"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("57600"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("115200"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("128000"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("230400"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("256000"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("460800"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("921600"));

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
                    comPortItems.get(i).addActionListener(new SelectComPortActionListener(portNames[i]));
                }

                if (serialPortOpen.isOpened()) {
                    try {
                        serialPortOpen.closePort();
                    } catch (SerialPortException e1) {
                        e1.printStackTrace();
                        lineTextExeption.setText(e1.getExceptionType());
                    }
                }
                try {
                    //Открываем порт
                    serialPortOpen.openPort();
                    //Выставляем параметры
                    serialPortOpen.setParams(portSpeed,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    if (serialPortOpen.isOpened()) {
                        lineTextExeption.setText(serialPortOpen.getPortName() + " is open");
                    }
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                    lineTextExeption.setText("Not selected port");
                }
                menuBar.add(settingsMenu);
                frame.setJMenuBar(menuBar);
                frame.setPreferredSize(new Dimension(400, 350));
                frame.setLayout(new GridBagLayout());
                JPanel filePathPanel = new JPanel();
                filePathPanel.add(pictureLabel);
                filePathPanel.setLayout(new GridBagLayout());
                frame.add(filePathPanel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));

                aChannelPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.gray, 2),
                        BorderFactory.createEmptyBorder(1, 1, 1, 1)));
                bChannelPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.gray, 2),
                        BorderFactory.createEmptyBorder(1, 1, 1, 1)));
                // Создание модели ползунков
                BoundedRangeModel model1 = new DefaultBoundedRangeModel(50, 0, 0, 100);
                BoundedRangeModel model2 = new DefaultBoundedRangeModel(50, 0, 0, 100);

                // Создание ползунков
                JSlider slider1 = new JSlider(model1);
                JSlider slider2 = new JSlider(model2);


                // Настройка внешнего вида ползунков
                slider1.setOrientation(JSlider.VERTICAL);
                slider1.setMajorTickSpacing(50);
                slider1.setMinorTickSpacing(10);
                slider1.setPaintTicks(true);
                slider2.setOrientation(JSlider.VERTICAL);
                slider2.setMajorTickSpacing(50);
                slider2.setMinorTickSpacing(10);
                slider2.setPaintTicks(true);

                labelA.setText(String.valueOf(slider1.getValue()));
                labelB.setText(String.valueOf(slider2.getValue()));
                slider1.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        int value = ((JSlider) e.getSource()).getValue();
                        labelA.setText(String.valueOf(value));
                        value = 1434 - (value * 1433 / 100);
                        byte[] bytes =
                                new byte[]{
                                        (byte) ('A'),
                                        (byte) (value >>> 8),
                                        (byte) value};

                        if (serialPortOpen.isOpened()) {
                            try {
                                serialPortOpen.writeBytes(bytes);
                            } catch (SerialPortException e1) {
                                lineTextExeption.setText("Not selected port");
                            }
                        }

                    }
                });
                slider2.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        int value = ((JSlider) e.getSource()).getValue();
                        labelB.setText(String.valueOf(value));
                        value = value * 4095 / 100;
                        byte[] bytes =
                                new byte[]{
                                        (byte) ('B'),
                                        (byte) (value >>> 8),
                                        (byte) value};

                        if (serialPortOpen.isOpened()) {
                            try {
                                serialPortOpen.writeBytes(bytes);
                            } catch (SerialPortException e1) {
                                lineTextExeption.setText("Not selected port");
                            }
                        }

                    }
                });

                aChannelPanel.add(slider1);
                bChannelPanel.add(slider2);
                aChannelPanel.add(labelA);
                bChannelPanel.add(labelB);
                channelPanelTextA.add(aChanelLabel);
                channelPanelTextB.add(bChanelLabel);

                frame.add(channelPanelTextA, new GridBagConstraints(0, 2, 1, 1, 0.9, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(1, 1, 1, 1), 0, 0));
                frame.add(channelPanelTextB, new GridBagConstraints(1, 2, 1, 1, 0.9, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(1, 1, 1, 1), 0, 0));
                frame.add(aChannelPanel, new GridBagConstraints(0, 1, 1, 1, 0.9, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(1, 1, 1, 1), 0, 0));
                frame.add(bChannelPanel, new GridBagConstraints(1, 1, 1, 1, 0.9, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(1, 1, 1, 1), 0, 0));

                frame.add(lineTextExeption, new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0, GridBagConstraints.LAST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);


            }
        });
    }

    public class SendTextButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (serialPortOpen.isOpened()) {
                try {
                    Charset cset = Charset.forName("Windows-1251");
                    ByteBuffer byteBuffer = cset.encode(fieldInText.getText());
                    byte[] bytes = byteBuffer.array();
                    int countByte32 = 0;
                    for (int i = 0; i < bytes.length; i++) {
                        if (countByte32 > 31) {
                            Thread.sleep(100);
                            countByte32 = 0;
                        }
                        serialPortOpen.writeByte(bytes[i]);
                        countByte32++;
                    }
                    lineTextExeption.setText("Text is transmitted");
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (SerialPortException e1) {
                    e1.printStackTrace();
                }
                System.out.println(fieldInText.getText());
            } else {
                lineTextExeption.setText("Don`t send " + serialPortOpen.getPortName() + " is closed");
            }
        }
    }

    public class SelectComPortActionListener implements ActionListener {

        private String portName;

        public SelectComPortActionListener(String portName) {
            this.portName = portName;
        }

        public void actionPerformed(ActionEvent e) {
            comPortName = portName;
            if (serialPortOpen.isOpened()) {
                try {
                    serialPortOpen.closePort();
                } catch (SerialPortException e1) {
                    e1.printStackTrace();
                    lineTextExeption.setText(e1.getExceptionType());
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
                    lineTextExeption.setText(serialPort.getPortName() + " is open");
                    serialPortOpen = serialPort;
                }
            } catch (SerialPortException ex) {
                System.out.println(ex);
                lineTextExeption.setText("Not selected port");
            }
        }

    }

    public class BaundRateActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            for (JRadioButtonMenuItem speedItem : jRadioButtonSpeedMenuItems) {

                if (speedItem.isSelected()) {
                    portSpeed = Integer.parseInt(speedItem.getText());
                    lineTextExeption.setText("Select speed = " + portSpeed);
                }
            }
        }
    }

    public class OpenPictureActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            JFileChooser fileopen = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Pictures (jpg, bmp)", "jpg", "bmp");
            fileopen.setFileFilter(filter);
            fileopen.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int ret = fileopen.showDialog(null, "Open");

            if (ret == JFileChooser.APPROVE_OPTION) {

                filePicture = fileopen.getSelectedFile();
                try {
                    bufferedImage = ImageIO.read(filePicture);
                    imagePanel.setImage(bufferedImage);
                    imagePanel.updateUI();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 0; // расположение элемента по х
                gridBagConstraints.gridy = 1; // расположение элемента по y
                gridBagConstraints.gridwidth = 2; // количество элементов, которое будет занимать по горизонтали
                gridBagConstraints.gridheight = 1; // количество элементов, которое будет занимать  по вертикали
                gridBagConstraints.weightx = 0.9; //как должна осуществляться растяжка компонента
                gridBagConstraints.weighty = 0.9;
                gridBagConstraints.anchor = GridBagConstraints.CENTER;
                gridBagConstraints.fill = GridBagConstraints.BOTH;
                gridBagConstraints.insets = new Insets(1, 1, 1, 1); // отступы от компонета (top, left, down, right)
                gridBagConstraints.ipadx = 0; // говорят о том на сколько будут увеличены минимальные размеры компонента
                gridBagConstraints.ipady = 0;

                pictureLabel.setText("Original size(width=" + bufferedImage.getWidth() + ", height=" + bufferedImage.getHeight() + ")" + "Real transfer size(width=" + imagePanel.getWidth() + ", height=" + imagePanel.getHeight() + ")");
                pathPicture.setText(filePicture.getAbsolutePath());
                lineTextExeption.setText("File " + filePicture.getName() + " is opened");

            } else {
                lineTextExeption.setText("File don`t selected");
            }
        }
    }

    public class SendFilePictureButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (bufferedImage != null) {
                if (serialPortOpen.isOpened()) {
                    UICallback ui = new UICallbackImpl();
                    loaderPicture = new SwingWorkerLoaderPicture(ui, serialPortOpen, imagePanel, bufferedImage);
                    loaderPicture.execute();
                    loaderPicture.addPropertyChangeListener(new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress".equals(evt.getPropertyName())) {
                                progressBar.setValue((Integer) evt.getNewValue());
                            }
                        }
                    });
                } else {
                    lineTextExeption.setText("Don`t send " + serialPortOpen.getPortName() + " is closed");
                }
            } else {
                lineTextExeption.setText("File picture don`t selected");
            }
        }
    }

    public class SendFileTextButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (fileText != null) {
                if (serialPortOpen.isOpened()) {
                    UICallback ui = new UICallbackImpl();
                    loaderText = new SwingWorkerLoaderText(ui, fileText, serialPortOpen);
                    loaderText.execute();
                    loaderText.addPropertyChangeListener(new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress".equals(evt.getPropertyName())) {
                                progressBar.setValue((Integer) evt.getNewValue());
                            }
                        }
                    });
                } else {
                    lineTextExeption.setText("Don`t send " + serialPortOpen.getPortName() + " is closed");
                }
            } else {
                lineTextExeption.setText("File text don`t selected");
            }
        }
    }

    public class OpenTextActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JFileChooser fileopen = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Text file (txt)", "txt");
            fileopen.setFileFilter(filter);
            fileopen.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int ret = fileopen.showDialog(null, "Open");

            if (ret == JFileChooser.APPROVE_OPTION) {

                fileText = fileopen.getSelectedFile();
                pathText.setText(fileText.getPath());
                lineTextExeption.setText("File " + fileText.getName() + "is opened");
            }
        }
    }

    /**
     * UI callback implementation
     */
    private class UICallbackImpl implements UICallback {

        @Override
        public void setText(final String text) {
            lineTextExeption.setText(text);
        }

        /**
         * Sets loading progress
         *
         * @param progressPercent progress value to set
         */

        @Override
        public void setProgress(final int progressPercent) {
            progressBar.setValue(progressPercent);
        }

        /**
         * Performs visual operations on loading start - clears the text and shows popup with the progress bar .
         */
        @Override
        public void startLoading() {
            lineTextExeption.setText("Send file...");
            progressBar.setValue(0);
            progressBarPanel.setVisible(true);
        }

        /**
         * Performs visual operations on loading stop - hides progress bar
         */
        @Override
        public void stopLoading() {
            progressBarPanel.setVisible(false);
            loaderText = null;
        }

        /**
         * Shows error message to user
         *
         * @param message message to display
         */
        @Override
        public void showError(final String message) {
            JOptionPane.showMessageDialog(DataTransfer.this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

        @Override
        public void appendText(String line) {
            lineTextExeption.setText(line);
        }
    }


}





