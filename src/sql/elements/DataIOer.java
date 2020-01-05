package sql.elements;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
        File path = new File(defaultFile);
        path.mkdir();
        path = new File(defaultFile + database.name + "\\");
        path.mkdir();
        path = new File(this.filePath);
        path.mkdir();
        dataByteCnt.add(-1);
        stringByteCnt.add(-1);
        dataByteCnt.add(0);
        stringByteCnt.add(0);
    }

    public Line getLine(long index) {
        try {
            int[] result = Caster.longToInt(index);
            RandomAccessFile ioFile = new RandomAccessFile(this.filePath + result[0] + defaultEnd,
                "r");
            ArrayList<Data> dataArray = new ArrayList<>();
            ioFile.seek(result[1]);
            for (Column x : table.columnList) {
                int size = Math.min(defaultSize, x.maxLength);
                StringBuilder stringBuilder = new StringBuilder();
                bytes = new byte[(size << 1) + 2];
                ioFile.read(bytes, 0, (size << 1) + 2);
                stringBuilder.append(new String(bytes, 0, (size << 1) + 2, "UNICODE"));
                if (x.maxLength > defaultSize) {
                    bytes = new byte[intSize];
                    ioFile.read(bytes, 0, intSize);
                    int strBlock = Caster.bytesToInt(bytes);
                    ioFile.read(bytes, 0, intSize);
                    int strIndex = Caster.bytesToInt(bytes);
                    if (strBlock != 0 || strIndex != 0) {
                        stringBuilder.append(this.getString(strBlock, strIndex));
                    }
                }
                int i;
                for (i = 0; i < stringBuilder.length(); i++) {
                    if (stringBuilder.charAt(i) == 32 || stringBuilder.charAt(i) == 0) {
                        break;
                    }
                }
                dataArray.add(new Data(i == stringBuilder.length() ? stringBuilder.toString()
                    : stringBuilder.substring(0, i)));
            }
            ioFile.close();
            return new Line(dataArray, table.columnList);
        } catch (IOException | TooLongException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long setLine(@NotNull Line lines, long position) {
        try {
            int[] result;
            if (position == -1) {
                result = allocatePosition(true);
            } else {
                result = Caster.longToInt(position);
            }
            File file = new File(this.filePath + result[0] + defaultEnd);
            file.createNewFile();
            RandomAccessFile ioFile = new RandomAccessFile(this.filePath + result[0] + defaultEnd,
                "rw");
            ioFile.seek(result[1]);
            for (int i = 0; i < table.columnList.size(); i++) {
                StringBuilder stringBuilder = new StringBuilder();
                Data data = lines.data.get(i);
                stringBuilder.append(data == null ? "" : data.getValue());
                int maxLength = table.columnList.get(i).maxLength;
                int size = Math.min(defaultSize, maxLength);
                if (stringBuilder.length() <= size) {
                    while (stringBuilder.length() < size) {
                        stringBuilder.append("\0");
                    }
                    bytes = stringBuilder.toString().getBytes("unicode");
                    ioFile.write(bytes, 0, (size << 1) + 2);
                    if (maxLength > defaultSize) {
                        Arrays.fill(bytes, (byte) 0);
                        ioFile.write(bytes, 0, longSize);
                    }
                } else {
                    StringBuilder builder = new StringBuilder(
                        stringBuilder.substring(0, defaultSize));
                    while (builder.length() < defaultSize) {
                        builder.append("\0");
                    }
                    bytes = builder.toString().getBytes("UNICODE");
                    ioFile.write(bytes, 0, (size << 1) + 2);
                    int[] next = setString(stringBuilder.delete(0, defaultSize - 1).toString());
                    bytes = Caster.intToBytes(next[0]);
                    ioFile.write(bytes, 0, intSize);
                    bytes = Caster.intToBytes(next[1]);
                    ioFile.write(bytes, 0, intSize);
                }
            }
            ioFile.close();
            return ((long) result[0] << 32) + result[1];
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @NotNull
    private String getString(int strBlock, int strIndex) {
        try {
            RandomAccessFile ioFile = new RandomAccessFile(
                this.filePath + "string" + strBlock + defaultEnd, "r");
            ioFile.seek(strIndex);
            bytes = new byte[(defaultSize << 1) + 2];
            ioFile.read(bytes, 0, (defaultSize << 1) + 2);
            String str = new String(bytes, 0, (defaultSize << 1) + 2, "UNICODE");
            ioFile.read(bytes, 0, intSize);
            int nextBlock = Caster.bytesToInt(bytes);
            ioFile.read(bytes, 0, intSize);
            int nextIndex = Caster.bytesToInt(bytes);
            if (nextBlock != 0 || nextIndex != 0) {
                str += getString(nextBlock, nextIndex);
            }
            ioFile.close();
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Nullable
    private int[] setString(String string) {
        try {
            int[] result = allocatePosition(false);
            RandomAccessFile ioFile = new RandomAccessFile(
                this.filePath + "string" + result[0] + defaultEnd, "rw");
            ioFile.seek(result[1]);
            StringBuilder stringBuilder = new StringBuilder(string);
            if (string.length() < defaultSize) {
                while (stringBuilder.length() < defaultSize) {
                    stringBuilder.append(" ");
                }
                bytes = stringBuilder.toString().getBytes("UNICODE");
                ioFile.write(bytes, 0, (defaultSize << 1) + 2);
                Arrays.fill(bytes, (byte) 0);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
            dataByteCnt.set(dataFileCnt, result[1] + this.getColumnSize());
        } else {
            if (stringByteCnt.get(stringFileCnt) > maxLengthInData) {
                stringFileCnt++;
                stringByteCnt.add(0);
            }
            result[0] = stringFileCnt;
            result[1] = stringByteCnt.get(stringFileCnt);
            stringByteCnt.set(stringFileCnt, result[1] + defaultSize * 2 + longSize);
        }
        return result;
    }

    private int getColumnSize() {
        ArrayList<Integer> size = table.getColumnSize();
        int ans = 0;
        for (int x : size) {
            ans += Math.min(x, defaultSize) + 1;
            if (x > defaultSize) {
                ans += 4;
            }
        }
        return ans << 1;
    }
}
