package com.example.v1.fragment1.function;


import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class LocationData {

    private Context context;

    private int colTotal = 0;
    private int rowTotal = 0;

    public LocationData(Context context){
        this.context = context;
    }


    // 지역 이름으로부터 xy 좌표를 얻는다//
    public int[] location_name_to_xy(String location_name){
        int[] location_xy = new int[2];
        String[] data = location_data_load();
        String[] location_names = location_name.split(" ");
        int number_data = data.length;
        int number_location_name =  location_names.length;

        switch (number_location_name){
            case 1:
                for(int i = 0; i < number_data ; i++) {
                    if (data[i].contains(location_names[0]) && data[i + 1].equals("col3: ") && data[i + 2].equals("col4: ")) {
                        location_xy[0] = Integer.parseInt(data[i + 3].replaceAll("col5: ", ""));
                        location_xy[1] = Integer.parseInt(data[i + 4].replaceAll("col6: ", ""));
                    }
                }
                break;
            case 2:
                for(int i = 0; i < number_data ; i++){
                    if(data[i].contains(location_names[0]) && data[i+1].contains(location_names[1])&&data[i+2].equals("col4: ")){
                        location_xy[0] = Integer.parseInt(data[i+3].replaceAll("col5: ", ""));
                        location_xy[1] = Integer.parseInt(data[i+4].replaceAll("col6: ", ""));
                    }
                }
                break;
            case 3:
                for(int i = 0; i < number_data ; i++){
                    if(data[i].contains(location_names[2])){
                        location_xy[0] = Integer.parseInt(data[i+1].replaceAll("col5: ", ""));
                        location_xy[1] = Integer.parseInt(data[i+2].replaceAll("col6: ", ""));
                    }
                }
                break;
            default:
                break;
        }

        return location_xy;
    }

    public void setRadioGroup(RadioGroup rg, int order, String front_location){

        ArrayList<String> first_location = location_extract(order, front_location); //order ==> 2: 시도 3: 시구군 4: 동면

        for(int i=0; i<first_location.size(); i++){
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(first_location.get(i));
            RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            rg.addView(radioButton, rprms);
        }
    }

    private String[] location_data_load() {

        StringBuilder sb = new StringBuilder();

        try {
            InputStream is = context.getResources().getAssets().open("location_file.xls"); //파일 불러오기
            Workbook wb = Workbook.getWorkbook(is); //작업을 위해 불러온 파일 세팅


            if(wb != null) { //파일이 존재한다면
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if(sheet != null) {//시트가 존재한다면
                    colTotal = sheet.getColumns(); //전체 열 수
                    rowTotal = sheet.getRows();// 전체 행 수
                    int rowIndexStart = 1;// 시작 행

                    for(int row=rowIndexStart;row<rowTotal;row++) { //모든 행과
                        for(int col=0;col<colTotal;col++) {// 모든 열에 대해서
                            String contents = sheet.getCell(col, row).getContents();// 정보를 불러오고
                            sb.append("col"+col+": "+contents+",");//열에 대해서 라벨링
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

        String datatostring = sb.toString(); // stringBuilder를 string으로 변환

        String data[] = datatostring.split(","); //','를 구분하여 string array에 정리

        return data;
    }

    //order ==> 2: 시도 3: 시구군 4: 동면
    private ArrayList<String> location_extract(int order, String front_location){
        String[] data = location_data_load(); // 지역 정보 부름
        int data_size = data.length; //정보 사이즈
        String order_s = Integer.toString(order);
        String col = "col" + order_s + ":";

        ArrayList<String> semi_result = new ArrayList<String>();

        if(order == 2){
            for(int i =0; i<data_size; i++){
                if(data[i].contains(col)){
                    String temp[] = data[i].split(col);
                    temp[1] = temp[1].replaceAll(" ", "");
                    if(!temp[1].isEmpty()){
                        semi_result.add(temp[1]);
                    }
                }
            }
        }
        else{
            String front_order_s = Integer.toString(order-1);
            String front_col = "col" + front_order_s + ":";

            for(int i = 0; i<data_size; i++){
                String[] data_temp = data[i].split(front_col);
                if(data_temp.length > 1){
                    data_temp[1] = data_temp[1].replaceAll(" ", "");
                    if(data_temp[1].equals(front_location)){
                        String temp[] = data[i+1].split(col);
                        temp[1] = temp[1].replaceAll(" ", "");
                        if(!temp[1].isEmpty()){
                            semi_result.add(temp[1]);
                        }
                    }
                }
            }
        }


        HashSet<String> result = new HashSet<String>(semi_result); //중복 제거를 위한 자료형 변환
        ArrayList<String> final_result = new ArrayList<String>(result);// 다시 리스트 형태로 변환

        Collections.sort(final_result);// 리스트를 가나다 순으로 정리

        return final_result;
    }


}