package restaurant.exceptions;

import restaurant.Party;

public class MissingPartyException extends Exception {
    private final Party party;

    public MissingPartyException(Party party) {
        this.party = party;
    }

    public Party getParty() {
        return party;
    }
}
