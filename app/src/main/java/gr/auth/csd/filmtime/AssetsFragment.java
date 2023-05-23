package gr.auth.csd.filmtime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gr.auth.csd.filmtime.databinding.FragmentAssetsBinding;
import gr.auth.csd.filmtime.helpers.Database;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssetsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssetsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FragmentAssetsBinding binding;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter<RecyclerAdapterGeneric.ViewHolder> adapter;
    private static final String ARG_PARAM1 = "param1";

    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AssetsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssetsFragment newInstance(String param1, String param2) {
        AssetsFragment fragment = new AssetsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapterGeneric(dbHandler.getCrewMembers());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("title", "Add"); // this changed the top bar title
                args.putLong("crew_member_id", -1);
                Navigation.findNavController(v).navigate(R.id.action_assetsFragment_to_editorAsset, args);


            }
        });





    }
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}