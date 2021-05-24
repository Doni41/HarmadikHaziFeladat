package hu.elte.hsfpoj.view;

import hu.elte.hsfpoj.model.Direction;
import hu.elte.hsfpoj.model.Game;
import hu.elte.hsfpoj.model.Item;
import hu.elte.hsfpoj.model.Position;
import hu.elte.hsfpoj.res.ResLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Board extends JPanel {
    private Game game;

    private final Image dark;
    private final Image destination;
    private final Image empty;
    private final Image ghost;
    private final Image player;
    private final Image wall;

    private double scale;
    private int scaledSize;
    private final int titleSize = 32;
    private Timer timer;
    private ActionListener ghostActionListener;
    private ActionEvent actionEvent;


    public Board(Game g) throws IOException {
        game = g;
        dark = ResLoader.loadImage("/hu/elte/hsfpoj/res/dark.png");
        destination = ResLoader.loadImage("/hu/elte/hsfpoj/res/destination.png");
        empty = ResLoader.loadImage("/hu/elte/hsfpoj/res/empty.png");
        ghost = ResLoader.loadImage("/hu/elte/hsfpoj/res/ghost.png");
        player = ResLoader.loadImage("/hu/elte/hsfpoj/res/player.png");
        wall = ResLoader.loadImage("/hu/elte/hsfpoj/res/wall.png");

        scale = 2.0;
        scaledSize = (int)(scale * titleSize);
    }

    public boolean setScale (double scale) {
        this.scale = scale;
        scaledSize = (int)(scale * titleSize);
        return refresh();
    }

    public boolean refresh () {
        if (!game.isLevelAlreadyLoaded()) {
            return false;
        }
        Dimension dimension = new Dimension(game.getLevel().getColumns() * scaledSize
                , game.getLevel().getRows() * scaledSize);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
        setSize(dimension);
        repaint();
        return true;
    }

    public void moveGhostEvent () {
        Direction dir = game.createDirection();
        ghostActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!game.getLevel().isGameOver()) {
                    Direction newDir = null;
                    if (game.getLevel().getGhostPosition() != null) {
                        newDir = game.getLevel().getGhostDirection();
                        game.getLevel().ghostChangeDirection(newDir);
                    } else {
                        game.getLevel().ghostChangeDirection(dir);
                    }
                    refresh();
                } else {
                    ((Timer)actionEvent.getSource()).stop();
                }
            }
        };
        timer = new Timer(500, ghostActionListener);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!game.isLevelAlreadyLoaded()) {
            return;
        }
        Graphics2D gr = (Graphics2D)g;
        int width = game.getLevel().getColumns();
        int height = game.getLevel().getRows();
        Position p = game.getLevel().getPlayerPosition();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Image image = null;
                Item item = game.getLevel().getLevelItem(y, x);
                if (game.isNearPlayer(new Position(x,y))) {
                    switch (item) {
                        case DESTINATION:   image = destination;
                            break;
                        case EMPTY:         image = empty;
                            break;
                        case GHOST:         image = ghost;
                            break;
                        case PLAYER:        image = player;
                            break;
                        case WALL:          image = wall;
                            break;
                    }
                    if (image == null) {
                        continue;
                    }
                    gr.drawImage(image, x * scaledSize, y * scaledSize, scaledSize, scaledSize, null);
                } else {
                    image = dark;
                    gr.drawImage(image, x * scaledSize, y * scaledSize, scaledSize, scaledSize, null);
                }

            }
        }
    }

    public void gameOver() {
        JOptionPane.showMessageDialog(this.getParent(),
                "Vesztettel! A legnagyobb teljesitett szint: " +
                        game.getLevel().getLevelNumber(),
                "Vesztettel!",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public Timer getTimer() {
        return timer;
    }

    public ActionListener getGhostActionListener() {
        return ghostActionListener;
    }
}
