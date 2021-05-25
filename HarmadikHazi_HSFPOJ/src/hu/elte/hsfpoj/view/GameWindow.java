package hu.elte.hsfpoj.view;

import hu.elte.hsfpoj.model.Direction;
import hu.elte.hsfpoj.model.Game;
import hu.elte.hsfpoj.model.GameIdentifier;
import hu.elte.hsfpoj.persistance.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameWindow extends JFrame {

    private Board board;
    private JMenuItem exit;
    private JMenu menu;
    private JMenuBar menuBar;
    private JMenuItem newGame;
    private JMenu results;
    private JLabel timeLabel;

    private Game game;
    private long startTime;
    private Timer timer;

    public GameWindow() throws IOException {
        initGame();
    }

    public long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    private void initGame() {
        game = new Game();
        URL url = GameWindow.class.getResource("/hu/elte/hsfpoj/res/wall.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        setTitle("Labyrinth");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        timeLabel = new JLabel();
        timeLabel.setText("0 ms");

        menuBar = new JMenuBar();
        menu = new JMenu();
        newGame = new JMenuItem();
        exit = new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        results = new JMenu();

        JMenuItem resultTable = new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    new HighScoreWindow((ArrayList<Result>) game.getResultManager().getSortedHighScores(), GameWindow.this);
                } catch (SQLException sql) {
                    System.out.println("Hiba a tablazat letrehozasa kozben!");
                }
            }
        });

        resultTable.setText("Eredmenyek lekerdezese");
        results.add(resultTable);


        menu.setText("Menu");
        newGame.setText("New Game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startGame();
            }
        });

        exit.setText("Exit");
        results.setText("Results");
        menu.add(newGame);
        menu.add(exit);
        menuBar.add(menu);
        menuBar.add(results);

        setJMenuBar(menuBar);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (!game.isLevelAlreadyLoaded()) {
                    return;
                }
                int keyCode = e.getKeyCode();
                Direction d = null;
                switch (keyCode) {
                    case KeyEvent.VK_LEFT:      d = Direction.LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:     d = Direction.RIGHT;
                        break;
                    case KeyEvent.VK_UP:        d = Direction.UP;
                        break;
                    case KeyEvent.VK_DOWN:      d = Direction.DOWN;
                        break;
                }

                board.repaint();
                if (d != null && game.step(d) && !game.getLevel().isGameOver()) {
                    if (game.getLevel().isGameOver()) {
                        board.gameOver();
                        int score = game.getLevel().getGameID().getLevel() - 1;
                        afterGameEnded(score);
                    }
                    if (game.getLevel().isFulfilled()) {
                        gameWinning();
                        if (game.getLevel().getGameID().getLevel() == 10) {
                            int score = game.getLevel().getGameID().getLevel();
                            afterGameEnded(score);
                        }
                    }

                    if (!game.getLevel().endOfTheGame() && game.getLevel().isFulfilled()) {
                        int score = game.getLevel().getGameID().getLevel() - 1;
                        afterGameEnded(score);
                    }
                }

            }
        });


        try {
            add(board = new Board(game));
        } catch (IOException e) {
            System.out.println("Hiba a jatek letrehozasa kozben!");
        }

        GroupLayout boardLayout = new GroupLayout(board);
        board.setLayout(boardLayout);
        boardLayout.setHorizontalGroup(
                boardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 398, Short.MAX_VALUE)
        );
        boardLayout.setVerticalGroup(
                boardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 330, Short.MAX_VALUE)
        );


        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(board, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(timeLabel)
                                .addContainerGap(381, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timeLabel)
                                .addGap(25, 25, 25))
        );

        //setResizable(false);
        setLocationRelativeTo(null);
        game.loadNewGame(new GameIdentifier("EASY", 1));
        board.refresh();


        pack();
        setVisible(true);
    }

    public void gameWinning () {
        GameIdentifier currentGameID = game.getLevel().getGameID();
        String currentDiff = currentGameID.getDifficulty();
        int currentLvl = currentGameID.getLevel();
        if (currentDiff.equals("EASY")) {
            if (currentLvl < 3) {
                game.loadNewGame(new GameIdentifier("EASY", currentLvl + 1));
            } else if (currentLvl == 3) {
                game.loadNewGame(new GameIdentifier("EASY", 4));
            } else {
                game.loadNewGame(new GameIdentifier("MEDIUM", 5));
            }
        } else if (currentDiff.equals("MEDIUM")) {
            if (currentLvl < 7) {
                game.loadNewGame(new GameIdentifier("MEDIUM", currentLvl + 1));
            } else if (currentLvl == 7) {
                game.loadNewGame(new GameIdentifier("MEDIUM", 8));
            } else {
                game.loadNewGame(new GameIdentifier("HARD", 9));
            }
        } else {
            if (currentLvl == 9) {
                game.loadNewGame(new GameIdentifier("HARD", currentLvl + 1));
            } else if (currentLvl == 10) {
                System.out.println("Vegig vitted a jatekot!");
            }
        }
        board.refresh();
        board.repaint();
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
    }

    public void startGame() {
        if (board != null) {
            remove(board);
            if (board.getTimer() != null && board.getGhostActionListener() != null) {
                board.getTimer().removeActionListener(board.getGhostActionListener());
                board.getTimer().stop();
            }
            board.removeAll();
            board = null;
        }


        try {
            add(board = new Board(game));
        } catch (IOException e) {
            System.out.println("Hiba a jatek letrehozasa kozben!");
        }

        GroupLayout boardLayout = new GroupLayout(board);
        //getContentPane().setLayout(boardLayout);
        board.setLayout(boardLayout);
        boardLayout.setHorizontalGroup(
                boardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 398, Short.MAX_VALUE)
        );
        boardLayout.setVerticalGroup(
                boardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 330, Short.MAX_VALUE)
        );


        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(board, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(timeLabel)
                                .addContainerGap(381, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timeLabel)
                                .addGap(25, 25, 25))
        );

        setResizable(false);
        setLocationRelativeTo(null);
        game.loadNewGame(new GameIdentifier("EASY", 1));
        board.refresh();
        pack();

        timeLabel.setText("0 ms");
        startTime = System.currentTimeMillis();

        ActionListener timerAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!game.getLevel().endOfTheGame()) {
                    timeLabel.setText(elapsedTime() + " ms");
                } else if (game.getLevel().isFulfilled() && game.getLevel().getGameID().getLevel() == 10) {
                    ((Timer)actionEvent.getSource()).stop();
                    System.out.println("Leallhat az idozito!");
                }
            }
        };
        if (timer != null) {
            timer.removeActionListener(timerAction);
        }
        timer = new Timer(10, timerAction);
        timer.start();
        board.moveGhostEvent();

        Timer playerGhostTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (game.getLevel().endOfTheGame()) {
                    int score = game.getLevel().getGameID().getLevel() - 1;
                    afterGameEnded(score);
                    ((Timer)actionEvent.getSource()).stop();
                }
            }
        });
        playerGhostTimer.start();

    }

    public void afterGameEnded (int score) {
        StringBuilder message = new StringBuilder();
        message
                .append("Teljesitett szint: ")
                .append(score)
                .append(". Add meg a nevedet: ");
        String name = JOptionPane.showInputDialog(GameWindow.this,
                (message),
                "Vege a jateknak" ,
                JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Name: " + name
                + " , Teljesitett szint: " + score);

        try {
            game.getResultManager().putHighScore(name, score);
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
}
