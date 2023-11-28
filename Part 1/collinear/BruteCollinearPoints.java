import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
   private ArrayList<LineSegment> lineSeg = new ArrayList<>();
   
   // finds all line segments containing 4 points
   public BruteCollinearPoints(Point[] points) {
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
      for (int i = 0; i < len; i++) {
         for (int j = i + 1; j < len; j++) {
            for (int k = j + 1; k < len; k++) {
               for (int m = k + 1; m < len; m++) {
                  double s1 = pointsCopy[i].slopeTo(pointsCopy[j]);
                  double s2 = pointsCopy[i].slopeTo(pointsCopy[k]);
                  double s3 = pointsCopy[i].slopeTo(pointsCopy[m]);
                  if (s1 == s2 && s2 == s3) lineSeg.add(new LineSegment(pointsCopy[i], pointsCopy[m]));
               }
            }
         }
      }
   }
   
   // the number of line segments
   public int numberOfSegments() {
      return lineSeg.size();
   }

   // the line segments
   public LineSegment[] segments() {
      LineSegment[] ls = new LineSegment[numberOfSegments()];
      for (int i = 0; i < numberOfSegments(); i++) {
         ls[i] = lineSeg.get(i);
      }
      return ls;
   }

   public static void main(String[] args) {
      Point a = new Point(0, 0);
      Point b = new Point(1, 1);
      Point c = new Point(2, 2);
      Point d = new Point(3, 3);
      Point[] p = {a, b, c, d};
      BruteCollinearPoints brute = new BruteCollinearPoints(p);
      
      System.out.println(brute.numberOfSegments());
      for (LineSegment i : brute.segments()) {
         System.out.println(i.toString());
      }
   }
}