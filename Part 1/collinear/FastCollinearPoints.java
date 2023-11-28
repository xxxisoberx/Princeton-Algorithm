import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
   private ArrayList<LineSegment> lineSeg = new ArrayList<>();
   
   // finds all line segments containing 4 or more points
   public FastCollinearPoints(Point[] points) {
      // Three conditions to throw IllegalArgumentException
      if (points == null) throw new IllegalArgumentException();
      for (Point i : points) {
         if (i == null) throw new IllegalArgumentException();
      }
      int len = points.length;
      for (int i = 0; i < len; i++) {
         for (int j = i + 1; j < len; j++) {
            if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
         }
      }

      // find segments containing at least 4 points
      Point[] pointsCopy = Arrays.copyOf(points, len);
      Arrays.sort(pointsCopy);
      Point[] pointsBySlope = Arrays.copyOf(pointsCopy, len);
      for (Point p : pointsCopy) {
         Arrays.sort(pointsBySlope, p.slopeOrder());
         for (int i = 1; i < len;) {
            double currSlope = p.slopeTo(pointsBySlope[i]);
            int step = 1;
            while (i + step < len && p.slopeTo(pointsBySlope[i + step]) == currSlope) {
               step++;
            }
            if (step >= 3 && p.compareTo(min(pointsBySlope, i, i + step - 1)) < 0) {
               lineSeg.add(new LineSegment(p, max(pointsBySlope, i, i + step - 1)));
            }
            i += step;
         }
      }
   }

   // Self-define method "min" which returns the largest Point
   private Point min(Point[] po, int i, int j) {
      int indexOfMin = i;
      for (int k = i + 1; k <= j; k++) {
         if (po[k].compareTo(po[indexOfMin]) < 0) indexOfMin = k;
      }
      return po[indexOfMin];
   }

   // Self-define method "max" which returns the smallest Point
   private Point max(Point[] po, int i, int j) {
      int indexOfMax = i;
      for (int k = i + 1; k <= j; k++) {
         if (po[k].compareTo(po[indexOfMax]) > 0) indexOfMax = k;
      }
      return po[indexOfMax];
   }

   // the number of line segments
   public int numberOfSegments() {
      return lineSeg.size();
   }

   // the line segments
   public LineSegment[] segments() {
      LineSegment[] ls = new LineSegment[lineSeg.size()];
      for (int i = 0; i < lineSeg.size(); i++) {
         ls[i] = lineSeg.get(i);
      }
      return ls;
   }

   public static void main(String[] args) {

      // read the n points from a file
      In in = new In(args[0]);
      int n = in.readInt();
      Point[] points = new Point[n];
      for (int i = 0; i < n; i++) {
         int x = in.readInt();
         int y = in.readInt();
         points[i] = new Point(x, y);
      }

      // draw the points
      StdDraw.enableDoubleBuffering();
      StdDraw.setXscale(0, 32768);
      StdDraw.setYscale(0, 32768);
      for (Point p : points) {
         p.draw();
      }
      StdDraw.show();

      // print and draw the line segments
      FastCollinearPoints collinear = new FastCollinearPoints(points);
      for (LineSegment segment : collinear.segments()) {
         StdOut.println(segment);
         segment.draw();
      }
      StdDraw.show();
   }
}