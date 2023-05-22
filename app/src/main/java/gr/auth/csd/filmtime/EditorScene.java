package gr.auth.csd.filmtime;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import gr.auth.csd.filmtime.helpers.CrewMember;
import gr.auth.csd.filmtime.helpers.Database;
import gr.auth.csd.filmtime.helpers.Scene;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditorScene#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditorScene extends Fragment {

    private Button saveButton;
    private Button deleteButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "scene_id";


    // TODO: Rename and change types of parameters
    private long parameter_scene_id = -1;

    private View Text_scene_Name;

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

        Scene scene_obj = null;
        Database dbHandler = new Database(getContext());
        HashSet<CrewMember> allCrewMembers = dbHandler.getCrewMembers();
        Log.d(TAG, "parameter_scene_id : " + parameter_scene_id);
        if (parameter_scene_id != -1) {
            Log.d(TAG, "Edit");
            scene_obj = dbHandler.getScene(parameter_scene_id);
            TextView sceneNameTextView = view.findViewById(R.id.editTextSceneName);
            sceneNameTextView.setText(scene_obj.getName());

            //scene_crew_members = dbHandler.getCrewMembersForScene(parameter_scene_id);
        }
        saveButton = view.findViewById(R.id.editor_scene_save);
        int i = 0;
        LinearLayout containerLayout = view.findViewById(R.id.fragment_editor_scene_crew_linear_layout);
        for (CrewMember member : allCrewMembers) {
            CheckBox checkBox = new CheckBox(view.getContext());
            checkBox.setId(i);
            if (parameter_scene_id != -1 && scene_obj != null) {
                if (scene_obj.getCrewMembers().contains(member)) {
                    checkBox.setChecked(true);
                }
            }
            StringBuilder checkBoxText = new StringBuilder();
            checkBoxText.append(member.getName());
            checkBoxText.append(": ");
            checkBoxText.append(member.getJob());


            checkBox.setText(checkBoxText);
            checkBox.setTag(member.getID()); //save the CrewMember ID to the tag
            containerLayout.addView(checkBox);
            i = i + 1;
        }

        Scene finalScene_obj = scene_obj;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSceneToDatabase(view, finalScene_obj);

                Navigation.findNavController(v).navigate(R.id.action_editorScene_to_ScenesFragment);
            }
        });
        deleteButton = view.findViewById(R.id.editor_scene_delete);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parameter_scene_id != -1)
                    dbHandler.deleteScene(parameter_scene_id);
                Navigation.findNavController(v).navigate(R.id.action_editorScene_to_ScenesFragment);

            }
        });



        super.onViewCreated(view, savedInstanceState);
    }


    private void saveSceneToDatabase(View view,Scene scene) {
        LinearLayout containerLayout = view.findViewById(R.id.fragment_editor_scene_crew_linear_layout);
        int childCount = containerLayout.getChildCount();
        ArrayList<Long> updatedCrewMembers = new ArrayList<>(childCount);
        for (int i = 0; i < childCount; i++) {
            View childView = containerLayout.getChildAt(i);
            if (childView instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) childView;
                boolean isChecked = checkBox.isChecked();
                if (isChecked){
                    long id = (long) checkBox.getTag();
                    updatedCrewMembers.add(id);
                }
            }
        }

        Database dbHandler = new Database(getContext());
        TextView sceneNameTextView = view.findViewById(R.id.editTextSceneName);
        String sceneName = sceneNameTextView.getText().toString().trim();
        long scene_id;
        if (parameter_scene_id != -1) { // editing existing scene
            scene = dbHandler.getScene(parameter_scene_id);
            scene.setName(sceneName);
            dbHandler.updateScene(scene, false);
            scene_id = scene.getID();
        } else { // creating new scene
            scene = new Scene(sceneName);
            scene_id = dbHandler.createScene(scene);
        }


        dbHandler.updateSceneCrewMembers(scene_id,updatedCrewMembers);

    }
}