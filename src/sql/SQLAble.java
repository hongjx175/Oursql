package sql;

import java.io.File;
import java.io.FileNotFoundException;

public interface SQLAble {
    default boolean load(String path) throws FileNotFoundException {
        return this.load(new File(path));
    }
    default boolean save(String path) throws FileNotFoundException {
        return this.save(new File(path));
    }
    boolean load(File file) throws FileNotFoundException;
    boolean save(File file) throws FileNotFoundException;
    boolean login(String name, String password);
    boolean changePassword(String oldOne, String newOne);
    boolean addUser(String name, String password) throws IsExistedException;
    boolean deleteUser(String name) throws NotFoundException, CannotDeleteException;
}
