package offset.move_counter;

import java.util.*;
import java.io.*;

import offset.sim.Pair;
import offset.sim.Point;
import offset.sim.movePair;

public class Player extends offset.sim.Player {
	
	int size = 32;

  int p;
  int q;

  int opp_p;
  int opp_q;

  int opp_id;

  File f1; 
  File f2;
  PrintWriter writer1;
  PrintWriter writer2;

  Point[] grid;

	public Player(Pair prin, int idin) throws Exception {
		super(prin, idin);

    // write training files for Sameer
    f1 = new File("x.txt");
    f2 = new File("y.txt");
    writer1 = new PrintWriter(f1);
    writer2 = new PrintWriter(f2);

		// TODO Auto-generated constructor stub
	}

	public void init() {

	}

	public movePair move(Point[] initGrid, Pair pr, Pair pr0, ArrayList<ArrayList> history) {

    // initialize the grid, p, and q at the class level so we don't have to
    // pass them as parameters to helper functions
    p = pr.p;
    q = pr.q;
    opp_p = pr0.p;
    opp_q = pr0.q;
    opp_id = this.id == 0 ? 1 : 0;
    grid = initGrid;

    // log moves for sameer
    logPossibleOpponentMoves();

    movePair nextMove = oneLevelMove(grid, pr, pr0);

    //movePair nextMove = nLevelMove(grid, pr, pr0, 3);

  	return nextMove == null ? new movePair() : nextMove;
  }

  public void logPossibleOpponentMoves() {
    ArrayList<movePair> possibleOpponentMoves = possibleMoves(grid, new Pair(opp_p, opp_q));
    for (int i = 0; i < possibleOpponentMoves.size(); i++) {
      Point[] newGrid = applyMoveToGrid(grid, possibleOpponentMoves.get(i), opp_id);
      int[] targetGrid = calcDifference(grid, newGrid);
      for (int x : targetGrid)
        writer1.print(x + " ");
      writer1.print("\n");
    }
  }

  public int[] calcDifference(Point[] grid1, Point[] grid2) {
    int[] difference = new int[1024];
    for (int i = 0; i < grid1.length; i++)
      difference[i] = grid1[i].value - grid2[i].value;
    return difference;
  }

  /**
   * need to implement some paring strategy here
   */
  public ArrayList<movePair> paredPossibleMoves(Point[] grid, Pair pr) {
  	ArrayList<movePair> possible = new ArrayList<movePair>();
  	for (int i = opp_p; i < size - opp_q; i++) {
  		for (int j = opp_q; j < size - opp_q; j++) {
  			Point currentPoint = pointAtGridIndex(grid, i, j);
  			if (currentPoint.value == 0)
  				continue;
  			for (Pair d : directionsForPair(pr)) {
  				if (isValidBoardIndex(i + d.p, j + d.q)) {
  					Point possiblePairing = pointAtGridIndex(grid, i + d.p, j + d.q);
  					if (currentPoint.value == possiblePairing.value) {
  						possible.add(new movePair(true, currentPoint, possiblePairing));
  						possible.add(new movePair(true, possiblePairing, currentPoint));
  					}
  				}
  			}
  		}
  	}
  	return possible;
  }

  /**
   * return an ArrayList of all possible movePairs for a given grid, p, q
   */
  public ArrayList<movePair> possibleMoves(Point[] grid, Pair pr) {
  	ArrayList<movePair> possible = new ArrayList<movePair>();
  	for (int i = 0; i < size; i++) {
  		for (int j = 0; j < size; j++) {
  			Point currentPoint = pointAtGridIndex(grid, i, j);
  			if (currentPoint.value == 0)
  				continue;
  			for (Pair d : directionsForPair(pr)) {
  				if (isValidBoardIndex(i + d.p, j + d.q)) {
  					Point possiblePairing = pointAtGridIndex(grid, i + d.p, j + d.q);
  					if (currentPoint.value == possiblePairing.value) {
  						possible.add(new movePair(true, currentPoint, possiblePairing));
  						possible.add(new movePair(true, possiblePairing, currentPoint));
  					}
  				}
  			}
  		}
  	}
  	return possible;
  }

  /**
   * method borrowed from group3
   * returns a new grid with a move applied to it
   */
  Point[] applyMoveToGrid(Point[] grid, movePair move, int newOwner) {
    Point[] newGrid = new Point[grid.length];
  	for (int i = 0; i < grid.length; i++) {
  		Point newPoint = new Point(grid[i].x, grid[i].y, grid[i].value, grid[i].owner);
      newPoint.change = grid[i].change;
  		newGrid[i] = newPoint;
  	}
  	Point src = move.src;
  	Point target = move.target;
  	assert isValidBoardIndex(src) : "Source point out of bounds";
  	assert isValidBoardIndex(target) : "Destination point out of bounds";
  	assert src.value == target.value : "Cannot combine points with different values";
  	Point newSrc = pointAtGridIndex(newGrid, src.x, src.y);
  	Point newTarget = pointAtGridIndex(newGrid, target.x, target.y);
  	newTarget.value += newSrc.value;
  	newTarget.owner = newOwner;
  	newTarget.change = true;
  	newSrc.value = 0;
  	newSrc.owner = -1;
  	return newGrid;
  }

  /**
   * make a move, looking one move ahead to minimize opponent's moves
   */
  public movePair oneLevelMove(Point[] grid, Pair pr, Pair pr0) {
  	movePair nextMove = null;
  	int fewestCompetitorMoves = Integer.MAX_VALUE;

    ArrayList<movePair> possibleMoves = possibleMoves(grid, pr);
    System.out.println("number of possible moves: " + possibleMoves.size());

  	for (movePair mp : possibleMoves) {
  		Point[] newGrid = applyMoveToGrid(grid, mp, this.id);
  		ArrayList<movePair> possibleOpponentMoves = possibleMoves(newGrid, pr0);
  		if (possibleOpponentMoves.size() <= fewestCompetitorMoves) {
  			fewestCompetitorMoves = possibleOpponentMoves.size();
  			nextMove = mp;
  			nextMove.move = true;
  		} 
  	}
  	System.out.println("fewest moves: " + fewestCompetitorMoves);
  	return nextMove;
  }

  
  //public movePair twoLevelMove(Point[] grid, Pair pr, Pair, pr0) {
  //  movePair nextMove = null;
  //  int fewestCompetitorMoves = Integer.MAX_VALUE;
  //  int fewestPlayerMoves = Integer.MAX_VALUE;
  //  for (movePair mp : possibleMoves(grid, pr)) {
  //    Point[] newGrid = applyMoveToGrid(grid, mp, this.id);
  //    ArrayList<movePair> possibleOpponentMoves = possibleMoves(newGrid, pr0);
  //    for (movePair op : possibleOpponentMoves) {
  //      Point[] twoLevelGrid = applyMoveToGrid(newGrid, op, opp_id);
  //      nextMove = oneLevelMove(twoLevelGrid, pr, pr0);
  //      ArrayList<movePair> twoLevelMoves = possibleMoves(twoLevelGrid, pr) 
  //      for (movePair twoLevelMove : twoLevelMoves) {
  //        Point[] finalGrid = applyMoveToGrid(twoLevelGrid, twoLevelMove, this.id);
  //        ArrayList<movePair> finalOpponentMoves = possibleMoves(finalGrid, pr0);
  //      }
  //    }
  //  }
  //}

  public movePair nLevelMove(Point[] grid, Pair pr, Pair pr0, int depth) {
  	movePair nextMove = new movePair();
  	movePair oppMove = new movePair();

    depth = depth % 2 == 0 ? depth + 1 : depth;
    int currentDepth = 0;

    Point[] gridCopy = new Point[grid.length];
  	for (int i = 0; i < grid.length; i++) {
  		Point newPoint = new Point(grid[i].x, grid[i].y, grid[i].value, grid[i].owner);
      newPoint.change = grid[i].change;
  		gridCopy[i] = newPoint;
  	}

    while (currentDepth < depth) {
      if (currentDepth % 2 == 0) {
        System.out.println("simulating my move ("+currentDepth+")");
        nextMove = oneLevelMove(gridCopy, pr, pr0);
        if (nextMove != null && nextMove.move)
          gridCopy = applyMoveToGrid(gridCopy, nextMove, this.id);
      } 
      else {
        System.out.println("simulating opponent move ("+currentDepth+")");
        oppMove = oneLevelMove(gridCopy, pr0, pr);
        if (oppMove != null && oppMove.move)
          gridCopy = applyMoveToGrid(gridCopy, oppMove, opp_id);
      }
      currentDepth += 1;
    }

    // here, we need to convert back to pointing at the actual grid
    nextMove.move = true;
    nextMove.src = pointAtGridIndex(grid, nextMove.src.x, nextMove.src.y);
    nextMove.target = pointAtGridIndex(grid, nextMove.target.x, nextMove.target.y);

    return nextMove;
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
   * check all 8 possible moves
   */
  Pair[] directionsForPair(Pair pr) {
  	Pair[] directions = new Pair[8];
  	directions[0] = new Pair(pr); 
  	directions[1] = new Pair(pr.p, -pr.q);
    directions[2] = new Pair(-pr.p, pr.q);
    directions[3] = new Pair(-pr.p, -pr.q);
    directions[4] = new Pair(pr.q, pr.p);
    directions[5] = new Pair(pr.q, -pr.p);
    directions[6] = new Pair(-pr.q, pr.p);
    directions[7] = new Pair(-pr.q, -pr.p);
  	return directions;
  }
  
  Point pointAtGridIndex(Point[] grid, int i, int j) {
  	return grid[i*size + j];
  }
  
  boolean isValidBoardIndex(Point p) {
  	return isValidBoardIndex(p.x, p.y);
  }
  
  boolean isValidBoardIndex(int i, int j) {
  	if (i < 0 || i >= size || j < 0 || j >= size) {
  		return false;
  	}
  	return true;
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

