package restaurant;

import restaurant.exceptions.EmptyTableException;
import restaurant.exceptions.MissingPartyException;
import restaurant.exceptions.MissingTableException;
import restaurant.exceptions.NoTablesAvailableException;
import restaurant.exceptions.NonPositiveArgumentException;
import restaurant.exceptions.OccupiedTableException;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Restaurant {
    /**
     * Creates a table with the given capacity and adds it to the restaurant.
     * If the capacity is less than or equal to zero,
     * this method throws an instance of {@link NonPositiveArgumentException}
     * with the capacity filled in.
     * @param capacity The capacity of the table.
     * @return A reference to the new table.
     * @throws NonPositiveArgumentException
     */
    
    private List<Table> tableList = new ArrayList<Table>();
    private PriorityQueue<Party> partyQueue = new PriorityQueue<Party>((p1, p2) -> Boolean.compare(p2.isVIP(), p1.isVIP()));
    
    public Table addTable(final int capacity) throws NonPositiveArgumentException {
        if (capacity <= 0) throw new NonPositiveArgumentException(capacity);
        Table newTable = new Table(capacity);
        tableList.add(newTable);
        return newTable;
    }

    /**
     * Removes the specified table from the restaurant.
     * If the table is occupied, this method throws an instance of {@link OccupiedTableException}
     * with the specified table and occupying party filled in.
     * If the table is null or does not exist, this method throws an instance of {@link MissingTableException}
     * with the specified table filled in.
     * @param table The table to remove from the restaurant.
     * @throws OccupiedTableException
     */
    public void removeTable(final Table table) throws OccupiedTableException, MissingTableException {
        boolean tableFound = false;
        
        if (table == null) throw new MissingTableException(table);
        
        for (int i = tableList.size() - 1; i >= 0; --i) {
            if (tableList.get(i) == table) {
                if (table.isOccupied()) {
                    throw new OccupiedTableException(table, table.getParty());
                } else {
                    tableList.remove(i);
                    tableFound = true;
                    break;
                }
            }
        }
        
        if (!tableFound) throw new MissingTableException(table);
    }

    /**
     * Creates a party with the given size and VIP status and adds it to the queue of parties waiting for a table.
     * If the size is less than or equal to zero,
     * this method throws an instance of {@link NonPositiveArgumentException}
     * with the size filled in.
     * @param size The size of the party.
     * @param isVIP Whether the party is a VIP.
     * @return A reference to the new party.
     * @throws NonPositiveArgumentException
     */
    public Party bookParty(final int size, final boolean isVIP) throws NonPositiveArgumentException {
        if (size <= 0) throw new NonPositiveArgumentException(size);
            
        Party newParty = new Party(size, isVIP);
        partyQueue.add(newParty);
        
        return newParty;
    }

    /**
     * Removes the specified party from the restaurant.
     * If the party is already seated, this method should empty the table that the party is currently seated at.
     * If the party does not exist, this method throws an instance of {@link MissingPartyException}
     * with the specified party filled in.
     * @param party The party to remove from the restaurant.
     */
    public void removeParty(final Party party) throws MissingPartyException {
        if (!partyQueue.contains(party)) throw new MissingPartyException(party);
        if (party.getSeatedTable() != null) party.getSeatedTable().freeTable();
    }

    /**
     * Seats the next eligible party to an empty table.
     * Priority should first be given to the earliest unseated VIP party.
     * If no VIP party can be seated, then priority should be given to the earliest unseated non-VIP party.
     * The table chosen should be any table with the smallest capacity that can seat the party.
     * If there is no table with a capacity large enough to seat the party, the next party should be checked.
     * If there is no table available that can seat any party in the queue,
     * this method throws an instance of {@link NoTablesAvailableException}
     * with the first party in the queue filled in.
     * If no parties are currently waiting, this method does nothing.
     * @throws NoTablesAvailableException
     */
    public void seatParty() throws NoTablesAvailableException {
    	if (partyQueue.size() == 0) return;
    	
    	Party[] waitingParties = (Party[]) partyQueue.toArray(new Party[partyQueue.size()]);
    	Arrays.sort(waitingParties, (p1, p2) -> Boolean.compare(p2.isVIP(), p1.isVIP()));
    	
    	boolean tableFound = false;
    	
    	for (Party party : waitingParties) {
    		List<Table> possibleTables = new ArrayList<Table>();
        	for (Table table : tableList) {
        		if (!table.isOccupied() && table.getTableCapacity() >= party.getPartySize()) possibleTables.add(table);
        	}
        	
        	if (possibleTables.size() == 0) continue;
        	
        	tableFound = true;
        	Table bestTable = possibleTables.get(0);
        	
        	for (Table table : possibleTables) {
        		if (table.getTableCapacity() < bestTable.getTableCapacity()) bestTable = table;
        	}
        	
        	partyQueue.remove(party);
        	party.seatAtTable(bestTable);
        	bestTable.OccupyTable(party);
        	break;
    	}
    	
    	if (!tableFound) throw new NoTablesAvailableException(partyQueue.peek());
    }

    /**
     * Removes a party from an occupied table.
     * If the table is not currently occupied, this method throws an instance of {@link EmptyTableException}
     * with the specified table filled in.
     * @param table The table to empty.
     * @throws EmptyTableException
     */
    public Party emptyTable(final Table table) throws EmptyTableException {        
        Party freedParty = null;

        if (!table.isOccupied()) throw new EmptyTableException(table);
        
        for (Table t : tableList) {
            if (t == table) {
                t.freeTable();
                freedParty = t.getParty();
                break;
            }
        }
        return freedParty;
    }

    /**
     * @return The list of tables currently occupied by a party.
     */
    public List<Table> getFilledTables() {
        List<Table> result = new ArrayList<Table>();
        for (Table table : tableList) {
            if (table.isOccupied()) result.add(table);
        }
        return result;
    }

    /**
     * @return The list of tables not occupied by a party.
     */
    public List<Table> getEmptyTables() {
        List<Table> result = new ArrayList<Table>();
        for (Table table : tableList) {
            if (!table.isOccupied()) result.add(table);
        }
        return result;
    }

    /**
     * @return The list of parties waiting for a table, with the earliest booked parties at the beginning of the list.
     */
    public List<Party> getUnseatedParties() {
    	List<Party> unseatedParties = new ArrayList<Party>();
    	for (Party party : (Party[]) partyQueue.toArray(new Party[partyQueue.size()])) {
    		unseatedParties.add(party);
    	}
        return unseatedParties;
    }
}
