package sql.elements;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import sql.exceptions.TooLongException;
import sql.functions.Caster;

public class DataIOer implements Serializable {

    public static final int defaultSize = 50;
    static final private String defaultFile = "D:\\sql\\";
    private static final int intSize = 4;
    private static final int longSize = 8;
    private static final int maxLengthInData = 1000000;
    static byte[] bytes = new byte[defaultSize];
    String filePath;
    Database database;
    Table table;
    RandomAccessFile ioFile;
    private int dataFileCnt = 0, stringFileCnt = 0;
    private ArrayList<Integer> dataByteCnt = new ArrayList<>();
    private ArrayList<Integer> stringByteCnt = new ArrayList<>();

    public DataIOer(@NotNull Database database, @NotNull Table table) {
        this.database = database;
        this.table = table;
        this.filePath = defaultFile + database.name + "\\" + table.name + "\\";
        dataByteCnt.add(0);
        stringByteCnt.add(0);
    }

    public Line getLine(long index) throws IOException, TooLongException {
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
        return new Line(dataArray, (Column[]) table.columnList.toArray());
    }

    public long setLine(@NotNull Line lines) throws IOException {
        int[] result = allocatePosition(true);
        ArrayList<Data> dataArray = lines.data;
        ioFile = new RandomAccessFile(this.filePath +
            new String(Caster.intToBytes(result[0])), "rw");
        ioFile.seek(result[1]);
        for (int i = 0; i < table.columnList.size(); i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(lines.data.get(i));
            int size = Math.min(defaultSize, table.columnList.get(i).maxLength);
            if (stringBuilder.length() < size) {
                while (stringBuilder.length() < size) {
                    stringBuilder.append("\0");
                }
                bytes = stringBuilder.toString().getBytes();
                ioFile.write(bytes, 0, size);
            } else {
                bytes = stringBuilder.substring(0, 99).getBytes();
                ioFile.write(bytes, 0, size);
                int[] next = setString(stringBuilder.delete(0, 99).toString());
                bytes = Integer.toString(next[0]).getBytes();
                ioFile.write(bytes, 0, intSize);
                bytes = Integer.toString(next[1]).getBytes();
                ioFile.write(bytes, 0, intSize);
            }
        }
        return ((long) result[0] << 32) + result[1];
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

    public int[] setString(String string) {
        int[] result = allocatePosition(false);

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
