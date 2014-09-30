## Wednesday update

Hey guys.

I got a chance to take a crack at the Wednesday deliverable tonight and have implemented the idea we touched on in our meeting.

Right now, the player searches through all cells, calculates all possible moves off of those cells (for each cell, 8 moves can be made assuming they are all valid), and makes the move that captures the biggest number of coins.

The simulator code for this project is a little frustrating, but there are some imporant things that you guys should definitely take a look into.

First and foremost, the game grid is represented as a one-dimensional array, not a two dimensional array. This means that if you want to access row i, column j, instead of doing this: grid[i][j], you need to do this: grid[i * size + j].

Essentially, grid[0 - 31] represent the first row, grid[32 - 63] represent the second row, etc. This was definitely not intuitive to me at first, so I wanted to warn you both about it.

Secondly, there is a difference between Points and Pairs. Points are cells on the game board, Pairs are two Points logically grouped together. So in a movePair mp, mp.x is one of the points, and mp.y is another. if mp.x and mp.y satisfy the constraints of our p and q values, then validateMove(mp) will return true. Furthermore, validateMove(mp) will only return true if mp.x.value = mp.x.value (this is the same as saying you can only pair up two cells that have the same number of coins).

This solutions totally works at this point, and can crush the dummy player. However, one small bug I've observed and am still working on understading is this: at the very end of the game, the simulator sometimes complains that our player has valid moves left, but is not making them. Not totally sure what the issue is, and it seems to not be affecting our player until the last 5 - 10 turns, but I'll try to work it out tomorrow.



Hope this helps,

Ian



