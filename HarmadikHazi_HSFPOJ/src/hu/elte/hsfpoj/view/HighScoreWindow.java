package hu.elte.hsfpoj.view;

import hu.elte.hsfpoj.model.HighScoreTableModel;
import hu.elte.hsfpoj.persistance.Result;

import javax.swing.*;
import java.util.ArrayList;

public class HighScoreWindow extends JDialog {
    private final JTable table;

    public HighScoreWindow(ArrayList<Result> results, JFrame parent) {
        super(parent, true);
        table = new JTable(new HighScoreTableModel(results));
        table.setFillsViewportHeight(true);

        add(new JScrollPane(table));
        setSize(400, 400);
        setTitle("Eredmenyek");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
