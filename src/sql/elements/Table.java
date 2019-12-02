package sql.elements;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sql.ables.TableAble;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;
import sql.functions.Hash;

import java.util.*;

public class Table implements TableAble {
    public String name;
    private int index_count = 0;
    private int column_count = 0;
    private ArrayList<Column> columnList = new ArrayList<>();
    private ArrayList<Line> data;
    private ArrayList<Index> indexList = new ArrayList<>();
    @Contract(pure = true)
    Table(String name) {
        this.name = name;
        this.data = new ArrayList<>();
    }

    Column getColumn(String name) {
        for(Column x: columnList) {
            if(x.name.equals(name)) return x;
        }
        return null;
    }

    Index getIndex(String name) {
        for(Index x: indexList) {
            if(x.name.equals(name)) return x;
        }
        return null;
    }

    private void insert(@NotNull Data[] str) throws Exception {
        if(str.length != this.columnList.size()) {
            throw new Exception("Length is not correct. Expect " + this.columnList.size() + " , found " + str.length + ".");
        }
        Line new_data = new Line();
        new_data.index = index_count++;
        new_data.data.addAll(Arrays.asList(str));
        this.data.add(new_data);
    }

    @Override
    public void addColumn(Column column) throws IsExistedException {
        Column x = this.getColumn(column.name);
        if(x != null) throw new IsExistedException("column", column.name);
        this.columnList.add(column);
    }

    @Override
    public void deleteColumn(String name) throws NotFoundException {
        Column x = this.getColumn(name);
        if(x == null) throw new NotFoundException("column", name);
        this.columnList.remove(x);
    }

    public void insert(@NotNull Order[] orders) {
        Line new_data = new Line();
        new_data.index = index_count++;
        for(Order x: orders) {
            new_data.data.add(x.column.id, x.value);
        }
        data.add(new_data);
    }
    @SuppressWarnings("unchecked")
    private static <T> ArrayList<T> getSame(ArrayList<T>... list) {
        ArrayList<T> result = new ArrayList<>();
        HashMap<T, Integer> hashMap = new HashMap<>();
        for(ArrayList<T> x: list) {
            for(T data: x) {
                hashMap.compute(data, (k, v) -> v != null ? v++ : 1);
            }
        }
        for(Map.Entry<T, Integer> entry: hashMap.entrySet()) {
            if(entry.getValue() == list.length) {
                result.add(entry.getKey());
            }
        }
        return result;
    }
    @SuppressWarnings("unchecked")
    private static <T> T[] getSame(T[]... list) {
        ArrayList<T> result = new ArrayList<>();
        HashMap<T, Integer> hashMap = new HashMap<>();
        for(T[] x: list) {
            for(T data: x) {
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
    @NotNull
    private ArrayList<Integer> selectWhereIntoNumbers(@NotNull Order[] where) {
//        for(Index index: this.indexList) {
//            boolean isContain = true;
//            if(getSame(index.columns.toArray(), Order.castNameList(where)).length == index.columns.size()) {
//
//            }
//        }
        ArrayList<Integer> result = new ArrayList<>();
        int length = data.size();
        for (int i = 0; i < length; i++) {
            Line x = data.get(i);
            boolean is_equal = true;
            for (Order y : where) {
                int index = y.column.id;
                if(!y.value.getStringValue().equalsIgnoreCase(x.data.get(index).getStringValue())) {
                    is_equal = false;
                    break;
                }
            }
            if(is_equal) {
                result.add(i);
            }
        }
        return result;
    }

    ArrayList<Line> selectPrivate(Column[] columns, Order[] where, Order[] order_by) throws Exception {
        ArrayList<Integer> result = selectWhereIntoNumbers(where);
        ArrayList<Line> array = new ArrayList<>();
        for(int i: result) {
            Line tmp = data.get(i), add = new Line();
            for(Column j : columns) {
                add.data.add(tmp.data.get(j.id));
            }
            if(order_by.length != 0) {
                for (Order j : order_by) {
                    String s = tmp.data.get(j.column.id).getStringValue();
                    int len = s.length();
                    for (int ch = 0; ch < len; ch++) {
                        if (j.value.getStringValue().equals("1")) tmp.cmp += s.charAt(ch);
                        else if (j.value.getStringValue().equals("-1")) tmp.cmp += (char) (65536 - s.charAt(ch));
                        else throw new Exception("Unknown sequence.");
                    }
                }
            }
            array.add(add);
        }
        if(order_by.length != 0) {
            Collections.sort(array);
            for(int j = 1; j < array.size(); j++) {
                if(array.get(j - 1).equals(array.get(j))) {
                    array.remove(j - 1);
                }
            }
        }
        return array;
    }
    public void update(@NotNull Order[] set, @NotNull Order[] where) {
        ArrayList<Integer> result= selectWhereIntoNumbers(where);
        for (int x: result) {
            for (Order y : set) {
                this.data.get(x).data.get(y.column.id).setString(y.value.getStringValue());
            }
        }
    }

    @Override
    public void deleteLine(Order[] search) throws NotFoundException {
        ArrayList<Integer> result = selectWhereIntoNumbers(search);
        if(result.size() == 0) throw new NotFoundException("line", "your searching form");
        for(int x: result) {
            this.data.remove(x);
        }
    }

    @Override
    public void setIndex(int type, String name, String[] columnInOrder) throws NotFoundException, IsExistedException {
        ArrayList<Integer> num = new ArrayList<>();
        Index x = getIndex(name);
        if(x != null) throw new IsExistedException("index", name);
        x = new Index();
        for(String str: columnInOrder) {
            Column column = getColumn(str);
            if(column == null) throw new NotFoundException("column", name);
            x.columns.add(column);
            num.add(column.id);
        }
        for(Line line: data) {
            StringBuilder stringBuilder = new StringBuilder();
            for(int id: num) {
                stringBuilder.append(line.data.get(id));
            }
            int hash1 = Hash.getHash1(stringBuilder.toString());
            int hash2 = Hash.getHash2(stringBuilder.toString());
            x.map1.computeIfAbsent(hash1, k -> new ArrayList<>());
            x.map2.computeIfAbsent(hash2, k -> new ArrayList<>());
            x.map1.get(hash1).add(line);
            x.map2.get(hash2).add(line);
        }
    }

    public void delete(@NotNull Order[] where) throws Exception {
        ArrayList<Integer> result = selectWhereIntoNumbers(where);
        if(result.isEmpty()) throw new Exception("Data not found.");
        for(int x:result) {
            data.remove(x);
        }
    }
    public void addColumn(@NotNull String line) throws Exception {
        String[] arr = line.split(" ");
        if(arr.length < 2) throw new Exception("Massage is too short in adding columns.");
        int max_length = 256;
        boolean is_main_key = false, can_null = false;
        for(int i = 2; i < arr.length; i++) {
            try{
                max_length = Integer.parseInt(arr[i]);
            } catch (Exception e) {
                if(arr[i-1].equalsIgnoreCase("NOT")
                        && arr[i].equalsIgnoreCase("NULL")){
                    can_null = true;
                }
                if(arr[i-1].equalsIgnoreCase("PRIMARY")
                        && arr[i].equalsIgnoreCase("KEY")) {
                    is_main_key = true;
                }
            }
        }
        addColumn(arr[0], arr[1], max_length, is_main_key, can_null);
    }
    private void addColumn(String name, String type, int max_length, boolean is_main_key, boolean can_null) {
        this.columnList.add(new Column(column_count++, name, type, max_length, is_main_key, can_null));
    }
}

