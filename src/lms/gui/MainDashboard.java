package lms.gui;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    public MainDashboard() {
        super("Smart Campus – Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 680);
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Book Search & Sort (BST)",    new BookSearchPanel());
        tabs.addTab("Student Waiting List (Queue)", new WaitingListPanel());
        tabs.addTab("Member Registration (SLL)",   new MemberRegistrationPanel());
        tabs.addTab("Activity Log (Stack)",        new ActivityLogPanel());

        add(tabs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainDashboard().setVisible(true));
    }
}
