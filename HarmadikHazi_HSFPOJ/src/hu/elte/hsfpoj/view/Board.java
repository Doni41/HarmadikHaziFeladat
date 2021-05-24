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
        setPreferredSize(dimension);
        setMaximumSize(dimension);
        setSize(dimension);
        repaint();
        return true;
    }

    public void moveGhostEvent () {
        Direction dir = game.createDirection();
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                game.getLevel().ghostChangeDirection(dir);
                System.out.println("Ghost x: " + game.getLevel().getGhostPosition().x + " , y: " + game.getLevel().getGhostPosition().y);
                refresh();
            }
        });
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
            }
        }
    }
}
