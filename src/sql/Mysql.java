package sql;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Mysql implements SQLAble {
    private static final String username = "root";
    private static final String password = "123456";
    private static final String IOFile = "data.db";
    private static HashMap<String, String> passwordList = new HashMap<>();
    private static Mysql instance = null;
    private Mysql(){}
    public static Mysql getInstance() throws IsExistedException {
        if(instance != null) {
            throw new IsExistedException("Mysql", "instance");
        } else return new Mysql();
    }
    @Override
    public boolean load(File file) throws FileNotFoundException {
        return true;
    }

    @Override
    public boolean save(File file) throws FileNotFoundException {
        return true;
    }

    @Override
    public boolean login(String name, String password) {
        return false;
    }

    @Override
    public boolean changePassword(String oldOne, String newOne) {
        return false;
    }

    @Override
    public boolean addUser(String name, String password) throws IsExistedException {
        return false;
    }

    @Override
    public boolean deleteUser(String name) throws NotFoundException, CannotDeleteException {
        return false;
    }
}
