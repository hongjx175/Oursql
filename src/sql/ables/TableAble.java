package sql.ables;

import sql.element.Column;
import sql.element.Line;
import sql.element.Order;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

import java.util.ArrayList;

public interface TableAble {
    ArrayList<Line> selectPrivate(Order[] where, Order[] orderBy);
    boolean addColumn(Column column) throws IsExistedException;
    boolean deleteColumn(String name) throws NotFoundException;
    boolean insert(Order[] orders);
    boolean update(Order[] search, Order[] update) throws NotFoundException;
    boolean deleteLine(Order[] search) throws NotFoundException;
    boolean setIndex(int type, String[] columnInOrder) throws NotFoundException, IsExistedException;
}
