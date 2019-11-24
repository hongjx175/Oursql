package sql;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Table implements Comparable<Table> {
    public ArrayList<Data> data;
    int index;
    String cmp;

    @Contract(pure = true)
    Table() {
        data = new ArrayList<>();
    }

    @Override
    public int compareTo(@NotNull Table rt) {
        return this.cmp.compareToIgnoreCase(rt.cmp);
    }

    boolean equals(@NotNull Table rt) {
        return this.cmp.equalsIgnoreCase(rt.cmp);
    }

}
