package sql.elements;

import org.jetbrains.annotations.Contract;
import sql.exceptions.NotFoundException;

public class Column {

    int id;
    int maxLength;
    String name;
    String type;
    boolean canNull;
    boolean isDeleted;

    public Column(int id, String name, String type, boolean canNull) throws NotFoundException {
        this(id, name, type, 0, canNull);
        switch (type) {
            case "String":
                this.maxLength = 100;
                break;
            case "CardID":
            case "Number":
            case "PhoneNumber":
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
    public Column(int id, String name, String type, int maxLength, boolean canNull) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.canNull = canNull;
        this.maxLength = maxLength;
    }

    Column(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
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
