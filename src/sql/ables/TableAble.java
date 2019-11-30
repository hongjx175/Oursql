package sql.ables;

import sql.element.Column;
import sql.element.Order;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

public interface TableAble {
    boolean addColumn(Column column) throws IsExistedException;
    boolean deleteColumn(String name) throws NotFoundException;
    boolean insert(Order[] orders);
    boolean update(Order[] search, Order[] update) throws NotFoundException;
    boolean deleteLine(Order[] search) throws NotFoundException;
    boolean setIndex(int type, String[] columnInOrder) throws NotFoundException, IsExistedException;
}
