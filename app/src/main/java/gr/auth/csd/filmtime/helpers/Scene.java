package gr.auth.csd.filmtime.helpers;

import java.io.Serializable;
import java.util.HashSet;

public class Scene implements Serializable {
    private long ID;
    private String name;

    private HashSet<CrewMember> crewMemberHashSet;

    public Scene(String name, HashSet<CrewMember> crew){
        this.name = name;
        this.crewMemberHashSet = crew;
    }

    public Scene(){
        this.name = "Scene";
        this.crewMemberHashSet = new HashSet<CrewMember>();
    }

    public Scene(String name){
        this.name = name;
        this.crewMemberHashSet = new HashSet<CrewMember>();
    }

    public Scene(long sceneId, String name, HashSet<CrewMember> crew ) {
        this.ID = sceneId;
        this.name = name;
        this.crewMemberHashSet = crew;
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

    public void addCrewMemberToScene(CrewMember member){
        this.crewMemberHashSet.add(member);
    }

    public void removeCrewMember(CrewMember member){
        this.crewMemberHashSet.remove(member);
    }

    public void insertCrewMember(CrewMember member){
        this.crewMemberHashSet.add(member);
    }

    public boolean CrewMemberInScene(CrewMember member){
        return this.crewMemberHashSet.contains(member);
    }

    public HashSet<CrewMember> getCrewMembers() {
        return new HashSet<>(crewMemberHashSet); //defensive copy
    }

    public int getCrewMembersSize(){
        return this.crewMemberHashSet.size();
    }

    public void replaceCrewMembers(HashSet<CrewMember> crewMemberHashSet){
        this.crewMemberHashSet = crewMemberHashSet;
    }

}
