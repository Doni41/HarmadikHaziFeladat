package hu.elte.hsfpoj.persistance;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResultManager {

    private final int maxScores;
    public List<Result> results;

    private final PreparedStatement insertStatement;
    private final PreparedStatement deleteStatement;
    private final Connection connection;

    public ResultManager(int maxScores) throws SQLException {
        this.maxScores = maxScores;

        String dbURL = "jdbc:mysql://localhost:3306/progtechhazi?serverTimezone=UTC";
        connection = DriverManager.getConnection(dbURL, "root", "adminroot");

        String insertQuery = "INSERT INTO RESULTS (NAME, SCORE) VALUES (?, ?)";
        insertStatement = connection.prepareStatement(insertQuery);

        String deleteQuery = "DELETE FROM RESULTS WHERE SCORE=?";
        deleteStatement = connection.prepareStatement(deleteQuery);
    }

    public List<Result> getSortedHighScores() throws SQLException {
        String query = "SELECT * FROM RESULTS";
        List<Result> highScores = new ArrayList<>();

        Statement stmt = connection.createStatement();
        ResultSet results = stmt.executeQuery(query);

        while (results.next()) {
            String name = results.getString("NAME");
            int score = results.getInt("SCORE");
            highScores.add(new Result(name, score));
        }

        sortHighScores(highScores);
        return highScores;
    }

    public void putHighScore(String name, int score) throws SQLException {
        List<Result> highScores = getSortedHighScores();

        if (highScores.size() < maxScores) {    // high score board is full
            insertScore(name, score);
        } else {
            int leastScore = highScores.get(highScores.size() - 1).getHighScore();
            if (leastScore < score) {
                deleteScores(leastScore);
                insertScore(name, score);
            }
        }
    }

    public void sortHighScores(List<Result> highScores) {
        Collections.sort(highScores, Comparator.comparing(Result::getHighScore).reversed());
    }

    public void insertScore(String name, int score) throws SQLException {

        insertStatement.setString(1, name);
        insertStatement.setInt(2, score);

        insertStatement.executeUpdate();
    }

    public void deleteScores(int score) throws SQLException {
        deleteStatement.setInt(1, score);
        deleteStatement.executeUpdate();
    }

}
