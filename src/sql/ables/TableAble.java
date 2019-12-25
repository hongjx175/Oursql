package sql.ables;

import org.jetbrains.annotations.NotNull;
import sql.elements.Column;
import sql.elements.Order;
import sql.exceptions.DataInvalidException;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

public interface TableAble {

    void addColumn(Column column) throws IsExistedException;

    void deleteColumn(String name) throws NotFoundException;

    void insert(Order[] orders) throws DataInvalidException;

    void update(Order[] search, Order[] update) throws NotFoundException, DataInvalidException;

    void deleteLine(Order[] search) throws NotFoundException;

    void setIndex(String type, String name, String[] columnInOrder)
        throws NotFoundException, IsExistedException;

    void delete(@NotNull Order[] where) throws NotFoundException;
}
