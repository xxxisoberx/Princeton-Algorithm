import java.util.ArrayList;

public class Board {
    private final int[][] tiles;
    private final int dimension;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles[0].length;
        this.tiles = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }
                                           
    // string representation of this board
    public String toString() {
        StringBuilder repr = new StringBuilder();
        repr.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                repr.append(" " + tiles[i][j]);
            }
            repr.append("\n");
        }
        return repr.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] == 0) continue;
                int goalNumber = i * dimension + j + 1;
                if (tiles[i][j] != goalNumber) hamming++;
            }
        }
        return hamming;
    }

    // Self-defined function "findNumber" to find the position of a number (exccept 0)
    private int[] findNumber(int num) {
        int goalGridRow = (num - 1) / dimension;
        int goalGridCol = num - goalGridRow * dimension - 1;
        int[] numPosition = {goalGridRow, goalGridCol};
        return numPosition;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int currNumber = tiles[i][j];
                if (currNumber == 0) continue;
                int[] positionOfCurrNumber = findNumber(currNumber);
                manhattan += Math.abs(positionOfCurrNumber[0] - i) + Math.abs(positionOfCurrNumber[1] - j);
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || getClass() != y.getClass()) return false;
        
        Board that = (Board) y;
        if (dimension() != that.dimension()) return false;
        for (int i = 0; i < that.dimension(); i++) {
            for (int j = 0; j < that.dimension(); j++) {
                if (tiles[i][j] != that.tiles[i][j]) return false;
            }
        }
        return true;
    }

    // Self-defined "exch" to exchange (i1, j1) and (i2, j2)
    private int[][] exch(int firstRow, int firstCol, int secondRow, int secondCol) {
        int[][] tileCopy = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                tileCopy[i][j] = tiles[i][j];
            }
        }
        
        int temp = tileCopy[firstRow][firstCol];
        tileCopy[firstRow][firstCol] = tileCopy[secondRow][secondCol];
        tileCopy[secondRow][secondCol] = temp;

        return tileCopy;
    }

    // Self-defined "SpaceLocation" to return the position of 0
    private int[] spaceLocation() {
        int[] position = new int[2];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] == 0) {
                    position[0] = i;
                    position[1] = j;
                    return position;
                }
            }
        }
        throw new RuntimeException();
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbor = new ArrayList<>();
        int[] position = spaceLocation();

        if (position[0] > 0) neighbor.add(new Board(exch(position[0], position[1], position[0] - 1, position[1])));
        if (position[0] < dimension - 1) neighbor.add(new Board(exch(position[0], position[1], position[0] + 1, position[1])));
        if (position[1] > 0) neighbor.add(new Board(exch(position[0], position[1], position[0], position[1] - 1)));
        if (position[1] < dimension - 1) neighbor.add(new Board(exch(position[0], position[1], position[0], position[1] + 1)));

        return neighbor;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension - 1; j++) {
                if (tiles[i][j] != 0 && tiles[i][j + 1] != 0) return new Board(exch(i, j, i, j + 1));
            }
        }
        return new Board(tiles); // just for solve the warning, cannot run in real tests.
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] t = new int[2][2];
        t[0][0] = 1;
        t[0][1] = 0;
        t[1][0] = 3;
        t[1][1] = 2;
        Board bo = new Board(t);

        System.out.println(bo);
        System.out.println("dimension: " + bo.dimension());
        System.out.println("hamming: " + bo.hamming() + "\nmanhattan: " + bo.manhattan());
        System.out.println(bo.isGoal());
        System.out.println(bo.twin());

        for (int i : bo.findNumber(3))
            System.out.print(i + " ");
    }
}