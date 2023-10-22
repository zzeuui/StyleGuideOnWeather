package com.example.v1;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.v1.fragment1.function.WeatherDataSetView;
import com.example.v1.fragment1.page.Fragment110;
import com.example.v1.fragment2.function.SetWeatherIcon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class Fragment1 extends Fragment {

    Context mContext;

    View rootView;
    TextView current_location;
    TextView current_temperatures;
    TextView current_time_weather;
    TextView min_temperature;
    TextView max_temperature;
    TextView email;

    Button logout;

    public String location_name = "서울특별시";
    public String userName;

    private WeatherDataSetView weatherData;
    private Fragment110 locationFragment;
    protected FragmentListener fragmentListener;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    //context 초기화
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    //view 세팅
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment1, container, false);

        //fragment1 레이아웃 위젯 정의
        final ImageButton weatherUpdateButton =(ImageButton)rootView.findViewById(R.id.weatherUpdateButton);
        final ImageButton locationUpdateButton = (ImageButton)rootView.findViewById(R.id.locationUpdateButton);

        current_location = (TextView)rootView.findViewById(R.id.current_location);
        current_temperatures = (TextView)rootView.findViewById(R.id.current_temperatures);
        current_time_weather = (TextView)rootView.findViewById(R.id.current_time_weather);
        min_temperature = (TextView)rootView.findViewById(R.id.frag2_min_tv);
        max_temperature = (TextView)rootView.findViewById(R.id.max_temperature);

        email = (TextView)rootView.findViewById(R.id.email);

        logout = (Button) rootView.findViewById(R.id.logout);

        weatherData =  new WeatherDataSetView(mContext, rootView, location_name);
        weatherData.setLocation(location_name);

        //*********** 기온 변경 버튼 클릭 *********** //
        weatherUpdateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                location_name = current_location.getText().toString();
                weatherData.setLocation(location_name);
            }
        });

        //*********** 지역 변경 버튼 클릭 *********** //
        locationUpdateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                locationFragment = new Fragment110(mContext); //첫번째 지역 선택 다이얼로그 정의
                locationFragment.callFunction(rootView); //첫번째 지역 선택 다이얼로그 호출
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fragmentListener.onToSignin();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        current_location.addTextChangedListener(current_location_watcher);
        current_temperatures.addTextChangedListener(current_temperatures_watcher);
        current_time_weather.addTextChangedListener(current_time_weather_watcher);
        min_temperature.addTextChangedListener(min_temperature_watcher);
        max_temperature.addTextChangedListener(max_temperature_watcher);
    }


   public void setEmail(String e) {
       email.setText(e);
       String[] emails = e.split("@");
       userName = emails[0];
       databaseReference.child("user").child(userName).child("location").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.getValue() != null) {
                   location_name = String.valueOf(snapshot.getValue());
                   weatherData.setLocation(location_name);
               }else{
                   databaseReference.child("user").child(userName).child("location").setValue(location_name);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
   }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() instanceof FragmentListener){
            this.fragmentListener = (FragmentListener) getActivity();
        }
    }

    TextWatcher current_location_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            databaseReference.child("user").child(userName).child("location").setValue(editable.toString());
            if(fragmentListener!=null){
                try {
                    fragmentListener.onCurrentLocationChange(editable.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    TextWatcher current_temperatures_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(fragmentListener!=null){
                try {
                    fragmentListener.onCurrentTemperaturesChange(editable.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    TextWatcher current_time_weather_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String current_weather = editable.toString().trim();

            SetWeatherIcon wi = new SetWeatherIcon(mContext, rootView, current_weather);
            try {
                wi.setView();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(fragmentListener!=null){
                try {
                    fragmentListener.onCurrentWeatherChange(current_weather);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };
    TextWatcher min_temperature_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(fragmentListener!=null){
                try {
                    fragmentListener.onMinTemperaturesChange(editable.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    TextWatcher max_temperature_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(fragmentListener!=null){
                try {
                    fragmentListener.onMaxTemperaturesChange(editable.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}

