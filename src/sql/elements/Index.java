package sql.elements;

import java.util.ArrayList;

public abstract class Index {

    public String name;
    public ArrayList<Column> columns = new ArrayList<>();
    public String type;
}
