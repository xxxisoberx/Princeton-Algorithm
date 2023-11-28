import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] lst;
    private int totalSize = 4;
    private int size;
    private double ratio = 0.25;

    // construct an empty randomized queue
    public RandomizedQueue() {
        lst = (Item[]) new Object[totalSize];
        size = 0;
    }

    private void resize(int newSize) {
        Item[] lstCopy = (Item[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            lstCopy[i] = lst[i];
        }
        lst = lstCopy;
        totalSize = newSize;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (totalSize == size) {
            resize(totalSize * 2);
        }
        lst[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int idxToRemove = StdRandom.uniformInt(size);
        Item retItem = lst[idxToRemove];
        lst[idxToRemove] = lst[size - 1];
        lst[size - 1] = null;
        size--;
        if (size > 0 && 1.0 * size / totalSize <= ratio) resize(totalSize / 2);
        return retItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        return lst[StdRandom.uniformInt(size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int remaining;
        private Item[] lstCopy;

        public RandomizedQueueIterator() {
            lstCopy = (Item[]) new Object[size];
            remaining = size;
            for (int i = 0; i < size; i++) {
                lstCopy[i] = lst[i];
            }
            StdRandom.shuffle(lstCopy);
        }

        public boolean hasNext() {
            return remaining > 0;
        }
        
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return lstCopy[--remaining];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> ranque = new RandomizedQueue<>();
        System.out.println("isEmpty: " + ranque.isEmpty() + " Size: " + ranque.size());
        
        for (int i = 0; i < 5; i++) {
            ranque.enqueue("a" + i);
        }
        System.out.println("isEmpty: " + ranque.isEmpty() + " Size: " + ranque.size());
        
        for (int i = 0; i < 3; i++) {
            System.out.print(ranque.sample() + " ");
        }
        System.out.println();

        for (int i = 0; i < 3; i++) {
            System.out.print(ranque.dequeue() + " ");
        }
        System.out.println("\nisEmpty: " + ranque.isEmpty() + " Size: " + ranque.size());
    }
}