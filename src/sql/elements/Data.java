package sql.elements;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import sql.exceptions.DataInvalidException;

public class Data {

    // TODO: 2019/12/21 change the save type
//    private String value;
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
    char[] value;
    Data next;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line;
        Pattern pattern = Pattern.compile(timeRegex);
        while ((line = scanner.nextLine()).length() != 0) {
            System.out.println(pattern.matcher(line).matches());
        }
    }

    public String getValue() {
        if (this.next == null) {
            return Arrays.toString(this.value);
        }
        return String.valueOf(this.value) + String.valueOf(this.next.value);
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
        this.setString(value);
    }

    private void setString(String value) {
        if (value.length() < size) {
            this.value = value.toCharArray();
        } else {
            this.value = value.substring(0, 100).toCharArray();
            value = value.substring(101);
            this.next = new Data();
            this.next.setString(value);
        }
    }
}
