package sql.element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sql.ables.TableAble;
import sql.exceptions.IsExistedException;
import sql.exceptions.NotFoundException;

import java.util.*;

public class Table implements TableAble {
    public String name;
    private int index_count = 0;
    private int column_count = 0;
    private ArrayList<Column> list = new ArrayList<>();
    private ArrayList<Line> table;
    @Contract(pure = true)
    Table(String name) {
        this.name = name;
        this.table = new ArrayList<>();
    }

    /**
     * get the column list
     * @return ArrayList<Column>
     */
    public ArrayList<Column> getColumnList() {
        return list;
    }

    Column getColumn(String column) throws Exception {
        for(Column x: list) {
            if(x.name.equals(column)) return x;
        }
        throw new Exception("Column not found.");
    }

    public void insertList(Column[] str){
        this.list.addAll(Arrays.asList(str));
    }

    private void insert(@NotNull Data[] str) throws Exception {
        if(str.length != this.list.size()) {
            throw new Exception("Length is not correct. Expect " + this.list.size() + " , found " + str.length + ".");
        }
        Line new_data = new Line();
        new_data.index = index_count++;
        new_data.data.addAll(Arrays.asList(str));
        this.table.add(new_data);
    }

    ArrayList<Line> selectPrivate(Order[] where, Order[] orderBy) {
        return null;
    }

    @Override
    public boolean addColumn(Column column) throws IsExistedException {
        return false;
    }

    @Override
    public boolean deleteColumn(String name) throws NotFoundException {
        return false;
    }

    public boolean insert(@NotNull Order[] orders) {
        Line new_data = new Line();
        new_data.index = index_count++;
        for(Order x: orders) {
            new_data.data.add(x.column.id, x.value);
        }
        table.add(new_data);
        return true;
    }
    @Contract(pure = true)
    public ArrayList<Line> selectAll() {
        return this.table;
    }
    public ArrayList<String> exportWithCSV(@NotNull ArrayList<Line> line,
                                           @NotNull ArrayList<Column> checklist) throws Exception {
        ArrayList<String> result = new ArrayList<>();
        result.add(exitWithCSV(checklist));
        for(Line x: line) {
            result.add(exitWithCSV(x.data));
        }
        return result;
    }
    @NotNull
    private String exitWithCSV(@NotNull ArrayList array) throws Exception {
        StringBuilder out = new StringBuilder();
        int length = array.size();
        for(int i = 0; i < length; i++){
            String str;
            if(array.get(i) instanceof Column) {
                Column x = (Column) array.get(i);
                str = x.name;
            } else if(array.get(i) instanceof Data){
                Data x = (Data) array.get(i);
                if(x.type.equals("Integer")) str = Integer.toString(x.getIntValue());
                else if(x.type.equals("String")) str = x.getStringValue();
                else throw new Exception("Type of data is invalid.");
            } else throw new Exception("Type of array is invalid.");
            Objects.requireNonNull(out).append(str);
            if(i != length - 1) out.append(",");
        }
        return out.toString();
    }
    public String exitWithJSON(@NotNull ArrayList a) {
        JSONArray jsonArray = JSONArray.parseArray(JSONObject.toJSONString(a));
        return jsonArray.toString();
    }
    @NotNull
    private ArrayList<Integer> selectWhereToIndex(@NotNull Order[] where) {
        ArrayList<Integer> result = new ArrayList<>();
        int length = table.size();
        for (int i = 0; i < length; i++) {
            Line x = table.get(i);
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

    public ArrayList<Line> selectInOrder(Column[] columns, Order[] where, Order[] order_by,
                                         boolean is_distinct) throws Exception {
        ArrayList<Integer> result = selectWhereToIndex(where);
        ArrayList<Line> array = new ArrayList<>();
        for(int i: result) {
            Line tmp = table.get(i), add = new Line();
            for(Column j : columns) {
                add.data.add(tmp.data.get(j.id));
            }
            if(order_by.length != 0 || is_distinct) {
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
        if(order_by.length != 0 || is_distinct) {
            Collections.sort(array);
            for(int j = 1; j < array.size(); j++) {
                if(!is_distinct) break;
                if(array.get(j - 1).equals(array.get(j))) {
                    array.remove(j - 1);
                }
            }
        }
        return array;
    }
    public boolean update(@NotNull Order[] set, @NotNull Order[] where) {
        ArrayList<Integer> result= selectWhereToIndex(where);
        for (int x: result) {
            for (Order y : set) {
                this.table.get(x).data.get(y.column.id).setString(y.value.getStringValue());
            }
        }
        return true;
    }

    @Override
    public boolean deleteLine(Order[] search) throws NotFoundException {
        return false;
    }

    @Override
    public boolean setIndex(int type, String[] columnInOrder) throws NotFoundException, IsExistedException {
        return false;
    }

    public void delete(@NotNull Order[] where) throws Exception {
        ArrayList<Integer> result = selectWhereToIndex(where);
        if(result.isEmpty()) throw new Exception("Data not found.");
        for(int x:result) {
            table.remove(x);
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
        this.list.add(new Column(column_count++, name, type, max_length, is_main_key, can_null));
    }
}

