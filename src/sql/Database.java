package sql;

import sql.ables.DatabaseAble;
import sql.exceptions.CannotDeleteException;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

public class Database implements DatabaseAble {
    @Override
    public Table create() throws IsExistedException {
        return null;
    }

    @Override
    public Table delete(String name) throws NotFoundException, CannotDeleteException {
        return null;
    }

    @Override
    public boolean alterDatabase(String name) throws NotFoundException {
        return false;
    }

    @Override
    public boolean changeDatabaseName(String oldOne, String newOne) throws NotFoundException, IsExistedException {
        return false;
    }
}
