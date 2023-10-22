package com.example.v1.fragment1.page;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.v1.R;
import com.example.v1.fragment1.function.LocationData;
import com.example.v1.fragment1.function.WeatherDataSetView;

public class Fragment120 extends DialogFragment {
    private Context context;


    RadioGroup location_radioGroup;
    Button fragment120_done_button;
    Button fragment120_to_fragment130_button;
    RadioButton check_radio_button;

    private String front_location;
    private String check_radio_button_string="";
    private String full_location;

    private LocationData location_data;
    private WeatherDataSetView weatherData;
    private Fragment130 locationFragment;

    public Fragment120(Context context, String front_location){
        this.context = context;
        this.front_location = front_location;
    }

    public void callFunction(final View rootView){

        final TextView tv = (TextView)rootView.findViewById(R.id.current_location);

        // *****다이얼로그 세팅****** //
        final Dialog dlg = new Dialog(context);
        dlg.setContentView(R.layout.fragment120);
        dlg.show();

        // ******다이얼로그 내의 위젯 정의 *******//
        location_radioGroup = (RadioGroup)dlg.findViewById(R.id.second_location_radioGroup);
        fragment120_done_button = (Button) dlg.findViewById(R.id.fragment120_done);
        fragment120_to_fragment130_button = (Button) dlg.findViewById(R.id.fragment120_to_fragment130);

        // ****  라디오 버튼 세팅: 두번째 지역으로(시구군) ****** //
        location_data = new LocationData(context);
        location_data.setRadioGroup(location_radioGroup, 3, front_location); //order ==> 2: 시도 3: 시구군 4: 동면읍

        // ******* 두번째 지역(시구)을 선택 ****** //
        location_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                check_radio_button = dlg.findViewById(i); //선택한 지역의 라디오 버튼 아이디 얻어 작업하려는 라디오 버튼 설정
                CharSequence check_radio_button_text = check_radio_button.getText();// 선택된 라디오 버튼으로부터 지역 이름 획득1(CharSequence)
                check_radio_button_string = check_radio_button_text.toString(); // 선택된 라디오 버튼으로부터 지역 이름 획득2(String)
                full_location = front_location + " " + check_radio_button_string;
                fragment120_to_fragment130_button.setEnabled(true);// 두번째 지역을 선택해 세번째 지역(동면읍)을 선택할 수 있는 버튼 활성
            }
        });

        // **************** 완료 버튼: 다이얼로그 종료 *************** //
        fragment120_done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!(check_radio_button_string.isEmpty())){
                    weatherData = new WeatherDataSetView(context, rootView, full_location);
                    weatherData.setLocation(full_location);
                }
                else{
                    weatherData = new WeatherDataSetView(context, rootView, front_location);
                    weatherData.setLocation(front_location);
                }

                dlg.dismiss();// 두번째 다이얼로그를 종료한다.
            }
        });

        //********* 세번째 지역을 선택하는 버튼 ********** //
        fragment120_to_fragment130_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                locationFragment = new Fragment130(context, check_radio_button_string, full_location); // context와 두번째 지역, 첫번째+두번째 지을 전달하여 인스턴트를 생성한다.
                locationFragment.callFunction(rootView); // 선택된 지역으로 세팅해야하는 Fragment1의 text view를 전달하여 세번째 지역 선택을 진행한다.
                dlg.dismiss(); // 두번째 다이얼로그는 종료한다.
            }
        });

    }
}
