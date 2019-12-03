package sql.elements;

import java.util.ArrayList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Order {

    Column column;
    Data value;

    @Contract(pure = true)
    public Order(@NotNull Table sql, String column, Data value) throws Exception {
        this.column = sql.getColumn(column);
        this.value = value;
    }

    public Order(@NotNull Table sql, String column, int value) throws Exception {
        this.column = sql.getColumn(column);
        this.value = new Data();
        this.value.type = "Integer";
        this.value.setNumber(this.column, value);
    }

    public Order(@NotNull Table sql, String column, String value) throws Exception {
        this.column = sql.getColumn(column);
        this.value = new Data();
        this.value.type = "String";
        this.value.setString(value);
    }

    @NotNull
    public static Column[] castNameList(@NotNull Order[] orders) {
        ArrayList<Column> array = new ArrayList<>();
        for (Order x : orders) {
            array.add(x.column);
        }
        return (Column[]) array.toArray();
    }

    Order(Column column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Order) {
            return this.column.equals(((Order) obj).column);
        }
        return false;
    }
}
