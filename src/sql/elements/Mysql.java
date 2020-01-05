package sql.elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import sql.exceptions.CommandDeniedException;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

public class Mysql implements Serializable {

    transient private static final String defaultUsername = "root";
    transient private static final String defaultPassword = "123456";
    transient private static final String IOFile = "data.db";
    private static HashMap<String, String> passwordList = new HashMap<>();
    transient private static Mysql instance = null;
    ArrayList<Database> databases = new ArrayList<>();
    transient private String userUsing = null;

    private Mysql() {
        try {
            passwordList.put(defaultUsername, defaultPassword);
            databases.add(new Database("default"));
        } catch (Exception ignored) {
        }
    }

    public static Mysql getInstance() {
        if (instance == null) {
            instance = new Mysql();
        }
        return instance;
    }

    public String getUserUsing() {
        return userUsing;
    }

    public void load(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(IOFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        instance = (Mysql) objectInputStream.readObject();
        objectInputStream.close();
    }

    public void save(File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(IOFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(instance);
        objectOutputStream.close();
    }

    /**
     * to login. Default user: root 123456
     *
     * @param name     username
     * @param password password
     * @return boolean is successfully login
     */
    public boolean login(String name, String password) {
        String passwords = passwordList.get(name);
        if (name == null || password == null) {
            return false;
        }
        if (password.equals(passwords)) {
            userUsing = name;
            return true;
        }
        return false;
    }

    public boolean changePassword(String oldOne, String newOne) {
        if (userUsing == null) {
            return false;
        }
        return passwordList.replace(userUsing, oldOne, newOne);
    }

    public boolean addUser(String name, String password) throws IsExistedException {
        if (userUsing == null) {
            return false;
        }
        if (passwordList.get(name) != null) {
            throw new IsExistedException("user", name);
        }
        passwordList.put(name, password);
        return true;
    }

    public boolean deleteUser(String name) throws NotFoundException, CommandDeniedException {
        if (userUsing == null) {
            return false;
        }
        if (passwordList.get(name) == null) {
            throw new NotFoundException("user", name);
        }
        if (name.equals("root")) {
            throw new CommandDeniedException();
        }
        passwordList.remove(name);
        return true;
    }

    public Database getDatabase(String name) {
        for (Database x : this.databases) {
            if (x.name.equals(name)) {
                return x;
            }
        }
        return null;
    }

    public void newDatabase(String name) throws IsExistedException, NotFoundException {
        Database database = getDatabase(name);
        if (database != null) {
            throw new IsExistedException("database", name);
        }
        database = new Database(name);
        this.databases.add(database);
    }

    public void deleteDatabase(String name) throws NotFoundException, CommandDeniedException {
        Database database = getDatabase(name);
        if (database == null) {
            throw new NotFoundException("database", name);
        } else if (name.equals("default")) {
            throw new CommandDeniedException();
        }
        this.databases.remove(database);
    }
}
