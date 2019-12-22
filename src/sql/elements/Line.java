package sql.elements;

import java.util.ArrayList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Line implements Comparable<Line> {

    public ArrayList<Data> data;
    int id;
    boolean isDeleted = false;
    String cmp;

    @Contract(pure = true)
    Line() {
        data = new ArrayList<>();
    }

    public Line(ArrayList<Data> dataArray) {
        this.data = dataArray;
    }

    @Override
    public int compareTo(@NotNull Line rt) {
        return this.cmp.compareToIgnoreCase(rt.cmp);
    }

    boolean equals(@NotNull Line rt) {
        return this.cmp.equals(rt.cmp);
    }

}
