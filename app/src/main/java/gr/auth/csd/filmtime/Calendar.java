package gr.auth.csd.filmtime;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gr.auth.csd.filmtime.databinding.FragmentCalendarBinding;
import gr.auth.csd.filmtime.databinding.FragmentHomeBinding;
import gr.auth.csd.filmtime.helpers.Database;
import gr.auth.csd.filmtime.helpers.Scene;
import gr.auth.csd.filmtime.helpers.Scheduler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Calendar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calendar extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private CalendarView calendarView;
    private ListView itemListView;

    private List<EventDay> eventDays;
    private ArrayAdapter<String> itemListAdapter;

    Map<LocalDate, Set<Scene>> possibleShootingDates;


    private FragmentCalendarBinding binding;
    public Calendar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calendar.
     */
    // TODO: Rename and change types and number of parameters
    public static Calendar newInstance(String param1, String param2) {
        Calendar fragment = new Calendar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Database dbHandler = new Database(getContext());
        ArrayList<Scene> scenes = dbHandler.getScenes();
        possibleShootingDates = Scheduler.getPossibleShootingDates(scenes);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        itemListView = view.findViewById(R.id.itemListView);

        eventDays = new ArrayList<>();

        itemListAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);

        addEventDots();

        itemListView.setAdapter(itemListAdapter);

        calendarView.setOnDayClickListener(eventDay -> {
            // Retrieve the selected date
            java.util.Calendar selectedDate = eventDay.getCalendar();

            // Fetch the list of items or events for the selected date
            List<String> items = getItemsForDate(selectedDate);

            // Display the list of items or events
            displayItemList(items);
        });

    }

    private void addEventDots() {

        // Add event dots for specific days

        for (LocalDate localdate : possibleShootingDates.keySet()) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(localdate.getYear(), localdate.getMonthValue() -1, localdate.getDayOfMonth());
            eventDays.add(new EventDay(calendar, getCircleDrawableWithText(getContext(), String.valueOf(possibleShootingDates.get(localdate).size()))));

        }

//        java.util.Calendar calendar = java.util.Calendar.getInstance();
//        calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
//        eventDays.add(new EventDay(calendar, R.drawable.event_dot));
//
//        calendar = java.util.Calendar.getInstance();
//        calendar.add(java.util.Calendar.DAY_OF_MONTH, 3);
//        eventDays.add(new EventDay(calendar, getCircleDrawableWithText(getContext(), "1")));
//
//        calendar = java.util.Calendar.getInstance();
//        calendar.add(java.util.Calendar.DAY_OF_MONTH, 5);
//        eventDays.add(new EventDay(calendar, R.drawable.event_dot));

        calendarView.setEvents(eventDays);
    }

    private void displayItemList(List<String> items) {
        if (items.isEmpty()) {
            // If there are no items, display "No events"
            itemListAdapter.clear();
            itemListAdapter.add("No events");
        } else {
            // Clear the previous items from the list adapter
            itemListAdapter.clear();
            // Add the new items to the list adapter
            itemListAdapter.addAll(items);
        }

        // Scroll the list view to the top
        itemListView.setSelection(0);
    }

    private List<String> getItemsForDate(java.util.Calendar date) {

        // Replace with your logic to fetch the items or events for the selected date
        // Return a list of items or events associated with the date
        List<String> items = new ArrayList<>();

        int year = date.get(java.util.Calendar.YEAR);
        int month = date.get(java.util.Calendar.MONTH) + 1; //January is 0st month
        int day = date.get(java.util.Calendar.DAY_OF_MONTH);

        if (possibleShootingDates.containsKey(LocalDate.of(year,month,day))){
            for (Scene scene : possibleShootingDates.get(LocalDate.of(year, month, day))) {
                items.add(scene.getName());
            }
        }
        return items;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    private static Drawable getCircleDrawableWithText(Context context, String string) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.event_dot);
        Drawable text = CalendarUtils.getDrawableText(context, string, null, android.R.color.white, 12);

        Drawable[] layers = {background, text};
        return new LayerDrawable(layers);
    }
}