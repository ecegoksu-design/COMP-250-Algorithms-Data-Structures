import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
    private int xCoord;
    private int yCoord;
    private int size; // height/width of the square
    private int level; // the root (outer most block) is at level 0
    private int maxDepth;
    private Color color;
    private Block[] children; // {UR, UL, LL, LR}
    public static Random gen = new Random();


    /*
     * These two constructors are here for testing purposes.
     */
    public Block() {}

    public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
        this.xCoord=x;
        this.yCoord=y;
        this.size=size;
        this.level=lvl;
        this.maxDepth = maxD;
        this.color=c;
        this.children = subBlocks;
    }


    /*
     * Creates a random block given its level and a max depth.
     *
     * xCoord, yCoord, size, and highlighted should not be initialized
     * (i.e. they will all be initialized by default)
     */
    // Creates a random block given its level and a max depth.
    public Block(int lvl, int maxDepth) {
        this.level = lvl;
        this.maxDepth = maxDepth;
        this.children = new Block[0];

        if (this.maxDepth > this.level) {
            double random = gen.nextDouble();
            double chance = Math.exp(-0.25 * this.level);

            if (random < chance) {
                // Subdivide
                this.children = new Block[4];
                for (int i = 0; i < 4; i++ ) {
                    this.children[i] = new Block(this.level + 1, this.maxDepth);

                }
            }

            else {
                // Do not subdivide
                int randomColor = gen.nextInt(4);
                this.color = GameColors.BLOCK_COLORS[randomColor];
            }
        }
        else {
            // At maximum depth
            int randomColor = gen.nextInt(4);
            this.color = GameColors.BLOCK_COLORS[randomColor];
        }
    }


    /*
     * Updates size and position for the block and all of its sub-blocks, while
     * ensuring consistency between the attributes and the relationship of the
     * blocks.
     *
     *  The size is the height and width of the block. (xCoord, yCoord) are the
     *  coordinates of the top left corner of the block.
     */

    private boolean validateSize(int size, int maxDepth) {
        int minimumSize = (int) Math.pow(2, maxDepth);
        return size >= minimumSize && ((size & (minimumSize - 1)) == 0);
    }

    public void updateSizeAndPosition (int size, int xCoord, int yCoord) {
        if (!validateSize(size, this.maxDepth - this.level)) {
            throw new IllegalArgumentException("Enter valid size.");
        }

        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.size = size;

        if(this.children != null && this.children.length > 0){
            int halvedSize = this.size/2;
            if(halvedSize == 1){
                return;
            }
            children[0].updateSizeAndPosition(halvedSize, xCoord + halvedSize, yCoord);
            children[1].updateSizeAndPosition(halvedSize, xCoord, yCoord);
            children[2].updateSizeAndPosition(halvedSize, xCoord,yCoord + halvedSize);
            children[3].updateSizeAndPosition(halvedSize, xCoord + halvedSize,yCoord + halvedSize);
        }
    }




    /*
     * Returns a List of blocks to be drawn to get a graphical representation of this block.
     *
     * This includes, for each undivided Block:
     * - one BlockToDraw in the color of the block
     * - another one in the FRAME_COLOR and stroke thickness 3
     *
     * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
     *
     * The order in which the blocks to draw appear in the list does NOT matter.
     */
    public ArrayList<BlockToDraw> getBlocksToDraw() {
        ArrayList<BlockToDraw> blocksToDraw = new ArrayList<>();

        // Block is a leaf
        if (this.children.length == 0) {
            blocksToDraw.add(new BlockToDraw(this.color, this.xCoord, this.yCoord, this.size, 0));
            blocksToDraw.add(new BlockToDraw(GameColors.FRAME_COLOR, this.xCoord, this.yCoord, this.size, 3));

        // Block has children
        } else {
            for (Block child : this.children) {
                ArrayList<BlockToDraw> childBlocksToDraw = child.getBlocksToDraw();
                blocksToDraw.addAll(childBlocksToDraw);
            }
        }

        return blocksToDraw;
    }

    /*
     * This method is provided and you should NOT modify it.
     */
    public BlockToDraw getHighlightedFrame() {
        return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
    }


    /*
     * Return the Block within this Block that includes the given location
     * and is at the given level. If the level specified is lower than
     * the lowest block at the specified location, then return the block
     * at the location with the closest level value.
     *
     * The location is specified by its (x, y) coordinates. The lvl indicates
     * the level of the desired Block. Note that if a Block includes the location
     * (x, y), and that Block is subdivided, then one of its sub-Blocks will
     * contain the location (x, y) too. This is why we need lvl to identify
     * which Block should be returned.
     *
     * Input validation:
     * - this.level <= lvl <= maxDepth (if not throw exception)
     * - if (x,y) is not within this Block, return null.
     */
    public Block getSelectedBlock(int x, int y, int lvl) {
        if(lvl > this.maxDepth || lvl < 0) {
            throw new IllegalArgumentException("Out of range.");
        }

        if (x < this.xCoord || x >= this.xCoord + this.size || y < this.yCoord || y >= this.yCoord + this.size) {
            return null;
        }

        if (this.level == lvl || this.children.length == 0) {
            return this;
        }

        for (Block child : this.children) {
            Block selected = child.getSelectedBlock(x, y, lvl);
            if (selected != null) {
                return selected;
            }
        }

        return null;
    }

    /*
     * Swaps the child Blocks of this Block.
     * If input is 1, swap vertically. If 0, swap horizontally.
     * If this Block has no children, do nothing. The swap
     * should be propagate, effectively implementing a reflection
     * over the x-axis or over the y-axis.
     *
     */
    public void reflect(int direction) {
        if (direction != 0 && direction != 1) {
            throw new IllegalArgumentException("Direction can be 0 or 1");
        }

        if(this.children.length != 4){
            return;
        }

        Block[] temp = new Block[4];

        if(direction == 0){
            for(int i = 0; i <4; i++){
                switch (i) {
                    case 0 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord, children[i].yCoord+children[i].size);
                        temp[3] = children[i];
                    }
                    case 1 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord, children[i].yCoord + children[i].size);
                        temp[2] = children[i];
                    }
                    case 2 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord, children[i].yCoord -children[i].size);
                        temp[1] = children[i];
                    }
                    case 3 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord, children[i].yCoord - children[i].size);
                        temp[0] = children[i];
                    }
                }
            }
        }

        if(direction == 1){
            for(int i = 0; i<4; i++){
                switch (i) {
                    case 0 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord - children[i].size, children[i].yCoord );
                        temp[1] = children[i];
                    }
                    case 1 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord + children[i].size, children[i].yCoord);
                        temp[0] = children[i];
                    }
                    case 2 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord + children[i].size, children[i].yCoord );
                        temp[3] = children[i];
                    }
                    case 3 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord-children[i].size, children[i].yCoord);
                        temp[2] = children[i];
                    }
                }
            }
        }

        this.children = temp;
        for(int i = 0; i<4; i++) {
            children[i].reflect(direction);
        }
    }



    /*
     * Rotate this Block and all its descendants.
     * If the input is 1, rotate clockwise. If 0, rotate
     * counterclockwise. If this Block has no children, do nothing.
     */
    public void rotate(int direction) {
        if(direction != 0 && direction != 1 ){
            throw new IllegalArgumentException("Direction can be 0 or 1");
        }
        Block[] temp = new Block[4];
        if(this.children.length == 0){
            return;
        }

        if(direction == 0){
            for(int i = 0; i<4; i++){
                switch (i) {
                    case 0 -> {
                        children[i].updateSizeAndPosition(children[i].size, (children[i].xCoord - children[i].size), children[i].yCoord);
                        temp[1] = children[i];
                    }
                    case 1 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord, children[i].yCoord + children[i].size);
                        temp[2] = children[i];
                    }
                    case 2 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord + children[i].size, children[i].yCoord);
                        temp[3] = children[i];
                    }
                    case 3 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord, children[i].yCoord - children[i].size);
                        temp[0] = children[i];
                    }
                }
            }
        }

        if(direction == 1){
            for(int i = 0; i<4; i++){
                switch (i) {
                    case 0 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord, children[i].yCoord + children[i].size);
                        temp[3] = children[i];
                    }
                    case 1 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord + children[i].size, children[i].yCoord);
                        temp[0] = children[i];
                    }
                    case 2 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord, children[i].yCoord - children[i].size);
                        temp[1] = children[i];
                    }
                    case 3 -> {
                        children[i].updateSizeAndPosition(children[i].size, children[i].xCoord-children[i].size, children[i].yCoord);
                        temp[2] = children[i];
                    }
                }

            }
        }

        this.children = temp;
        for(int i = 0; i<4; i++) {
            children[i].rotate(direction);
        }
    }



    /*
     * Smash this Block.
     *
     * If this Block can be smashed,
     * randomly generate four new children Blocks for it.
     * (If it already had children Blocks, discard them.)
     * Ensure that the invariants of the Blocks remain satisfied.
     *
     * A Block can be smashed iff it is not the top-level Block
     * and it is not already at the level of the maximum depth.
     *
     * Return True if this Block was smashed and False otherwise.
     *
     */
    public boolean smash() {
        if(this.level == 0 || this.level == maxDepth){
            return false;
        }
        int updatedSize = this.size / 2;
        Block[] generatedBlocks = new Block[4];
        for(int i = 0; i < 4; i++){
            int updatedXCoord = this.xCoord + (i % 2) * updatedSize;
            int updatedYCoord = this.yCoord + (i / 2) * updatedSize;
            generatedBlocks[i] = new Block(this.level + 1, this.maxDepth);
            generatedBlocks[i].updateSizeAndPosition(updatedSize, updatedXCoord, updatedYCoord);
        }
        this.children = generatedBlocks;
        return true;
    }



    /*
     * Return a two-dimensional array representing this Block as rows and columns of unit cells.
     *
     * Return and array arr where, arr[i] represents the unit cells in row i,
     * arr[i][j] is the color of unit cell in row i and column j.
     *
     * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
     */
    public Color[][] flatten() {
        int unitBlockSize = (int) Math.pow(2, this.maxDepth - this.level);
        Color[][] newColor = new Color[unitBlockSize][unitBlockSize];

        if (this.children.length == 0) {
            for (int i = 0; i < unitBlockSize; i++) {
                for (int j = 0; j < unitBlockSize; j++) {
                    newColor[i][j] = this.color;
                }
            }
        }
        else {
            int halvedSize = unitBlockSize / 2;
            Color[][] topLeft = this.children[1].flatten();
            Color[][] topRight = this.children[0].flatten();
            Color[][] bottomLeft = this.children[2].flatten();
            Color[][] bottomRight = this.children[3].flatten();

            for (int i = 0; i < halvedSize; i++) {
                for (int j = 0; j < halvedSize; j++) {
                    newColor[i][j] = topLeft[i][j];
                    newColor[i][j + halvedSize] = topRight[i][j];
                    newColor[i + halvedSize][j] = bottomLeft[i][j];
                    newColor[i + halvedSize][j + halvedSize] = bottomRight[i][j];
                }
            }
        }
        return newColor;
    }

    // These two get methods have been provided. Do NOT modify them.
    public int getMaxDepth() {
        return this.maxDepth;
    }

    public int getLevel() {
        return this.level;
    }


    /*
     * The next 5 methods are needed to get a text representation of a block.
     * You can use them for debugging. You can modify these methods if you wish.
     */
    public String toString() {
        return String.format("pos=(%d,%d), size=%d, level=%d"
                , this.xCoord, this.yCoord, this.size, this.level);
    }

    public void printBlock() {
        this.printBlockIndented(0);
    }

    private void printBlockIndented(int indentation) {
        String indent = "";
        for (int i=0; i<indentation; i++) {
            indent += "\t";
        }

        if (this.children.length == 0) {
            // it's a leaf. Print the color!
            String colorInfo = GameColors.colorToString(this.color) + ", ";
            System.out.println(indent + colorInfo + this);
        } else {
            System.out.println(indent + this);
            for (Block b : this.children)
                b.printBlockIndented(indentation + 1);
        }
    }

    private static void coloredPrint(String message, Color color) {
        System.out.print(GameColors.colorToANSIColor(color));
        System.out.print(message);
        System.out.print(GameColors.colorToANSIColor(Color.WHITE));
    }

    public void printColoredBlock(){
        Color[][] colorArray = this.flatten();
        for (Color[] colors : colorArray) {
            for (Color value : colors) {
                String colorName = GameColors.colorToString(value).toUpperCase();
                if(colorName.length() == 0){
                    colorName = "\u2588";
                }else{
                    colorName = colorName.substring(0, 1);
                }
                coloredPrint(colorName, value);
            }
            System.out.println();
        }
    }

}
