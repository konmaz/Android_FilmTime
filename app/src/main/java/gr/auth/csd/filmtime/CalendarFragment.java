package gr.auth.csd.filmtime;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import gr.auth.csd.filmtime.databinding.FragmentCalendarBinding;
import gr.auth.csd.filmtime.helpers.Database;
import gr.auth.csd.filmtime.helpers.Scene;
import gr.auth.csd.filmtime.helpers.Scheduler;

/**
 * A fragment class responsible for displaying a calendar and showing the scenes that can be shot on a user-defined day.
 */
public class CalendarFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerAdapterGeneric itemRecyclerAdapter;

    private List<EventDay> eventDays;
    private Map<LocalDate, Set<Scene>> possibleShootingDates;

    private FragmentCalendarBinding binding;

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database dbHandler = new Database(getContext());
        ArrayList<Scene> scenes = dbHandler.getScenes();
        possibleShootingDates = Scheduler.getPossibleShootingDates(scenes);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the selected date
        outState.putSerializable("selected_date", calendarView.getFirstSelectedDate());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // Restore the selected date if it was saved
        if (savedInstanceState != null && savedInstanceState.containsKey("selected_date")) {
            java.util.Calendar selectedDate = (java.util.Calendar) savedInstanceState.getSerializable("selected_date");
            try {
                calendarView.setDate(selectedDate);
                displayItemList(getItemsForDate(selectedDate));
            } catch (OutOfDateRangeException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = binding.calendarView;
        RecyclerView itemRecyclerView = binding.itemRecyclerView;

        eventDays = new ArrayList<>();

        itemRecyclerAdapter = new RecyclerAdapterGeneric(new ArrayList<>());
        itemRecyclerView.setAdapter(itemRecyclerAdapter);
        itemRecyclerAdapter.disableButton();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemRecyclerView.setLayoutManager(layoutManager);

        addEventDots();

        displayItemList(getItemsForDate(calendarView.getFirstSelectedDate()));


        calendarView.setOnDayClickListener(eventDay -> {  //handle user clicking on a date
            java.util.Calendar selectedDate = eventDay.getCalendar();

            java.util.Calendar visibleMonthStartDate = calendarView.getCurrentPageDate();

            java.util.Calendar visibleMonthEndDate = (java.util.Calendar) visibleMonthStartDate.clone();
            visibleMonthEndDate.add(java.util.Calendar.MONTH, 1);
            visibleMonthEndDate.add(java.util.Calendar.DAY_OF_MONTH, -1);
            // check if user has selected a day outside of this month
            if (selectedDate.compareTo(visibleMonthStartDate) >= 0 && selectedDate.compareTo(visibleMonthEndDate) <= 0) {
                ArrayList<Scene> scenes = getItemsForDate(selectedDate);
                displayItemList(scenes);
            }
        });

    }
    /**
     * Add event dots to the calendar based on shootable scene dates.
     */
    private void addEventDots() {
        for (LocalDate localdate : possibleShootingDates.keySet()) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(localdate.getYear(), localdate.getMonthValue() - 1, localdate.getDayOfMonth());
            eventDays.add(new EventDay(calendar, getCircleDrawableWithText(getContext(), String.valueOf(Objects.requireNonNull(possibleShootingDates.get(localdate)).size()))));
        }

        calendarView.setEvents(eventDays);
    }
    /**
     * Display the list of scenes for a selected date.
     *
     * @param scenes The list of scenes to be displayed.
     */
    private void displayItemList(ArrayList<Scene> scenes) {
        binding.calendarNoScenesMessage.setVisibility(View.INVISIBLE);
        itemRecyclerAdapter.setScenes(scenes);
        itemRecyclerAdapter.notifyDataSetChanged();
        if (scenes.size() == 0){
            binding.calendarNoScenesMessage.setVisibility(View.VISIBLE);
        }
    }
    /**
     * Get the shootable scenes for a selected date.
     *
     * @param date The selected date.
     * @return The list of shootable scenes for the selected date.
     */
    @NonNull
    private ArrayList<Scene> getItemsForDate(java.util.Calendar date) {
        ArrayList<Scene> scenes = new ArrayList<>();

        int year = date.get(java.util.Calendar.YEAR);
        int month = date.get(java.util.Calendar.MONTH) + 1;
        int day = date.get(java.util.Calendar.DAY_OF_MONTH);

        if (possibleShootingDates.containsKey(LocalDate.of(year, month, day))) {
            scenes.addAll(Objects.requireNonNull(possibleShootingDates.get(LocalDate.of(year, month, day))));
        }

        return scenes;
    }
    /**
     * Get a circle drawable with text.
     *
     * @param context The context.
     * @param string  The text to be displayed.
     * @return The circle drawable with text.
     */
    @NonNull
    private static Drawable getCircleDrawableWithText(Context context, String string) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.event_dot);
        Drawable text = CalendarUtils.getDrawableText(context, string, null, android.R.color.white, 12);

        Drawable[] layers = {background, text};
        return new LayerDrawable(layers);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
