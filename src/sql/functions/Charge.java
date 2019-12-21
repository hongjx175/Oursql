package sql.functions;

import java.util.ArrayList;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;
import sql.elements.Column;
import sql.elements.Database;
import sql.elements.Mysql;
import sql.elements.Order;
import sql.elements.Table;
import sql.exceptions.NotAlterException;
import sql.exceptions.NotFoundException;
import sql.exceptions.UnknownSequenceException;
import sql.exceptions.WrongCommandException;

public class Charge {

    static Scanner scan = new Scanner(System.in);
    static WrongCommandException problem = new WrongCommandException();
    static Mysql sql = Mysql.getInstance();
    static Database database;
    static Table tablea;


    static boolean compare(@NotNull String a, String b) {
        return a.equalsIgnoreCase(b);
    }

    public static void select(String[] s)
        throws WrongCommandException, NotAlterException, UnknownSequenceException {
        //SELECT 列名称 FROM 表名称 (WHERE 列 运算符 值)
        if (database == null) {
            throw new NotAlterException();
        }
        if (s[1].equals("*")) {//选取表中所有列
            // TODO: 2019/12/21  api selectAll
        } else {
            if (s.length < 4) {
                throw new WrongCommandException();
            }
            String[] cols = s[1].split(",");
            Table table = database.getTable(s[3]);
            ArrayList<Column> getColumn = new ArrayList<>();
            ArrayList<Order> orders = new ArrayList<>();
            for (String str : cols) {
                Column x = table.getColumn(str);
                getColumn.add(x);
            }
            //假设只有and
            for (int i = 5; i < s.length; i++) {
                //TODO:等号应该是单独在一个s[]里的，因为指令等号两边都有空格
                String[] len = s[i].split("=");
                if (len.length != 1) {
                    Order order = new Order(table, len[0], len[1]);
                    orders.add(order);
                }
            }
            database
                .select(s[3], (Column[]) getColumn.toArray(), (Order[]) orders.toArray(), null);
        }
    }

    private static void delete(String[] s)
        throws NotAlterException, NotFoundException, WrongCommandException {//删除表中的行  DELETE FROM 表名称 WHERE 列名称 = 值
        if (s.length != 7 || !compare(s[1], "FROM") || !compare(s[3], "WHERE") || !s[5]
            .equals("=")) {
            throw new WrongCommandException();
        } else {
            if (database == null) {
                throw new NotAlterException();
            }
            Table table = database.getTable(s[2]);
            ArrayList<Order> orders = new ArrayList<>();
            for (int i = 4; i < s.length; i++) {
                if (s[i].equals("=")) {
                    orders.add(new Order(table, s[i - 1], s[i + 1]));
                }
            }
            table.deleteLine((Order[]) orders.toArray());
        }
    }

    private static void update(String[] s) {
        //UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
    }

    private static void drop(String[] s)
        throws WrongCommandException, NotAlterException, NotFoundException {
        //DROP TABLE 表名称
        //DROP DATABASE 数据库名称
        //ALTER TABLE table_name
        //DROP COLUMN column_name
        if (s.length != 3) {
            throw new WrongCommandException();
        }
        if (s[1].equalsIgnoreCase("TABLE")) {
            if (database == null) {
                throw new NotAlterException();
            }
            database.deleteTable(s[2]);
        }
        if (s[1].equalsIgnoreCase("COLUMN")) {
            if (tablea == null) {
                throw new NotAlterException();
            }
            tablea.deleteColumn(s[2]);
        }


    }

    private static void insert(String[] s) {
        //INSERT INTO 语句用于向表格中插入新的行。
        //INSERT INTO 表名称 VALUES (值1, 值2,....)
        //INSERT INTO 表名称 (列1, 列2,...) VALUES (值1, 值2,....)指定列

    }

    private static void create(String[] s) {
        //CREATE DATABASE database_name
        //CREATE TABLE 表名称
        //(
        //列名称1 数据类型, NOT NULL
        //列名称2 数据类型,
        //列名称3 数据类型,
        //....
        //)例子
        // CREATE TABLE Persons
        //(
        //Id_P int,
        //LastName varchar(length) / number(int) / Uid / Date/time
        //FirstName varchar(255),
        //Address varchar(255),
        //City varchar(255)
        //)
        if () {

        }
    }

    public static void alter(String[] s) throws WrongCommandException, NotAlterException {
        //ALTER TABLE table_name
        //ALTER DATABASE database_name
        if (!s[1].equalsIgnoreCase("TABLE") && s[1].equalsIgnoreCase("DATABASE")) {
            throw new WrongCommandException();
        }
        if (s[1].equalsIgnoreCase("TABLE")) {
            if (database == null) {
                throw new NotAlterException();
            } else {
                tablea = database.getTable(s[2]);
            }

        }
        if (s[1].equalsIgnoreCase("DATABASE")) {
            database = sql.getDatabase(s[2]);
        }
    }

    public static void main(String[] args) {
        String cmd;
        try {
            cmd = scan.nextLine();
            String[] sp = cmd.split(" ");
            sp[0] = sp[0].toUpperCase();
            //System.out.println(sp[0]);
            switch (sp[0]) {
                case "ALTER":
                    alter(sp);
                    break;
                case "SELECT":
                    select(sp);
                    break;
                case "DELETE":
                    delete(sp);
                    break;
                case "DROP":
                    drop(sp);
                    break;
                case "UPDATE":
                    update(sp);
                    break;
                case "INSERT":
                    insert(sp);
                    break;
                case "CREATE":
                    create(sp);
                    break;
                default:
                    throw problem;
            }
        } catch (Exception e) {
            System.out.println("请输入合法的命令.");
        }
    }
}
