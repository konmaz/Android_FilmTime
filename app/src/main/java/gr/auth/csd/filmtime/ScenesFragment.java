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

import java.util.ArrayList;
import java.util.Arrays;

import gr.auth.csd.filmtime.databinding.FragmentScenesBinding;
import gr.auth.csd.filmtime.helpers.CrewMember;
import gr.auth.csd.filmtime.helpers.Database;
import gr.auth.csd.filmtime.helpers.Scene;

public class ScenesFragment extends Fragment {

    private FragmentScenesBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter<RecyclerAdapterScene.ViewHolder> adapter;
    private Button addButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScenesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Database dbHandler = new Database(getContext());
//        dbHandler.createScene(new Scene());
        dbHandler.debug_open_database_and_dont_close_it();


        recyclerView = view.findViewById(R.id.recycler_scenes);
        addButton = view.findViewById(R.id.add_new_scene);

//        ArrayList<CrewMember> crewmembers1 = new ArrayList<CrewMember>(Arrays.asList(
//                new CrewMember(1,"Dave", "Actor"), new CrewMember(1,"John", "Camera")));
//
//        ArrayList<CrewMember> crewmembers2 = new ArrayList<CrewMember>(Arrays.asList(
//                new CrewMember(1,"Kathy", "Actor"), new CrewMember(1,"John", "Camera")));
//
//        ArrayList<ArrayList<CrewMember>> crews1 = new ArrayList<ArrayList<CrewMember>>();
//        crews1.add(crewmembers1);
//        crews1.add(crewmembers2);
//
//        ArrayList<Scene> scenes1 = new ArrayList<Scene>(Arrays.asList(
//                new Scene("Intro", crews1.get(0)),
//                new Scene("Bathroom", crews1.get(1))));

        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapterScene(dbHandler.getScenes());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("title", "Add"); // this changed the top bar title
                Navigation.findNavController(v).navigate(R.id.action_ScenesFragment_to_editorScene, args);


            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
