package sql.functions;

import com.ndktools.javamd5.Mademd5;

public class Hash {
    public static int getHash1(String str) {
        Mademd5 maker = new Mademd5();
        String x = maker.toMd5(str);
        return x.hashCode();
    }
    public static int getHash2(String str) {
        Mademd5 maker = new Mademd5();
        String x = maker.toMd5(str + "998244353");
        return x.hashCode();
    }
}
