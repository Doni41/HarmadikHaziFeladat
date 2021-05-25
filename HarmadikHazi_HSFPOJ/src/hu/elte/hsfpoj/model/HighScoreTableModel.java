package hu.elte.hsfpoj.model;

import hu.elte.hsfpoj.persistance.Result;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class HighScoreTableModel extends AbstractTableModel {
    private final ArrayList<Result> results;
    private final String[] columnNames = new String[] {"Name", "Score"};

    public HighScoreTableModel(ArrayList<Result> results) {
        this.results = results;
    }

    @Override
    public int getRowCount() {
        return results.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        Result result = results.get(i);
        if (i1 == 0) {
            return result.getName();
        }
        else if (i1 == 1) {
            return result.getHighScore();
        }
        return result.getHighScore() == 0 ? "Nincs megoldva" : result.getHighScore();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

