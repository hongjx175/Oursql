package sql.functions;

import java.util.ArrayList;
import java.util.Scanner;
import sql.elements.Column;
import sql.elements.Database;
import sql.elements.Mysql;
import sql.elements.Order;
import sql.elements.Table;
import sql.exceptions.NotAlterException;
import sql.exceptions.UnknownSequenceException;
import sql.exceptions.WrongCommandException;

public class Charge {

    static Scanner scan = new Scanner(System.in);
    static WrongCommandException problem = new WrongCommandException();
    static Mysql sql = Mysql.getInstance();
    static Database database;

    static boolean compare(String a, String b) {
        return a.toUpperCase().equals(b);
    }

    public static void select(String[] s)
        throws WrongCommandException, NotAlterException, UnknownSequenceException {
        //SELECT 列名称 FROM 表名称 (WHERE 列 运算符 值)
        if (database == null) {
            throw new NotAlterException();
        }
        if (s[1].equals("*")) {//选取表中所有列

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
            for (int i = 5; i < s.length; i++) {
                String[] len = s[i].split("=");
                Order order = new Order(table, len[0], len[1]);
            }
            database.select(s[3], (Column[]) getColumn.toArray(), (Order[]) orders.toArray(), null);
        }
    }

    private static void delete(String[] s) {//删除表中的行  DELETE FROM 表名称 WHERE 列名称 = 值
        if (s.length != 7 || !compare(s[1], "FROM") || !compare(s[3], "WHERE") || !compare(s[5],
            "=")) {

        }
    }

    private static void update(String[] s) {
        //UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
    }

    private static void drop(String[] s) {
        //DROP TABLE 表名称
        //DROP DATABASE 数据库名称
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
        //LastName varchar(length) / number(int) / Uid / Date:time
        //FirstName varchar(255),
        //Address varchar(255),
        //City varchar(255)
        //)
    }

    public static void main(String[] args) {
        String cmd;
        try {
            cmd = scan.nextLine();
            String[] sp = cmd.split(" ");
            sp[0] = sp[0].toUpperCase();
            //System.out.println(sp[0]);
            switch (sp[0]) {
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
