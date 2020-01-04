package sql.elements;

import java.util.ArrayList;
import java.util.Arrays;
import org.jetbrains.annotations.Contract;
import sql.exceptions.NotFoundException;

public class Column {

    private static ArrayList<String> typeList = new ArrayList<>(
        Arrays.asList("String", "CardID", "Number", "Integer", "PhoneNumber", "Date", "Time"));
    public int id;
    public int maxLength;
    public String name;
    public String type;
    public boolean canNull;
    public boolean isDeleted = false;
    public boolean canShow = true;

    public Column(String name, String type, boolean canNull) throws NotFoundException {
        this(name, type, 0, canNull);
        switch (type) {
            case "String":
                this.maxLength = 100;
                break;
            case "CardID":
            case "Number":
            case "Integer":
            case "PhoneNumber":
                this.maxLength = 20;
                break;
            case "Date":
            case "Time":
                this.maxLength = 15;
                break;
            default:
                throw new NotFoundException("column type", type);
        }
    }

    @Contract(pure = true)
    public Column(String name, String type, int maxLength, boolean canNull)
        throws NotFoundException {
        boolean find = false;
        for (String x : typeList) {
            if (x.equals(type)) {
                find = true;
                break;
            }
        }
        if (!find) {
            throw new NotFoundException("column type", type);
        }
        this.name = name;
        this.type = type;
        this.canNull = canNull;
        this.maxLength = maxLength;
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
