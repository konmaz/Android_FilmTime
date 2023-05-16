package gr.auth.csd.filmtime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

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
        holder.itemTitle.setText(scenes.get(position).getName());
        String nameOfCrew = scenes.get(position).getCrewMember(0).getName();
        String jobOfCrew = scenes.get(position).getCrewMember(0).getJob();
        String crewText = nameOfCrew + ": " + jobOfCrew;
        holder.itemDetail.setText(crewText);
    }

    @Override
    public int getItemCount() {

        return scenes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView itemTitle;
        final TextView itemDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDetail = itemView.findViewById(R.id.item_detail);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition() + 1;
                    Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG).show();
                }
            });

        }


    }
}
