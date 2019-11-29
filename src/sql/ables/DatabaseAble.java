package sql.ables;

import sql.Table;
import sql.exceptions.CannotDeleteException;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

public interface DatabaseAble {
    Table create() throws IsExistedException;
    Table delete(String name) throws NotFoundException, CannotDeleteException;
    boolean alterDatabase(String name) throws NotFoundException;
    boolean changeDatabaseName(String oldOne, String newOne) throws NotFoundException, IsExistedException;
}
