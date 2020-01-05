package sql.elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;
import sql.exceptions.UnknownSequenceException;

public class Database implements Serializable {

    transient private static final String historyTableName = "History";
    public Table choosingTable;
    public String name;
    ArrayList<Table> tables = new ArrayList<>();

    public Database(String name) throws NotFoundException, IsExistedException {
        this.name = name;
        Column date = new Column("date", "Date", false);
        Column time = new Column("time", "Time", false);
        Column user = new Column("user", "String", false);
        Column type = new Column("event", "String", false);
        newTable(historyTableName, new ArrayList<>(Arrays.asList(date, time, user, type)),
            null, false);
    }

    public Table getTable(String name) {
        for (Table x : tables) {
            if (x.name.equals(name)) {
                return x;
            }
        }
        return null;
    }

    public ArrayList<Line> select(String table, ArrayList<Column> columns, ArrayList<Order> where,
        ArrayList<Order> orderBy) throws UnknownSequenceException {
        Table x = this.getTable(table);
        return x == null ? null : x.selectPrivate(columns, where, orderBy);
    }

    public ArrayList<Line> selectAll(String table, ArrayList<Order> where, ArrayList<Order> orderby)
        throws UnknownSequenceException {
        Table x = this.getTable(table);
        return x == null ? null : x.selectAll(where, orderby);
    }

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

    public void changeName(String newName) {
        this.name = newName;
    }

    public void newTable(String name, ArrayList<Column> columns, ArrayList<HashIndex> index,
        boolean reset) throws IsExistedException {
        Table x = this.getTable(name);
        if (x != null) {
            throw new IsExistedException("table", name);
        }
        x = new Table(name, this, reset);
        tables.add(x);
        x.addColumns(columns);
        if (index != null) {
            for (HashIndex t : index) {
                try {
                    x.setIndexByColumns(t.type, t.name, t.columns);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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

    public void alterTable(String name) {
        this.choosingTable = getTable(name);
    }
}
