package com.example.v1.loginpage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.v1.FragmentListener;
import com.example.v1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPasswordFragment extends Fragment {

    Context mComtext;
    FragmentListener fragmentListener;

    private EditText editTextUserEmail;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private TextView toSignIn;
    private Button send_email;

    private String emailAddress;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mComtext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.find_password_fragment, container, false);

        editTextUserEmail = (EditText)rootView.findViewById(R.id.find_password_enter_email);
        send_email = (Button)rootView.findViewById(R.id.send_password_to_email);
        progressDialog = new ProgressDialog(mComtext);
        firebaseAuth = FirebaseAuth.getInstance();
        toSignIn = (TextView)rootView.findViewById(R.id.to_signin_from_pw);


        button_events();

        return rootView;
    }

    private void button_events(){
        send_email.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요...");
                progressDialog.show();
                //비밀번호 재설정 이메일 보내기
                find_pw();
            }
        });

        toSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fragmentListener.onToSignin();
            }
        });
    }

    private void find_pw(){
        emailAddress = editTextUserEmail.getText().toString().trim();
        firebaseAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(mComtext, "이메일을 보냈습니다.", Toast.LENGTH_LONG).show();
                            fragmentListener.onToSignin();

                        } else {
                            Toast.makeText(mComtext, "메일 보내기 실패!", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() instanceof FragmentListener){
            this.fragmentListener = (FragmentListener) getActivity();
        }
    }

}
