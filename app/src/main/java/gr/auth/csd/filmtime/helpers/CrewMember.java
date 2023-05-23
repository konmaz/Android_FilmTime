package gr.auth.csd.filmtime.helpers;


import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class CrewMember implements Comparable<CrewMember> {
    private long ID; // used for database
    private String name;

    private String job;

    private TreeSet<LocalDate> availabilities;

    public CrewMember(long ID,String name, String job){
        this.ID = ID;
        this.name = name;
        this.job = job;
        this.availabilities = new TreeSet<>();
    }

    public CrewMember(String name, String job){
        this.name = name;
        this.job = job;
        this.availabilities = new TreeSet<>();
    }

    public void addDate(LocalDate date){
        this.availabilities.add(date);
    }

    public void addDate(@NonNull Calendar date){
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);

        addDate(LocalDate.of(year, month, dayOfMonth));
    }

    public TreeSet<LocalDate> getAvailabilities(){
        return this.availabilities;
    }

    public List<Calendar> getAvailabilitiesCalendarDatatype(){
        List<Calendar> calendarList = new ArrayList<>();

        for (LocalDate availability : this.availabilities) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(availability.getYear(), availability.getMonthValue() - 1, availability.getDayOfMonth());
            calendarList.add(calendar);
        }
        return calendarList;
    }

//    public CrewMember(){
//        name = "Crew Member";
//        job = "Actor";
//    }

    public long getID() {
        return ID;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setJob(String job){
        this.job = job;
    }

    public String getJob(){
        return job;
    }

    @Override
    public int compareTo(CrewMember o) {
        return Long.compare(this.ID, o.ID);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CrewMember other = (CrewMember) obj;
        return Objects.equals(ID, other.ID) && Objects.equals(name, other.name) && Objects.equals(job, other.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID ,name, job);
    }

    public void setAvailabilities(TreeSet<LocalDate> availabilities) {
        this.availabilities = availabilities;
    }
    public void deleteAvailabilities() {
        this.availabilities = new TreeSet<>();
    }
}
