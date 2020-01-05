package sql.functions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

public class BTree<V> implements Serializable {

    private Node<V> root = new Node<>(null, true, 0);

    public void add(long key, V value) {
        NeedRenew newError = root.add(key, value);
        if (newError == null) {
            return;
        }
        Node<V> newRoot = new Node<>(null, false, 0);
        newRoot.refers[0] = root;
        newRoot.refers[1] = newError.node;
        newRoot.hash[0] = newError.hash;
        newRoot.parent = root;
        root = newRoot;
    }

    public V get(long key) {
        return root.get(key);
    }

    public Node<V> getFirstLeaf() {
        return root.getFirstLeaf();
    }

    @Deprecated
    public void delete(long key) {
        this.root.getLeaf(key).delete(key);
    }
}

class Node<V> implements Serializable {

    public static final int n = 5;
    public static final long inf = (long) 1e18;
    boolean isLeaf;
    Object[] refers = new Object[n + 1];
    long[] hash = new long[n];
    Node<V> parent;
    int pos;

    Node(Node<V> parent, boolean isLeaf, int pos) {
        Arrays.fill(hash, inf);
        this.parent = parent;
        this.isLeaf = isLeaf;
        this.pos = pos;
    }

    @SuppressWarnings("unchecked")
    NeedRenew add(long key, V value) {
        int pos = 0;
        while (pos < n && key > hash[pos]) {
            pos++;
        }
        if (this.isLeaf) {
            if (this.refers[n - 1] != null) {
                Node<V> tmp = this.copyNew(new NeedRenew(value, key), this.pos);
                return new NeedRenew(tmp, tmp.hash[0]);
            }
            if (this.refers[pos] == null) {
                this.hash[pos] = key;
                refers[pos] = value;
            } else {
                this.copyAdd(pos, new NeedRenew(value, key));
            }
        } else {
            if (this.refers[pos] == null) {
                System.out.println(key + "  " + this.hash[pos - 1]);
            }
            NeedRenew newError = ((Node<V>) this.refers[pos]).add(key, value);
            if (newError == null) {
                return null;
            }
            if (this.refers[n] != null) {
                Node<V> tmp = this.copyNew(newError, this.pos);
                long x = this.hash[n - 1];
                this.hash[n - 1] = inf;
                return new NeedRenew(tmp, x);
            }
            if (this.refers[pos + 1] == null) {
                this.refers[pos + 1] = newError.node;
                this.hash[pos] = newError.hash;
                return null;
            }
            this.copyAdd(pos, newError);
        }
        return null;
    }

    private int getSize() {
        int lt = 0, rt = n - 2;
        while (lt < rt) {
            int mid = (lt + rt) >> 1;
            if (refers[mid] == null) {
                rt = mid - 1;
            } else {
                lt = mid;
            }
        }
        if (!isLeaf && refers[n - 1] != null) {
            lt++;
        }
        return lt;
    }

    @SuppressWarnings("unchecked")
    V get(long key) {
        int pos = 0;
        while (pos < n && (isLeaf && key > hash[pos] || !isLeaf && key >= hash[pos])) {
            pos++;
        }
        if (this.isLeaf && pos > n - 1 || !this.isLeaf && pos > n) {
            return null;
        }
        if (this.isLeaf && refers[pos] != null) {
            return (V) refers[pos];
        }
        if (refers[pos] == null) {
            return null;
        }
        return ((Node<V>) refers[pos]).get(key);
    }

    @SuppressWarnings("unchecked")
    Node<V> getLeaf(long key) {
        int pos = 0;
        if (this.isLeaf) {
            return this;
        }
        while (pos < n && key >= hash[pos]) {
            pos++;
        }
        if (refers[pos] == null) {
            return null;
        }
        return ((Node<V>) refers[pos]).getLeaf(key);
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    void delete(long key) {
        int pos = 0;
        while (pos < n - 1 && hash[pos] < key) {
            pos++;
        }
        if (refers[pos + 1] != null) {
            for (int i = pos; i < n - 1; i++) {
                refers[i] = refers[i + 1];
                hash[i] = hash[i + 1];
            }
        }
        if (this.getSize() < n / 2) {
            if (this.pos != 0
                && ((Node<V>) this.parent.refers[this.pos - 1]).getSize() + this.getSize() < n) {
                // TODO: 2020/1/3
            }
        }
    }

    Node<V> copyNew(@NotNull NeedRenew newError, int posA) {
        Node<V> tmp = new Node<>(this.parent, this.isLeaf, posA + 1);
        if (this.isLeaf) {
            tmp.refers[n] = this.refers[n];
            this.refers[n] = tmp;
            for (int i = 0; i < (n + 1) / 2; i++) {
                tmp.refers[i] = this.refers[n / 2 + i];
                tmp.hash[i] = this.hash[n / 2 + i];
                this.hash[n / 2 + i] = inf;
                this.refers[n / 2 + i] = null;
            }
        } else {
            long x = this.hash[n / 2];
            for (int i = 0; i < (n + 1) / 2; i++) {
                tmp.refers[i] = this.refers[n / 2 + i + 1];
                this.refers[n / 2 + i + 1] = null;
                if (i > 0) {
                    tmp.hash[i - 1] = this.hash[n / 2 + i];
                }
                this.hash[n / 2 + i] = inf;
            }
            this.hash[n - 1] = x;
        }

        int pos = 0;
        if (newError.hash < (isLeaf ? tmp.hash[0] : this.hash[n - 1])) {
            while (pos < n && newError.hash >= this.hash[pos]) {
                pos++;
            }
            this.copyAdd(pos, newError);
        } else {
            while (pos < n && newError.hash >= tmp.hash[pos]) {
                pos++;
            }
            tmp.copyAdd(pos, newError);
        }
        return tmp;
    }

    void copyAdd(int pos, NeedRenew newError) {
        int index = pos;
        while (this.refers[index] != null) {
            index++;
        }
        if (this.isLeaf) {
            for (int i = index; i > pos; i--) {
                this.refers[i] = this.refers[i - 1];
                this.hash[i] = this.hash[i - 1];
            }
            this.refers[pos] = newError.node;
        } else {
            for (int i = index; i > pos + 1; i--) {
                this.refers[i] = this.refers[i - 1];
                this.hash[i - 1] = this.hash[i - 2];
            }
            this.refers[pos + 1] = newError.node;
        }
        this.hash[pos] = newError.hash;
    }

    @SuppressWarnings("unchecked")
    Node<V> getFirstLeaf() {
        if (this.isLeaf) {
            return this;
        } else {
            return ((Node<V>) this.refers[0]).getFirstLeaf();
        }
    }
}

class Controller {

    public static void main(String[] args) {
        while (true) {
            BTree<Integer> bTree = new BTree<>();
            ArrayList<Integer> array = new ArrayList<>();
            HashMap<Integer, Boolean> map = new HashMap<>();
            Random random = new Random();
            for (int i = 0; i < 100; i++) {
                int x = random.nextInt(1000000);
                while (map.getOrDefault(x, false)) {
                    x = random.nextInt(1000000);
                }
                map.put(i, true);
                bTree.add(x, x);
                array.add(x);
            }
            ArrayList<Integer> cnt = new ArrayList<>();
            for (Integer x : array) {
                Integer w = bTree.get(x);
                if (!x.equals(w)) {
                    cnt.add(x);
                }
            }
            array.sort(Integer::compareTo);
            if (cnt.size() > 0) {
                break;
            }
        }
    }
}

class NeedRenew implements Serializable {

    public Object node;
    public long hash;

    NeedRenew(Object node, long hash) {
        this.node = node;
        this.hash = hash;
    }
}