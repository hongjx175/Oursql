package sql.ables;

import sql.element.Order;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

public interface DatabaseAble {
    boolean changeTableName(String oldOne, String newOne) throws NotFoundException, IsExistedException;
    boolean newTable(String name, Order[] columns, Order index) throws IsExistedException;
    boolean deleteTable(String name) throws NotFoundException;
}
