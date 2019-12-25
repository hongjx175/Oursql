package sql.ables;

import sql.elements.Table;
import sql.exceptions.CommandDeniedException;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

public interface SQLAble {

    Table create() throws IsExistedException;

    Table delete(String name) throws NotFoundException, CommandDeniedException;

    boolean alterDatabase(String name) throws NotFoundException;

    boolean changeDatabaseName(String oldOne, String newOne)
        throws NotFoundException, IsExistedException;
}
