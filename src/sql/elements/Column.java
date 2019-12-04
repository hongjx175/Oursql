package sql.elements;

import org.jetbrains.annotations.Contract;
import sql.exceptions.NotFoundException;

public class Column {

    int id;
    int maxLength;
    String name;
    String type;
    boolean can_null;

    @Contract(pure = true)
    public Column(int id, String name, String type, int maxLength, boolean can_null)
        throws NotFoundException {
        this.id = id;
        this.name = name;
        this.type = type;
        this.can_null = can_null;
        if (maxLength != 0) {
            this.maxLength = maxLength;
            return;
        }
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
