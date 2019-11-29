package sql;

import sql.ables.SQLAble;
import sql.exceptions.CannotDeleteException;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Mysql implements SQLAble, Serializable {
    transient private static final String defaultUsername = "root";
    transient private static String defaultPassword = "123456";
    transient private static final String IOFile = "data.db";
    private static HashMap<String, String> passwordList = new HashMap<>();
    transient private static Mysql instance = null;
    transient public static String userUsing = null;
    ArrayList<Database> databases = new ArrayList<>();
    private Mysql(){
        passwordList.put(defaultUsername, defaultPassword);
    }

    public static Mysql getInstance() {
        if(instance == null) instance = new Mysql();
        return instance;
    }

    @Override
    public void load(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(IOFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        instance = (Mysql)objectInputStream.readObject();
        objectInputStream.close();
    }

    @Override
    public void save(File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(IOFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(instance);
        objectOutputStream.close();
    }

    @Override
    public boolean login(String name, String password) throws NotFoundException {
        String passwords = passwordList.get(name);
        if(passwords == null) throw new NotFoundException("username", name);
        else if(password.equals(passwords)) {
            userUsing = name;
            return true;
        } else return false;

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
