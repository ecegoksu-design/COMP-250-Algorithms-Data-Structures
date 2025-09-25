Blocky Game

This assignment consists of implementing a visual game in which players apply operations such as rotations to a recursive structure in order to work towards a goal. The main data structure can be represented with a quad-tree (i.e. a tree in which each internal node has exactly four children). The rules are simple, but the game is still challenging to play. The game board resembles a Mondrian painting, and you can easily pick the color scheme that is most appealing to you. This assignment is adapted from an assignment created by Diane Horton and David Liu from University of Toronto.
The Game
The game is played on a randomly-generated game board made of squares of four different colors, such as the following:
Each player is randomly assigned their own goal to work towards: either to create the largest connected “blob” of a given color or to put as much of a given color on the outer perimeter as possible.
There are three kinds of moves a player can do:
• rotating a block (clockwise or counterclockwise),
• reflecting the block horizontally or vertically (i.e. along the x-axis or the y-axis if you imagine the origin of the axes being place in the center of the block), and
• “smashing” a block (giving it four brand-new, randomly generated, sub-blocks).
After each move, the player sees their score, determined by how well they have achieved their goal. The game continues for a certain number of turns, and the player with the highest score at the end is the winner.
3
The Game Board
We will call the game board a “block”. Blocks can be recursively defined; a block is either:
• a square of one color, or
• a square that is subdivided into 4 equal-sized blocks.
The largest block of all, containing the whole structure, is called the top-level block. We say that the top-level block is at level 0 (i.e. it would be at the root of the quad-tree used to represent it). If the top-level block is subdivided, we say that its four sub-blocks are at level 1 (i.e. these would correspond to the children of the root in the aforementioned quad-tree). More generally, if a block at level k is subdivided, its four sub-blocks are at level k + 1.
A board will have a maximum allowed depth, which is the number of levels down it can go. A board with maximum allowed depth 0 would not be fun to play on since it couldn’t be subdivided beyond the top level, meaning that it would be of one solid color. The following board was generated with maximum depth 5:
For scoring, the units of measure are squares the size of the blocks at the maximum allowed depth. We will call these blocks unit cells.
Moves
To achieve their goal, the players are allowed the three type of moves described above. Note that, smashing the top-level block is not allowed, since that would be creating a whole new game. And smashing a unit cell is also not allowed, since it’s already at the maximum allowed depth. What makes moves interesting is that they can be applied to any block at any level. For example, if the player selects the entire top-level block (highlighted) for this board
4

and chooses to rotate it counter-clockwise, the resulting board would be the following:
But if instead, on the original board, they choose to rotate (still counter-clockwise) the block at level 1 in the upper left-hand corner, then the resulting board would be the following:
Finally, if instead they choose to rotate the block a further level down, still sticking to the upper-left 5

corner, they would get this:
Of course, the player could have chosen many other possible blocks on the board and they could have decided to perform a different type of move.
Goals and Scoring
At the beginning of the game, each player is assigned a randomly-generated goal. There are two types of goal:
• Blob goal. The player must aim for the largest “blob” of a given color c. A blob is a group of orthogonally connected blocks with the same color. That is, two blocks are considered connected if their sides touch; touching corners does not count. The player’s score is the number of unit cells in the largest blob of color c.
• Perimeter goal. The player must aim to put the most possible units of a given color c on the outer perimeter of the board. The player’s score is the total number of unit cells of color c that are on the perimeter. There is a premium on corner cells: they count twice towards the score.
Notice that both goals are relative to a particular color. We will call that the target color for the goal.