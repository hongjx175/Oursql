package sql.elements;

import java.util.ArrayList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sql.exceptions.TooLongException;

public class Line implements Comparable<Line> {

    public ArrayList<Data> data;
    int id;
    boolean isDeleted = false;
    String cmp;

    @Contract(pure = true)
    Line() {
        data = new ArrayList<>();
    }

    public Line(ArrayList<Data> dataArray, Column[] columns) throws TooLongException {
        this.data = dataArray;
        lengthCheck(columns);
    }

    @Contract(pure = true)
    private void lengthCheck(@NotNull Column[] columns) throws TooLongException {
        for (int i = 0; i < columns.length; i++) {
            if (data.get(i).value.length > columns[i].maxLength) {
                throw new TooLongException(columns[i].name, columns[i].maxLength);
            }
        }
    }

    @Override
    public int compareTo(@NotNull Line rt) {
        return this.cmp.compareToIgnoreCase(rt.cmp);
    }

    boolean equals(@NotNull Line rt) {
        return this.cmp.equals(rt.cmp);
    }

}
