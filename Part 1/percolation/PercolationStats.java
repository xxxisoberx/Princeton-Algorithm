import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int t;
    private int numGrid;
    private double[] percThre;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("n and trials must be positive!");
        
        t = trials;
        numGrid = n * n;
        percThre = new double[t];

        for (int i = 0; i < t; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int ranRow = StdRandom.uniformInt(1, n + 1);
                int ranCol = StdRandom.uniformInt(1, n + 1);
                perc.open(ranRow, ranCol);
            }
            percThre[i] = perc.numberOfOpenSites() * 1.0 / numGrid;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percThre);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percThre);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(t);
    }

   // test client (see below)
   public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("There must be two inputs!");
            return;
        }

        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats percstats = new PercolationStats(n, t);
        System.out.println("mean                    = " + percstats.mean());
        System.out.println("stddev                  = " + percstats.stddev());
        System.out.println("95% confidence interval = [" + percstats.confidenceLo() + ", " + percstats.confidenceHi() + "]");
   }
}