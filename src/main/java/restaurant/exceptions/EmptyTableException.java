package restaurant.exceptions;

import restaurant.Table;

public class EmptyTableException extends Exception {
    private final Table table;

    public EmptyTableException(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }
}
