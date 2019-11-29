package sql.element;

import sql.ables.DatabaseAble;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

import java.io.Serializable;
import java.util.ArrayList;

public class Database implements DatabaseAble, Serializable {
    ArrayList<Table> tables = new ArrayList<>();

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
