package sql.elements;

import org.jetbrains.annotations.Contract;
import sql.exceptions.NotFoundException;

public class Column {

    int id;
    int maxLength;
    String name;
    String type;
    boolean can_null;

    public Column(int id, String name, String type, boolean can_null) throws NotFoundException {
        this(id, name, type, 0, can_null);
        switch (type) {
            case "String":
            case "Number":
                this.maxLength = 100;
                break;
            case "Integer":
            case "CardID":
                this.maxLength = 20;
                break;
            case "Date":
            case "Time":
                this.maxLength = 15;
            default:
                throw new NotFoundException("column type", type);
        }
    }

    @Contract(pure = true)
    public Column(int id, String name, String type, int maxLength, boolean can_null) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.can_null = can_null;
        this.maxLength = maxLength;
    }

    Column(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Column) {
            return this.name.equals(((Column) obj).name);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
