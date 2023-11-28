import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {
   private final int x;
   private final int y;
   
   // constructs the point (x, y)
   public Point(int x, int y) {
      this.x = x;
      this.y = y;
   }       

   // draws this point
   public void draw() {
      StdDraw.point(x, y);
   }

    // draws the line segment from this point to that point
   public void drawTo(Point that) {
      StdDraw.line(this.x, this.y, that.x, that.y);
   }

   // string representation
   public String toString() {
      return "(" + x + ", " + y + ")";
   }

   // compare two points by y-coordinates, breaking ties by x-coordinates
   public int compareTo(Point that) {
      int yDiff = this.y - that.y;
      int xDiff = this.x - that.x;
      if (yDiff == 0 && xDiff == 0) return 0;
      if (yDiff < 0 || yDiff == 0 && xDiff < 0) return -1;
      return 1;
   }

   // the slope between this point and that point
   public double slopeTo(Point that) {
      if (this.x == that.x) {
         if (this.y == that.y) return Double.NEGATIVE_INFINITY;
         return Double.POSITIVE_INFINITY;
      }
      if (this.y == that.y) return +0.0;
      return 1.0 * (that.y - this.y) / (that.x - this.x); 
   }

   // Self-defined
   private class SlopeComparator implements Comparator<Point> {
      public int compare(Point a, Point b) {
         double s1 = slopeTo(a);
         double s2 = slopeTo(b);
         if (s1 < s2) return -1;
         if (s1 > s2) return 1;
         else return 0;
      }
   }
   
   // compare two points by slopes they make with this point
   public Comparator<Point> slopeOrder() {
      return new SlopeComparator();
   }

   public static void main(String[] args) {
      Point a = new Point(0, 0);
      Point b = new Point(1, 0);
      Point c = new Point(0, 1);
      Point d = new Point(1, 1);

      System.out.println(b.toString());
      System.out.println(c.compareTo(a));
      System.out.println(a.slopeTo(c) == b.slopeTo(d));
      System.out.println(a.slopeOrder().compare(c, d));
   }
}