import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> set;
    
    // construct an empty set of points
    public PointSET() {
        set = new SET<>();
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : set) p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        
        ArrayList<Point2D> pointInRec = new ArrayList<>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                pointInRec.add(p);
            }
        }
        return pointInRec;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        
        Point2D nearestPoint = set.min();
        double nearestDistance = nearestPoint.distanceSquaredTo(p);
        for (Point2D point : set) {
            double distance = point.distanceSquaredTo(p);
            if (distance < nearestDistance) {
                nearestPoint = point;
                nearestDistance = distance;
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}