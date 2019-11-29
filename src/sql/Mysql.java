package sql;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Mysql implements SQLAble, Serializable {
    transient private static final String username = "root";
    transient private static final String password = "123456";
    transient private static final String IOFile = "data.db";
    private static HashMap<String, String> passwordList = new HashMap<>();
    transient private static Mysql instance = null;
    ArrayList<Database> databases = new ArrayList<>();
    private Mysql(){
        passwordList.put(username, password);
    }

    public static Mysql getInstance() {
        if(instance == null) instance = new Mysql();
        return instance;
    }

    @Override
    public void load(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(IOFile);
        ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
        instance = (Mysql)inputStream.readObject();
    }

    @Override
    public void save(File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(IOFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(instance);
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
