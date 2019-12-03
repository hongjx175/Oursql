package sql.elements;

import java.util.ArrayList;
import java.util.HashMap;

public class Index {

    public String name;
    public ArrayList<Column> columns;
    public String type;
    public HashMap<Integer, ArrayList<Integer>> map1 = new HashMap<>();
    public HashMap<Integer, ArrayList<Integer>> map2 = new HashMap<>();

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
