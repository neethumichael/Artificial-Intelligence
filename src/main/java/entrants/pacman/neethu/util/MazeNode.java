package main.java.entrants.pacman.neethu.util;

import java.util.ArrayList;
import java.util.EnumMap;

import pacman.game.Constants.MOVE;


public class MazeNode implements Comparable<MazeNode> {
    public MazeNode parent;
    public double g, h;
    public boolean visited = false;
    public EnumMap<MOVE, MazeNode> neighbours = null;
    public int index;
    public MOVE reached = null;

    public MazeNode(int index) {
        neighbours = new EnumMap<MOVE, MazeNode>(MOVE.class);
        this.index = index;
    }

    public MazeNode(double g, double h) {
        this.g = g;
        this.h = h;
    }

    public boolean isEqual(MazeNode another) {
        return index == another.index;
    }

    public int compareTo(MazeNode other) {

        if ((g + h) < (other.g + other.h))
            return -1;
        else if ((g + h) > (other.g + other.h))
            return 1;

        return 0;
    }
}