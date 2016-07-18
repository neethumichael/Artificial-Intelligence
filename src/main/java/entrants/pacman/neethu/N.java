package main.java.entrants.pacman.neethu;

import java.util.ArrayList;

import pacman.game.Constants.MOVE;


class N implements Comparable<N> {
    public N parent;
    public double g, h;
    public boolean visited = false;
    public ArrayList<E> adj;
    public int index;
    public MOVE reached = null;

    public N(int index) {
        adj = new ArrayList<E>();
        this.index = index;
    }

    public N(double g, double h) {
        this.g = g;
        this.h = h;
    }

    public boolean isEqual(N another) {
        return index == another.index;
    }

    public String toString() {
        return "" + index;
    }

    public int compareTo(N another) {
        if ((g + h) < (another.g + another.h))
            return -1;
        else if ((g + h) > (another.g + another.h))
            return 1;

        return 0;
    }
}