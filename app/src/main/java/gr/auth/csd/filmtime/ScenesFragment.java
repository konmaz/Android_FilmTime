package gr.auth.csd.filmtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import gr.auth.csd.filmtime.databinding.FragmentScenesBinding;
import gr.auth.csd.filmtime.helpers.Database;

/**
 * A fragment class responsible for displaying a list of Scenes using a RecyclerView.
 */
public class ScenesFragment extends Fragment {

    private FragmentScenesBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScenesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Database dbHandler = new Database(getContext());


        RecyclerView recyclerView = view.findViewById(R.id.recycler_scenes);
        Button addButton = view.findViewById(R.id.add_new_scene);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter<RecyclerAdapterGeneric.ViewHolder> adapter = new RecyclerAdapterGeneric(dbHandler.getScenes());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        addButton.setOnClickListener(v -> { // Handle 'Add New Scene' button
            Bundle args = new Bundle();
            args.putString("title", "Add"); // this changed the top bar title
            args.putLong("scene_id", -1);
            Navigation.findNavController(v).navigate(R.id.action_ScenesFragment_to_editorScene, args);


        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
