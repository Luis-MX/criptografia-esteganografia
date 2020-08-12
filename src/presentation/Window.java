package presentation;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private JTabbedPane pane;
    public Window() {
        setTitle("Cifrado en imagenes");
        Toolkit tk = Toolkit.getDefaultToolkit();
        setSize(tk.getScreenSize().width/2, tk.getScreenSize().height/2);
        setLocationRelativeTo(null);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);

        pane = new JTabbedPane();
        CipherForm cipherForm = new CipherForm(true);
        CipherForm decipherForm = new CipherForm(false);
        pane.addTab("Cifrado", cipherForm);
        pane.addTab("Descifrado", decipherForm);

        // Primer elemento
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 2;
        constraints.weighty = 1;
        constraints.insets = new Insets(4, 4, 4, 4);
        add(pane, constraints);

        // Segundo elemento
        constraints.gridx = 1;
        constraints.weightx = 2;
        add(LogViewer.logger, constraints);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}