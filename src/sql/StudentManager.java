package sql;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//
public class StudentManager {
//    private static StudentManager manager = null;
//    @Contract(pure = true)
//    private StudentManager() {
//        table = new ArrayList<>();
//    }
//    public static StudentManager getInstance() {
//        if(manager == null){
//            manager = new StudentManager();
//        }
//        return manager;
//    }
//    private ArrayList<Points> table;
//    public void insert(String stuName, String subName, int score) {
//        table.add(new Points(stuName, subName, score));
//        System.out.println("Insert successfully");
//    }
//    public void insert(@NotNull Order[] orders) throws Exception {
//        String stuName = null, subName = null;
//        int score = 0;
//        boolean isempty = true;
//        for (Order x: orders) {
//            if(x.column.equalsIgnoreCase("student_name")) {
//                stuName = x.value;
//                isempty = false;
//            }
//            else if(x.column.equalsIgnoreCase("subject_name")) {
//                subName = x.value;
//                isempty = false;
//            }
//            else if(x.column.equalsIgnoreCase("score")) {
//                score = Integer.parseInt(x.value, 10);
//                isempty = false;
//            } else {
//                throw new Exception("Row name is not found");
//            }
//        }
//        if(isempty) {
//            throw new Exception("Order is empty");
//        } else insert(stuName, subName, score);
//    }
//    public ArrayList<Points> selectAll() {
//        return table;
//    }
//    public String exitWithJSON(ArrayList a) {
//        JSONArray jsonArray = JSONArray.parseArray(JSONObject.toJSONString(a));
//        return jsonArray.toString();
//    }
//    public ArrayList<Points> selectWhere(Order[] where) throws Exception {
//        ArrayList<Points> result = new ArrayList<>();
//        for (Points x:table) {
//            boolean isequal = true;
//            for (Order y : where) {
//                if (y.column.equalsIgnoreCase("student_name")) {
//                    if (!y.value.equalsIgnoreCase(x.student_name))
//                        isequal = false;
//                }
//                else if (y.column.equalsIgnoreCase("subject_name")) {
//                    if (!y.value.equalsIgnoreCase(x.subject_name))
//                        isequal = false;
//                }
//                else if (y.column.equalsIgnoreCase("score")) {
//                    if (!(Integer.parseInt(y.value) == x.score))
//                        isequal = false;
//                } else throw new Exception("The order is invalid. The where column is not found");
//                if(isequal) {
//                    result.add(x);
//                }
//            }
//        }
//        return result;
//    }
//    public void update(Order[] set, Order[] where) throws Exception {
//        ArrayList<Points> result= selectWhere(where);
//        for (Points x:result) {
//            for (Order y : set) {
//                switch (y.column) {
//                    case "student_name": {
//                        x.student_name = y.value; break;
//                    }
//                    case "subject_name": {
//                        x.subject_name = y.value; break;
//                    }
//                    case "score": {
//                        x.score = Integer.parseInt(y.value); break;
//                    }
//                    default : {
//                        throw new Exception("The order is invalid. The set column is not found");
//                    }
//                }
//            }
//        }
//    }
//    public void delete(Order[] where) throws Exception {
//        ArrayList<Points> result = selectWhere(where);
//        for(Points x:result) {
//            table.remove(x);
//        }
//    }
//}
//
//class Points {
//    public static String[] list = {"student_name", "subject_name", "score"};
//    String student_name;
//    String subject_name;
//    int score;
//    @Contract(pure = true)
//    public Points(String student_name, String subject_name, int score) {
//        this.student_name = student_name;
//        this.subject_name = subject_name;
//        this.score = score;
//    }
}
