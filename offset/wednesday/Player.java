package offset.wednesday;

import java.util.*;

import offset.sim.Pair;
import offset.sim.Point;
import offset.sim.movePair;

public class Player extends offset.sim.Player {
	
	int size = 32;

  int p;
  int q;
  Point[] grid;

	public Player(Pair prin, int idin) {
		super(prin, idin);
		// TODO Auto-generated constructor stub
	}

	public void init() {

	}

	public movePair move(Point[] initGrid, Pair pr, Pair pr0, ArrayList<ArrayList> history) {

    // initialize the grid, p, and q at the class level so we don't have to
    // pass them as parameters to helper functions
    p = pr.p;
    q = pr.q;
    grid = initGrid;

    // the movePair that will eventually be returned
		movePair movepr = new movePair();
    
    /** 
     * i controls the row, j controls the column
     *
     * grid SHOULD be a 2-dimensional array, but for some crazy reason it is
     * not, and so you need to multiply i by the size to access the given row.
     */

    int maxValue = Integer.MIN_VALUE;
    movepr.move = false;

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < size; j++) {

        Point current = accessGrid(i, j);
        
        if (existsValidMove(current) && current.value > maxValue) {
          movepr = returnValidMove(current);
          maxValue = current.value;
          movepr.move = true;
        }
			}
		}
		return movepr;
	}

  /**
   * Given an origin point, this method returns the first available valid move
   * Note that this method assumes that there IS a valid move from this point
   */
  public movePair returnValidMove(Point origin) {

    int oRow = origin.x;
    int oCol = origin.y;

    movePair optimalPair = new movePair();

    // there are 8 moves in the perfect case for any given point on the board
    for (int i = 0; i < 8; i++) {

      movePair tmpPair = new movePair();
      tmpPair.src = origin;
      int nRow;
      int nCol;

      if (i == 0) {
        nRow = oRow - p;
        nCol = oCol - q;
      } else if (i == 1) {
        nRow = oRow - p;
        nCol = oCol + q;
      } else if (i == 2) {
        nRow = oRow + p;
        nCol = oCol - q;
      } else if (i == 3) {
        nRow = oRow + p;
        nCol = oCol + q;
      } else if (i == 4) {
        nRow = oRow - q;
        nCol = oCol - p;
      } else if (i == 5) {
        nRow = oRow - q;
        nCol = oCol + p;
      } else if (i == 6) {
        nRow = oRow + q;
        nCol = oCol - p;
      } else {
        nRow = oRow + q;
        nCol = oCol + p;
      }
    
      // take the first valid move
      if (nRow >= 0 && nRow < 32 && nCol >= 0 && nCol < 32) {
        tmpPair.target = accessGrid(nRow, nCol);

        if (validateMove(tmpPair)) {
          optimalPair = tmpPair;
          break;
        }
      }
    }
    return optimalPair;
  }

  /**
   * Given an origin point, this method returns true if a valid move exists off
   * of the point in any of the 8 directions it can move
   */
  public boolean existsValidMove(Point origin) {

    int oRow = origin.x;
    int oCol = origin.y;

    movePair optimalPair = null;

    // there are 8 moves in the perfect case for any given point on the board
    for (int i = 0; i < 8; i++) {

      movePair tmpPair = new movePair();
      tmpPair.src = origin;
      tmpPair.target = null;
      
      int nRow;
      int nCol;

      if (i == 0) {
        nRow = oRow - p;
        nCol = oCol - q;
      } else if (i == 1) {
        nRow = oRow - p;
        nCol = oCol + q;
      } else if (i == 2) {
        nRow = oRow + p;
        nCol = oCol - q;
      } else if (i == 3) {
        nRow = oRow + p;
        nCol = oCol + q;
      } else if (i == 4) {
        nRow = oRow - q;
        nCol = oCol - p;
      } else if (i == 5) {
        nRow = oRow - q;
        nCol = oCol + p;
      } else if (i == 6) {
        nRow = oRow + q;
        nCol = oCol - p;
      } else {
        nRow = oRow + q;
        nCol = oCol + p;
      }

      if (nRow >= 0 && nRow < 32 && nCol >= 0 && nCol < 32)
        tmpPair.target = accessGrid(nRow, nCol);

      // if any of these moves are valid, we're good
      if (tmpPair.target != null && validateMove(tmpPair)) {
        optimalPair = tmpPair;
        break;
      }
    }
    if (optimalPair == null) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * this method abstracts away some of the confusion surrounding grid's
   * lack of 2-dimensionality
   */
  public Point accessGrid(int row, int column) {
    return grid[row * size + column];
  }

  /**
   * validate a move on the grid, passed in as a movePair
   */
  boolean validateMove(movePair movepr) {
    	
  	Point src = movepr.src;
  	Point target = movepr.target;
  	boolean rightposition = false;
  	if (Math.abs(target.x - src.x) == Math.abs(p) && Math.abs(target.y - src.y) == Math.abs(q)) {
  		rightposition = true;
  	}
  	if (Math.abs(target.x - src.x) == Math.abs(q) && Math.abs(target.y - src.y) == Math.abs(p)) {
  		rightposition = true;
  	}

    if (rightposition && src.value == target.value && src.value > 0) {
    	return true;
    } else {
    	return false;
    }
  }

}

