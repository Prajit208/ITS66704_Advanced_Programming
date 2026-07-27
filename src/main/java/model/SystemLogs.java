package model;

import java.time.LocalDateTime;
import java.util.UUID;
// Class for the system logs
public class SystemLogs {

    private String logID;
    private String userID;
    private LocalDateTime timestamp;
    private String action;

    public SystemLogs(String userID, String action) {
        this.logID = UUID.randomUUID().toString();
        this.userID = userID;
        this.timestamp = LocalDateTime.now();
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public String getUserID(){
        return userID;
    }

    public String getLogID(){
        return  logID;
    }

    public LocalDateTime getTimestamp(){
        return  timestamp;
    }
}