package com.example.v1;

import androidx.fragment.app.Fragment;

import java.io.IOException;

public interface FragmentListener {
    void onCurrentLocationChange(String s) throws IOException;
    void onCurrentTemperaturesChange(String s) throws IOException;
    void onCurrentWeatherChange(String s) throws IOException;
    void onMinTemperaturesChange(String s) throws IOException;
    void onMaxTemperaturesChange(String s) throws IOException;

    void onToSignin();
    void onToSignup();
    void onToFindPassword();
    void onToFragments(String email);
}
