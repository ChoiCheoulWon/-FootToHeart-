package com.example.foottoheart.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.foottoheart.MainActivity;
import com.example.foottoheart.R;
import com.example.foottoheart.Esp8266communication;
import com.github.lzyzsd.circleprogress.ArcProgress;

public class HomeFragment extends Fragment {

    int count = 0;
    Button unocommunication;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragmet_home,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button controller = (Button)getView().findViewById(R.id.controller);
        final ArcProgress arcProgress = (ArcProgress)getView().findViewById(R.id.fragmenthome_arcprogress);
        controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                arcProgress.setProgress(count);
                arcProgress.setBottomText("달성도 : "+ (int)(count/(float)arcProgress.getMax()*100) + "%");
            }
        });

        unocommunication = (Button)getView().findViewById(R.id.fragmenthome_button_communication);
        unocommunication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commu_intent =  new Intent(getActivity().getApplicationContext(), Esp8266communication.class);
                startActivity(commu_intent);

            }
        });
    }
}
