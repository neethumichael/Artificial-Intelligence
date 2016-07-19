package main.java.entrants.pacman.neethu;

import java.util.EnumMap;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

class MazeNode implements Comparable<MazeNode>
{
	public Game gameState;
	public int index;
	public EnumMap<MOVE, MazeNode> neighbors = new EnumMap<MOVE, MazeNode>(MOVE.class);
	public double g = 0;
	public double h = 0;
	public MazeNode parent = null;
	public MOVE reachedBy = null;

    public MazeNode(int index)
    {
        this.index=index;
    }
    
    public double f() {
    	return g + h;
    }
    
    public boolean isEqual(MazeNode other)
    {
        return index == other.index;
    }
    
    public int compareTo(MazeNode other)
    {
		if (f() < other.f()) {
			return -1;
		}
		else if (f() > other.f()) {
			return 1;
		}
		return 0;
    }
}
