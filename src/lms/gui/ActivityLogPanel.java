package lms.gui;

import lms.data.ActivityStack;
import lms.data.DataStore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ActivityLogPanel extends JPanel {

    private final ActivityStack stack = new ActivityStack();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final JTextField actionField = new JTextField(30);
    private final JTextArea  display     = new JTextArea(14, 55);

    public ActivityLogPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(12, 12, 12, 12));

        add(buildControls(), BorderLayout.NORTH);
        add(new JScrollPane(display), BorderLayout.CENTER);

        Theme.styleTextArea(display);

        DataStore.loadActivity(stack);
        if (!stack.isEmpty())
            display.setText("Loaded " + stack.size() + " activity log entry/entries from saved data.");
    }

    private JPanel buildControls() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        p.setBorder(BorderFactory.createTitledBorder("Activity Log"));
        p.add(new JLabel("Action / Event:"));
        p.add(actionField);

        JButton pushBtn  = new JButton("Push (Log Action)");
        JButton popBtn   = new JButton("Pop (Undo Last)");
        JButton peekBtn  = new JButton("Peek (Latest)");
        JButton viewBtn  = new JButton("View Full Log");
        JButton clearBtn = new JButton("Clear Display");

        pushBtn.addActionListener(e  -> push());
        popBtn.addActionListener(e   -> pop());
        peekBtn.addActionListener(e  -> peek());
        viewBtn.addActionListener(e  -> viewLog());
        clearBtn.addActionListener(e -> display.setText(""));

        p.add(pushBtn); p.add(popBtn); p.add(peekBtn); p.add(viewBtn); p.add(clearBtn);
        return p;
    }

    private void push() {
        String action = actionField.getText().trim();
        if (action.isEmpty()) { display.setText("Enter an action description."); return; }
        String entry = "[" + LocalDateTime.now().format(FMT) + "]  " + action;
        try {
            stack.push(entry);
            DataStore.saveActivity(stack.display());
            append("Logged: " + entry + "  (Stack size: " + stack.size() + ")");
            actionField.setText("");
        } catch (Exception ex) { display.setText(ex.getMessage()); }
    }

    private void pop() {
        try {
            String removed = stack.pop();
            DataStore.saveActivity(stack.display());
            append("Popped / Undone: " + removed + "\nRemaining: " + stack.size());
        } catch (Exception ex) { display.setText(ex.getMessage()); }
    }

    private void peek() {
        try {
            display.setText("Latest action (top of stack):\n\n  " + stack.peek()
                            + "\n\nTotal entries: " + stack.size());
        } catch (Exception ex) { display.setText(ex.getMessage()); }
    }

    private void viewLog() {
        List<String> log = stack.display();
        if (log.isEmpty()) { display.setText("Activity log is empty."); return; }
        StringBuilder sb = new StringBuilder("Activity Log (" + log.size() + " entries) - most recent first:\n\n");
        for (int i = 0; i < log.size(); i++)
            sb.append(String.format("  %2d.  %s\n", i + 1, log.get(i)));
        display.setText(sb.toString());
    }

    private void append(String text) {
        String cur = display.getText();
        display.setText(cur.isBlank() ? text : cur + "\n" + text);
    }
}
