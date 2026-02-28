package com.maksim;


import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;

public class SqrtList<T> implements Serializable {

    private final int blockSize;

    private Node head;

    private int size;

    @Serial
    private static final long serialVersionUID = 1234L;

    public SqrtList(int initialBlockSize) {
        if (initialBlockSize < 2)
            throw new IllegalArgumentException("Block size must be greater or equal than 2");
        this.blockSize = initialBlockSize;
        head = createNode();
    }


    public void set(int index, T el) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);
        Node curr = head;
        int count = index;
        while (curr.size <= count && curr.next != null) {
            count -= curr.size;
            curr = curr.next;
        }
        curr.set(count, el);
        size++;
    }

    public T get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);
        Node curr = head;
        int count = index;
        while (curr.size <= count && curr.next != null) {
            count -= curr.size;
            curr = curr.next;
        }
        return curr.get(count);
    }


    public void remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + size);
        Node curr = head;
        Node prevNode = null;
        int count = index;
        while (curr.size <= count && curr.next != null) {
            prevNode = curr;
            count -= curr.size;
            curr = curr.next;
        }
        curr.remove(prevNode, count);
        size--;
    }


    private Node createNode() {
        return new Node(blockSize + (blockSize << 1));
    }

    private class Node {
        int size;
        Object[] elementData;
        Node next;

        Node(int initialCapacity) {
            elementData = new Object[initialCapacity];
        }

        void set(int index, T el) {
            if (size == elementData.length)
                grow();

            for (int i = size; i > index; i--) {
                elementData[i] = elementData[i - 1];
            }
            elementData[index] = el;
            size++;
            split();
        }

        @SuppressWarnings("unchecked")
        public T get(int i) {
            return (T) elementData[i];
        }

        void remove(Node prev, int index) {
            for (int i = index; i + 1 < size; i++) {
                elementData[i] = elementData[i + 1];
            }
            size--;

            if (size == 0) {
                if (prev != null) {
                    prev.next = next;
                } else if (this.next != null) {
                    head = this.next;
                }
            }
        }

        void split() {
            if (size < blockSize * 2) return;
            Node nextNode = createNode();
            System.arraycopy(elementData, blockSize, nextNode.elementData, 0, size - blockSize);
            nextNode.size = size - blockSize;
            this.size = blockSize;
            nextNode.next = next;
            this.next = nextNode;
        }


        void grow() {
            int newCapacity = blockSize + (blockSize << 1);
            elementData = Arrays.copyOf(elementData, newCapacity);
        }
    }


    @Override
    public String toString() {
        Node cur = head;
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        while (cur != null) {
            for (int i = 0; i + 1 < cur.size; i++) {
                sb.append(cur.elementData[i]);
                sb.append(", ");
            }
            sb.append(cur.elementData[cur.size - 1]);
            cur = cur.next;
            if (cur != null) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
