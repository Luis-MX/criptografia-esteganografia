package presentation;

import javax.swing.*;

import java.awt.*;

public class LogViewer extends JPanel {
    private JTextArea logView;
    public static LogViewer logger = new LogViewer();
    private LogViewer() {
        setLayout(new BorderLayout());
        setPreferredSize(getPreferredSize());
        logView = new JTextArea();
        logView.setDropTarget(null);
        logView.setLineWrap(true);
        logView.setWrapStyleWord(true);
        logView.setEditable(false);
        logView.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        JScrollPane jScrollPane = new JScrollPane(logView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(new JLabel("Log interno:"), BorderLayout.NORTH);
        add(jScrollPane);
    }

    public void log(String text) {
        logView.append(text.concat("\n"));
    }
}