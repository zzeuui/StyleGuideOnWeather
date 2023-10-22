package com.example.v1.fragment2.function;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.example.v1.R;

import java.io.IOException;
import java.io.InputStream;

public class SetWeatherIcon {

    Context context;

    View rootView;
    ImageView current_weather;
    private String weather;

    public SetWeatherIcon(Context context, View rootView, String weather){
        this.context = context;
        this.rootView = rootView;
        this.weather = weather;
    }

    public void setView() throws IOException {
        current_weather = (ImageView)rootView.findViewById(R.id.weather_icon);

        String weather_icon_path = get_weather_icon_path();

        InputStream is = context.getResources().getAssets().open(weather_icon_path);
        current_weather.setImageDrawable(Drawable.createFromStream(is, null));
    }

    private String get_weather_icon_path() {
        String icon_path = "no_path";
        switch (weather) {
            case "Clear":
                icon_path = "weather_icon/clear.png";
                break;
            case "Mostly Cloudy":
                icon_path = "weather_icon/mostly_cloudy.png";
                break;
            case "Cloudy":
                icon_path = "weather_icon/cloudy.png";
                break;
            case "Rain":
                icon_path = "weather_icon/rain.png";
                break;
            case "Rain/Snow":
                icon_path = "weather_icon/rain_snow.png";
                break;
            case "Snow":
                icon_path = "weather_icon/snow.png";
                break;
        }

        return icon_path;
    }

}
