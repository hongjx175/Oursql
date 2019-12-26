package sql.elements;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import sql.exceptions.TooLongException;
import sql.functions.Caster;

public class DataIOer implements Serializable {

    public static final int defaultSize = 50;
    static final private String defaultFile = "D:\\sql\\";
    static final private String defaultEnd = ".data";
    private static final int intSize = 4;
    private static final int longSize = 8;
    private static final int maxLengthInData = 1000000;
    static byte[] bytes = new byte[defaultSize];
    String filePath;
    Database database;
    Table table;
    private int dataFileCnt = 1, stringFileCnt = 1;
    private ArrayList<Integer> dataByteCnt = new ArrayList<>();
    private ArrayList<Integer> stringByteCnt = new ArrayList<>();

    public DataIOer(@NotNull Database database, @NotNull Table table) {
        this.database = database;
        this.table = table;
        this.filePath = defaultFile + database.name + "\\" + table.name + "\\";
        dataByteCnt.add(-1);
        stringByteCnt.add(-1);
        dataByteCnt.add(0);
        stringByteCnt.add(0);
    }

    public Line getLine(long index) throws IOException, TooLongException {
        int[] result = Caster.longToInt(index);
        RandomAccessFile ioFile = new RandomAccessFile(this.filePath + result[0] + defaultEnd, "r");
        ArrayList<Data> dataArray = new ArrayList<>();
        ioFile.seek(result[1]);
        for (Column x : table.columnList) {
            int size = Math.min(defaultSize, x.maxLength);
            StringBuilder stringBuilder = new StringBuilder();
            ioFile.read(bytes, 0, size);
            stringBuilder.append(new String(bytes, 0, size, "GBK"));
            if (x.maxLength > defaultSize) {
                ioFile.read(bytes, 0, intSize);
                int strBlock = Caster.bytesToInt(bytes);
                ioFile.read(bytes, 0, intSize);
                int strIndex = Caster.bytesToInt(bytes);
                if (strBlock != 0 && strIndex != 0) {
                    stringBuilder.append(this.getString(strBlock, strIndex));
                }
            }
            dataArray.add(new Data(stringBuilder.toString()));
        }
        ioFile.close();
        return new Line(dataArray, table.columnList.toArray(new Column[0]));
    }

    public long setLine(@NotNull Line lines) throws IOException {
        int[] result = allocatePosition(true);
        RandomAccessFile ioFile = new RandomAccessFile(this.filePath + result[0] + defaultEnd,
            "rw");
        ioFile.seek(result[1]);
        for (int i = 0; i < table.columnList.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            Data data = lines.data.get(i);
            stringBuilder.append(data == null ? null : data.getValue());
            int maxLength = table.columnList.get(i).maxLength;
            int size = Math.min(defaultSize, maxLength);
            if (stringBuilder.length() < size) {
                while (stringBuilder.length() < size) {
                    stringBuilder.append("\0");
                }
                bytes = stringBuilder.toString().getBytes("GBK");
                ioFile.write(bytes, 0, size);
                if (maxLength > defaultSize) {
                    Arrays.fill(bytes, 0, longSize - 1, (byte) 0);
                    ioFile.write(bytes, 0, longSize);
                }
            } else {
                StringBuilder builder = new StringBuilder(
                    stringBuilder.substring(0, defaultSize - 1));
                while (builder.length() < defaultSize) {
                    builder.append("\0");
                }
                bytes = builder.toString().getBytes("GBK");
                ioFile.write(bytes, 0, size);
                int[] next = setString(stringBuilder.delete(0, defaultSize - 1).toString());
                bytes = Caster.intToBytes(next[0]);
                ioFile.write(bytes, 0, intSize);
                bytes = Caster.intToBytes(next[1]);
                ioFile.write(bytes, 0, intSize);
            }
        }
        ioFile.close();
        return ((long) result[0] << 32) + result[1];
    }

    @NotNull
    private String getString(int strBlock, int strIndex) throws IOException {
        RandomAccessFile ioFile = new RandomAccessFile(
            this.filePath + "string" + strBlock + defaultEnd, "r");
        ioFile.seek(strIndex);
        ioFile.read(bytes, 0, defaultSize);
        String str = new String(bytes, 0, defaultSize, "GBK");
        ioFile.read(bytes, 0, intSize);
        int nextBlock = Caster.bytesToInt(bytes);
        ioFile.read(bytes, 0, intSize);
        int nextIndex = Caster.bytesToInt(bytes);
        if (nextBlock != 0 && nextIndex != 0) {
            str += getString(nextBlock, nextIndex);
        }
        ioFile.close();
        return str;
    }

    @NotNull
    private int[] setString(String string) throws IOException {
        int[] result = allocatePosition(false);
        RandomAccessFile ioFile = new RandomAccessFile(
            this.filePath + string + result[0] + defaultEnd, "rw");
        ioFile.seek(result[1]);
        StringBuilder stringBuilder = new StringBuilder(string);
        if (string.length() < defaultSize) {
            while (stringBuilder.length() < defaultSize) {
                stringBuilder.append(" ");
            }
            bytes = stringBuilder.toString().getBytes("GBK");
            ioFile.write(bytes, 0, defaultSize);
            Arrays.fill(bytes, 0, longSize - 1, (byte) 0);
            ioFile.write(bytes, 0, longSize);
        } else {
            int[] next = setString(stringBuilder.substring(0, defaultSize - 1));
            bytes = Caster.intToBytes(next[0]);
            ioFile.write(bytes, 0, intSize);
            bytes = Caster.intToBytes(next[1]);
            ioFile.write(bytes, 0, intSize);
        }
        ioFile.close();
        return result;
    }

    @NotNull
    private int[] allocatePosition(boolean isData) {
        int[] result = new int[2];
        if (isData) {
            if (dataByteCnt.get(dataFileCnt) > maxLengthInData) {
                dataFileCnt++;
                dataByteCnt.add(0);
            }
            result[0] = dataFileCnt;
            result[1] = dataByteCnt.get(dataFileCnt);
        } else {
            if (stringByteCnt.get(stringFileCnt) > maxLengthInData) {
                stringFileCnt++;
                stringByteCnt.add(0);
            }
            result[0] = stringFileCnt;
            result[1] = stringByteCnt.get(stringFileCnt);
        }
        return result;
    }
}
