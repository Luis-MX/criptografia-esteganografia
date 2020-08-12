package presentation;

import data.ImageFileIO;
import domain.PixelsLeastSignificantBit;
import domain.RC4Cipher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CipherForm extends JPanel implements ActionListener {
    private JButton imageChooserButton;
    private JButton cipherButton;
    private JLabel imageViewLabel;
    private ImageFileIO imageFile;
    private PlaceHolderTextField keyTextField;
    private PlaceHolderTextArea plainTextArea;

    public CipherForm(boolean cipher) {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);

        // Primer elemento
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(4, 4, 4, 4);
        add(new JLabel("Imagen:"), constraints);

        // Segundo elemento
        imageChooserButton = new JButton("Buscar una imagen");
        //add(imageChooserButton, constraints);
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty= 1;
        imageViewLabel = new JLabel();
        JPanel stackPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        imageViewLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        imageChooserButton.addActionListener(this);
        stackPanel.add(imageViewLabel, gridBagConstraints);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        stackPanel.add(imageChooserButton, gridBagConstraints);
        add(stackPanel, constraints);

        // Tercer elemento
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty= 1;
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(4, 4));
        keyTextField = new PlaceHolderTextField();
        keyTextField.setPlaceholder("Escribe una clave valida (solo se permiten entre 5 y 32 caracteres)");
        keyTextField.setFont(new Font("Monospace", Font.BOLD, 16));
        plainTextArea = new PlaceHolderTextArea();
        plainTextArea.setPlaceholder("Escribe el texto a cifrar...");
        plainTextArea.setFont(new Font("Monospace", Font.BOLD, 16));
        plainTextArea.setLineWrap(true);
        plainTextArea.setWrapStyleWord(true);
        panel.add(plainTextArea);
        panel.add(keyTextField, BorderLayout.NORTH);
        cipherButton = new JButton(cipher ? "Cifrar y ocultar" : "Descifrar");
        PixelsLeastSignificantBit lsb = new PixelsLeastSignificantBit();
        RC4Cipher cipher1 = new RC4Cipher();
        // EL if se ejecuta cuando se va a cifrar un mensaje dentro de la imagen
        if (cipher)
        cipherButton.addActionListener((e) -> {
            String plainText = plainTextArea.getText();
            String keyText = keyTextField.getText();
            int[] bitsPlainText = RC4Cipher.textToBin(plainText);
            int[] bitsKey = RC4Cipher.buildKey(keyText);
            int[] bitsCipherText = cipher1.cipher(bitsPlainText, bitsKey);
            if (bitsCipherText == null) {
                plainTextArea.setText("Clave invalida");
            }
            String res = RC4Cipher.binToText(bitsCipherText);
            LogViewer.logger.log(String.format("> Cifrado %s", res));
            imageFile.writeImage(
                    lsb.generateLeastSignificantBitArrayFromString(
                            imageFile.readImage(), res
                    ),
                    imageFile.getImageWidth(),
                    imageFile.getImageHeight()
            );
        });
        else cipherButton.addActionListener(
                (e) -> {
                    // Descifrar
                    String cipherText = lsb.generateStringFromLeastSignificantBitArray(imageFile.readImage());
                    // Se lee la llave
                    String keyText = keyTextField.getText();
                    int[] bitsText = RC4Cipher.textToBin(cipherText);
                    int[] bitsKey = RC4Cipher.buildKey(keyText);
                    int[] bitsDescipherText = cipher1.cipher(bitsText, bitsKey);
                    String res = RC4Cipher.binToText(bitsDescipherText);
                    LogViewer.logger.log(String.format("> Descifrado %s", res));
                    plainTextArea.setText(res);
                }
        );
        panel.add(cipherButton, BorderLayout.SOUTH);
        add(panel, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.showOpenDialog(imageChooserButton);
            File file = chooser.getSelectedFile();
            if (file == null) return;
            imageFile = new ImageFileIO(file);
            LogViewer.logger.log(file.getAbsolutePath());
            //cardLayout.removeLayoutComponent(imageChooserButton);
            int imageSide = Math.min(imageViewLabel.getSize().width, imageViewLabel.getSize().height);
            imageViewLabel.setVerticalAlignment(JLabel.TOP);
            imageViewLabel.setPreferredSize(imageViewLabel.getPreferredSize());
            imageViewLabel.setIcon(new ImageIcon(ImageIO.read(file).getScaledInstance(imageSide, imageSide, Image.SCALE_FAST)));
            //imageViewLabel.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
            LogViewer.logger.log(e.getMessage());
        }
    }
}

class PlaceHolderTextField extends JTextField {

    private Dimension d = new Dimension(200,32);
    private String placeholder = "";
    private Color phColor= new Color(0,0,0);
    private boolean band = true;

    /** Constructor de clase */
    public PlaceHolderTextField()
    {
        super();
        setSize(d);
        setPreferredSize(d);
        setVisible(true);
        setMargin( new Insets(3,6,3,6));
        //atento a cambios
        getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                band = (getText().length()>0) ? false:true ;
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                band = false;
            }

            @Override
            public void changedUpdate(DocumentEvent de) {}

        });
    }

    public void setPlaceholder(String placeholder)
    {
        this.placeholder=placeholder;
    }

    public String getPlaceholder()
    {
        return placeholder;
    }

    public Color getPhColor() {
        return phColor;
    }

    public void setPhColor(Color phColor) {
        this.phColor = phColor;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //color de placeholder
        g.setColor( new Color(phColor.getRed(),phColor.getGreen(),phColor.getBlue(),90));
        //dibuja texto
        g.drawString((band)?placeholder:"",
                getMargin().left + 4,
                (getSize().height)/2 - 2 + getFont().getSize()/2 );
    }
}
class PlaceHolderTextArea extends JTextArea {

    private Dimension d = new Dimension(200,32);
    private String placeholder = "";
    private Color phColor= new Color(0,0,0);
    private boolean band = true;

    /** Constructor de clase */
    public PlaceHolderTextArea()
    {
        super();
        setSize(d);
        setPreferredSize(d);
        setVisible(true);
        setMargin( new Insets(3,6,3,6));
        /*addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                band = (getText().length()>0) ? false:true ;
                revalidate();
                invalidate();
                repaint();
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                band = (getText().length()>0) ? false:true ;
                revalidate();
                invalidate();
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
                band = (getText().length()>0) ? false:true ;
                revalidate();
                invalidate();
                repaint();
            }
        });*/
        //atento a cambios
        getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                band = (getText().length()>0) ? false:true ;
                //revalidate();
                //invalidate();
                //repaint();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                band = false;
            }

            @Override
            public void changedUpdate(DocumentEvent de) {}

        });
    }

    public void setPlaceholder(String placeholder)
    {
        this.placeholder=placeholder;
    }

    public String getPlaceholder()
    {
        return placeholder;
    }

    public Color getPhColor() {
        return phColor;
    }

    public void setPhColor(Color phColor) {
        this.phColor = phColor;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //color de placeholder
        g.setColor( new Color(phColor.getRed(),phColor.getGreen(),phColor.getBlue(),90));
        //dibuja texto
        g.drawString((band)?placeholder:"",
                getMargin().left,
                (getMargin().top + getFont().getSize() ));
    }
}
