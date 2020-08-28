package com.minesweeper.model;

/**
 * 
 * Class that represents the coordinates (x,y) of a Cell on a Board
 *
 */
public class CellPosition {

	public int x;
	public int y;

	public CellPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static boolean isInRange(int max, int coordinate) {
		return (0 <= coordinate && coordinate < max);
	}

	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof CellPosition)) {
			return false;
		}
		CellPosition position = (CellPosition) object;
		return (position.x == this.x && position.y == this.y);
	}
}
