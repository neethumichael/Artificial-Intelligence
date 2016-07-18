package main.java.entrants.pacman.neethu;

import pacman.game.Constants.MOVE;

class E {
    public N node;
    public MOVE move;
    public double cost;

    public E(N node, MOVE move, double cost) {
        this.node = node;
        this.move = move;
        this.cost = cost;
    }
}
