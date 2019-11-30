package sql.ables;

import sql.elements.Column;
import sql.elements.Order;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

public interface TableAble {
    void addColumn(Column column) throws IsExistedException;
    void deleteColumn(String name) throws NotFoundException;
    void insert(Order[] orders);
    void update(Order[] search, Order[] update) throws NotFoundException;
    void deleteLine(Order[] search) throws NotFoundException;
    void setIndex(int type, String name, String[] columnInOrder) throws NotFoundException, IsExistedException;
}
