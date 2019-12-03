package sql.elements;

import org.jetbrains.annotations.Contract;

public class Column {

    int id;
    String name;
    String type;
    int max_length;
    boolean can_null;

    @Contract(pure = true)
    public Column(int id, String name, String type, int max_length, boolean can_null) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.max_length = max_length;
        this.can_null = can_null;
    }

    Column(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Column) {
            return this.name.equals(((Column) obj).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
