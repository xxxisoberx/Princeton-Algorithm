import java.util.ArrayList;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {
    private boolean solvable = false;
    private int finalMove = 0;
    private ArrayList<Board> unsortedSolution = new ArrayList<>();

    private class SearchNode implements Comparable<SearchNode> {
        public final Board board;
        public final SearchNode parent;
        public final int move;
        public final int manhattan;
        public final int priority;

        public SearchNode(Board board, int move, SearchNode parent) {
            this.board = board;
            this.parent = parent;
            this.move = move;
            this.manhattan = board.manhattan();
            this.priority = this.manhattan + move;
        }

        public int compareTo(SearchNode sn) {
            if (this.priority == sn.priority) return 0;
            if (this.priority > sn.priority) return 1;
            return -1;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<SearchNode> mp = new MinPQ<>();
        SearchNode initialNode = new SearchNode(initial, 0, null);
        mp.insert(initialNode);

        while (true) {
            SearchNode curNode = mp.delMin();
            if (curNode.board.isGoal()) {
                solvable = true;
                finalMove = curNode.move;
                while (curNode.parent != null) {
                    unsortedSolution.add(curNode.board);
                    curNode = curNode.parent;
                }
                unsortedSolution.add(curNode.board);
                break;
            }
            for (Board b : curNode.board.neighbors()) {
                if (curNode.parent == null || !b.equals(curNode.parent.board)) {
                    mp.insert(new SearchNode(b, curNode.move + 1, curNode));
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return finalMove;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        ArrayList<Board> solution = new ArrayList<>();
        for (int i = unsortedSolution.size() - 1; i >= 0; i--) {
            solution.add(unsortedSolution.get(i));
        }
        return solution;
    }

    // test client (see below) 
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
    
        // solve the puzzle
        Solver solver = new Solver(initial);
    
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
