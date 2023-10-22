package com.example.v1.fragment1.function;

import android.graphics.Color;
import android.os.Handler;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.example.v1.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class WeatherDataSetView {

    private Context context;
    View rootView;

    String location_name;
    int[] location_xy = new int[2];
    private LocationData locationData;
    private WeatherDataFromWeb weatherDataFromWeb;

    private int current_location_text_size = 18;
    private int current_temper_text_size = 85;



    public WeatherDataSetView(Context context, final View tv, String location_name){
        this.context = context;
        this.rootView = tv;
        this.location_name = location_name;
    }

     public void setLocation(String location_name){
        this.location_name = location_name;

        TextView current_location = (TextView)rootView.findViewById(R.id.current_location);

        current_location.setText(this.location_name);
        current_location.setTextSize(current_location_text_size);
        current_location.setTextColor(Color.BLACK);

        locationData = new LocationData(context);
        location_xy = locationData.location_name_to_xy(this.location_name);

        setWeather();

    }

     private void setWeather(){
        final Bundle bun = new Bundle();
        Thread t = new Thread(){
            @Override
            public void run(){
                bun.clear();

                weatherDataFromWeb = new WeatherDataFromWeb(location_xy);
                ArrayList<String> weatherData_html = weatherDataFromWeb.getWeatherDataFromWeb();

                bun.putStringArrayList("data", weatherData_html);
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

     }

  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Bundle bun = msg.getData();

            /*
            ArrayList<String> weatherData_html
            .get(0): 초단기 실황 조회
            .get(1): 초단기 예보 조회
            .get(2): 동네 예보 조회
            .get(3): (4시간 이전 기준)동네 예보 조회
            .get(4): (최저기온, 최고기온 발표 시각 기준)동네 예보 조회
             */

            ArrayList<String> weatherData_html = bun.getStringArrayList("data");

            while (true){
                if(weatherData_html.size() == 5){
                    break;
                }
            }

            set_current_temperature_view(weatherData_html);
            set_current_weather_view(weatherData_html);
            set_minmax_temperature(weatherData_html);
            set_forcast_chart(weatherData_html);

        }
  };

    private void set_current_temperature_view(ArrayList<String>arrayList){
        String current_time_weather_str = arrayList.get(1);
        StringBuilder temperatures = new StringBuilder();
        TextView current_time_temperatures = (TextView)rootView.findViewById(R.id.current_temperatures);

        String[] current_time_weather_array = current_time_weather_str.split(" ");
        String time = current_time_weather_array[15];

        for(int i = 0; i< current_time_weather_array.length; i++){
            if(current_time_weather_array[i].equals(time)){
                if(current_time_weather_array[i-2].equals("T1H")){
                    temperatures.append(current_time_weather_array[i+1]);
                }
            }
        }

        current_time_temperatures.setText(temperatures);
        current_time_temperatures.setTextSize(current_temper_text_size);
        current_time_temperatures.setTextColor(Color.BLACK);
    }

    private void set_current_weather_view(ArrayList<String> arrayList){
        TextView current_time_weather = (TextView)rootView.findViewById(R.id.current_time_weather);
        String current_time_weather_str = arrayList.get(1);

        HashMap<String, String> temp = new HashMap<String, String>();
        String weather = "";

        String[] current_time_weather_array = current_time_weather_str.split(" ");
        String time = current_time_weather_array[15];

        for(int i = 0; i< current_time_weather_array.length; i++){
            if(current_time_weather_array[i].equals(time)){
                if(current_time_weather_array[i-2].equals("SKY")){
                    temp.put("SKY", current_time_weather_array[i+1]);
                }
                if(current_time_weather_array[i-2].equals("PTY")){
                    temp.put("PTY", current_time_weather_array[i+1]);
                }
            }
        }

        if(temp.get("PTY").equals("0")){
            switch (temp.get("SKY")){
                case "1":
                    weather = "Clear";
                    break;
                case"3":
                    weather = "Mostly Cloudy";
                    break;
                case"4":
                    weather = "Cloudy";
                    break;
            }
        }
        else{
            switch(temp.get("PTY")){
                case "1":
                    weather = "Rain";
                    break;
                case "2":
                    weather = "Rain/Snow";
                    break;
                case "3":
                    weather = "Snow";
                    break;
                case "4":
                    weather = "Rain"; //Shower
                    break;
                case "5":
                    weather = "Rain"; //Drizzle
                    break;
                case "6":
                    weather = "Rain/Snow"; //Sleet
                    break;
                case "7":
                    weather = "Snow"; //BlowingSnow
                    break;
            }
        }


        current_time_weather.setText(weather);

    }

    private void set_minmax_temperature(ArrayList<String> arrayList){
        TextView min_temperature = (TextView)rootView.findViewById(R.id.frag2_min_tv);
        TextView max_temperature = (TextView)rootView.findViewById(R.id.max_temperature);

        String minmax_temperature_str = arrayList.get(4);

        String[] minmax_temperature_array = minmax_temperature_str.split(" ");

        String min_temperature_str = "no data";
        String max_temperature_str = "no data";

        String[] temp = new String[2];

        for(int i =0; i<minmax_temperature_array.length; i++){
            minmax_temperature_array[i] = minmax_temperature_array[i].trim();
        }

        for(int i =0; i<minmax_temperature_array.length; i++){
            if(minmax_temperature_array[i].equals("TMN")){
                temp = minmax_temperature_array[i+3].split("\\.");
                min_temperature_str = temp[0].trim();
            }
            if(minmax_temperature_array[i].equals("TMX")){
                temp = minmax_temperature_array[i+3].split("\\.");
                max_temperature_str = temp[0].trim();
            }
        }

        min_temperature.setText(min_temperature_str);
        max_temperature.setText(max_temperature_str);
    }

    private void set_forcast_chart(ArrayList<String> arrayList){

        String weather_forecast_for_time_minus_four_str = arrayList.get(3);

        TextView forcast1_tv = (TextView)rootView.findViewById(R.id.forcast1_tv1);
        TextView forcast2_tv = (TextView)rootView.findViewById(R.id.forcast2_tv1);
        TextView forcast3_tv = (TextView)rootView.findViewById(R.id.forcast3_tv1);
        TextView forcast4_tv = (TextView)rootView.findViewById(R.id.forcast4_tv1);
        TextView forcast5_tv = (TextView)rootView.findViewById(R.id.forcast5_tv1);

        TextView forcast1_tv2 = (TextView)rootView.findViewById(R.id.forcast1_tv2);
        TextView forcast2_tv2 = (TextView)rootView.findViewById(R.id.forcast2_tv2);
        TextView forcast3_tv2 = (TextView)rootView.findViewById(R.id.forcast3_tv2);
        TextView forcast4_tv2 = (TextView)rootView.findViewById(R.id.forcast4_tv2);
        TextView forcast5_tv2 = (TextView)rootView.findViewById(R.id.forcast5_tv2);

        List<Entry> temperatures = new ArrayList<>();

        String[] days = new String[5];
        String[] times = new String[5];
        String[] t3hs = new String[5];
        String[] pops = new String[5];

        String[] y_labels = new String[5];

        String day;
        String time;
        String pop;

        int count_1 = 0;
        int count_2 = 0;

        String[] result_daytime = new String[5];
        String[] result_pop = new String[5];

        String[] weather_forecast_for_time__minus_four_array = weather_forecast_for_time_minus_four_str.split(" ");
        for(int i =0; i<weather_forecast_for_time__minus_four_array.length; i++){
            weather_forecast_for_time__minus_four_array[i] = weather_forecast_for_time__minus_four_array[i].trim();
        }

        for(int i =0; i<weather_forecast_for_time__minus_four_array.length; i++){
            if(weather_forecast_for_time__minus_four_array[i].equals("T3H")){
                days[count_1] = weather_forecast_for_time__minus_four_array[i+1];
                times[count_1] = weather_forecast_for_time__minus_four_array[i+2];
                t3hs[count_1] = weather_forecast_for_time__minus_four_array[i+3];
                count_1++;
            }
            if(weather_forecast_for_time__minus_four_array[i].equals("POP")){
                pops[count_2] = weather_forecast_for_time__minus_four_array[i+3];
                count_2 ++;
           }
        }

        for(int i = 0; i < days.length; i++){

            if(t3hs[i] == null){
                days[i] = days[i-1];
                times[i] = days[i-1];
                t3hs[i] = t3hs[i-1];
                pops[i] = pops[i-1];

                temperatures.add(new Entry(i, Integer.parseInt(t3hs[i]))); //차트 기온 설정

                day = " ";
                time = "no data";
                pop = "??%";
                y_labels[i] = "null";

                result_daytime[i] = day + "\n" + time;
                result_pop[i] = pop;

            }else{

                temperatures.add(new Entry(i, Integer.parseInt(t3hs[i]))); //차트 기온 설정

                day = days[i].substring(4,6) +"."+days[i].substring(6,8); // YYYYMMDD ==> MM.DD
                time = times[i].substring(0,2) + "시";// hhmm ==> hh:mm
                y_labels[i] = t3hs[i] + "°";
                pop = pops[i] + "%";

                result_daytime[i] = day + "\n" + time;
                result_pop[i] = pop;
            }
        }

        //set the chart x axis label
        //day time
        forcast1_tv.setText(result_daytime[0]);
        forcast2_tv.setText(result_daytime[1]);
        forcast3_tv.setText(result_daytime[2]);
        forcast4_tv.setText(result_daytime[3]);
        forcast5_tv.setText(result_daytime[4]);

        //rain prob
        forcast1_tv2.setText(result_pop[0]);
        forcast2_tv2.setText(result_pop[1]);
        forcast3_tv2.setText(result_pop[2]);
        forcast4_tv2.setText(result_pop[3]);
        forcast5_tv2.setText(result_pop[4]);



        //set the chart line
        MakeForcastChart makeForcastChart = new MakeForcastChart(rootView);

        LineChart forcast_chart = makeForcastChart.set_forcast_chart();

        LineDataSet lineDataSet = makeForcastChart.set_line_dataset(temperatures);

        LineData lineData = makeForcastChart.set_line_data(lineDataSet, y_labels);
        forcast_chart.setData(lineData);
    }

}