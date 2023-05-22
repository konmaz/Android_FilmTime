package gr.auth.csd.filmtime;

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

import gr.auth.csd.filmtime.helpers.CrewMember;
import gr.auth.csd.filmtime.helpers.Scene;

public class RecyclerAdapterScene extends RecyclerView.Adapter<RecyclerAdapterScene.ViewHolder>{

    private final ArrayList<Scene> scenes;

    public RecyclerAdapterScene(ArrayList<Scene> scenes){
        this.scenes = scenes;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_scene, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Scene scene = scenes.get(position);
        holder.itemTitle.setText(scene.getName());
        StringBuilder crewText = new StringBuilder();
        for (CrewMember crewMember : scene.getCrewMembers()) { // for loop iterator
            String nameOfCrew = crewMember.getName();
            String jobOfCrew = crewMember.getJob();
            crewText.append(nameOfCrew).append(": ").append(jobOfCrew).append(", ");
        }
        holder.itemDetail.setText(crewText);
        holder.itemView.setTag(scene);

        holder.itemButton.setTag(scene);

        holder.itemButton.setOnClickListener(v -> {
            Scene scene1 = (Scene) v.getTag();
            if (scene1 != null) {
                Bundle args = new Bundle();
                args.putLong("scene_id", scene1.getID());
                args.putString("title", "Edit"); // this changed the top bar title
                Navigation.findNavController(v).navigate(R.id.action_ScenesFragment_to_editorScene, args);
                //Snackbar.make(v, "Click detected on item", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return scenes.size();
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


//            itemButton.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    Scene scene = (Scene) v.getTag();
//                    int position = getAdapterPosition() + 1;
//                    Bundle args = new Bundle();
//                    args.putLong("scene_id", scene.getID()); // i want to refrence the id of the scene.getCrewMember(i).getID();
//                    Navigation.findNavController(v).navigate(R.id.action_ScenesFragment_to_editorScene);
//                    Snackbar.make(v, "Click detected on item " + position,
//                            Snackbar.LENGTH_LONG).show();
//                }
//            });

        }


    }
}
