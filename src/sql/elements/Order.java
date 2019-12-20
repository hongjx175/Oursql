package sql.elements;

import java.util.ArrayList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Order {

    Column column;
    Data value;

    @Contract(pure = true)
    public Order(@NotNull Table table, String column, Data value) {
        this.column = table.getColumn(column);
        this.value = value;
    }

    public Order(@NotNull Table table, String column, int value) {
        this.column = table.getColumn(column);
        this.value = new Data();
        this.value.type = "Integer";
        this.value.setNumber(this.column, value);
    }

    public Order(@NotNull Table table, String column, String value) {
        this.column = table.getColumn(column);
        this.value = new Data();
        this.value.type = "String";
        this.value.setString(value);
    }

    Order(Column column) {
        this.column = column;
    }

    @NotNull
    public static Column[] castNameList(@NotNull Order[] orders) {
        ArrayList<Column> array = new ArrayList<>();
        for (Order x : orders) {
            array.add(x.column);
        }
        return (Column[]) array.toArray();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Order) {
            return this.column.equals(((Order) obj).column);
        }
        return false;
    }
}
