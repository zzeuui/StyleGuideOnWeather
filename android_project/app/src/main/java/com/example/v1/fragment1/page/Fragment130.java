package com.example.v1.fragment1.page;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.v1.R;
import com.example.v1.fragment1.function.LocationData;
import com.example.v1.fragment1.function.WeatherDataSetView;

public class Fragment130 {
    private Context context;

    RadioGroup location_radioGroup;
    Button fragment130_done_button;
    RadioButton check_radio_button;

    private String front_location;
    private String check_radio_button_string="";
    private String full_location;
    private String full_full_location;

    private LocationData location_data;
    private WeatherDataSetView weatherData;

    public Fragment130(Context context, String front_location, String full_location){
        this.context = context;
        this.front_location = front_location;
        this.full_location = full_location;
        this.full_full_location = full_location;
    }

    public void callFunction(final View rootView){

        final TextView tv = (TextView)rootView.findViewById(R.id.current_location);

        // *****다이얼로그 세팅****** //
        final Dialog dlg = new Dialog(context);
        dlg.setContentView(R.layout.fragment130);
        dlg.show();

        // ******다이얼로그 내의 위젯 정의 *******//
        location_radioGroup = (RadioGroup)dlg.findViewById(R.id.third_location_radioGroup);
        fragment130_done_button = (Button) dlg.findViewById(R.id.fragment130_done);

        // ****  라디오 버튼 세팅: 세번째 지역으로(동면읍) ****** //
        location_data = new LocationData(context);
        location_data.setRadioGroup(location_radioGroup, 4, front_location); //order ==> 2: 시도 3: 시구군 4: 동면

        // ******* 세번째 지역(동면읍)을 선택 ****** //
        location_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                check_radio_button = dlg.findViewById(i); //선택한 지역의 라디오 버튼 아이디 얻어 작업하려는 라디오 버튼 설정
                CharSequence check_radio_button_text = check_radio_button.getText();// 선택된 라디오 버튼으로부터 지역 이름 획득1(CharSequence)
                check_radio_button_string = check_radio_button_text.toString(); // 선택된 라디오 버튼으로부터 지역 이름 획득2(String)
                full_location = full_full_location + " " + check_radio_button_string;
            }
        });

        // **************** 완료 버튼: 다이얼로그 종료 *************** //
        fragment130_done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!(check_radio_button_string.isEmpty())){
                    weatherData = new WeatherDataSetView(context, rootView, full_location);
                    weatherData.setLocation(full_location);
                }
                else{
                    weatherData = new WeatherDataSetView(context, rootView, full_full_location);
                    weatherData.setLocation(full_full_location);
                }
                dlg.dismiss();// 세번째 지역 선택 다이얼로그를 종료한다.
            }
        });
    }
}
