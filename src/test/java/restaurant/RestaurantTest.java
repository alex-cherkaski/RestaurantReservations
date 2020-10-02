package restaurant;

import org.junit.Test;
import restaurant.exceptions.EmptyTableException;
import restaurant.exceptions.MissingPartyException;
import restaurant.exceptions.MissingTableException;
import restaurant.exceptions.NoTablesAvailableException;
import restaurant.exceptions.NonPositiveArgumentException;
import restaurant.exceptions.OccupiedTableException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RestaurantTest {
    private final Restaurant restaurant = new Restaurant();

    @Test
    public void addTable_NegativeArgument() {
        try {
            restaurant.addTable(-1);
            fail();
        } catch (NonPositiveArgumentException e) {
            assertEquals(-1, e.getArgument());
        }
        try {
            restaurant.addTable(-124);
            fail();
        } catch (NonPositiveArgumentException e) {
            assertEquals(-124, e.getArgument());
        }
    }

    @Test
    public void addTable_ZeroArgument() {
        try {
            restaurant.addTable(0);
            fail();
        } catch (NonPositiveArgumentException e) {
            assertEquals(0, e.getArgument());
        }
    }

    @Test
    public void addTable() throws NonPositiveArgumentException {
        Table table1 = restaurant.addTable(23);
        assertTrue(restaurant.getEmptyTables().contains(table1));
        assertFalse(restaurant.getFilledTables().contains(table1));
        assertEquals(1, restaurant.getEmptyTables().size());
        assertTrue(restaurant.getFilledTables().isEmpty());
        Table table2 = restaurant.addTable(11);
        assertTrue(restaurant.getEmptyTables().contains(table2));
        assertFalse(restaurant.getFilledTables().contains(table2));
        assertEquals(2, restaurant.getEmptyTables().size());
        assertTrue(restaurant.getFilledTables().isEmpty());
        assertTrue(restaurant.getEmptyTables().contains(table1));
        assertTrue(restaurant.getEmptyTables().contains(table2));
    }

    @Test
    public void removeTable_Null() throws OccupiedTableException {
        try {
            restaurant.removeTable(null);
            fail();
        } catch (final MissingTableException e) {
            assertNull(e.getTable());
        }
    }

    @Test
    public void removeTable_MissingTable() throws OccupiedTableException {
        Table table = null;
        try {
            table = restaurant.addTable(5);
            restaurant.removeTable(table);
            restaurant.removeTable(table);
            fail();
        } catch (final MissingTableException e) {
            assertEquals(table, e.getTable());
        } catch (NonPositiveArgumentException e) {
            fail();
        }
    }

    @Test
    public void removeTable_OccupiedTable() throws NonPositiveArgumentException, MissingTableException, NoTablesAvailableException {
        Table table = restaurant.addTable(5);
        Party party = restaurant.bookParty(5, false);
        assertFalse(party.isVIP());
        try {
            restaurant.seatParty();
            restaurant.removeTable(table);
            fail();
        } catch (OccupiedTableException e) {
            assertEquals(table, e.getTable());
            assertEquals(party, e.getParty());
            assertEquals(1, restaurant.getFilledTables().size());
            assertTrue(restaurant.getFilledTables().contains(table));
            assertEquals(0, restaurant.getUnseatedParties().size());
        }
    }

    @Test
    public void removeTable_EmptyTable() throws OccupiedTableException, MissingTableException, NonPositiveArgumentException {
        Table table = restaurant.addTable(5);
        assertEquals(1, restaurant.getEmptyTables().size());
        assertTrue(restaurant.getEmptyTables().contains(table));
        restaurant.removeTable(table);
        assertEquals(0, restaurant.getEmptyTables().size());
    }

    @Test
    public void bookParty_Negative() {
        try {
            restaurant.bookParty(-5, false);
        } catch (NonPositiveArgumentException e) {
            assertEquals(-5, e.getArgument());
        }
    }

    @Test
    public void bookParty_Zero() {
        try {
            restaurant.bookParty(0, false);
        } catch (NonPositiveArgumentException e) {
            assertEquals(0, e.getArgument());
        }
    }

    @Test
    public void bookParty_Positive() throws NonPositiveArgumentException {
        Party party = restaurant.bookParty(10, false);
        assertFalse(party.isVIP());
        assertEquals(1, restaurant.getUnseatedParties().size());
        assertEquals(party, restaurant.getUnseatedParties().get(0));
    }

    @Test
    public void bookParty_VIP() throws NonPositiveArgumentException {
        Party party = restaurant.bookParty(10, true);
        assertTrue(party.isVIP());
        assertEquals(1, restaurant.getUnseatedParties().size());
        assertEquals(party, restaurant.getUnseatedParties().get(0));
    }

    @Test
    public void removeParty_Null() {
        try {
            restaurant.removeParty(null);
        } catch (MissingPartyException e) {
            assertNull(e.getParty());
        }
    }

    @Test
    public void emptyRestaurant() {
        assertTrue(restaurant.getEmptyTables().isEmpty());
        assertTrue(restaurant.getFilledTables().isEmpty());
        assertTrue(restaurant.getUnseatedParties().isEmpty());
    }

    @Test
    public void emptyTable_Unoccupied() {
        Table table = null;
        try {
            table = restaurant.addTable(5);
            restaurant.emptyTable(table);
            fail();
        } catch (NonPositiveArgumentException e) {
            fail();
        } catch (EmptyTableException e) {
            assertEquals(table, e.getTable());
        }
    }

    @Test
    public void seatParty_NoParties() {
        try {
            Table table = restaurant.addTable(5);
            restaurant.seatParty();
            assertEquals(1, restaurant.getEmptyTables().size());
            assertTrue(restaurant.getEmptyTables().contains(table));
            assertTrue(restaurant.getFilledTables().isEmpty());
        } catch (NonPositiveArgumentException | NoTablesAvailableException e) {
            fail();
        }
    }

    @Test
    public void seatParty_NoTables() {
        Party party = null;
        try {
            party = restaurant.bookParty(5, false);
            assertFalse(party.isVIP());
            restaurant.seatParty();
            fail();
        } catch (NonPositiveArgumentException e) {
            fail();
        } catch (NoTablesAvailableException e) {
            assertEquals(party, e.getParty());
            assertFalse(restaurant.getUnseatedParties().isEmpty());
            assertEquals(party, restaurant.getUnseatedParties().get(0));
        }
    }

    @Test
    public void seatParty_SmallTable() {
        Party party = null;
        Table table = null;
        try {
            table = restaurant.addTable(4);
            party = restaurant.bookParty(5, false);
            assertFalse(party.isVIP());
            restaurant.seatParty();
            fail();
        } catch (NonPositiveArgumentException e) {
            fail();
        } catch (NoTablesAvailableException e) {
            assertEquals(party, e.getParty());
        }
        assertTrue(restaurant.getFilledTables().isEmpty());
        assertEquals(1, restaurant.getEmptyTables().size());
        assertTrue(restaurant.getEmptyTables().contains(table));
        assertFalse(restaurant.getUnseatedParties().isEmpty());
        assertEquals(party, restaurant.getUnseatedParties().get(0));
    }

    @Test
    public void seatParty_EqualSizedTable() {
        try {
            Table table = restaurant.addTable(5);
            Party party = restaurant.bookParty(5, false);
            assertFalse(party.isVIP());
            restaurant.seatParty();
            assertTrue(restaurant.getEmptyTables().isEmpty());
            assertEquals(1, restaurant.getFilledTables().size());
            assertTrue(restaurant.getFilledTables().contains(table));
            assertEquals(party, table.getParty());
            assertTrue(restaurant.getUnseatedParties().isEmpty());
        } catch (NonPositiveArgumentException | NoTablesAvailableException e) {
            fail();
        }
    }

    @Test
    public void seatParty_LargerTable() {
        try {
            Table table = restaurant.addTable(6);
            Party party = restaurant.bookParty(5, false);
            assertFalse(party.isVIP());
            restaurant.seatParty();
            assertTrue(restaurant.getEmptyTables().isEmpty());
            assertEquals(1, restaurant.getFilledTables().size());
            assertTrue(restaurant.getFilledTables().contains(table));
            assertEquals(party, table.getParty());
            assertTrue(restaurant.getUnseatedParties().isEmpty());
        } catch (NonPositiveArgumentException | NoTablesAvailableException e) {
            fail();
        }
    }

    @Test
    public void seatParty_FirstPartyInList() {
        try {
            Table table = restaurant.addTable(6);
            Party party1 = restaurant.bookParty(5, false);
            Party party2 = restaurant.bookParty(5, false);
            assertFalse(party1.isVIP());
            assertFalse(party2.isVIP());
            restaurant.seatParty();
            assertTrue(restaurant.getEmptyTables().isEmpty());
            assertEquals(1, restaurant.getFilledTables().size());
            assertTrue(restaurant.getFilledTables().contains(table));
            assertEquals(party1, table.getParty());
            assertEquals(1, restaurant.getUnseatedParties().size());
            assertEquals(party2, restaurant.getUnseatedParties().get(0));
        } catch (NonPositiveArgumentException | NoTablesAvailableException e) {
            fail();
        }
    }

    @Test
    public void seatParty_SecondPartyInList() {
        try {
            Table table = restaurant.addTable(6);
            Party party1 = restaurant.bookParty(7, false);
            Party party2 = restaurant.bookParty(5, false);
            assertFalse(party1.isVIP());
            assertFalse(party2.isVIP());
            restaurant.seatParty();
            assertTrue(restaurant.getEmptyTables().isEmpty());
            assertEquals(1, restaurant.getFilledTables().size());
            assertTrue(restaurant.getFilledTables().contains(table));
            assertEquals(party2, table.getParty());
            assertEquals(1, restaurant.getUnseatedParties().size());
            assertEquals(party1, restaurant.getUnseatedParties().get(0));
        } catch (NonPositiveArgumentException | NoTablesAvailableException e) {
            fail();
        }
    }

    @Test
    public void seatParty_VIPPartyInList() {
        try {
            Table table = restaurant.addTable(6);
            Party party1 = restaurant.bookParty(5, false);
            Party party2 = restaurant.bookParty(5, true);
            assertFalse(party1.isVIP());
            assertTrue(party2.isVIP());
            restaurant.seatParty();
            assertTrue(restaurant.getEmptyTables().isEmpty());
            assertEquals(1, restaurant.getFilledTables().size());
            assertTrue(restaurant.getFilledTables().contains(table));
            assertEquals(party2, table.getParty());
            assertEquals(1, restaurant.getUnseatedParties().size());
            assertEquals(party1, restaurant.getUnseatedParties().get(0));
        } catch (NonPositiveArgumentException | NoTablesAvailableException e) {
            fail();
        }
    }

}
