import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // creates variables
    private WeightedQuickUnionUF uf;
    private int opensite;
    private int gridEdge;
    private boolean[][] gridStatus;
    private int[] drow = {-1, 0, 0, 1};
    private int[] dcol = {0, -1, 1, 0};

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive!");
        uf = new WeightedQuickUnionUF(n * n + 2);
        gridStatus = new boolean[n][n];
        opensite = 0;
        gridEdge = n;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                gridStatus[i][j] = false;
            }
        }
    }

    // Self Defined: check if (row, col) are in grid
    private boolean ifInGrid(int row, int col) {
        if (row <= 0 || row > gridEdge || col <= 0 || col > gridEdge) return false;
        return true;
    }

    // Self Defined: calculate from (row, col) to index in uf
    private int calc(int row, int col) {
        return (row - 1) * gridEdge + col;
    }

    // Self Defined: union an open site with its neighbor
    private void connect(int row, int col) {
        int newRow, newCol;
        int curIndex = calc(row, col);
        for (int i = 0; i < 4; i++) {
            newRow = row + drow[i];
            newCol = col + dcol[i];
            if (ifInGrid(newRow, newCol) && isOpen(newRow, newCol)) uf.union(curIndex, calc(newRow, newCol));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!ifInGrid(row, col)) throw new IllegalArgumentException("site (row, col) are not in grid!");
        if (!gridStatus[row - 1][col - 1]) {
            gridStatus[row - 1][col - 1] = true;
            opensite++;
        }

        int curIndex = calc(row, col);
        if (row == 1) uf.union(0, curIndex);
        if (row == gridEdge) uf.union(gridEdge * gridEdge + 1, curIndex);
        connect(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!ifInGrid(row, col)) throw new IllegalArgumentException("site (row, col) are not in grid!");
        return gridStatus[row - 1][col -1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!ifInGrid(row, col)) throw new IllegalArgumentException("site (row, col) are not in grid!");
        return uf.find(calc(row, col)) == uf.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opensite;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(gridEdge * gridEdge + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(3);
        perc.open(1, 3);
        perc.open(2, 3);
        perc.open(3, 3);
        System.out.println(perc.percolates());
    }
}