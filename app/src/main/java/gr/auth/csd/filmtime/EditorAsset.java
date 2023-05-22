package gr.auth.csd.filmtime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gr.auth.csd.filmtime.helpers.CrewMember;
import gr.auth.csd.filmtime.helpers.Database;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditorAsset#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditorAsset extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "crew_member_id";

    private static final String SELECTED_DATES_KEY = "selected_dates";

    private CalendarView calendarView;
    private List<Calendar> selectedDates;

    // TODO: Rename and change types of parameters
    private long parameter_asset_id;

    public EditorAsset() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EditorAsset.
     */
    // TODO: Rename and change types and number of parameters
    public static EditorAsset newInstance(Long param1) {
        EditorAsset fragment = new EditorAsset();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parameter_asset_id = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editor_asset, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(SELECTED_DATES_KEY, new ArrayList<>(calendarView.getSelectedDates()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Database dbHandler = new Database(getContext());
        CrewMember crewMemberObj = dbHandler.getCrewMember(parameter_asset_id);

        TextView AssetNameTextView = view.findViewById(R.id.editTextAssetName);
        TextView AssetJobTextView = view.findViewById(R.id.editTextAssetJob);

        calendarView = view.findViewById(R.id.calendarView);
        if (savedInstanceState != null) {
            // Restore the selected dates from the saved state
            calendarView.setSelectedDates((List<Calendar>) savedInstanceState.getSerializable(SELECTED_DATES_KEY));
        } else if (crewMemberObj != null){
            // Use the initial selected dates
            List<Calendar> calendarList = crewMemberObj.getAvailabilitiesCalendarDatatype();
            calendarView.setSelectedDates(calendarList);
        }

    if (crewMemberObj != null){
            AssetNameTextView.setText(crewMemberObj.getName());
            AssetJobTextView.setText(crewMemberObj.getJob());

        }else
            crewMemberObj = new CrewMember(AssetNameTextView.getText().toString().trim(), AssetJobTextView.getText().toString());

        Button saveButton = view.findViewById(R.id.editor_asset_save);
        CrewMember finalCrewMemberObj = crewMemberObj;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalCrewMemberObj.setName(AssetNameTextView.getText().toString().trim());
                finalCrewMemberObj.setJob(AssetJobTextView.getText().toString().trim());
                finalCrewMemberObj.deleteAvailabilities();

                for (Calendar calendar : calendarView.getSelectedDates()) {
                    finalCrewMemberObj.addDate(calendar);
                }


                if (parameter_asset_id == -1) { // create asset
                    dbHandler.createCrewMember(finalCrewMemberObj);
                } else { // update asset
                    dbHandler.updateCrewMember(finalCrewMemberObj);
                }
                Navigation.findNavController(v).navigate(R.id.action_editorAsset_to_assetsFragment);
            }
        });


        Button deleteButton = view.findViewById(R.id.editor_asset_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parameter_asset_id != -1)
                    dbHandler.deleteCrewMember(parameter_asset_id);
                Navigation.findNavController(v).navigate(R.id.action_editorAsset_to_assetsFragment);

            }
        });





    }
}