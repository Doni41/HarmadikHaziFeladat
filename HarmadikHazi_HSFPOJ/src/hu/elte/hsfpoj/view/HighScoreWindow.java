package hu.elte.hsfpoj.view;

import hu.elte.hsfpoj.model.HighScoreTableModel;
import hu.elte.hsfpoj.persistance.Result;

import javax.swing.*;
import java.util.ArrayList;

public class HighScoreWindow extends JDialog {
    private final JTable table;

    public HighScoreWindow(ArrayList<Result> resutls, JFrame parent) {
        super(parent, true);
        table = new JTable(new HighScoreTableModel(resutls));
        table.setFillsViewportHeight(true);


        /*
        TableRowSorter<TableModel> sorter =
                new TableRowSorter<TableModel>(table.getModel());
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.UNSORTED));
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.UNSORTED));
        sorter.setSortKeys(sortKeys);
        table.setRowSorter(sorter);
         */

        add(new JScrollPane(table));
        setSize(400, 400);
        setTitle("Eredmenyek");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
