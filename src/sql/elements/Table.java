package sql.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sql.ables.TableAble;
import sql.exceptions.DataInvalidException;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;
import sql.exceptions.UnknownSequenceException;
import sql.functions.Hash;

public class Table implements TableAble {

    public String name;
    private int index_count = 0;
    private int column_count = 0;
    private ArrayList<Column> columnList = new ArrayList<>();
    transient private ArrayList<Line> data;
    private ArrayList<HashIndex> indexList = new ArrayList<>();

    @Contract(pure = true)
    Table(String name) {
        this.name = name;
        this.data = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    private static <T> ArrayList<T> getSame(ArrayList<T>... list) {
        ArrayList<T> result = new ArrayList<>();
        HashMap<T, Integer> hashMap = new HashMap<>();
        for (ArrayList<T> x : list) {
            for (T data : x) {
                hashMap.compute(data, (k, v) -> v != null ? v++ : 1);
            }
        }
        for (Map.Entry<T, Integer> entry : hashMap.entrySet()) {
            if (entry.getValue() == list.length) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] getSame(T[]... list) {
        ArrayList<T> result = new ArrayList<>();
        HashMap<T, Integer> hashMap = new HashMap<>();
        for (T[] x : list) {
            for (T data : x) {
                hashMap.compute(data, (k, v) -> v != null ? v++ : 1);
            }
        }
        for (Map.Entry<T, Integer> entry : hashMap.entrySet()) {
            if (entry.getValue() == list.length) {
                result.add(entry.getKey());
            }
        }
        return (T[]) result.toArray();
    }

    private static <T> ArrayList<T> getSame(ArrayList<ArrayList<T>> list) {
        ArrayList<T> result = new ArrayList<>();
        HashMap<T, Integer> hashMap = new HashMap<>();
        for (ArrayList<T> x : list) {
            for (T data : x) {
                hashMap.compute(data, (k, v) -> v != null ? v++ : 1);
            }
        }
        for (Map.Entry<T, Integer> entry : hashMap.entrySet()) {
            if (entry.getValue() == list.size()) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public Column getColumn(String name) {
        for (Column x : columnList) {
            if (x.name.equals(name)) {
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
        Line new_data = new Line();
        new_data.index = index_count++;
        new_data.data.addAll(Arrays.asList(str));
        this.data.add(new_data);
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
        this.columnList.remove(x);
    }

    @Override
    public void insert(@NotNull Order[] orders) {
        Line new_data = new Line();
        new_data.index = index_count++;
        for (Order x : orders) {
            new_data.data.add(x.column.id, x.value);
        }
        data.add(new_data);
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
        return getSame(ans1, ans2);
    }

    private ArrayList<Integer> selectDefault(HashMap<Column, Data> where,
        ArrayList<Integer> checkList) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i : checkList) {
            Line x = data.get(i);
            if (x.isDeleted) {
                continue;
            }
            boolean is_equal = true;
            for (Entry<Column, Data> y : where.entrySet()) {
                int index = y.getKey().id;
                if (!y.getValue().getValue()
                    .equalsIgnoreCase(x.data.get(index).getValue())) {
                    is_equal = false;
                    break;
                }
            }
            if (is_equal) {
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
            if (getSame(index.columns.toArray(), Order.castNameList(where)).length == index.columns
                .size()) {
                HashMap<Column, Data> checkList = new HashMap<>();
                for (Column x : index.columns) {
                    checkList.putIfAbsent(x, map.get(x));
                    map.remove(x);
                }
                checkResult.add(selectInIndex(checkList, index));
            }
        }
        ArrayList<Integer> result = getSame(checkResult);
        if (map.size() > 0 && result.size() > 0) {
            return getSame(selectDefault(map, result), result);
        }
        return result;
    }

    ArrayList<Line> selectPrivate(Column[] columns, Order[] where, Order[] order_by)
        throws UnknownSequenceException {
        ArrayList<Integer> result = selectWhereIntoNumbers(where);
        ArrayList<Line> array = new ArrayList<>();
        for (int i : result) {
            Line tmp = data.get(i), add = new Line();
            for (Column j : columns) {
                add.data.add(tmp.data.get(j.id));
            }
            if (order_by.length != 0) {
                for (Order j : order_by) {
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
        if (order_by.length != 0) {
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
    public void delete(@NotNull Order[] where) throws Exception {
        ArrayList<Integer> result = selectWhereIntoNumbers(where);
        if (result.isEmpty()) {
            throw new Exception("Data not found.");
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
        boolean can_null = false;
        for (int i = 2; i < arr.length; i++) {
            if (arr[i - 1].equalsIgnoreCase("NOT")
                && arr[i].equalsIgnoreCase("NULL")) {
                can_null = true;
                break;
            }
        }
        addColumn(arr[0], arr[1], can_null);
    }

    private void addColumn(String name, String type, boolean can_null) throws NotFoundException {
        this.columnList.add(new Column(column_count++, name, type, can_null));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Table) {
            return this.name.equals(((Table) obj).name);
        }
        return super.equals(obj);
    }

    public int getColumn_count() {
        return column_count;
    }
}

