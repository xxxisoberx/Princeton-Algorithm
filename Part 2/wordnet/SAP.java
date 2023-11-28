import java.util.Iterator;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 || v >= digraph.V() || w > digraph.V()) throw new IllegalArgumentException();
        
        BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfdp2 = new BreadthFirstDirectedPaths(digraph, w);
        int shortestDis = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfdp1.hasPathTo(i) && bfdp2.hasPathTo(i)) {
                int dis = bfdp1.distTo(i) + bfdp2.distTo(i);
                if (dis < shortestDis) shortestDis = dis;
            }
        }
        return (shortestDis == Integer.MAX_VALUE ? -1 : shortestDis);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v >= digraph.V() || w > digraph.V()) throw new IllegalArgumentException();
        
        BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfdp2 = new BreadthFirstDirectedPaths(digraph, w);
        int shortCommonAnces = -1;
        int shortDis = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfdp1.hasPathTo(i) && bfdp2.hasPathTo(i)) {
                int dis = bfdp1.distTo(i) + bfdp2.distTo(i);
                if (dis < shortDis) {
                    shortDis = dis;
                    shortCommonAnces = i;
                }
            }
        }
        return shortCommonAnces;
    }

    // Self-added to check iterable variables
    private void checkIterable(Iterable<Integer> iterable) {
        if (iterable == null) throw new IllegalArgumentException();
        for (Integer i : iterable) {
            if (i == null) throw new IllegalArgumentException();
        }
    }

    // Self-added to check if iterable variable has items
    private boolean hasValue(Iterable<Integer> iterable) {
        Iterator<Integer> iterator = iterable.iterator();
        return iterator.hasNext();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v);
        checkIterable(w);

        if (!hasValue(v) || !hasValue(w)) return -1;
        BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfdp2 = new BreadthFirstDirectedPaths(digraph, w);
        int shortestDis = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfdp1.hasPathTo(i) && bfdp2.hasPathTo(i)) {
                int dis = bfdp1.distTo(i) + bfdp2.distTo(i);
                if (dis < shortestDis) shortestDis = dis;
            }
        }
        return (shortestDis == Integer.MAX_VALUE ? -1 : shortestDis);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        checkIterable(v);
        checkIterable(w);

        if (!hasValue(v) || !hasValue(w)) return -1;
        BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfdp2 = new BreadthFirstDirectedPaths(digraph, w);
        int shortCommonAnces = -1;
        int shortDis = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfdp1.hasPathTo(i) && bfdp2.hasPathTo(i)) {
                int dis = bfdp1.distTo(i) + bfdp2.distTo(i);
                if (dis < shortDis) {
                    shortDis = dis;
                    shortCommonAnces = i;
                }
            }
        }
        return shortCommonAnces;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // for code test
    }
}