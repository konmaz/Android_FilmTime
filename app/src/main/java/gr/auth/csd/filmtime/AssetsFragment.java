package gr.auth.csd.filmtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import gr.auth.csd.filmtime.helpers.Database;

/**
 * A fragment class responsible for displaying a list of assets (CrewMembers) using a RecyclerView.
 */
public class AssetsFragment extends Fragment {

    public AssetsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Database dbHandler = new Database(getContext());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_assets);
        View addButton = view.findViewById(R.id.add_new_Asset);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        // populate the recycler view with the the assets (Crew Members)
        RecyclerView.Adapter<RecyclerAdapterGeneric.ViewHolder> adapter = new RecyclerAdapterGeneric(dbHandler.getCrewMembers());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        addButton.setOnClickListener(v -> { // add event handling for button 'Add New Scene'
            Bundle args = new Bundle();
            args.putString("title", "Add"); // this changed the top bar title
            args.putLong("crew_member_id", -1);
            Navigation.findNavController(v).navigate(R.id.action_assetsFragment_to_editorAsset, args);


        });


    }

}