package restaurant.exceptions;

import restaurant.Table;

public class MissingTableException extends Exception {
    private final Table table;

    public MissingTableException(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }
}
