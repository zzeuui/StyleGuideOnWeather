package com.example.v1.fragment2.function;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.v1.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;

public class SelectClothes {

    private int[] temperatures;
    private String current_weather;
    Context context;
    View rootView;

    public SelectClothes(Context context, int[] temperatures, String current_weather, View rootView){
        this.temperatures = temperatures;
        this.current_weather = current_weather;
        this.context = context;
        this.rootView = rootView;
    }

    public void set_view() throws IOException {
        for(int i = 0; i<temperatures.length; i++){
            set_images(temperatures[i], i);
        }
    }

    private void set_images(int temperature, int type) throws IOException {
        ArrayList<String> clothes_list = get_clothes_list(temperature);
        LinearLayout images;


        switch (type){
            case 0:
                images = (LinearLayout)rootView.findViewById(R.id.current_image);
                images.removeAllViews();
                if(current_weather.equals("Rain")||current_weather.equals("Rain/Snow")||current_weather.equals("Snow")){
                    ImageView iv = new ImageView(context);
                    InputStream is = context.getResources().getAssets().open("clothes/weather_things/raincoat.png");
                    iv.setImageDrawable(Drawable.createFromStream(is, null));
                    images.addView(iv);
                    if(current_weather.contains("Snow")){
                        ImageView iv2 = new ImageView(context);
                        InputStream is2 = context.getResources().getAssets().open("clothes/weather_things/umbrella.png");
                        iv2.setImageDrawable(Drawable.createFromStream(is2, null));
                        images.addView(iv2);
                    }
                }
                break;
            case 1:
                images = (LinearLayout)rootView.findViewById(R.id.max_image);
                images.removeAllViews();
                break;
            case 2:
                images = (LinearLayout)rootView.findViewById(R.id.min_image);
                images.removeAllViews();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        for(int i = 0; i < clothes_list.size(); i++){
            ImageView iv = new ImageView(context);

            InputStream is = context.getResources().getAssets().open(clothes_list.get(i));

            iv.setImageDrawable(Drawable.createFromStream(is, null));
            images.addView(iv);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
            params.bottomMargin = 10;
            iv.setLayoutParams(params);
        }


    }

    private ArrayList<String> get_clothes_list(int temperature) throws IOException {

        ArrayList<String> all_clothes_path =  new ArrayList<String>();
        ArrayList<String> result_clothes = new ArrayList<String>();

        String temperature_cls = "";

        InputStream is = context.getResources().getAssets().open("clothes_path.txt");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        while(true){
            String str = bufferedReader.readLine();

            if(str != null){
                all_clothes_path.add(str);
            }
            else{
                break;
            }
        }

        if(temperature >= 28){
            temperature_cls ="28";
        }
        else if(temperature >= 23){
            temperature_cls = "23";
        }
        else if(temperature >= 20){
            temperature_cls = "20";
        }
        else if(temperature >= 17){
            temperature_cls = "17";
        }
        else if(temperature >= 12){
            temperature_cls = "12";
        }
        else if(temperature >= 9){
            temperature_cls = "9";
        }
        else if(temperature >= 5){
            temperature_cls = "5";
        }
        else{
            temperature_cls = "inf";
        }

        for(int i=0; i <all_clothes_path.size(); i++) {
            String[] step1 = all_clothes_path.get(i).split("/");
            String[] step2 = step1[1].split("_");

            if(temperature_cls.equals(step2[2].trim())){
                result_clothes.add(all_clothes_path.get(i));
            }
        }

        return result_clothes;
    }

}