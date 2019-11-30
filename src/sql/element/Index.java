package sql.element;

import java.util.ArrayList;
import java.util.HashMap;

public class Index {
    public String name;
    public Column[] columns;
    public String type;
    public HashMap<String, ArrayList<Line>> map1 = new HashMap<>();
    public HashMap<String, ArrayList<Line>> map2 = new HashMap<>();
}
