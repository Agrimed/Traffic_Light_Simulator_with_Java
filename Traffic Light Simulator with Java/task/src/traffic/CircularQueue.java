package traffic;

import java.util.Iterator;
import java.util.NoSuchElementException;

class CircularQueue<T> implements Iterable<T> {

    private final T[] circularQueueArray;

    private int front = 0;
    private int rear = 0;
    private final int arraySize;
    private int size = 0;
    //private boolean full = false;

    public CircularQueue(int maxSize) {
        arraySize = maxSize + 1;
        circularQueueArray  = (T[]) new Object[arraySize];
    }

    public boolean add(T data) {
        if (!isFull()) {
            circularQueueArray[rear] = data;
            rear = getNextIndex(rear);
            size++;
            return true;
        }

        throw new IllegalStateException("queue is full");
    }

    public T remove() {
        if (isEmpty()) {
            throw new IllegalStateException("queue is empty");
        }

        T data = circularQueueArray[front];
        front = getNextIndex(front);
        size--;
        return data;
    }

    public T peek()
    {
        T data = null;
        if (!isEmpty()) {
            data = circularQueueArray[front];
        }
        return data;
    }

    private boolean isEmpty() {
        return rear == front;
    }

    public int size() {
        return size;
    }

    private int getNextIndex(int i) {
        return (i + 1) % arraySize;
    }

    private boolean isFull() {
        return getNextIndex(rear) == front;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int current = front;

            @Override
            public boolean hasNext() {
                return current != rear;
            }

            @Override
            public T next() {
                T data = null;
                if (hasNext()) {
                    data = circularQueueArray[current];
                    current = getNextIndex(current);
                    return data;
                }
                throw new NoSuchElementException();
            }
        };
    }
}
