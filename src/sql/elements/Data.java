package sql.elements;

import org.jetbrains.annotations.NotNull;

public class Data {

    String type;
    private String value;

    String getStringValue() {
        return value;
    }

    int getIntValue() {
        return Integer.parseInt(value);
    }

    void setNumber(@NotNull Column column, int number) {
        String numString = Integer.toString(number);
        StringBuilder result = new StringBuilder();
        int spaceLength = column.maxLength - numString.length();
        while (spaceLength-- > 0) {
            result.append("0");
        }
        value = result + numString;
    }

    public void setString(String value) {
        this.value = value;
    }

    public void setLongNumber(long number) {

    }
}
