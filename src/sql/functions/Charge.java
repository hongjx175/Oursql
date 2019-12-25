package sql.functions;

import java.util.ArrayList;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;
import sql.elements.Column;
import sql.elements.Database;
import sql.elements.Mysql;
import sql.elements.Order;
import sql.elements.Table;
import sql.exceptions.CannotDeleteException;
import sql.exceptions.DataInvalidException;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotAlterException;
import sql.exceptions.NotFoundException;
import sql.exceptions.UnknownSequenceException;
import sql.exceptions.WrongCommandException;

public class Charge {

    static Scanner scan = new Scanner(System.in);
    static Mysql sql = Mysql.getInstance();
    static Database database;


    static boolean notCompare(@NotNull String a, String b) {
        return !a.equalsIgnoreCase(b);
    }

    public static void select(String s)
        throws WrongCommandException, NotAlterException, UnknownSequenceException, DataInvalidException {
        //SELECT 列名称 FROM 表名称 WHERE 列 运算符= 值 ORDER BY 列名 ASC/DESC,列名 ASC/DESC
        //规定指令中ASC和DESC不可省略
        //例：
        //SELECT Company,OrderNumber/* FROM Orders WHERE 列 = 值
        //SELECT Company,OrderNumber FROM Orders ORDER BY Company DESC,OrderNumber ASC
        //SELECT Company,OrderNumber FROM Orders WHERE 列 = 值 ORDER BY Company DESC,OrderNumber ASC

        if (database == null) {
            throw new NotAlterException();
        }
        String[] sp = s.split("SELECT|FROM|WHERE|ORDER BY");
        Table table = database.getTable(sp[1]);
        String[] sp1 = s.split(" ");
        boolean hasORDER = false, hasWHERE = false;
        for (int i = 0; i < sp1.length; i++) {
            if (sp1[i].equalsIgnoreCase("ORDER")) {
                hasORDER = true;
            }
            if (sp1[i].equalsIgnoreCase("WHERE")) {
                hasWHERE = true;
            }
            if (hasORDER && hasWHERE) {
                break;
            }
        }
        if (sp[0].equals("*")) {
            //ArrayList<Line> selectAll(String table, Order[] where, Order[] orderby)
            if (sp.length == 3) {//WHERE和ORDER BY只有一个
                if (hasORDER && hasWHERE) {
                    throw new WrongCommandException();
                }
                if (hasORDER) {
                    //SELECT * FROM 表名 ORDER BY Company DESC,OrderNumber ASC
                    ArrayList<Order> orderby = new ArrayList<Order>();
                    String[] orderbys = sp[2].split(" |,");
                    if (orderbys.length % 2 != 0) {
                        throw new WrongCommandException();
                    }
                    for (int i = 0; i < sp.length - 1; i += 2) {
                        String ord = "1";
                        if (sp[i + 1].equalsIgnoreCase("DESC")) {
                            ord = "-1";
                        }
                        orderby.add(new Order(table, sp[i], ord));
                    }
                    database.selectAll(sp[1], null, (Order[]) orderby.toArray());
                }
                if (hasWHERE) {
                    //SELECT * FROM Orders WHERE 列 = 值
                    String[] wheres = sp[2].split(" |=");
                    ArrayList<Order> where = new ArrayList<Order>();
                    if (wheres.length % 2 != 0) {
                        throw new WrongCommandException();
                    }
                    for (int i = 0; i < wheres.length; i += 2) {
                        where.add(new Order(table, wheres[i], wheres[i + 1]));
                    }
                    database.selectAll(sp[1], (Order[]) where.toArray(), null);
                }
            } else if (sp.length == 4) {//WHERE和ORDER BY都有
                ArrayList<Order> orderby = new ArrayList<Order>();
                String[] orderbys = sp[3].split(" |,");
                if (orderbys.length % 2 != 0) {
                    throw new WrongCommandException();
                }
                for (int i = 0; i < sp.length - 1; i += 2) {
                    String ord = "1";
                    if (sp[i + 1].equalsIgnoreCase("DESC")) {
                        ord = "-1";
                    }
                    orderby.add(new Order(table, sp[i], ord));
                }
                String[] wheres = sp[2].split(" |=");
                ArrayList<Order> where = new ArrayList<Order>();
                if (wheres.length % 2 != 0) {
                    throw new WrongCommandException();
                }
                for (int i = 0; i < wheres.length; i += 2) {
                    where.add(new Order(table, wheres[i], wheres[i + 1]));
                }
                database.selectAll(sp[1], (Order[]) where.toArray(), (Order[]) orderby.toArray());
            } else {
                throw new WrongCommandException();
            }

        } else {
            //ArrayList<Line> select(String table, Column[] columns, Order[] where, Order[] orderBy)
            // SELECT Company,OrderNumber FROM Orders WHERE 列 = 值 ORDER BY Company DESC,OrderNumber ASC
            String[] colName = sp[0].split(",");
            Column[] cols = new Column[colName.length];
            for (int i = 0; i < colName.length; i++) {
                cols[i] = table.getColumn(colName[i]);
            }
            if (sp.length == 3) {//WHERE和ORDER BY只有一个
                if (hasORDER && hasWHERE) {
                    throw new WrongCommandException();
                }
                if (hasORDER) {
                    //SELECT * FROM 表名 ORDER BY Company DESC,OrderNumber ASC
                    ArrayList<Order> orderby = new ArrayList<Order>();
                    String[] orderbys = sp[2].split(" |,");
                    if (orderbys.length % 2 != 0) {
                        throw new WrongCommandException();
                    }
                    for (int i = 0; i < sp.length - 1; i += 2) {
                        String ord = "1";
                        if (sp[i + 1].equalsIgnoreCase("DESC")) {
                            ord = "-1";
                        }
                        orderby.add(new Order(table, sp[i], ord));
                    }
                    database.select(sp[1], cols, null, (Order[]) orderby.toArray());
                }
                if (hasWHERE) {
                    //SELECT * FROM Orders WHERE 列 = 值
                    String[] wheres = sp[2].split(" |=");
                    ArrayList<Order> where = new ArrayList<Order>();
                    if (wheres.length % 2 != 0) {
                        throw new WrongCommandException();
                    }
                    for (int i = 0; i < wheres.length; i += 2) {
                        where.add(new Order(table, wheres[i], wheres[i + 1]));
                    }
                    database.select(sp[1], cols, (Order[]) where.toArray(), null);
                }
            } else if (sp.length == 4) {//WHERE和ORDER BY都有
                ArrayList<Order> orderby = new ArrayList<Order>();
                String[] orderbys = sp[3].split(" |,");
                if (orderbys.length % 2 != 0) {
                    throw new WrongCommandException();
                }
                for (int i = 0; i < sp.length - 1; i += 2) {
                    String ord = "1";
                    if (sp[i + 1].equalsIgnoreCase("DESC")) {
                        ord = "-1";
                    }
                    orderby.add(new Order(table, sp[i], ord));
                }
                String[] wheres = sp[2].split(" |=");
                ArrayList<Order> where = new ArrayList<Order>();
                if (wheres.length % 2 != 0) {
                    throw new WrongCommandException();
                }
                for (int i = 0; i < wheres.length; i += 2) {
                    where.add(new Order(table, wheres[i], wheres[i + 1]));
                }
                database
                    .select(sp[1], cols, (Order[]) where.toArray(), (Order[]) orderby.toArray());
            } else {
                throw new WrongCommandException();
            }
        }
    }

    //删除表中的行  DELETE FROM 表名称 WHERE 列名称 = 值
    private static void delete(String[] s)
        throws NotAlterException, NotFoundException, WrongCommandException, DataInvalidException {
        if (s.length != 7 || notCompare(s[1], "FROM") || notCompare(s[3], "WHERE") || !s[5]
            .equals("=")) {
            throw new WrongCommandException();
        }
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

    private static void update(String[] s)
        throws NotAlterException, WrongCommandException, DataInvalidException {
        //UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
        if (database == null) {
            throw new NotAlterException();
        }
        if (s.length != 10) {
            throw new WrongCommandException();
        }
        if (!s[2].equalsIgnoreCase("SET") || !s[4].equals("=") || !s[8].equals("=") || !s[6]
            .equalsIgnoreCase("WHERE")) {
            throw new WrongCommandException();
        }
        Table table = database.getTable(s[1]);
        //ArrayList<Order> search = new ArrayList<Order>();
        //ArrayList<Order> update = new ArrayList<Order>();
        Order[] search = {new Order(table, s[7], s[9])};
        Order[] update = {new Order(table, s[3], s[5])};
        table.update(search, update);
    }

    //删除库、表、列
    private static void drop(String[] s)
        throws WrongCommandException, NotAlterException, NotFoundException, CannotDeleteException {
        //DROP TABLE 表名称
        //DROP DATABASE 数据库名称
        //ALTER TABLE table_name
        //DROP COLUMN column_name
        if (s.length != 3 || (!s[1].equalsIgnoreCase("TABLE") && !s[1].equalsIgnoreCase("DATABASE")
            && !s[1].equalsIgnoreCase("COLUMN"))) {
            throw new WrongCommandException();
        }
        if (s[1].equalsIgnoreCase("DATABASE")) {
            sql.deleteDatabase(s[2]);
        }
        if (s[1].equalsIgnoreCase("TABLE")) {
            if (database == null) {
                throw new NotAlterException();
            }
            database.deleteTable(s[2]);
        }
        if (s[1].equalsIgnoreCase("COLUMN")) {
            if (database.choosingTable == null) {
                throw new NotAlterException();
            }
            database.choosingTable.deleteColumn(s[2]);
        }
    }

    //插入行
    private static void insert(String[] s)
        throws NotAlterException, WrongCommandException, DataInvalidException {
        //INSERT INTO 语句用于向表格中插入新的行。
        //INSERT INTO 表名称 VALUES 值1,值2,....
        //INSERT INTO 表名称 列1,列2,... VALUES 值1, 值2,....//(指定列)
        if (database == null) {
            throw new NotAlterException();
        }
        if (s.length != 5 && s.length != 6) {
            throw new WrongCommandException();
        }
        Table table = database.getTable(s[2]);
        ArrayList<Order> orders = new ArrayList<>();
        if (s.length == 5) {
            if (!s[1].equalsIgnoreCase("INTO") || !s[3].equalsIgnoreCase("VALUES")) {
                throw new WrongCommandException();
            } else {
                String[] colNames = table.getColumnNames();
                String[] values = s[4].split(",");
                if (colNames.length != values.length) {
                    throw new WrongCommandException();
                }
                for (int i = 0; i < colNames.length; i++) {
                    orders.add(new Order(table, colNames[i], values[i]));
                }
            }
        }
        if (s.length == 6) {
            if (!s[1].equalsIgnoreCase("INTO") || !s[4].equalsIgnoreCase("VALUES")) {
                throw new WrongCommandException();
            } else {
                String[] colNames = s[3].split(",");
                String[] values = s[5].split(",");
                if (colNames.length != values.length) {
                    throw new WrongCommandException();
                }
                for (int i = 0; i < values.length; i++) {
                    orders.add(new Order(table, colNames[i], values[i]));
                }
            }
        }
        table.insert((Order[]) orders.toArray());
    }

    //建库、表
    private static void create(String[] s)
        throws WrongCommandException, NotAlterException, IsExistedException, NotFoundException {
        //CREATE DATABASE database_name
        //CREATE TABLE 表名称
        //(
        //列名称1 数据类型 NOT NULL
        //列名称2 数据类型 String/Number/Integer/CardID/Data/Time
        //列名称3 数据类型
        //....
        //)
        if (s.length != 3 || (!s[1].equalsIgnoreCase("TABLE") && !s[1]
            .equalsIgnoreCase("DATABASE"))) {
            throw new WrongCommandException();
        }
        if (s[1].equalsIgnoreCase("DATABASE")) {//创建新的数据库
            sql.newDatabase(s[2]);
        }
        if (s[1].equalsIgnoreCase(("TABLE"))) {//创建新表
            if (database == null) {
                throw new NotAlterException();
            }
            int colNum = 0;
            ArrayList<Column> cols = new ArrayList<>();
            scan.nextLine();
            String str = scan.nextLine();
            while (!str.equals(")")) {
                String[] sp = str.split(" ");
                if (sp.length != 4 && sp.length != 2) {
                    throw new WrongCommandException();
                }
                if (sp.length == 4 && (!sp[2].equalsIgnoreCase("NOT") || !sp[3]
                    .equalsIgnoreCase("NULL"))) {
                    throw new WrongCommandException();
                }

                boolean canNull = (sp.length != 4);
                cols.add(new Column(colNum++, sp[0], sp[1], canNull));
                str = scan.nextLine();
            }
            // TODO: 2019/12/21 处理index[]
            database.newTable(s[2], (Column[]) cols.toArray(), null);
        }
    }

    //更改
    private static void alter(String[] s) throws WrongCommandException, NotAlterException {
        //ALTER TABLE table_name (MODIFY NAME = new_tbname)
        //ALTER DATABASE database_name (MODIFY NAME = new_dbname)
        if ((!s[1].equalsIgnoreCase("TABLE") && !s[1].equalsIgnoreCase("DATABASE")) || (
            s.length != 3 && s.length != 7)) {
            throw new WrongCommandException();
        }
        if (s.length == 7 && (!s[3].equalsIgnoreCase("MODIFY") || !s[5].equals("=") || !s[4]
            .equalsIgnoreCase("NAME"))) {
            throw new WrongCommandException();
        }
        if (s[1].equalsIgnoreCase("TABLE")) {
            if (database == null) {
                throw new NotAlterException();
            } else {
                database.choosingTable = database.getTable(s[2]);
            }
            if (s.length == 7) {
                //TODO:改名
            }
        }
        if (s[1].equalsIgnoreCase("DATABASE")) {
            database = sql.getDatabase(s[2]);
            if (s.length == 7) {
                //TODO:改名
            }
        }
    }

    //向表中添加列
    private static void add(String[] s)
        throws WrongCommandException, NotFoundException, IsExistedException {
        ////ALTER TABLE table_name
        //ADD column_name datatype
        if ((s.length != 3 && s.length != 5) || (s.length == 5 && (!s[3].equalsIgnoreCase("NOT")
            || !s[4].equalsIgnoreCase("NULL")))) {
            throw new WrongCommandException();
        }
        Column col;
        col = new Column(database.choosingTable.getColumnCount() + 1, s[1], s[2], s.length != 5);
        database.choosingTable.addColumn(col);
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
                    select(cmd);
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
                case "ADD":
                    add(sp);
                    break;
                default:
                    throw new WrongCommandException();
            }
        } catch (Exception e) {
            System.out.println("请输入合法的命令.");
        }
    }
}
