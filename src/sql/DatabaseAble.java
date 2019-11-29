package sql;

public interface DatabaseAble {
    Table create() throws IsExistedException;
    Table delete(String name) throws NotFoundException;
    boolean alterDatabase(String name) throws NotFoundException;
    boolean changeDatabaseName(String oldOne, String newOne) throws NotFoundException, IsExistedException;
}
