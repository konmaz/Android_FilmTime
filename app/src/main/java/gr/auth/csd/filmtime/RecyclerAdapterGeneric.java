package gr.auth.csd.filmtime;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;

import gr.auth.csd.filmtime.helpers.CrewMember;
import gr.auth.csd.filmtime.helpers.Scene;

public class RecyclerAdapterGeneric extends RecyclerView.Adapter<RecyclerAdapterGeneric.ViewHolder>{

    private ArrayList<Scene> scenes;
    private final ArrayList<CrewMember> crewMemberArrayList;

    private boolean isButtonEnabled = true;


    public RecyclerAdapterGeneric(ArrayList<Scene> scenes){
        this.crewMemberArrayList = null;
        this.scenes = scenes;
    }
    public void disableButton(){
        this.isButtonEnabled = false;
    }

    public RecyclerAdapterGeneric(HashSet<CrewMember> crewMemberArrayList){
        this.scenes = null;
        this.crewMemberArrayList = new ArrayList<>(crewMemberArrayList);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (scenes == null)
            onBindViewHolderAsset(holder, position);
        else
            onBindViewHolderScene(holder,position);
    }

    private void onBindViewHolderScene(@NonNull ViewHolder holder, int position){
        Scene scene = scenes.get(position);
        holder.itemTitle.setText(scene.getName());
        StringBuilder crewText = new StringBuilder();
        for (CrewMember crewMember : scene.getCrewMembers()) { // for loop iterator
            String nameOfCrew = crewMember.getName();
            String jobOfCrew = crewMember.getJob();
            crewText.append(nameOfCrew).append(": ").append(jobOfCrew).append(", ");
        }
        if(crewText.length()>0)
            crewText.delete(crewText.length()-2,crewText.length()-1);
        holder.itemDetail.setText(crewText);
        holder.itemView.setTag(scene);

        holder.itemButton.setTag(scene);


        if (isButtonEnabled) {
            holder.itemButton.setEnabled(true);
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#386150"));
            holder.itemButton.setVisibility(View.GONE);
            holder.itemButton.setEnabled(false);
        }

        if (isButtonEnabled)
            holder.itemButton.setOnClickListener(v -> {
                Scene scene1 = (Scene) v.getTag();
                if (scene1 != null) {
                    Bundle args = new Bundle();
                    args.putLong("scene_id", scene1.getID());
                    args.putString("title", "Edit"); // this changed the top bar title
                    Navigation.findNavController(v).navigate(R.id.action_ScenesFragment_to_editorScene, args);
                }
            });
    }

    private void onBindViewHolderAsset(ViewHolder holder, int position) {
        assert crewMemberArrayList != null;
        CrewMember member = crewMemberArrayList.get(position);
        holder.itemTitle.setText(member.getName());

        holder.itemDetail.setText(member.getJob());
        holder.itemView.setTag(member);

        holder.itemButton.setTag(member);

        holder.itemButton.setOnClickListener(v -> {
            CrewMember scene1 = (CrewMember) v.getTag();
            if (scene1 != null) {
                Bundle args = new Bundle();
                args.putLong("crew_member_id", scene1.getID());
                args.putString("title", "Edit"); // this changed the top bar title
                Navigation.findNavController(v).navigate(R.id.action_assetsFragment_to_editorAsset, args);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (scenes == null) {
            assert crewMemberArrayList != null;
            return crewMemberArrayList.size();
        }
        else
            return scenes.size();
    }

    public void setScenes(ArrayList<Scene> scenes) {
        this.scenes = scenes;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView itemTitle;
        final TextView itemDetail;

        final Button itemButton;

        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDetail = itemView.findViewById(R.id.item_detail);
            itemButton = itemView.findViewById(R.id.edit);


        }


    }


}
