package sql.elements;

import java.util.ArrayList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sql.exceptions.TooLongException;

public class Line implements Comparable<Line> {

    public ArrayList<Data> data;
    StringBuilder cmp;

    @Contract(pure = true)
    Line() {
        data = new ArrayList<>();
    }

    public Line(ArrayList<Data> dataArray, ArrayList<Column> columns) throws TooLongException {
        this.data = dataArray;
        lengthCheck(columns);
    }

    @Contract(pure = true)
    private void lengthCheck(@NotNull ArrayList<Column> columns) throws TooLongException {
        for (int i = 0; i < columns.size(); i++) {
            if (data.get(i) == null) {
                continue;
            }
            if (data.get(i).getValue().length() > columns.get(i).maxLength) {
                throw new TooLongException(columns.get(i).name, columns.get(i).maxLength);
            }
        }
    }

    @Override
    public int compareTo(@NotNull Line rt) {
        return this.cmp.toString().compareToIgnoreCase(rt.cmp.toString());
    }

    boolean equals(@NotNull Line rt) {
        return this.cmp.toString().equals(rt.cmp.toString());
    }

}
