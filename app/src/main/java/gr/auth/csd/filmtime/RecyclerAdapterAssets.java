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
import java.util.HashSet;

import gr.auth.csd.filmtime.helpers.CrewMember;

public class RecyclerAdapterAssets extends RecyclerView.Adapter<RecyclerAdapterAssets.ViewHolder>{

    private final ArrayList<CrewMember> crewMemberArrayList;

    public RecyclerAdapterAssets(HashSet<CrewMember> crewMemberArrayList){
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
                //Snackbar.make(v, "Click detected on item", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return crewMemberArrayList.size();
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
