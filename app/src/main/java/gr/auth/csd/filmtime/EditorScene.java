package gr.auth.csd.filmtime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditorScene#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditorScene extends Fragment {

    private Button saveButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "scene_id";

    // TODO: Rename and change types of parameters
    private long parameter_scene_id = -1;

    public EditorScene() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EditorScene.
     */
    // TODO: Rename and change types and number of parameters
    public static EditorScene newInstance(long param1) {
        EditorScene fragment = new EditorScene();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("myCustom", "onCreate: called");
        if (getArguments() != null) {
            parameter_scene_id = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("myCustom", "onCreateView: called");
        // Inflate the layout for this fragment
        Log.d("myCustom", "onViewCreated: called also " + parameter_scene_id);
        return inflater.inflate(R.layout.fragment_editor_scene, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        saveButton = view.findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_editorScene_to_ScenesFragment);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}