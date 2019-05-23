package com.example.foottoheart.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.style.ForegroundColorSpan;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.foottoheart.R;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
 
import java.util.Calendar;


public class CumulativeFragment extends Fragment {

    MaterialCalendarView materialCalendarView;
    ArcProgress arcProgress;
    TextView datetextview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cumulative,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arcProgress = (ArcProgress)getView().findViewById(R.id.fragmentcumulative_arcprogress);
        datetextview = (TextView)getView().findViewById(R.id.fragmentcumulative_textview_date);

        materialCalendarView = (MaterialCalendarView)getView().findViewById(R.id.fragmentcumulative_calendar);
        datetextview.setText(String.format("%04d년 %02d월 %02d일",CalendarDay.today().getYear(),CalendarDay.today().getMonth()+1,CalendarDay.today().getDay()));
        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
                );



        materialCalendarView.setDateSelected(CalendarDay.today(),true);

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //임시
                datetextview.setText(String.format("%04d년 %02d월 %02d일",date.getYear(),date.getMonth()+1,date.getDay()));
                arcProgress.setVisibility(View.INVISIBLE);
            }
        });

    }

    public class SundayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SundayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    public class SaturdayDecorator implements DayViewDecorator {

        private final Calendar calendar = Calendar.getInstance();

        public SaturdayDecorator() {
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }
}

