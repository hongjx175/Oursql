package sql.ables;

import sql.element.Table;
import sql.exceptions.CannotDeleteException;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

public interface SQLAble {
    Table create() throws IsExistedException;
    Table delete(String name) throws NotFoundException, CannotDeleteException;
    boolean alterDatabase(String name) throws NotFoundException;
    boolean changeDatabaseName(String oldOne, String newOne) throws NotFoundException, IsExistedException;
}
