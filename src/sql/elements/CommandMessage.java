package sql.elements;

import java.io.Serializable;

public class CommandMessage implements Serializable {

    String date, time, command, ip, user;

    public CommandMessage(String date, String time, String command, String ip, String user) {
        this.time = time;
        this.date = date;
        this.command = command;
        this.ip = ip;
        this.user = user;
    }
}
