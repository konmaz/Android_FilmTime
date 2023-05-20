package gr.auth.csd.filmtime.helpers;

import java.io.Serializable;

public class CrewMember implements Serializable {
    private long ID; // used for database
    private String name;

    private String job;

    public CrewMember(long ID,String name, String job){
        this.ID = ID;
        this.name = name;
        this.job = job;
    }

    public CrewMember(){
        name = "Crew Member";
        job = "Actor";
    }

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
}
