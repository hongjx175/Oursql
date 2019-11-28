package sql;

import java.util.ArrayList;

public interface TableAble {
    boolean changeTableName(String oldOne, String newOne) throws NotFoundException, IsExistedException;
    boolean newTable(String name, Order[] columns, Order index) throws IsExistedException;
    boolean deleteTable(String name) throws NotFoundException;
    ArrayList<Line> select(Order[] orders, Order[] orderBy, String[] distinctColumnName);
    boolean addColumn(Column column) throws IsExistedException;
    boolean setIndex(int type, String[] columnInOrder) throws NotFoundException, IsExistedException;
}
