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
        URL url = GameWindow.class.getResource("/hu/elte/hsfpoj/res/wall.png");
        setIconImage(Toolkit.getDefaultToolkit().getImage(url));
        game = new Game();
        initGame();

    }

    public long elapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    private void initGame() {
        frame = new JFrame("Labyrinth");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //frame.setSize(600, 600);

        try {
            frame.add(board = new Board(game));
        } catch (IOException e) {
            System.out.println("Hiba a jatek letrehozasa kozben!");
        }

        separator = new JSeparator();
        timeLabel = new JLabel();
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

        //frame.setPreferredSize(new java.awt.Dimension(600, 600));

        GroupLayout boardLayout = new javax.swing.GroupLayout(board);
        board.setLayout(boardLayout);
        boardLayout.setHorizontalGroup(
                boardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(separator, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        boardLayout.setVerticalGroup(
                boardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, boardLayout.createSequentialGroup()
                                .addGap(10, 299, Short.MAX_VALUE)
                                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        timeLabel.setText(" ");

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

        frame.setJMenuBar(menuBar);

        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLabel.setText(elapsedTime() + " ms");
            }
        });
        startTime = System.currentTimeMillis();

        GroupLayout layout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(board, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(timeLabel)
                                .addContainerGap(380, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(board, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timeLabel)
                                .addGap(11, 11, 11))
        );

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
                    case KeyEvent.VK_A:         d = Direction.LEFT;
                                                break;
                    case KeyEvent.VK_D:         d = Direction.RIGHT;
                                                break;
                    case KeyEvent.VK_W:         d = Direction.UP;
                                                break;
                    case KeyEvent.VK_S:         d = Direction.DOWN;
                                                break;
                    case KeyEvent.VK_ESCAPE:    game.loadNewGame(game.getLevel().getGameID());
                }
                board.repaint();
                if (d != null && game.step(d)) {
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

        //frame.add(board);

        frame.pack();
        frame.setVisible(true);
    }

    public void startGame() {
        timer.start();
    }
}
