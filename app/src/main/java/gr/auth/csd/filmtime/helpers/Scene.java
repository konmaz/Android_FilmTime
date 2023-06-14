package gr.auth.csd.filmtime.helpers;

import java.io.Serializable;
import java.util.HashSet;

/**
 * A class representing a scene in a film or production.
 * It provides methods and properties to store and retrieve information about a scene.
 * This class implements the Serializable interface to allow objects of this class to be serialized and deserialized.
 */
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

    public HashSet<CrewMember> getCrewMembers() {
        return new HashSet<>(crewMemberHashSet); //defensive copy
    }

    public int getCrewMembersSize(){
        return this.crewMemberHashSet.size();
    }

}
