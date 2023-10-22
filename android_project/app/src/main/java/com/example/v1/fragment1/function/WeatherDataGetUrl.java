package com.example.v1.fragment1.function;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeatherDataGetUrl {

    public int[] location_xy;
    public String url ="";

    public WeatherDataGetUrl(int[] location_xy){
        this.location_xy = location_xy;
    }

    // 초단기 실황//
    public String getUltraSrtNcstUrl(){

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat time = new SimpleDateFormat("HHmm");
        String day_str = today.format(new Date(currentTime));
        String time_str = time.format(new Date(currentTime));

        String homepage_address = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst";

        String key = "%2Fv7%2FpW9zwSb9wM0UsbEzT7fxnhVTlqRmjm0EM5Fv57W3WKi8liFelt1R8KCPb24gg74EswZSM0XiVLVFpMJ5zA%3D%3D";

        String time_hour_str = time_str.substring(0,2);
        String time_min_str = time_str.substring(time_str.length()-2, time_str.length());
        int time_hour_int = Integer.parseInt(time_hour_str);
        int time_min_int = Integer.parseInt(time_min_str);

        if(time_min_int < 40){
            if(time_hour_int == 0){
                Calendar day = Calendar.getInstance();
                day.add(Calendar.DATE , -1);
                day_str = new java.text.SimpleDateFormat("yyyyMMdd").format(day.getTime());
                time_str = "2340";
            }
            else{
                time_hour_int = time_hour_int - 1;
                if(time_hour_int < 10){
                    time_str = "0" +Integer.toString(time_hour_int) + "40";
                }
                else{
                    time_str = Integer.toString(time_hour_int) + "40";
                }
            }
        }

        url = homepage_address+ "?serviceKey=" + key +
                "&numOfRows=10"+
                "&pageNo=1" +
                "&base_date=" + day_str +
                "&base_time=" + time_str +
                "&nx=" + this.location_xy[0] +
                "&ny=" + this.location_xy[1];

        return url;
    }

    // 초단기 예보 //
    public String getUltraSrtFcst(){

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat time = new SimpleDateFormat("HHmm");
        String day_str = today.format(new Date(currentTime));
        String time_str = time.format(new Date(currentTime));

        String homepage_address = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtFcst";

        String key = "%2Fv7%2FpW9zwSb9wM0UsbEzT7fxnhVTlqRmjm0EM5Fv57W3WKi8liFelt1R8KCPb24gg74EswZSM0XiVLVFpMJ5zA%3D%3D";

        String time_hour_str = time_str.substring(0,2);
        String time_min_str = time_str.substring(time_str.length()-2, time_str.length());
        int time_hour_int = Integer.parseInt(time_hour_str);
        int time_min_int = Integer.parseInt(time_min_str);

        if(time_min_int < 45){
            if(time_hour_int == 0){
                Calendar day = Calendar.getInstance();
                day.add(Calendar.DATE , -1);
                day_str = new java.text.SimpleDateFormat("yyyyMMdd").format(day.getTime());
                time_str = "2345";
            }
            else{
                time_hour_int = time_hour_int - 1;
                if(time_hour_int < 10){
                    time_str = "0" +Integer.toString(time_hour_int) + "45";
                }
                else{
                    time_str = Integer.toString(time_hour_int) + "45";
                }
            }
        }

        url = homepage_address+ "?serviceKey=" + key +
                "&numOfRows=60"+
                "&pageNo=1" +
                "&base_date=" + day_str +
                "&base_time=" + time_str +
                "&nx=" + this.location_xy[0] +
                "&ny=" + this.location_xy[1];

        return url;

    }

    // 동네 예보 //
    public String getVilageFcst(int type, String page_number){
        /*
        type == 0: 동네예보 조회
        type == 1: 4시간 이전 기준 동네예보 조회
        type == 2: 최저기온 발표시각 기준 동네예보 조회
        type == 3: 최고기온 발표시각 기준 동네예보 조회
         */

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat time = new SimpleDateFormat("HHmm");
        String day_str = today.format(new Date(currentTime));
        String time_str = time.format(new Date(currentTime));

        String homepage_address = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";

        String key = "%2Fv7%2FpW9zwSb9wM0UsbEzT7fxnhVTlqRmjm0EM5Fv57W3WKi8liFelt1R8KCPb24gg74EswZSM0XiVLVFpMJ5zA%3D%3D";

        int time_int = Integer.parseInt(time_str);


        if(type == 1){
            if(time_int < 210){
                Calendar day = Calendar.getInstance();
                day.add(Calendar.DATE , -1);
                day_str = new java.text.SimpleDateFormat("yyyyMMdd").format(day.getTime());
                time_str = "2000";
            }
            else if(time_int < 510){
                Calendar day = Calendar.getInstance();
                day.add(Calendar.DATE , -1);
                day_str = new java.text.SimpleDateFormat("yyyyMMdd").format(day.getTime());
                time_str = "2300";
            }
            else if(time_int < 810){
                time_str = "0200";
            }
            else if(time_int < 1110){
                time_str = "0500";
            }
            else if(time_int < 1410){
                time_str = "0800";
            }
            else if(time_int < 1710){
                time_str = "1100";
            }
            else if(time_int < 2010){
                time_str = "1400";
            }
            else if(time_int < 2310){
                time_str = "1700";
            }
            else{
                time_str = "2000";
            }
        }
        else if(type == 0){
            if(time_int < 210){
                Calendar day = Calendar.getInstance();
                day.add(Calendar.DATE , -1);
                day_str = new java.text.SimpleDateFormat("yyyyMMdd").format(day.getTime());
                time_str = "2300";
            }
            else if(time_int < 510){
                time_str = "0200";
            }
            else if(time_int < 810){
                time_str = "0500";
            }
            else if(time_int < 1110){
                time_str = "0800";
            }
            else if(time_int < 1410){
                time_str = "1100";
            }
            else if(time_int < 1710){
                time_str = "1400";
            }
            else if(time_int < 2010){
                time_str = "1700";
            }
            else if(time_int < 2310){
                time_str = "2000";
            }
            else{
                time_str = "2300";
            }
        }
        else if(type == 2){
            time_str = "0200";

            if(time_int < 200){
                Calendar day = Calendar.getInstance();
                day.add(Calendar.DATE , -1);
                day_str = new java.text.SimpleDateFormat("yyyyMMdd").format(day.getTime());
            }

        }
        else if(type == 3){
            time_str = "1100";

            if(time_int < 1110){
                Calendar day = Calendar.getInstance();
                day.add(Calendar.DATE , -1);
                day_str = new java.text.SimpleDateFormat("yyyyMMdd").format(day.getTime());
            }
        }

        url = homepage_address+ "?serviceKey=" + key +
                "&numOfRows=10"+
                "&pageNo=" + page_number +
                "&base_date=" + day_str +
                "&base_time=" + time_str +
                "&nx=" + this.location_xy[0] +
                "&ny=" + this.location_xy[1];

        return url;

    }
}
