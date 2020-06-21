package main.webapp.Model;

import java.io.Serializable;

public class TableAttributes implements Serializable {
    public enum Orientation {VERTICAL, HORIZONTAL}
    public final String START;
    public final String END;
    public final Boolean contains;
    public final String tableId;
    public final Orientation orientation;
    public final boolean ignoreOneLine;
    private int occurrence = 1;

    public TableAttributes(String start, String end, Boolean contains, String tableId, Orientation orientation, boolean ignoreOneLine) {
        this.START = start;
        this.END = end;
        this.contains = contains;
        this.tableId = tableId;
        this.orientation = orientation;
        this.ignoreOneLine = ignoreOneLine;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }

    public int getOccurrence() {
        return occurrence;
    }
}
