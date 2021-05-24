package hu.elte.hsfpoj.view;

import hu.elte.hsfpoj.model.Direction;
import hu.elte.hsfpoj.model.Game;
import hu.elte.hsfpoj.model.GameIdentifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

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
        //frame = new JFrame("Labyrinth");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //frame.setSize(600, 600);

        try {
            add(board = new Board(game));
        } catch (IOException e) {
            System.out.println("Hiba a jatek letrehozasa kozben!");
        }

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

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (!game.isLevelAlreadyLoaded()) {
                    return;
                }
                System.out.println("Elote x: " + game.getLevel().getPlayerPosition().x + " , elotte y: " + game.getLevel().getPlayerPosition().y);
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
                repaint();
                if (d != null && game.step(d)) {
                    if (game.getLevel().isGameOver()) {
                        // ide kell majd a beirni a nevet az adatbazishoz
                        JOptionPane.showMessageDialog(GameWindow.this,
                                "Vesztettel! A legnagyobb teljesitett szint: " +
                                game.getLevel(),
                                "Vesztettel!",
                                JOptionPane.INFORMATION_MESSAGE);
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

        setResizable(false);
        setLocationRelativeTo(null);
        game.loadNewGame(new GameIdentifier("EASY", 1));
        board.refresh();

        pack();
        setVisible(true);
    }

    public void startGame() {
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
