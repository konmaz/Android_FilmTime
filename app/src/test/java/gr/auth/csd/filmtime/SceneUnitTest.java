package gr.auth.csd.filmtime;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import gr.auth.csd.filmtime.helpers.CrewMember;
import gr.auth.csd.filmtime.helpers.Scene;

public class SceneUnitTest {

    private Scene scene;

    @Before
    public void setUp() {
        scene = new Scene("Scene");
    }

    @Test
    public void getName() {
        assertEquals("Scene", scene.getName());
    }

    @Test
    public void setName() {
        scene.setName("New Scene");
        assertEquals("New Scene", scene.getName());
    }

    @Test
    public void getCrewMembers() {
        CrewMember crewMember1 = new CrewMember(1, "Maria", "Director");
        CrewMember crewMember2 = new CrewMember(2, "Jane", "Producer");
        HashSet<CrewMember> crewMembers = new HashSet<>();
        crewMembers.add(crewMember1);
        crewMembers.add(crewMember2);
        scene = new Scene("Scene", crewMembers);

        HashSet<CrewMember> retrievedCrewMembers = scene.getCrewMembers();

        assertEquals(2, retrievedCrewMembers.size());
        assertTrue(retrievedCrewMembers.contains(crewMember1));
        assertTrue(retrievedCrewMembers.contains(crewMember2));
    }

    @Test
    public void getCrewMembersSize() {
        assertEquals(0, scene.getCrewMembersSize());

        CrewMember crewMember = new CrewMember(1, "Maria", "Director");
        HashSet<CrewMember> crewMembers = new HashSet<>();
        crewMembers.add(crewMember);
        scene = new Scene("Scene", crewMembers);

        assertEquals(1, scene.getCrewMembersSize());
    }
}
