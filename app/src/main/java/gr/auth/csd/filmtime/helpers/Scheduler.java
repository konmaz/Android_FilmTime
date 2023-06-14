package gr.auth.csd.filmtime.helpers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * A helper class used to calculate the shooting dates for a scene based on the availabilities of crew members.
 * It provides methods to determine the dates on which a scene can be shot, considering the availability of crew members.
 */
public class Scheduler {
    /**
     * Retrieves a set of possible shooting dates for scenes based on crew members' availability.
     * A scene is considered available to be shot if all crew members have at least one common day of availability.
     * If there are no common days of availability among crew members, the scene is excluded from the output set of scenes.
     *
     * @param scenes An ArrayList of scenes.
     * @return A HashMap containing the shooting dates as keys and the corresponding set of scenes that can be shot on those dates as values.
     */
    static public Map<LocalDate, Set<Scene>> getPossibleShootingDates(ArrayList<Scene> scenes) {
        Map<LocalDate, Set<Scene>> shootingSchedule = new HashMap<>();

        for (Scene scene : scenes) {
            Set<CrewMember> crewMembers = scene.getCrewMembers();
            if (crewMembers.isEmpty()) {
                continue; // Ignore scenes with no crew members
            }

            Set<LocalDate> commonAvailableDates = null;

            for (CrewMember crewMember : crewMembers) { // for each scene CrewMember
                TreeSet<LocalDate> memberAvailabilities = crewMember.getAvailabilities(); // get their availabilities

                if (commonAvailableDates == null) {
                    commonAvailableDates = new TreeSet<>(memberAvailabilities);
                } else {
                    commonAvailableDates.retainAll(memberAvailabilities);
                }
            }

            if (!commonAvailableDates.isEmpty()) {
                // Add the scene to the shooting schedule for each common available date
                for (LocalDate date : commonAvailableDates) {
                    shootingSchedule.computeIfAbsent(date, k -> new HashSet<>()).add(scene);
                }
            }
        }

        return shootingSchedule;
    }
}
