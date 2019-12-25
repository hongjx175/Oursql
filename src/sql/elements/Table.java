package sql.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sql.ables.TableAble;
import sql.exceptions.DataInvalidException;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;
import sql.exceptions.UnknownSequenceException;
import sql.functions.GetSame;
import sql.functions.Hash;

public class Table implements TableAble {

    public String name;
    public Database database;
    public ArrayList<Column> columnList = new ArrayList<>();
    public ArrayList<HashIndex> indexList = new ArrayList<>();
    private int idCount = 0;
    private int columnCount = 0;
    transient private ArrayList<Line> data;
    private DataIOer dataIOer;

    @Contract(pure = true)
    Table(String name, Database database) {
        this.name = name;
        this.database = database;
        this.data = new ArrayList<>();
        this.dataIOer = new DataIOer(this.database, this);
    }

    public String[] getColumnNames() {
        ArrayList<String> colNames = new ArrayList<String>();
        for (Column c : columnList) {
            if (!c.isDeleted) {
                colNames.add(c.getName());
            }
        }
        return (String[]) colNames.toArray();
    }

    public Column getColumn(String name) {
        for (Column x : columnList) {
            if (x.name.equals(name) && !x.isDeleted) {
                return x;
            }
        }
        return null;
    }

    HashIndex getIndex(String name) {
        for (HashIndex x : indexList) {
            if (x.name.equals(name)) {
                return x;
            }
        }
        return null;
    }

    @Deprecated
    private void insert(@NotNull Data[] str) throws Exception {
        if (str.length != this.columnList.size()) {
            throw new Exception(
                "Length is not correct. Expect " + this.columnList.size() + " , found " + str.length
                    + ".");
        }
        Line newData = new Line();
        newData.id = idCount++;
        newData.data.addAll(Arrays.asList(str));
        this.data.add(newData);
    }

    @Override
    public void addColumn(Column column) throws IsExistedException {
        Column x = this.getColumn(column.name);
        if (x != null) {
            throw new IsExistedException("column", column.name);
        }
        this.columnList.add(column);
    }

    @Override
    public void deleteColumn(String name) throws NotFoundException {
        Column x = this.getColumn(name);
        if (x == null) {
            throw new NotFoundException("column", name);
        }
        x.isDeleted = true;
        x.name += x.hashCode();
    }

    @Override
    public void insert(@NotNull Order[] orders) {
        Line newData = new Line();
        newData.id = idCount++;
        for (Order x : orders) {
            newData.data.add(x.column.id, x.value);
        }
        data.add(newData);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private ArrayList<Integer> selectInIndex(HashMap<Column, Data> map, @NotNull HashIndex index) {
        StringBuilder str = new StringBuilder();
        for (Column x : index.columns) {
            str.append(map.get(x));
        }
        int hash1 = Hash.getHash1(str.toString());
        int hash2 = Hash.getHash2(str.toString());
        ArrayList<Integer> ans1 = index.map1.get(hash1);
        ArrayList<Integer> ans2 = index.map2.get(hash2);
        if (ans1 == null || ans2 == null) {
            return new ArrayList<>();
        }
        return GetSame.getSame(ans1, ans2);
    }

    @NotNull
    private ArrayList<Integer> selectDefault(HashMap<Column, Data> where,
        @NotNull ArrayList<Integer> checkList) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i : checkList) {
            Line x = data.get(i);
            if (x.isDeleted) {
                continue;
            }
            boolean isEqual = true;
            for (Entry<Column, Data> y : where.entrySet()) {
                int index = y.getKey().id;
                if (!y.getValue().getValue()
                    .equalsIgnoreCase(x.data.get(index).getValue())) {
                    isEqual = false;
                    break;
                }
            }
            if (isEqual) {
                result.add(i);
            }
        }
        return result;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private ArrayList<Integer> selectWhereIntoNumbers(@NotNull Order[] where) {
        HashMap<Column, Data> map = new HashMap<>();
        ArrayList<ArrayList<Integer>> checkResult = new ArrayList<>();
        for (Order x : where) {
            map.putIfAbsent(x.column, x.value);
        }
        for (HashIndex index : this.indexList) {
            if (GetSame.getSame(index.columns.toArray(), Order.castNameList(where)).length
                == index.columns
                .size()) {
                HashMap<Column, Data> checkList = new HashMap<>();
                for (Column x : index.columns) {
                    checkList.putIfAbsent(x, map.get(x));
                    map.remove(x);
                }
                checkResult.add(selectInIndex(checkList, index));
            }
        }
        ArrayList<Integer> result = GetSame.getSame(checkResult);
        if (map.size() > 0 && result.size() > 0) {
            return GetSame.getSame(selectDefault(map, result), result);
        }
        return result;
    }

    ArrayList<Line> selectAll(Order[] where, Order[] orderBy)
        throws UnknownSequenceException {
        return this.selectPrivate((Column[]) this.columnList.toArray(), where, orderBy);
    }

    ArrayList<Line> selectPrivate(Column[] columns, Order[] where, Order[] orderBy)
        throws UnknownSequenceException {
        ArrayList<Integer> result = selectWhereIntoNumbers(where);
        ArrayList<Line> array = new ArrayList<>();
        for (int i : result) {
            Line tmp = data.get(i), add = new Line();
            for (Column j : columns) {
                add.data.add(tmp.data.get(j.id));
            }
            if (orderBy.length != 0) {
                for (Order j : orderBy) {
                    String s = tmp.data.get(j.column.id).getValue();
                    int len = s.length();
                    for (int ch = 0; ch < len; ch++) {
                        if (j.value.getValue().equals("1")) {
                            tmp.cmp += s.charAt(ch);
                        } else if (j.value.getValue().equals("-1")) {
                            tmp.cmp += (char) (65536 - s.charAt(ch));
                        } else {
                            throw new UnknownSequenceException();
                        }
                    }
                }
            }
            array.add(add);
        }
        if (orderBy.length != 0) {
            Collections.sort(array);
            for (int j = 1; j < array.size(); j++) {
                if (array.get(j - 1).equals(array.get(j))) {
                    array.remove(j - 1);
                }
            }
        }
        return array;
    }

    @Override
    public void update(@NotNull Order[] set, @NotNull Order[] where) throws DataInvalidException {
        ArrayList<Integer> result = selectWhereIntoNumbers(where);
        for (int x : result) {
            for (Order y : set) {
                Line line = this.data.get(x);
                Data datum = line.data.get(y.column.id);
                datum.setValue(y.column, y.value.getValue());
            }
        }
    }

    @Override
    public void deleteLine(Order[] search) throws NotFoundException {
        ArrayList<Integer> result = selectWhereIntoNumbers(search);
        if (result.size() == 0) {
            throw new NotFoundException("line", "your searching form");
        }
        for (int x : result) {
            this.data.get(x).isDeleted = true;
        }
    }

    public void setIndex(String type, String name, ArrayList<Column> columns)
        throws NotFoundException, IsExistedException {
        ArrayList<Integer> num = new ArrayList<>();
        HashIndex x = getIndex(name);
        if (x != null) {
            throw new IsExistedException("index", name);
        }
        x = new HashIndex();
        for (Column column : columns) {
            if (column == null) {
                throw new NotFoundException("column", name);
            }
            x.columns.add(column);
            num.add(column.id);
        }
        for (int i = 0; i < data.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int id : num) {
                stringBuilder.append(this.data.get(i).data.get(id));
            }
            int hash1 = Hash.getHash1(stringBuilder.toString());
            int hash2 = Hash.getHash2(stringBuilder.toString());
            x.map1.putIfAbsent(hash1, new ArrayList<>());
            x.map2.putIfAbsent(hash2, new ArrayList<>());
            x.map1.get(hash1).add(i);
            x.map2.get(hash2).add(i);
        }
        indexList.add(x);
    }

    @Override
    public void setIndex(String type, String name, String[] columnInOrder)
        throws NotFoundException, IsExistedException {
        ArrayList<Column> columns = new ArrayList<>();
        for (String str : columnInOrder) {
            columns.add(getColumn(str));
        }
        setIndex(type, name, columns);
    }

    @Override
    public void delete(@NotNull Order[] where) throws NotFoundException {
        ArrayList<Integer> result = selectWhereIntoNumbers(where);
        if (result.isEmpty()) {
            throw new NotFoundException("data", "your information");
        }
        for (int x : result) {
            data.remove(x);
        }
    }

    @Deprecated
    public void addColumn(@NotNull String line) throws Exception {
        String[] arr = line.split(" ");
        if (arr.length < 2) {
            throw new Exception("Massage is too short in adding columns.");
        }
        boolean canNull = false;
        for (int i = 2; i < arr.length; i++) {
            if (arr[i - 1].equalsIgnoreCase("NOT")
                && arr[i].equalsIgnoreCase("NULL")) {
                canNull = true;
                break;
            }
        }
        addColumn(arr[0], arr[1], canNull);
    }

    private void addColumn(String name, String type, boolean canNull) throws NotFoundException {
        this.columnList.add(new Column(columnCount++, name, type, canNull));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Table) {
            return this.name.equals(((Table) obj).name);
        }
        return super.equals(obj);
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Column[] getColumnList() {
        return (Column[]) this.columnList.toArray();
    }
}

