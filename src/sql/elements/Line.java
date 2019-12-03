package sql.elements;

import java.util.ArrayList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Line implements Comparable<Line> {

    public ArrayList<Data> data;
    int index;
    boolean isDeleted = false;
    String cmp;

    @Contract(pure = true)
    Line() {
        data = new ArrayList<>();
    }

    @Override
    public int compareTo(@NotNull Line rt) {
        return this.cmp.compareToIgnoreCase(rt.cmp);
    }

    boolean equals(@NotNull Line rt) {
        return this.cmp.equalsIgnoreCase(rt.cmp);
    }

}
