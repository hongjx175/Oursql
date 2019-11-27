package sql;

public interface TableAble {
    boolean changeTableName(String oldOne, String newOne) throws NotFoundException, IsExistedException;
    boolean newTable(Order[] orders);

}
