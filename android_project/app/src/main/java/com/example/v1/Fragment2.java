package com.example.v1;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.IOException;

import com.example.v1.fragment2.function.SelectClothes;
import com.example.v1.fragment2.function.SetWeatherIcon;

public class Fragment2 extends Fragment {

    Context mComtext;

    View rootView;
    TextView current_location;
    TextView current_time_weather;
    TextView current_temperatures;
    TextView min_temperature;
    TextView max_temperature;
    ImageView weather_icon;

    private String location_name = "서울특별시";

    private String current_temperature_str = "";
    private String current_weather = "";
    private String min_temperature_str = "";
    private String max_temperature_str = "";

    private int [] temperatures_int_arr = new int[3];
    private int current_temperature_int = 0;
    private int min_temperature_int = 0;
    private int max_temperature_int = 0;

    private int current_location_text_size = 18;
    private int temperatures_text_size = 16;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mComtext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment2, container, false);

        current_location = (TextView)rootView.findViewById(R.id.frag2_current_location);
        current_time_weather = (TextView)rootView.findViewById(R.id.frag2_current_time_weather);
        current_temperatures = (TextView)rootView.findViewById(R.id.current_temperatures);
        min_temperature = (TextView)rootView.findViewById(R.id.frag2_min_tv);
        max_temperature = (TextView)rootView.findViewById(R.id.max_temperature);

        weather_icon = (ImageView)rootView.findViewById(R.id.weather_icon);

        set_texts_style();

        return rootView;
    }

    public void setText(String s, int i) throws IOException {
        switch (i){
            case 1:
                current_location.setText(s);
                break;
            case 2:
                current_temperatures.setText(s);
                break;
            case 3:
                current_time_weather.setText(s);
                current_weather = s.trim();
                break;
            case 4:
                min_temperature.setText(s);
                break;
            case 5:
                max_temperature.setText(s);
                set_image();
                break;
        }
    }

    private void set_image() throws IOException {
        String [] temp;
        Double temp_db = 0.0;

        current_temperature_str = current_temperatures.getText().toString();
        temp_db = Double.parseDouble(current_temperature_str.trim());
        current_temperature_int = (int) Math.round(temp_db);
        temperatures_int_arr[0] = current_temperature_int;

        max_temperature_str = max_temperature.getText().toString();
        temp_db = Double.parseDouble(max_temperature_str.trim());
        max_temperature_int = (int) Math.round(temp_db);
        temperatures_int_arr[1] = max_temperature_int;

        min_temperature_str = min_temperature.getText().toString();
        temp_db = Double.parseDouble(min_temperature_str.trim());
        min_temperature_int = (int) Math.round(temp_db);
        temperatures_int_arr[2] = min_temperature_int;

        SelectClothes sc = new SelectClothes(mComtext, temperatures_int_arr, current_weather, rootView);
        sc.set_view();

        SetWeatherIcon wi = new SetWeatherIcon(mComtext, rootView, current_weather);
        wi.setView();


    }

    private void set_texts_style(){
        current_location.setText(location_name);

        current_location.setTextSize(current_location_text_size);
        current_location.setTextColor(Color.BLACK);

        current_temperatures.setTextSize(temperatures_text_size);
        current_temperatures.setTextColor(Color.BLACK);

        min_temperature.setTextSize(temperatures_text_size);
        min_temperature.setTextColor(Color.BLACK);

        max_temperature.setTextSize(temperatures_text_size);
        max_temperature.setTextColor(Color.BLACK);
    }

}
