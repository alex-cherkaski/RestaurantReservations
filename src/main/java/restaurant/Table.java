package restaurant;

public class Table {
    private Party party = null;
    private int tableCapacity = 0;
    private boolean isOccupied = false;

    protected Table(int capacity) {
        this.setTableCapacity(capacity);
    }

    public Party getParty() {
        return party;
    }
    
    public boolean isOccupied() {
        return this.isOccupied;
    }
    
    public void freeTable() {
        this.isOccupied = false;
    }

	public int getTableCapacity() {
		return tableCapacity;
	}

	public void setTableCapacity(int tableCapacity) {
		this.tableCapacity = tableCapacity;
	}

	public void OccupyTable(Party party) {
		this.isOccupied = true;
		this.party = party;
	}
}
