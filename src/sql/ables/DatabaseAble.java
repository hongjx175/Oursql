package sql.ables;

import sql.element.Line;
import sql.element.Order;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

import java.util.ArrayList;

public interface DatabaseAble {
    ArrayList<Line> select(String table, Order[] where, Order[] orderBy);
    void changeTableName(String oldOne, String newOne) throws NotFoundException, IsExistedException;
    void newTable(String name, Order[] columns, Order index) throws IsExistedException;
    void deleteTable(String name) throws NotFoundException;
}
