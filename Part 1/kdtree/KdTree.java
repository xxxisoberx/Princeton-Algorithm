import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;

    private class Node {
        public Point2D point;
        public Node left, right;
        public Node parent;
        public RectHV rect;
        public boolean depthIsOdd;

        public Node(double x, double y, RectHV rect) {
            point = new Point2D(x, y);
            left = null;
            right = null;
            parent = null;
            this.rect = rect;
        }

        public boolean isVertical() {
            return depthIsOdd;
        }

        public boolean isLeftToParent() {
            return this == this.parent.left;
        }

        // small side is left for vertical node and down for horizontal node
        public RectHV smallSideRect() {
            if (isVertical()) return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            else return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
        }

        public RectHV largeSideRect() {
            if (isVertical()) return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            else return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
        }
    }
    
    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }
    
    // is the set empty? 
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // Self-defined function to compare point and node
    private int compare(Point2D p, Node oldNode) {
        if (oldNode.depthIsOdd) {
            if (Double.compare(p.x(), oldNode.point.x()) < 0) return -1;
            else return 1;
        } else {
            if (Double.compare(p.y(), oldNode.point.y()) < 0) return -1;
            else return 1;
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (root == null) {
            RectHV rect = new RectHV(0, 0, 1, 1);
            root = new Node(p.x(), p.y(), rect);
            root.depthIsOdd = true;
            size++;
            return;
        }

        Node n = root;
        Node pa = n;
        boolean isLeft = true;
        while (n != null) {
            if (n.point.equals(p)) return;
            
            pa = n;
            int cmp = compare(p, n);
            if (cmp < 0) {
                n = n.left;
                isLeft = true;
            }
            else {
                n = n.right;
                isLeft = false;
            }
        }
        RectHV rect = (isLeft ? pa.smallSideRect() : pa.largeSideRect());
        Node newNode = new Node(p.x(), p.y(), rect);
        newNode.parent = pa;
        if (isLeft) pa.left = newNode;
        else pa.right = newNode;
        newNode.depthIsOdd = !pa.depthIsOdd;
        size++;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        if (isEmpty()) return false;
        Node curNode = root;
        while (curNode != null) {
            int cmp = compare(p, curNode);
            if (cmp < 0) curNode = curNode.left;
            else if (Double.compare(p.x(), curNode.point.x()) == 0 && Double.compare(p.y(), curNode.point.y()) == 0) return true;
            else curNode = curNode.right;
        }
        return false;
    }
    
    // Self-defined function to draw all nodes and lines under a certain node
    private void draw(Node n) {
        if (n == null) return;
        
        StdDraw.setPenColor(StdDraw.BLACK);
        n.point.draw();
        if (n == root) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.point.x(), 0, n.point.x(), 1);
        } else {
            boolean isLeft = n.isLeftToParent();
            if (n.depthIsOdd) {
                StdDraw.setPenColor(StdDraw.RED);
                if (isLeft) StdDraw.line(n.point.x(), 0, n.point.x(), n.parent.point.y());
                else StdDraw.line(n.point.x(), n.parent.point.y(), n.point.x(), 1);
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                if (isLeft) StdDraw.line(0, n.point.y(), n.parent.point.x(), n.point.y());
                else StdDraw.line(n.parent.point.x(), n.point.y(), 1, n.point.y());
            }
        }
        draw(n.left);
        draw(n.right);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    // Self-defined function to return Iterable<Point2D> under the Node n
    private void range(RectHV rect, Node n, ArrayList<Point2D> points) {
        if (n == null) return;
        
        if (rect.contains(n.point)) points.add(n.point);
        if (n.isVertical()) {
            if (n.point.x() >= rect.xmin()) range(rect, n.left, points);
            if (n.point.x() <= rect.xmax()) range(rect, n.right, points);
        } else {
            if (n.point.y() >= rect.ymin()) range(rect, n.left, points);
            if (n.point.y() <= rect.ymax()) range(rect, n.right, points);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        
        ArrayList<Point2D> points = new ArrayList<>();
        range(rect, root, points);
        return points;
    }

    // Self-defined function, return nearest point under tree with root "curNode", curNear - from root to curNode
    private Point2D nearest(Point2D p, Node curNode, Point2D curNearPoint, double curNearDis) {
        if (curNode == null) return null;

        Point2D nearPoint = (curNearPoint == null ? null : curNearPoint); // nearest point from till now
        double nearDis = curNearDis; // nearest distance till now
        Point2D thisNearPoint, thatNearPoint; // nearest point in the tree with root node "curNode.left" and "curNode.right"
        Node thisNode, thatNode; // root node of two subtrees of "curNode"
        double curDisToRect; // distance between "p" and the rect of "curNode" that "p" is not inside
        
        // consider curNode.point
        double curNodeDis = curNode.point.distanceSquaredTo(p);
        if (curNodeDis < nearDis) {
            nearDis = curNodeDis;
            nearPoint = curNode.point;
        }
        
        // see which side of curNode is "p" inside
        int cmp = compare(p, curNode);
        if (cmp < 0) {
            curDisToRect = curNode.largeSideRect().distanceSquaredTo(p);
            thisNode = curNode.left;
            thatNode = curNode.right;
        } else {
            curDisToRect = curNode.smallSideRect().distanceSquaredTo(p);
            thisNode = curNode.right;
            thatNode = curNode.left;
        }
        // consider "this side" subtree of curNode
        if (thisNode != null) {
            thisNearPoint = nearest(p, thisNode, nearPoint, nearDis);
            double thisNearDis = thisNearPoint.distanceSquaredTo(p);
            if (thisNearDis < nearDis) {
                nearDis = thisNearDis;
                nearPoint = thisNearPoint;
            }
        }
        // consider "that side" subtree of curNode
        if (curDisToRect < nearDis && thatNode != null) {
            thatNearPoint = nearest(p, thatNode, nearPoint, nearDis);
            double thatNearDis = thatNearPoint.distanceSquaredTo(p);
            if (thatNearDis < nearDis) {
                nearDis = thatNearDis;
                nearPoint = thatNearPoint;
            }
        }
        
        return nearPoint;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return nearest(p, root, null, Double.POSITIVE_INFINITY);
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kd = new KdTree();
        RectHV rect = new RectHV(0.1, 0.2, 0.5, 0.7);
        Point2D p1 = new Point2D(0.3, 0.3);
        Point2D p2 = new Point2D(0.5, 0.5);
        Point2D p3 = new Point2D(0.6, 0.6);
        Point2D p4 = new Point2D(0.5625, 0.0);
        Point2D p5 = new Point2D(0.5625, 0.0);
        
        // test function "size()"
        kd.insert(p1);
        kd.insert(p2);
        kd.insert(p3);
        kd.insert(p4);
        kd.insert(p5);
        
        // test function "contains()"
        System.out.println(kd.contains(new Point2D(0.7, 0.7)));
        // test function "range(rect)"
        for (Point2D p : kd.range(rect)) System.out.println(p.x() + " " + p.y());
        // test function "draw()"
        kd.draw();
    }
}