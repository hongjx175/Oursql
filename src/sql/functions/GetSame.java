package sql.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class GetSame {

    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> getSame(@NotNull ArrayList<T>... list) {
        ArrayList<T> result = new ArrayList<>();
        HashMap<T, Integer> hashMap = new HashMap<>();
        for (ArrayList<T> x : list) {
            for (T data : x) {
                hashMap.compute(data, (k, v) -> v != null ? ++v : 1);
            }
        }
        for (Map.Entry<T, Integer> entry : hashMap.entrySet()) {
            if (entry.getValue() == list.length) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> T[] getSame(@NotNull T[]... list) {
        ArrayList<T> result = new ArrayList<>();
        HashMap<T, Integer> hashMap = new HashMap<>();
        for (T[] x : list) {
            for (T data : x) {
                hashMap.compute(data, (k, v) -> v != null ? ++v : 1);
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
    public static <T> ArrayList<T> getSame(@NotNull ArrayList<ArrayList<T>> list) {
        ArrayList<T> result = new ArrayList<>();
        HashMap<T, Integer> hashMap = new HashMap<>();
        for (ArrayList<T> x : list) {
            for (T data : x) {
                hashMap.compute(data, (k, v) -> v != null ? ++v : 1);
            }
        }
        for (Map.Entry<T, Integer> entry : hashMap.entrySet()) {
            if (entry.getValue() == list.size()) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

}
