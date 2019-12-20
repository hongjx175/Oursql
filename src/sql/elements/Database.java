package sql.elements;

import java.io.Serializable;
import java.util.ArrayList;
import sql.ables.DatabaseAble;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

public class Database implements DatabaseAble, Serializable {

    transient private static final String historyTableName = "History";
    String name;
    Table choosingTable;
    ArrayList<Table> tables = new ArrayList<>();

    public Database(String name) {
        try {
            Column date = new Column(1, "name", "Date", false);
            Column time = new Column(2, "time", "Time", false);
            Column user = new Column(3, "user", "String", false);
            Column type = new Column(4, "event", "String", false);
            newTable(historyTableName, new Column[]{date, time, user, type}, null);
        } catch (Exception ignored) {
        }
    }

    private Table getTable(String name) {
        for (Table x : tables) {
            if (x.name.equals(name)) {
                return x;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Line> select(String table, Column[] columns, Order[] where, Order[] orderBy)
        throws Exception {
        Table x = this.getTable(table);
        return x == null ? null : x.selectPrivate(columns, where, orderBy);
    }

    @Override
    public void changeTableName(String oldOne, String newOne)
        throws NotFoundException, IsExistedException {
        Table x = this.getTable(oldOne);
        if (x == null) {
            throw new NotFoundException("table", oldOne);
        }
        Table y = this.getTable(newOne);
        if (y != null) {
            throw new IsExistedException("table", newOne);
        }
        x.name = newOne;
    }

    @Override
    public void newTable(String name, Column[] columns, Index[] index)
        throws IsExistedException, NotFoundException {
        Table x = this.getTable(name);
        if (x != null) {
            throw new IsExistedException("table", name);
        }
        x = new Table(name);
        tables.add(x);
        for (Column column : columns) {
            x.addColumn(column);
        }
        if (index != null) {
            for (Index t : index) {
                x.setIndex(t.type, t.name, t.columns);
            }
        }
    }

    @Override
    public void deleteTable(String name) throws NotFoundException {
        boolean isFound = false;
        for (int i = 0; i < this.tables.size(); i++) {
            if (this.tables.get(i).name.equals(name)) {
                isFound = true;
                this.tables.remove(i);
                break;
            }
        }
        if (!isFound) {
            throw new NotFoundException("table", name);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Database) {
            return this.name.equals(((Database) obj).name);
        }
        return super.equals(obj);
    }

    @Override
    public void alterTable(String name) {
        this.choosingTable = getTable(name);
    }
}
