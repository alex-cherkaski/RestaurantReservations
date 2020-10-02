package restaurant;

/**
 * Represents a group of people to be seated together at the restaurant.
 */
public class Party {
    private int partySize = 0;
    private boolean isVIP = false;
    private Table seatedTable = null;

	public Party(int size, boolean vip) {
        this.partySize = size;
        this.isVIP = vip;
    }
    
    public boolean isVIP() {
        return this.isVIP;
    }
    
    public int getPartySize() {
        return this.partySize;
    }
    
    public Table getSeatedTable() {
		return seatedTable;
	}

	public void seatAtTable(Table seatedTable) {
		this.seatedTable = seatedTable;
	}
}
