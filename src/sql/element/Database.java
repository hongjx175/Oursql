package sql.element;

import sql.ables.DatabaseAble;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

import java.io.Serializable;
import java.util.ArrayList;

public class Database implements DatabaseAble, Serializable {
    ArrayList<Table> tables = new ArrayList<>();

    private Table getTable(String name) {
        for(Table x: tables) {
            if(x.name.equals(name)) return x;
        }
        return null;
    }
    @Override
    public ArrayList<Line> select(String table, Order[] where, Order[] orderBy) {
        Table x = this.getTable(table);
        return x == null ? null : x.selectPrivate(where, orderBy);
    }

    @Override
    public boolean changeTableName(String oldOne, String newOne) throws NotFoundException, IsExistedException {
        return false;
    }

    @Override
    public boolean newTable(String name, Order[] columns, Order index) throws IsExistedException {
        return false;
    }

    @Override
    public boolean deleteTable(String name) throws NotFoundException {
        return false;
    }
}
