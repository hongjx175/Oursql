package sql;

public interface DatabaseAble {
    Database create() throws IsExistedException;
    Database delete(String name) throws NotFoundException;
    boolean alterDatabase(String name) throws NotFoundException;
    boolean changeDatabaseName(String oldOne, String newOne) throws NotFoundException, IsExistedException;
}
