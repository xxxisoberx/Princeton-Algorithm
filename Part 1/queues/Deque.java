import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        Item item;
        Node pre, next;
    }

    private Node first;
    private Node last;
    private int size;
    
    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("input cannot be null!");
        
        Node newNode = new Node();
        newNode.item = item;
        newNode.next = first;

        if (isEmpty()) {
            last = newNode;
        } else {
            first.pre = newNode;
        }

        first = newNode;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("input cannot be null!");
        
        Node newNode = new Node();
        newNode.item = item;
        newNode.pre = last;
    
        if (isEmpty()) {
            first = newNode;
        } else {
            last.next = newNode;
        }
    
        last = newNode;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException("cannot remove item from an empty deque!");
        
        Item retNum = first.item;
        first = first.next;
        size--;

        if (isEmpty()) {
            last = null;
        } else {
            first.pre = null;
        }
        return retNum;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException("cannot remove item from an empty deque!");
        
        Item retNum = last.item;
        last = last.pre;
        size--;

        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        return retNum;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        Node curr = first;
        public boolean hasNext() {
            return curr != null;
        }
        public void remove() {
            throw new UnsupportedOperationException("cannot call remove() on iterator!");
        }
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("iterator has no items!");
            Item i = curr.item;
            curr = curr.next;
            return i;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deq = new Deque<Integer>();
        System.out.println(deq.isEmpty());
        System.out.println(deq.size());
        
        for (int i = 0; i < 3; i++) {
            deq.addFirst(i);
        }
        for (int i = 3; i < 6; i++) {
            deq.addLast(i);
        }
        System.out.println(deq.isEmpty());
        System.out.println(deq.size());
        for (int i : deq) System.out.println(i);

        for (int i = 0; i < 3; i++) {
            System.out.println(deq.removeFirst());
            System.out.println(deq.removeLast());
            System.out.println(deq.size());
        }
    }

}