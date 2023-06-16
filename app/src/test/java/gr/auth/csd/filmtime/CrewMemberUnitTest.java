package gr.auth.csd.filmtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import gr.auth.csd.filmtime.helpers.CrewMember;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CrewMemberUnitTest {

    public CrewMember crewMember;

    @Before
    public void setUp() {
        crewMember = new CrewMember(1, "Bob", "Director");
    }

    @Test
    public void addDate() {
        crewMember.addDate(LocalDate.of(2023, 6, 15));
        assertEquals(1, crewMember.getAvailabilities().size());
    }

    @Test
    public void addDateWithCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JUNE, 15);
        crewMember.addDate(calendar);
        assertEquals(1, crewMember.getAvailabilities().size());
    }

    @Test
    public void getAvailabilitiesCalendarDatatype() {
        crewMember.addDate(LocalDate.of(2023, 6, 15));
        List<Calendar> calendarList = crewMember.getAvailabilitiesCalendarDatatype();
        assertEquals(1, calendarList.size());
        Calendar calendar = calendarList.get(0);
        assertEquals(2023, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.JUNE, calendar.get(Calendar.MONTH));
        assertEquals(15, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void getName() {
        assertEquals("Bob", crewMember.getName());
    }

    @Test
    public void setName() {
        crewMember.setName("Jake");
        assertEquals("Jake", crewMember.getName());
    }

    @Test
    public void getJob() {
        assertEquals("Director", crewMember.getJob());
    }

    @Test
    public void setJob() {
        crewMember.setJob("Producer");
        assertEquals("Producer", crewMember.getJob());
    }

    @Test
    public void compareTo() {
        CrewMember otherCrewMember = new CrewMember(2, "Jake", "Producer");
        assertTrue(crewMember.compareTo(otherCrewMember) < 0);
        assertTrue(otherCrewMember.compareTo(crewMember) > 0);
        assertEquals(0, crewMember.compareTo(crewMember));
    }

    @Test
    public void equals() {
        CrewMember sameCrewMember = new CrewMember(1, "Bob", "Director");
        assertTrue(crewMember.equals(sameCrewMember));
    }

    @Test
    public void hashCodeTest() {
        CrewMember sameCrewMember = new CrewMember(1, "Bob", "Director");
        assertEquals(crewMember.hashCode(), sameCrewMember.hashCode());
    }

    @Test
    public void setAvailabilities() {
        crewMember.setAvailabilities(new TreeSet<>());
        assertTrue(crewMember.getAvailabilities().isEmpty());
    }

    @Test
    public void deleteAvailabilities() {
        crewMember.addDate(LocalDate.of(2023, 6, 15));
        crewMember.deleteAvailabilities();
        assertTrue(crewMember.getAvailabilities().isEmpty());
    }


}