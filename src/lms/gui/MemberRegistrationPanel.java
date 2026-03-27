package lms.gui;

import lms.data.DataStore;
import lms.data.Member;
import lms.data.MemberLinkedList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MemberRegistrationPanel extends JPanel {

    private final MemberLinkedList list = new MemberLinkedList();

    private final JTextField idField     = new JTextField(10);
    private final JTextField nameField   = new JTextField(18);
    private final JTextField emailField  = new JTextField(22);
    private final JTextField removeField = new JTextField(12);
    private final JTextArea  display     = new JTextArea(14, 55);

    public MemberRegistrationPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel(new GridLayout(2, 1, 6, 6));
        top.add(buildAddRow());
        top.add(buildRemoveRow());

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(display), BorderLayout.CENTER);

        Theme.styleTextArea(display);

        DataStore.loadMembers(list);
        if (!list.isEmpty())
            display.setText("Loaded " + list.size() + " member(s) from saved data.");
    }

    private JPanel buildAddRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        p.setBorder(BorderFactory.createTitledBorder("Add Member"));
        p.add(new JLabel("Member ID:")); p.add(idField);
        p.add(new JLabel("Name:"));      p.add(nameField);
        p.add(new JLabel("Email:"));     p.add(emailField);
        JButton btn = new JButton("Add Member");
        btn.addActionListener(e -> addMember());
        p.add(btn);
        return p;
    }

    private JPanel buildRemoveRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        p.setBorder(BorderFactory.createTitledBorder("Remove / View"));
        p.add(new JLabel("Remove by ID:")); p.add(removeField);
        JButton removeBtn  = new JButton("Remove Member");
        JButton displayBtn = new JButton("Display All");
        JButton clearBtn   = new JButton("Clear Display");
        removeBtn.addActionListener(e  -> removeMember());
        displayBtn.addActionListener(e -> displayAll());
        clearBtn.addActionListener(e   -> display.setText(""));
        p.add(removeBtn); p.add(displayBtn); p.add(clearBtn);
        return p;
    }

    private void addMember() {
        String id    = idField.getText().trim();
        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();
        if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
            display.setText("Please fill in Member ID, Name, and Email."); return;
        }
        list.addMember(new Member(id, name, email));
        DataStore.saveMembers(list.displayAll());
        display.setText("Member added: " + name + "  (ID: " + id + ")\nTotal members: " + list.size());
        idField.setText(""); nameField.setText(""); emailField.setText("");
    }

    private void removeMember() {
        String id = removeField.getText().trim();
        if (id.isEmpty()) { display.setText("Enter a Member ID to remove."); return; }
        if (list.removeMember(id)) {
            DataStore.saveMembers(list.displayAll());
            display.setText("Member with ID \"" + id + "\" removed.\nTotal members: " + list.size());
            removeField.setText("");
        } else {
            display.setText("No member found with ID: \"" + id + "\"");
        }
    }

    private void displayAll() {
        List<Member> members = list.displayAll();
        if (members.isEmpty()) { display.setText("No members registered yet."); return; }
        StringBuilder sb = new StringBuilder("Registered Members (" + members.size() + "):\n\n");
        sb.append(String.format("%-10s  %-22s  %s\n", "ID", "Name", "Email"));
        sb.append("-".repeat(60)).append("\n");
        for (Member m : members) sb.append(m).append("\n");
        display.setText(sb.toString());
    }
}
