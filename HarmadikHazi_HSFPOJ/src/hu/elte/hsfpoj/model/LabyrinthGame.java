package hu.elte.hsfpoj.model;

import hu.elte.hsfpoj.view.GameWindow;

import java.io.IOException;

public class LabyrinthGame {
    public static void main(String[] args) {
        try {
            GameWindow gameWindow = new GameWindow();
        } catch (IOException e) {
            System.out.println("IOException!");
        }
    }
}
