package gr.auth.csd.filmtime.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Scene implements Serializable {
    private long ID;
    private String name;

    private ArrayList<CrewMember> crew;

    public Scene(String name, ArrayList<CrewMember> crew){
        this.name = name;
        this.crew = crew;
    }

    public Scene(){
        this.name = "Scene";
        this.crew = new ArrayList<CrewMember>(Collections.singletonList(new CrewMember()));
    }

    public Scene(long sceneId, String name, ArrayList<CrewMember> crew ) {
        this.ID = sceneId;
        this.name = name;
        this.crew = crew;
    }

    public long getID() {
        return ID;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setCrewList(ArrayList<CrewMember> crew){
        this.crew = crew;
    }

    public void pushCrewMember(CrewMember member){
        this.crew.add(member);
    }

    public void popCrewMember(){
        this.crew.remove(this.crew.size());
    }

    public void removeCrewMember(int position){
        this.crew.remove(position);
    }

    public void insertCrewMember(CrewMember member, int position){
        this.crew.add(position, member);
    }

    public CrewMember getCrewMember(int position){
        return this.crew.get(position);
    }

    public int getCrewMembersSize(){
        return this.crew.size();
    }

}
