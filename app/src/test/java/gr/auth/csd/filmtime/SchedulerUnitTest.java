package gr.auth.csd.filmtime;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import gr.auth.csd.filmtime.helpers.CrewMember;
import gr.auth.csd.filmtime.helpers.Scene;
import gr.auth.csd.filmtime.helpers.Scheduler;

public class SchedulerUnitTest {

    private ArrayList<Scene> scenes;
    private CrewMember crewMember1;
    private CrewMember crewMember2;
    private CrewMember crewMember3;

    @Before
    public void setUp() {
        scenes = new ArrayList<>();

        crewMember1 = new CrewMember(1, "John", "Director");
        crewMember2 = new CrewMember(2, "Jane", "Producer");
        crewMember3 = new CrewMember(3, "Maria", "Cinematographer");

        crewMember1.addDate(LocalDate.of(2023, 6, 15));
        crewMember1.addDate(LocalDate.of(2023, 6, 16));
        crewMember2.addDate(LocalDate.of(2023, 6, 15));
        crewMember2.addDate(LocalDate.of(2023, 6, 17));
        crewMember3.addDate(LocalDate.of(2023, 6, 16));
        crewMember3.addDate(LocalDate.of(2023, 6, 17));

        HashSet<CrewMember> crew1 = new HashSet<>();
        crew1.add(crewMember1);
        crew1.add(crewMember2);

        HashSet<CrewMember> crew2 = new HashSet<>();
        crew2.add(crewMember2);
        crew2.add(crewMember3);

        Scene scene1 = new Scene("Scene 1", crew1);
        Scene scene2 = new Scene("Scene 2", crew2);

        scenes.add(scene1);
        scenes.add(scene2);
    }

    @Test
    public void getPossibleShootingDates() {
        Map<LocalDate, Set<Scene>> shootingSchedule = Scheduler.getPossibleShootingDates(scenes);

        assertEquals(2, shootingSchedule.size());

        assertTrue(shootingSchedule.containsKey(LocalDate.of(2023, 6, 15)));
        Set<Scene> scenesOnDate1 = shootingSchedule.get(LocalDate.of(2023, 6, 15));
        assertEquals(1, scenesOnDate1.size());
        assertTrue(scenesOnDate1.contains(scenes.get(0)));

        assertTrue(shootingSchedule.containsKey(LocalDate.of(2023, 6, 17)));
        Set<Scene> scenesOnDate2 = shootingSchedule.get(LocalDate.of(2023, 6, 17));
        assertEquals(1, scenesOnDate2.size());
        assertTrue(scenesOnDate2.contains(scenes.get(1)));
    }
}
