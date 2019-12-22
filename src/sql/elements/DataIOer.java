package sql.elements;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import sql.functions.Caster;

public class DataIOer implements Serializable {

    public static final int defaultSize = 50;
    static final private String defaultFile = "D:\\sql\\";
    private static final int intSize = 4;
    private static final int longSize = 8;
    static byte[] bytes = new byte[defaultSize];
    String filePath;
    Database database;
    Table table;
    RandomAccessFile ioFile;

    public DataIOer(Database database, Table table) {
        this.database = database;
        this.table = table;
        this.filePath = defaultFile + database.name + "\\" + table.name + "\\";
    }

    public Line getLine(long index) throws IOException {
        int[] result = Caster.longToInt(index);
        ioFile = new RandomAccessFile(this.filePath +
            new String(Caster.intToBytes(result[0])), "r");
        ArrayList<Data> dataArray = new ArrayList<>();
        for (Column x : table.columnList) {
            int size = Math.min(defaultSize, x.maxLength);
            StringBuilder stringBuilder = new StringBuilder();
            ioFile.seek(result[1]);
            ioFile.read(bytes, 0, size);
            stringBuilder.append(new String(bytes));
            if (x.maxLength > size) {
                ioFile.read(bytes, 0, intSize);
                int strBlock = Caster.bytesToInt(bytes);
                ioFile.read(bytes, 0, intSize);
                int strIndex = Caster.bytesToInt(bytes);
                stringBuilder.append(this.getString(strBlock, strIndex));
            }
            dataArray.add(new Data(stringBuilder.toString()));
        }
        return new Line(dataArray);
    }

    public void setLine(long index, Line line) throws IOException {
        int[] result = Caster.longToInt(index);
        ArrayList<Data> dataArray = line.data;
        ioFile = new RandomAccessFile(this.filePath +
            new String(Caster.intToBytes(result[0])), "rw");
        

    }

    public String getString(int strBlock, int strIndex) throws IOException {
        byte[] blockBytes = Caster.intToBytes(strBlock);
        ioFile = new RandomAccessFile(this.filePath + "string" + new String(blockBytes), "r");
        ioFile.seek(strIndex);
        ioFile.read(bytes, 0, defaultSize);
        String str = new String(bytes);
        ioFile.read(bytes, 0, intSize);
        int nextBlock = Caster.bytesToInt(bytes);
        ioFile.read(bytes, 0, intSize);
        int nextIndex = Caster.bytesToInt(bytes);
        if (nextBlock != 0 && nextIndex != 0) {
            str += getString(nextBlock, nextIndex);
        }
        return str;
    }
}
