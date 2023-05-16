package gr.auth.csd.filmtime;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import gr.auth.csd.filmtime.databinding.FragmentScenesBinding;
import gr.auth.csd.filmtime.helpers.CrewMember;
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

        recyclerView = view.findViewById(R.id.recycler_scenes);
        addButton = view.findViewById(R.id.add_new_scene);

        ArrayList<CrewMember> crewmembers1 = new ArrayList<CrewMember>(Arrays.asList(
                new CrewMember("Dave", "Actor"), new CrewMember("John", "Camera")));

        ArrayList<CrewMember> crewmembers2 = new ArrayList<CrewMember>(Arrays.asList(
                new CrewMember("Kathy", "Actor"), new CrewMember("John", "Camera")));

        ArrayList<ArrayList<CrewMember>> crews1 = new ArrayList<ArrayList<CrewMember>>();
        crews1.add(crewmembers1);
        crews1.add(crewmembers2);

        ArrayList<Scene> scenes1 = new ArrayList<Scene>(Arrays.asList(
                new Scene("Intro", crews1.get(0)),
                new Scene("Bathroom", crews1.get(1))));

        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapterScene(scenes1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scenes1.add(0, new Scene());
                adapter.notifyItemInserted(0);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
