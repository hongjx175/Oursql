package sql.element;

import org.jetbrains.annotations.Contract;

public class Column {
    int id;
    String name;
    String type;
    int max_length;
    boolean is_main_key;
    boolean can_null;

    @Contract(pure = true)
    public Column(int id, String name, String type, int max_length, boolean is_main_key, boolean can_null) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.max_length = max_length;
        this.is_main_key = is_main_key;
        this.can_null = can_null;
    }
}
