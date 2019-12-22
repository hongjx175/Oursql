package sql.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import org.jetbrains.annotations.NotNull;

public class BTree<V> {

    private Node<V> root = new Node<>(null, true);

    public void add(long key, V value) {
        NeedRenew newError = root.add(key, value);
        if (newError == null) {
            return;
        }
        Node<V> newRoot = new Node<>(null, false);
        newRoot.refers[0] = root;
        newRoot.refers[1] = newError.node;
        newRoot.hash[0] = newError.hash;
        newRoot.parent = root;
        root = newRoot;
    }

    public V get(long key) {
        return root.get(key);
    }
}

class Node<V> {

    public static final int n = 123;
    public static final long inf = (long) 1e18;
    boolean isLeaf;
    Object[] refers = new Object[n + 1];
    long[] hash = new long[n];
    Node<V> parent;

    Node(Node<V> parent, boolean isLeaf) {
        Arrays.fill(hash, inf);
        this.isLeaf = isLeaf;
    }

    @SuppressWarnings("unchecked")
    NeedRenew add(long key, V value) {
        int pos = 0;
        while (pos < n && key > hash[pos]) {
            pos++;
        }
        if (this.isLeaf) {
            if (this.refers[n - 1] != null) {
                Node<V> tmp = this.copyNew(new NeedRenew(value, key));
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
                Node<V> tmp = this.copyNew(newError);
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

    void delete(long key) {
        // TODO: 2019/12/21 delete
    }

    Node<V> copyNew(@NotNull NeedRenew newError) {
        Node<V> tmp = new Node<>(this.parent, this.isLeaf);
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
}

class Controller {

    public static void main(String[] args) {
        BTree<Integer> bTree = new BTree<>();
        ArrayList<Integer> array = new ArrayList<>();
        HashMap<Integer, Boolean> map = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
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
        System.out.println(cnt);
    }
}

class NeedRenew {

    public Object node;
    public long hash;

    NeedRenew(Object node, long hash) {
        this.node = node;
        this.hash = hash;
    }
}