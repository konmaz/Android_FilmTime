package gr.auth.csd.filmtime.helpers;

public class CrewMember {
    private String name;
    private String job;

    public CrewMember(String name, String job){
        this.name = name;
        this.job = job;
    }

    public CrewMember(){
        name = "Crew Member";
        job = "Actor";
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
