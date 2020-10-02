package restaurant.exceptions;

import restaurant.Party;
import restaurant.Table;

public class OccupiedTableException extends Exception {
    private final Table table;
    private final Party party;

    public OccupiedTableException(Table table, Party party) {
        this.table = table;
        this.party = party;
    }

    public Table getTable() {
        return table;
    }

    public Party getParty() {
        return party;
    }
}
