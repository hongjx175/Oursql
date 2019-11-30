package sql.elements;

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
    public ArrayList<Line> select(String table, Column[] columns, Order[] where, Order[] orderBy) throws Exception {
        Table x = this.getTable(table);
        return x == null ? null : x.selectPrivate(columns, where, orderBy);
    }

    public void changeTableName(String oldOne, String newOne) throws NotFoundException, IsExistedException {
        Table x = this.getTable(oldOne);
        if(x == null) throw new NotFoundException("table", oldOne);
        Table y = this.getTable(newOne);
        if(y != null) throw new IsExistedException("table", newOne);
        x.name = newOne;
    }

    @Override
    public void newTable(String name, Order[] columns, Order index) throws IsExistedException {
        Table x = this.getTable(name);
        if(x != null) throw new IsExistedException("table", name);
        tables.add(new Table(name));
    }

    @Override
    public void deleteTable(String name) throws NotFoundException {
        boolean isFound = false;
        for(int i = 0; i < this.tables.size(); i++) {
            if(this.tables.get(i).name.equals(name)) {
                isFound = true;
                this.tables.remove(i);
                break;
            }
        }
        if(!isFound) throw new NotFoundException("table", name);
    }
}
