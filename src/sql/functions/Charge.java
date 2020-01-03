package sql.functions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import sql.elements.Column;
import sql.elements.Database;
import sql.elements.Line;
import sql.elements.Mysql;
import sql.elements.Order;
import sql.elements.Table;
import sql.exceptions.CommandDeniedException;
import sql.exceptions.DataInvalidException;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotAlterException;
import sql.exceptions.NotFoundException;
import sql.exceptions.TooLongException;
import sql.exceptions.UnknownSequenceException;
import sql.exceptions.WrongCommandException;

public class Charge {

    static Mysql sql;
    Database database;
    ObjectInputStream reader;
    ObjectOutputStream writer;
    StringBuilder stringBuilder;

    public Charge(ObjectInputStream reader, ObjectOutputStream writer) {
        try {
            sql = Mysql.getInstance();
            this.reader = reader;
            this.writer = writer;
        } catch (NotFoundException | IsExistedException e) {
            e.printStackTrace();
        }
    }

    static boolean notCompare(@NotNull String a, String b) {
        return !a.equalsIgnoreCase(b);
    }

    private String getLine() throws IOException {
        try {
            stringBuilder.append("waiting a line\n");
            String str = (String) this.reader.readObject();
            stringBuilder.append(str);
            return str;
        } catch (ClassNotFoundException ignored) {
            return "";
        }
    }

    public String process() throws IOException {
        String cmd = "";
        stringBuilder = new StringBuilder();
        try {
            System.out.println("lalala");
            cmd = this.getLine();
            System.out.println(cmd);
            System.out.println(cmd);
            String[] sp = cmd.split(" ");
            sp[0] = sp[0].toUpperCase();
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
                    throw new WrongCommandException("超出指令范围");
            }
        } catch (Exception e) {
            stringBuilder.append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        writer.writeObject(stringBuilder.toString());
        return cmd;
    }

    //更改
    private void alter(@NotNull String[] s)
        throws WrongCommandException, NotAlterException, NotFoundException, IsExistedException {
        //ALTER TABLE table_name (MODIFY NAME = new_tbname)
        //ALTER DATABASE database_name (MODIFY NAME = new_dbname)
        if ((!s[1].equalsIgnoreCase("TABLE") && !s[1].equalsIgnoreCase("DATABASE"))) {
            throw new WrongCommandException("ATLER:请对TABLE和DATABASE操作");
        }
        if (s.length != 3 && s.length != 7) {
            throw new WrongCommandException("ALTER:指令长度不合法，请注意空格位置");
        }
        if (s.length == 7 && (!s[3].equalsIgnoreCase("MODIFY") || !s[5].equals("=") || !s[4]
            .equalsIgnoreCase("NAME"))) {
            throw new WrongCommandException("ALTER:");
        }
        if (s[1].equalsIgnoreCase("TABLE")) {
            if (database == null) {
                throw new NotAlterException();
            } else {
                database.alterTable(s[2]);
            }
            if (s.length == 7) {
                database.changeTableName(s[2], s[6]);
            }
        }
        if (s[1].equalsIgnoreCase("DATABASE")) {
            database = sql.getDatabase(s[2]);
            if (s.length == 7) {
                database.changeName(s[6]);
            }
        }
    }

    //向表中添加列
    private void add(@NotNull String[] s)
        throws WrongCommandException, NotFoundException, IsExistedException {
        ////ALTER TABLE table_name
        //ADD column_name datatype
        if ((s.length != 3 && s.length != 5) || (s.length == 5 && (!s[3].equalsIgnoreCase("NOT")
            || !s[4].equalsIgnoreCase("NULL")))) {
            throw new WrongCommandException("ADD");
        }
        Column col = new Column(s[1], s[2], s.length != 5);
        database.choosingTable.addColumns(new ArrayList<>(Collections.singletonList(col)));
    }

    public void select(String s)
        throws WrongCommandException, NotAlterException, UnknownSequenceException, DataInvalidException {
        //SELECT 列名称 FROM 表名称 WHERE 列 运算符= 值 ORDER BY 列名 ASC/DESC,列名 ASC/DESC
        //规定指令中ASC和DESC不可省略
        //例：
        //SELECT Company,OrderNumber/* FROM Orders WHERE 列 = 值
        //SELECT Company,OrderNumber FROM Orders ORDER BY Company DESC,OrderNumber ASC
        //SELECT Company,OrderNumber FROM Orders WHERE 列 = 值 ORDER BY Company DESC,OrderNumber ASC

        ArrayList<Line> lines = new ArrayList<Line>();
        if (database == null) {
            throw new NotAlterException();
        }
        String[] sp = s.split("SELECT|FROM|WHERE|ORDER BY");
        Table table = database.getTable(sp[1]);
        String[] sp1 = s.split(" ");
        boolean hasORDER = false, hasWHERE = false;
        for (String value : sp1) {
            if (value.equalsIgnoreCase("ORDER")) {
                hasORDER = true;
            }
            if (value.equalsIgnoreCase("WHERE")) {
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
                    throw new WrongCommandException("SELECT");
                }
                if (hasORDER) {
                    //SELECT * FROM 表名 ORDER BY Company DESC,OrderNumber ASC
                    ArrayList<Order> orderby = new ArrayList<>();
                    String[] orderbys = sp[2].split("[ ,]");
                    if (orderbys.length % 2 != 0) {
                        throw new WrongCommandException("SELECT");
                    }
                    for (int i = 0; i < sp.length - 1; i += 2) {
                        String ord = "1";
                        if (sp[i + 1].equalsIgnoreCase("DESC")) {
                            ord = "-1";
                        }
                        orderby.add(new Order(table, sp[i], ord));
                    }
                    lines = database.selectAll(sp[1], null, orderby);
                }
                if (hasWHERE) {
                    //SELECT * FROM Orders WHERE 列 = 值
                    String[] wheres = sp[2].split("[ =]");
                    ArrayList<Order> where = new ArrayList<Order>();
                    if (wheres.length % 2 != 0) {
                        throw new WrongCommandException("SELECT");
                    }
                    for (int i = 0; i < wheres.length; i += 2) {
                        where.add(new Order(table, wheres[i], wheres[i + 1]));
                    }
                    lines = database.selectAll(sp[1], where, null);
                }
            } else if (sp.length == 4) {//WHERE和ORDER BY都有
                ArrayList<Order> orderby = new ArrayList<Order>();
                String[] orderbys = sp[3].split("[ ,]");
                if (orderbys.length % 2 != 0) {
                    throw new WrongCommandException("SELECT");
                }
                for (int i = 0; i < sp.length - 1; i += 2) {
                    String ord = "1";
                    if (sp[i + 1].equalsIgnoreCase("DESC")) {
                        ord = "-1";
                    }
                    orderby.add(new Order(table, sp[i], ord));
                }
                String[] wheres = sp[2].split("[ =]");
                ArrayList<Order> where = new ArrayList<Order>();
                if (wheres.length % 2 != 0) {
                    throw new WrongCommandException("SELECT");
                }
                for (int i = 0; i < wheres.length; i += 2) {
                    where.add(new Order(table, wheres[i], wheres[i + 1]));
                }
                lines = database.selectAll(sp[1], where, orderby);
            } else {
                throw new WrongCommandException("SELECT");
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
                    throw new WrongCommandException("SELECT");
                }
                if (hasORDER) {
                    //SELECT * FROM 表名 ORDER BY Company DESC,OrderNumber ASC
                    ArrayList<Order> orderby = new ArrayList<>();
                    String[] orderbys = sp[2].split("[ ,]");
                    if (orderbys.length % 2 != 0) {
                        throw new WrongCommandException("SELECT");
                    }
                    for (int i = 0; i < sp.length - 1; i += 2) {
                        String ord = "1";
                        if (sp[i + 1].equalsIgnoreCase("DESC")) {
                            ord = "-1";
                        }
                        orderby.add(new Order(table, sp[i], ord));
                    }
                    lines = database
                        .select(sp[1], new ArrayList<>(Arrays.asList(cols)), null, orderby);
                }
                if (hasWHERE) {
                    //SELECT * FROM Orders WHERE 列 = 值
                    String[] wheres = sp[2].split("[ =]");
                    ArrayList<Order> where = new ArrayList<Order>();
                    if (wheres.length % 2 != 0) {
                        throw new WrongCommandException("SELECT");
                    }
                    for (int i = 0; i < wheres.length; i += 2) {
                        where.add(new Order(table, wheres[i], wheres[i + 1]));
                    }
                    lines = database
                        .select(sp[1], new ArrayList<>(Arrays.asList(cols)), where, null);
                }
            } else if (sp.length == 4) {//WHERE和ORDER BY都有
                ArrayList<Order> orderby = new ArrayList<Order>();
                String[] orderbys = sp[3].split("[ ,]");
                if (orderbys.length % 2 != 0) {
                    throw new WrongCommandException("SELECT");
                }
                for (int i = 0; i < sp.length - 1; i += 2) {
                    String ord = "1";
                    if (sp[i + 1].equalsIgnoreCase("DESC")) {
                        ord = "-1";
                    }
                    orderby.add(new Order(table, sp[i], ord));
                }
                String[] wheres = sp[2].split("[ =]");
                ArrayList<Order> where = new ArrayList<Order>();
                if (wheres.length % 2 != 0) {
                    throw new WrongCommandException("SELECT");
                }
                for (int i = 0; i < wheres.length; i += 2) {
                    where.add(new Order(table, wheres[i], wheres[i + 1]));
                }
                lines = database
                    .select(sp[1], new ArrayList<>(Arrays.asList(cols)), where, orderby);
            } else {
                throw new WrongCommandException("SELECT");
            }
        }
        printLines(lines, table);
        //ArrayList<String> colNames = table.getColumnNames();
    }

    //todo:
    private void printLines(ArrayList<Line> lines, Table table) {
        ArrayList<String> colNames = table.getColumnNames();

        //writer.newLine();
    }

    //删除表中的行  DELETE FROM 表名称 WHERE 列名称 = 值
    private void delete(@NotNull String[] s)
        throws NotAlterException, NotFoundException, WrongCommandException, DataInvalidException {
        if (s.length != 7 || notCompare(s[1], "FROM") || notCompare(s[3], "WHERE") || !s[5]
            .equals("=")) {
            throw new WrongCommandException("DELETE");
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
        table.deleteLine(orders);
    }

    private void update(String[] s)
        throws NotAlterException, WrongCommandException, DataInvalidException {
        //UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
        if (database == null) {
            throw new NotAlterException();
        }
        if (s.length != 10) {
            throw new WrongCommandException("DELETE");
        }
        if (!s[2].equalsIgnoreCase("SET") || !s[4].equals("=") || !s[8].equals("=") || !s[6]
            .equalsIgnoreCase("WHERE")) {
            throw new WrongCommandException("DELETE");
        }
        Table table = database.getTable(s[1]);
        ArrayList<Order> search = new ArrayList<>(
            Collections.singletonList(new Order(table, s[7], s[9])));
        ArrayList<Order> update = new ArrayList<>(
            Collections.singletonList(new Order(table, s[3], s[5])));
        table.update(search, update);
    }

    //删除库、表、列
    private void drop(@NotNull String[] s)
        throws WrongCommandException, NotAlterException, NotFoundException, CommandDeniedException {
        //DROP TABLE 表名称
        //DROP DATABASE 数据库名称
        //ALTER TABLE table_name
        //DROP COLUMN column_name
        if (s.length != 3 || (!s[1].equalsIgnoreCase("TABLE") && !s[1].equalsIgnoreCase("DATABASE")
            && !s[1].equalsIgnoreCase("COLUMN"))) {
            throw new WrongCommandException("DROP");
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
    private void insert(String[] s)
        throws NotAlterException, WrongCommandException, DataInvalidException, IOException, TooLongException {
        //INSERT INTO 语句用于向表格中插入新的行。
        //INSERT INTO 表名称 VALUES 值1,值2,....
        //INSERT INTO 表名称 列1,列2,... VALUES 值1,值2,....//(指定列)
        if (database == null) {
            throw new NotAlterException();
        }
        if (s.length != 5 && s.length != 6) {
            throw new WrongCommandException("INSERT");
        }
        Table table = database.getTable(s[2]);
        ArrayList<Order> orders = new ArrayList<>();
        if (s.length == 5) {
            if (!s[1].equalsIgnoreCase("INTO") || !s[3].equalsIgnoreCase("VALUES")) {
                throw new WrongCommandException("INSERT");
            } else {
                ArrayList<String> colNames = table.getColumnNames();
                String[] values = s[4].split(",");
                if (colNames.size() != values.length) {
                    throw new WrongCommandException("INSERT");
                }
                for (int i = 0; i < colNames.size(); i++) {
                    orders.add(new Order(table, colNames.get(i), values[i]));
                }
            }
        }
        if (s.length == 6) {
            if (!s[1].equalsIgnoreCase("INTO") || !s[4].equalsIgnoreCase("VALUES")) {
                throw new WrongCommandException("INSERT");
            } else {
                String[] colNames = s[3].split(",");
                String[] values = s[5].split(",");
                if (colNames.length != values.length) {
                    throw new WrongCommandException("INSERT");
                }
                for (int i = 0; i < values.length; i++) {
                    orders.add(new Order(table, colNames[i], values[i]));
                }
            }
        }
        table.insertByOrders(orders);
    }

    //建库、表
    private void create(@NotNull String[] s)
        throws WrongCommandException, NotAlterException, IsExistedException, NotFoundException, IOException {
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
            throw new WrongCommandException("CREATE");
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
            this.getLine();
            String str = this.getLine();
            while (!str.equals(")")) {
                String[] sp = str.split(" ");
                if (sp.length != 4 && sp.length != 2) {
                    throw new WrongCommandException("CREATE");
                }
                if (sp.length == 4 && (!sp[2].equalsIgnoreCase("NOT") || !sp[3]
                    .equalsIgnoreCase("NULL"))) {
                    throw new WrongCommandException("CREATE");
                }

                boolean canNull = (sp.length != 4);
                cols.add(new Column(sp[0], sp[1], canNull));
                str = this.getLine();
            }
            // TODO: 2019/12/21 处理index[]
            database.newTable(s[2], cols, null);
        }
    }

    public String getUser() {
        return sql.getUserUsing();
    }
}
