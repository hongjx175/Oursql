package sql;

import java.util.ArrayList;

public interface TableAble {
    boolean changeTableName(String oldOne, String newOne) throws NotFoundException, IsExistedException;
    boolean newTable(String name, Order[] columns, Order index) throws IsExistedException;
    boolean deleteTable(String name) throws NotFoundException;
    ArrayList<Line> select(Order[] orders, Order[] orderBy);
    boolean addColumn(Column column) throws IsExistedException;
    boolean deleteColumn(String name) throws NotFoundException;
    boolean insert(Order[] orders);
    boolean update(Order[] search, Order[] update) throws NotFoundException;
    boolean deleteLine(Order[] search) throws NotFoundException;
    boolean setIndex(int type, String[] columnInOrder) throws NotFoundException, IsExistedException;
}
