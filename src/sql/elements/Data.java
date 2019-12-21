package sql.elements;

import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import sql.exceptions.DataInvalidException;

public class Data {

    private static final String cardIDRegex = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(("
        + "[0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2]["
        + "1-9])|10|20|30|31)\\d{3}$)";
    private static final String numberRegex = "^[0-9]*$";
    private static final String dateRegex =
        "[0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3}";
    private static final String timeRegex = "(20|21|22|23|[0-1]?\\\\d):[0-5]?\\\\d:[0-5]?\\\\d$";
    private static final String phoneNumberRegex = "^1[3|4|5|7|8][0-9]\\d{4,8}$";

    // TODO: 2019/12/21 change the save type
    String type;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(@NotNull Column column, String value) throws DataInvalidException {
        String type = column.type;
        Pattern pattern;
        switch (type) {
            case "Number":
                pattern = Pattern.compile(numberRegex);
                break;
            case "CardID":
                pattern = Pattern.compile(cardIDRegex);
                break;
            case "Date":
                pattern = Pattern.compile(dateRegex);
                break;
            case "Time":
                pattern = Pattern.compile(timeRegex);
                break;
            case "PhoneNumber":
                pattern = Pattern.compile(phoneNumberRegex);
                break;
            default:
                pattern = Pattern.compile("*");
        }
        if (!pattern.matcher(value).matches()) {
            throw new DataInvalidException(type, value);
        }
        this.value = value;
    }
}
