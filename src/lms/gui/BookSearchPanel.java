package lms.gui;

import lms.data.Book;
import lms.data.BookBST;
import lms.data.DataStore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class BookSearchPanel extends JPanel {

    private final BookBST bst = new BookBST();

    private final JTextField titleField  = new JTextField(18);
    private final JTextField authorField = new JTextField(18);
    private final JTextField isbnField   = new JTextField(12);
    private final JTextField searchField = new JTextField(18);
    private final JTextArea  display     = new JTextArea(14, 55);

    public BookSearchPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(new EmptyBorder(12, 12, 12, 12));

        add(buildInsertRow(), BorderLayout.NORTH);
        add(buildSearchRow(), BorderLayout.CENTER);
        add(new JScrollPane(display), BorderLayout.SOUTH);

        Theme.styleTextArea(display);

        DataStore.loadBooks(bst);
        if (!bst.isEmpty())
            display.setText("Loaded " + bst.inorderList().size() + " book(s) from saved data.");
    }

    private JPanel buildInsertRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        p.setBorder(BorderFactory.createTitledBorder("Insert Book"));
        p.add(new JLabel("Title:"));   p.add(titleField);
        p.add(new JLabel("Author:")); p.add(authorField);
        p.add(new JLabel("ISBN:"));   p.add(isbnField);
        JButton btn = new JButton("Insert Book");
        btn.addActionListener(e -> insertBook());
        p.add(btn);
        return p;
    }

    private JPanel buildSearchRow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        p.setBorder(BorderFactory.createTitledBorder("Search / View"));
        p.add(new JLabel("Search Title:"));
        p.add(searchField);
        JButton searchBtn = new JButton("Search");
        JButton sortBtn   = new JButton("Show All (Sorted)");
        JButton clearBtn  = new JButton("Clear");
        searchBtn.addActionListener(e -> searchBook());
        sortBtn.addActionListener(e   -> showAllSorted());
        clearBtn.addActionListener(e  -> display.setText(""));
        p.add(searchBtn); p.add(sortBtn); p.add(clearBtn);
        return p;
    }

    private void insertBook() {
        String t = titleField.getText().trim();
        String a = authorField.getText().trim();
        String i = isbnField.getText().trim();
        if (t.isEmpty() || a.isEmpty() || i.isEmpty()) {
            display.setText("Please fill in Title, Author, and ISBN."); return;
        }
        bst.insert(new Book(t, a, i));
        DataStore.saveBooks(bst.inorderList());
        display.setText("Book inserted: \"" + t + "\"  (Total: " + bst.inorderList().size() + ")");
        titleField.setText(""); authorField.setText(""); isbnField.setText("");
    }

    private void searchBook() {
        String q = searchField.getText().trim();
        if (q.isEmpty()) { display.setText("Enter a title to search."); return; }
        Book found = bst.search(q);
        if (found != null)
            display.setText("Found:\n\n" + header() + "\n" + found);
        else
            display.setText("No book found with title: \"" + q + "\"");
    }

    private void showAllSorted() {
        if (bst.isEmpty()) { display.setText("Catalog is empty. Insert books first."); return; }
        List<Book> books = bst.inorderList();
        StringBuilder sb = new StringBuilder("Sorted Catalog (" + books.size() + " book(s)) - A to Z\n\n");
        sb.append(header()).append("\n");
        for (Book b : books) sb.append(b).append("\n");
        display.setText(sb.toString());
    }

    private String header() {
        return String.format("%-35s | %-25s | %s", "Title", "Author", "ISBN")
               + "\n" + "-".repeat(72);
    }
}
