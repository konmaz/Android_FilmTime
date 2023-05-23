package gr.auth.csd.filmtime.helpers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Scheduler {
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
