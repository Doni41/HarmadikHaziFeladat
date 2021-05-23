package hu.elte.hsfpoj.model;

public enum Item {
    PLAYER('@'),GHOST('*'), DESTINATION('.'), WALL('#'), EMPTY(' ');
    Item(char rep){ representation = rep; }
    public final char representation;
}
