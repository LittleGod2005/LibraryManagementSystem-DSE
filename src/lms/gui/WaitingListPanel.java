package lms.gui;

import lms.data.DataStore;
import lms.data.StudentQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class WaitingListPanel extends JPanel {

    private final StudentQueue queue = new StudentQueue();

    private final JTextField nameField = new JTextField(22);
    private final JTextArea  display   = new JTextArea(14, 55);

    public WaitingListPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(12, 12, 12, 12));

        add(buildControls(), BorderLayout.NORTH);
        add(new JScrollPane(display), BorderLayout.CENTER);

        Theme.styleTextArea(display);

        DataStore.loadQueue(queue);
        if (!queue.isEmpty())
            display.setText("Loaded " + queue.size() + " student(s) from saved waiting list.");
    }

    private JPanel buildControls() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        p.setBorder(BorderFactory.createTitledBorder("Student Waiting List"));
        p.add(new JLabel("Student Name:"));
        p.add(nameField);

        JButton enqBtn   = new JButton("Enqueue (Add to List)");
        JButton deqBtn   = new JButton("Dequeue (Assign Book)");
        JButton peekBtn  = new JButton("Peek");
        JButton viewBtn  = new JButton("View List");
        JButton clearBtn = new JButton("Clear Display");

        enqBtn.addActionListener(e   -> enqueue());
        deqBtn.addActionListener(e   -> dequeue());
        peekBtn.addActionListener(e  -> peek());
        viewBtn.addActionListener(e  -> showList());
        clearBtn.addActionListener(e -> display.setText(""));

        p.add(enqBtn); p.add(deqBtn); p.add(peekBtn); p.add(viewBtn); p.add(clearBtn);
        return p;
    }

    private void enqueue() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) { display.setText("Enter a student name."); return; }
        try {
            queue.enqueue(name);
            DataStore.saveQueue(queue.display());
            append("Enqueued: " + name + "  (Position #" + queue.size() + ")");
            nameField.setText("");
        } catch (Exception ex) { display.setText(ex.getMessage()); }
    }

    private void dequeue() {
        try {
            String s = queue.dequeue();
            DataStore.saveQueue(queue.display());
            append("Book assigned to: " + s + "  (removed from queue)\nRemaining: " + queue.size());
        } catch (Exception ex) { display.setText(ex.getMessage()); }
    }

    private void peek() {
        try {
            display.setText("Next in line: " + queue.peek() + "\nQueue size: " + queue.size());
        } catch (Exception ex) { display.setText(ex.getMessage()); }
    }

    private void showList() {
        List<String> list = queue.display();
        if (list.isEmpty()) { display.setText("Waiting list is empty."); return; }
        StringBuilder sb = new StringBuilder("Waiting List (" + list.size() + " student(s)):\n\n");
        for (int i = 0; i < list.size(); i++)
            sb.append(String.format("  #%d  %s\n", i + 1, list.get(i)));
        display.setText(sb.toString());
    }

    private void append(String text) {
        String cur = display.getText();
        display.setText(cur.isBlank() ? text : cur + "\n" + text);
    }
}
