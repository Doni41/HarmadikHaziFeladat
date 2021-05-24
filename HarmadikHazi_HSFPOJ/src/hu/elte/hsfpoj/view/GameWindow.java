package hu.elte.hsfpoj.view;

import hu.elte.hsfpoj.model.Direction;
import hu.elte.hsfpoj.model.Game;
import hu.elte.hsfpoj.model.GameIdentifier;
import hu.elte.hsfpoj.model.ResultManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class GameWindow extends JFrame {

    private JFrame frame;
    private Board board;
    private JMenuItem exit;
    private JMenu menu;
    private JMenuBar menuBar;
    private JMenuItem newGame;
    private JMenu results;
    private JSeparator separator;
    private JLabel timeLabel;

    private final Game game;
    private long startTime;
    private Timer timer;

    public GameWindow() throws IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("" + e);
        }
        try {
            ResultManager resultManager = new ResultManager(10);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        game = new Game();
        URL url = GameWindow.class.getResource("/hu/elte/hsfpoj/res/wall.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        initGame();

    }

    public long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    private void initGame() {
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
                if (d != null && game.step(d)) {
                    if (game.getLevel().isGameOver()) {
                        // ide kell majd a beirni a nevet az adatbazishoz
                        board.gameOver();
                    }
                    if (game.getLevel().isFulfilled()) {
                        gameWinning();
                    }

                    if (!game.getLevel().endOfTheGame() && game.getLevel().isFulfilled()) {
                        JOptionPane.showMessageDialog(GameWindow.this,
                                "Gratulalok, kijutottal a labirintusbol!",
                                "Nyertel!",
                                JOptionPane.INFORMATION_MESSAGE);
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
                timeLabel.setText(elapsedTime() + " ms");
            }
        };
        if (timer != null) {
            timer.removeActionListener(timerAction);
        }
        timer = new Timer(10, timerAction);
        timer.start();
        board.moveGhostEvent();

    }
}
