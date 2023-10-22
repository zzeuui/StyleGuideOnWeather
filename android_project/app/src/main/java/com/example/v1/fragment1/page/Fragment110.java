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


public class Fragment110 {

    private Context context;
    RadioGroup first_location_radioGroup;
    Button fragment110_done_button;
    Button fragment110_to_fragment120_button;
    RadioButton check_radio_button;

    String check_radio_button_string = "";
    private LocationData location_data;
    private Fragment120 locationFragment;
    private WeatherDataSetView weatherData;

    public Fragment110(Context context){
        this.context = context;
    }

    //main function
    public void callFunction(final View rootView){

        final TextView tv = (TextView)rootView.findViewById(R.id.current_location);

        // *****다이얼로그 세팅****** //
        final Dialog dlg = new Dialog(context);
        dlg.setContentView(R.layout.fragment110);
        dlg.show();

        // ******다이얼로그 내의 위젯 정의 *******//
        first_location_radioGroup = (RadioGroup)dlg.findViewById(R.id.first_location_radioGroup);
        fragment110_done_button = (Button) dlg.findViewById(R.id.fragment110_done);
        fragment110_to_fragment120_button = (Button) dlg.findViewById(R.id.fragment110_to_fragment120);

        // ****  라디오 버튼 세팅: 첫번째 지역으로(특별시 및 도) ****** //
        location_data = new LocationData(context);
        location_data.setRadioGroup(first_location_radioGroup, 2, ""); //order ==> 2: 시도 3: 시구군 4: 동면

        // ******* 첫번째 지역(시도)을 선택 ****** //
        first_location_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                check_radio_button = dlg.findViewById(i); //선택한 지역의 라디오 버튼 아이디 얻어 작업하려는 라디오 버튼 설정
                CharSequence check_radio_button_text = check_radio_button.getText();// 선택된 라디오 버튼으로부터 지역 이름 획득1(CharSequence)
                check_radio_button_string = check_radio_button_text.toString(); // 선택된 라디오 버튼으로부터 지역 이름 획득2(String)
                if(!check_radio_button_string.equals("이어도")){
                    fragment110_to_fragment120_button.setEnabled(true);// 첫번째 지역을 선택해 두번째 지역(시구군)을 선택할 수 있는 버튼 활성
                }
        }
        });

        // **************** 완료 버튼: 다이얼로그 종료 *************** //
        fragment110_done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(!(check_radio_button_string.isEmpty())){
                    weatherData =  new WeatherDataSetView(context, rootView, check_radio_button_string);
                    weatherData.setLocation(check_radio_button_string);
                }
                dlg.dismiss();// 첫번째 다이얼로그를 종료한다.
            }
        });

        //********* 두번째 지역을 선택하는 버튼 ********** //
        fragment110_to_fragment120_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                locationFragment = new Fragment120(context, check_radio_button_string); // context와 첫번째 지역을 전달하여 인스턴트를 생성한다.
                locationFragment.callFunction(rootView); // 선택된 지역으로 세팅해야하는 Fragment1의 text view를 전달하여 두번째 지역 선택을 진행한다.
                dlg.dismiss(); // 첫번째 다이얼로그는 종료한다.
            }
        });

    }

}
