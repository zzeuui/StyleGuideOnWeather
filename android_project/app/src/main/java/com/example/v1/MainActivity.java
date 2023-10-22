package com.example.v1;

import android.Manifest;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.v1.MypagerAdapter.MyPagerAdapter;
import com.example.v1.loginpage.FindPasswordFragment;
import com.example.v1.loginpage.SigninFragment;
import com.example.v1.loginpage.SignupFragment;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentListener {
    ViewPager pager;

    private SignupFragment singupFragment;
    private SigninFragment signinFragment;
    private FindPasswordFragment findPasswordFragment;

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;

    int fragment_length = 4;

    final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tedPermission();

        pager = findViewById(R.id.main_paer);
        pager.setOffscreenPageLimit(fragment_length);

        signinFragment = new SigninFragment();
        singupFragment = new SignupFragment();
        findPasswordFragment = new FindPasswordFragment();


        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        adapter.addItem(signinFragment); //첫번째 페이지

        pager.setAdapter(adapter);

    }

    public void toSignin(){
        adapter.clearItem();
        adapter.addItem(signinFragment);
        pager.setAdapter(adapter);
    }

    public void toSingup(){
        adapter.clearItem();
        adapter.addItem(singupFragment);
        pager.setAdapter(adapter);
    }

    public void toFindPassword(){
        adapter.clearItem();
        adapter.addItem(findPasswordFragment);
        pager.setAdapter(adapter);
    }

    public void startFragment(){
        adapter.clearItem();

        adapter.addItem(fragment1);
        adapter.addItem(fragment2);
        adapter.addItem(fragment3);

        //Fragment4 fragment4 = new Fragment4();
        //adapter.addItem(fragment4);

        pager.setAdapter(adapter);
    }

    @Override
    public void onCurrentLocationChange(String s) throws IOException { fragment2.setText(s, 1); fragment3.setText(s, 1);}
    @Override
    public void onCurrentTemperaturesChange(String s) throws IOException { fragment2.setText(s, 2); fragment3.setText(s, 2);}
    @Override
    public void onCurrentWeatherChange(String s) throws IOException {fragment2.setText(s, 3);}
    @Override
    public void onMinTemperaturesChange(String s) throws IOException { fragment2.setText(s, 4); }
    @Override
    public void onMaxTemperaturesChange(String s) throws IOException { fragment2.setText(s, 5); }
    @Override
    public void onToSignin(){ toSignin(); }
    @Override
    public void onToSignup(){toSingup();}
    @Override
    public void onToFindPassword(){toFindPassword();}
    @Override
    public void onToFragments(String email){ startFragment(); fragment1.setEmail(email); }

    private void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

}