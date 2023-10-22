package com.example.v1.fragment1.function;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class WeatherDataFromWeb {
    /*
    인터넷으로부터 얻어온 정보를 를.
    public WeatherDataFromWeb(int[] location_xy): 위치 좌표를 받고 인스턴스를 초기화.
     public ArrayList<String> getWeatherDataFromWeb(): 위치 좌표에 따라 인텟으로부터 얻어온 정보를 저장한 weatherData_html를 리턴.
     */
    public int[] location_xy;
    public ArrayList<String> weatherData_html;

    protected String url_UltraSrtNcst; //초단기 실황 조회
    protected String url_UltraSrtFcst; //초단기 예보 조회
    protected String url_VilageFcst; //동네 예보 조회
    protected String url_VilageFcst_minus_four; //(4시간 이전 기준) 동네 예보 조회
    protected String url_Vilage_Fcst_min; // (최저 기온 발표 시각 기준) 동네 예보 조회
    protected String url_Vilage_Fcst_max; // (최고 기온 발표 시각 기준) 동네 예보 조회

    private WeatherDataGetUrl geturl;
    private String[] page_number = {"1", "2", "3", "4", "5"};


    public WeatherDataFromWeb(int[] location_xy){
        this.location_xy = location_xy;
        this.weatherData_html = new ArrayList<String>();
    }

    public ArrayList<String> getWeatherDataFromWeb(){
        try{
            geturl = new WeatherDataGetUrl(location_xy);

            this.url_UltraSrtNcst = geturl.getUltraSrtNcstUrl();
            this.url_UltraSrtFcst = geturl.getUltraSrtFcst();
            this.url_VilageFcst = geturl.getVilageFcst(0, "1");

            StringBuilder forcast_for_time = new StringBuilder();
            StringBuilder minmax_temperature = new StringBuilder();

            Document doc = Jsoup.connect(url_UltraSrtNcst).get();
            Elements contents;
            contents = doc.getAllElements();

            weatherData_html.add(contents.text()); // weatherData_html.get(0)

            doc = Jsoup.connect(url_UltraSrtFcst).get();
            contents = doc.getAllElements();

            weatherData_html.add(contents.text());  // weatherData_html.get(1)

            doc = Jsoup.connect(url_VilageFcst).get();
            contents = doc.getAllElements();

            weatherData_html.add(contents.text()); // weatherData_html.get(2)


            for(int i = 0; i <page_number.length; i++){
                this.url_VilageFcst_minus_four = geturl.getVilageFcst(1, page_number[i]);

                doc = Jsoup.connect(url_VilageFcst_minus_four).get();
                contents = doc.getAllElements();

                forcast_for_time.append(contents.text());
            }

            String forcast_for_time_str = forcast_for_time.toString();
            weatherData_html.add(forcast_for_time_str); // weatherData_html.get(3)

            this.url_Vilage_Fcst_min = geturl.getVilageFcst(2, "1");
            doc = Jsoup.connect(url_Vilage_Fcst_min).get();
            contents = doc.getAllElements();
            minmax_temperature.append(contents.text());

            this.url_Vilage_Fcst_max = geturl.getVilageFcst(3, "1");
            doc = Jsoup.connect(url_Vilage_Fcst_max).get();
            contents = doc.getAllElements();
            minmax_temperature.append(contents.text());

            String minmax_temperature_str = minmax_temperature.toString();
            weatherData_html.add(minmax_temperature_str); // weatherData_html.get(4)


        }catch (IOException e){
            Log.d("IOException: ", e.getMessage());
        }

        return weatherData_html;
    }


}
