package sql.elements;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sql.exceptions.DataInvalidException;

public class Data {

    static public final int size = 100;
    private static final String cardIDRegex = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(("
        + "[0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2]["
        + "1-9])|10|20|30|31)\\d{3}$)";
    private static final String numberRegex = "^[0-9]*$";
    private static final String dateRegex =
        "((\\d{2}(([02468][048])|([13579][26]))[\\-]((((0?[13578])|(1[02]))[\\-]((0?[1-9])|([1-2][0"
            + "-9])|(3[01])))|(((0?[469])|(11))[\\-]((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-]((0?[1-"
            + "9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-]((((0?[1357"
            + "8])|(1[02]))[\\-]((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-]((0?[1-9])|"
            + "([1-2][0-9])|(30)))|(0?2[\\-]((0?[1-9])|(1[0-9])|(2[0-8]))))))";
    private static final String timeRegex =
        "(((0?[0-9])|([1][0-9])|([2][0-4]))\\\\:([0-5]?[0-9])((\\\\s)|(\\\\:([0-5]?[0-9]))))?$";
    private static final String phoneNumberRegex = "^1[3|4|5|7|8][0-9]\\d{4,8}$";
    private String string;

    @Contract(pure = true)
    public Data(@NotNull Column column, @NotNull String string) throws DataInvalidException {
        this.setValue(column, string);
    }

    @Deprecated
    public Data(String string) {
        this.string = string;
    }

    private static boolean validCheck(String str, @NotNull String type) {
        switch (type) {
            case "Number":
                return str.matches(numberRegex);
            case "CardID":
                return str.matches(cardIDRegex);
            case "Date":
                return str.matches(dateRegex);
            case "Time":
                return str.matches(timeRegex);
            case "PhoneNumber":
                return str.matches(phoneNumberRegex);
        }
        return true;
    }

    public String getValue() {
        return this.string;
    }

    public void setValue(@NotNull Column column, String value) throws DataInvalidException {
        String type = column.type;
        if (!validCheck(value, type)) {
            throw new DataInvalidException(type, value);
        }
        this.string = value;
    }

}
