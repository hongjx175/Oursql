package sql.elements;

import java.util.ArrayList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sql.exceptions.DataInvalidException;
import sql.exceptions.NotFoundException;

public class Order {

    Column column;
    Data value;

    @Contract(pure = true)
    public Order(@NotNull Table table, String column, Data value) {
        this.column = table.getColumn(column);
        this.value = value;
    }

    public Order(@NotNull Table table, String column, String value)
        throws DataInvalidException, NotFoundException {
        this.column = table.getColumn(column);
        if (this.column == null) {
            throw new NotFoundException("column", column);
        }
        this.value = new Data(this.column, value);
        this.value.setValue(this.column, value);
    }

    Order(Column column, String value) throws DataInvalidException {
        this.column = column;
        this.value = new Data(column, value);
    }

    Order(Column column, Data data) {
        this.column = column;
        this.value = data;
    }

    @NotNull
    public static Column[] castNameList(@NotNull ArrayList<Order> orders) {
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
