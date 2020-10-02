package restaurant.exceptions;

import restaurant.Party;

public class NoTablesAvailableException extends Exception {
    private final Party party;

    public NoTablesAvailableException(Party party) {
        this.party = party;
    }

    public Party getParty() {
        return party;
    }
}
