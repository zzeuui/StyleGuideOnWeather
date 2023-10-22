package com.example.v1.loginpage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.widget.Toast.LENGTH_SHORT;

public class SignupFragment extends Fragment {

    private FragmentListener fragmentListener;


    EditText editTextEmail;
    EditText editTextPassword;
    TextView textviewSingin;
    TextView textviewMessage;
    Context mComtext;
    ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private Button buttonSignup;
    private TextView to_Signin;

    private String email;
    private String password;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mComtext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_up_fragment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();


        editTextEmail = (EditText)rootView.findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = (EditText)rootView.findViewById(R.id.editTextTextPassword);
        textviewSingin= (TextView)rootView.findViewById(R.id.textView5);
        textviewMessage = (TextView)rootView.findViewById(R.id.signup_description);
        buttonSignup = (Button)rootView.findViewById(R.id.signup);
        to_Signin = (TextView)rootView.findViewById(R.id.to_signin_from_signup);

        progressDialog = new ProgressDialog(mComtext);

        buttonSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                registerUser();
            }
        });
        to_Signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(fragmentListener!=null){
                    fragmentListener.onToSignin();
                }
            }
        });


        return rootView;
    }

    private void registerUser(){
        //사용자가 입력하는 email, password를 가져온다.
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        //email과 password가 비었는지 아닌지를 체크 한다.
        if(email.isEmpty()){
            Toast.makeText(mComtext, "Email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password.isEmpty()){
            Toast.makeText(mComtext, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //email과 password가 제대로 입력되어 있다면 계속 진행된다.
        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mComtext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(fragmentListener!=null){
                                String email = editTextEmail.getText().toString();
                                fragmentListener.onToFragments(email);
                            }
                        } else {
                            //에러발생시
                            textviewMessage.setText("에러유형\n - 이미 등록된 이메일  \n - 올바른 이메일 양식이 아님(***@***.***)  \n - 암호 최소 6자리 이상 \n - 서버에러");
                            Toast.makeText(mComtext, "등록 에러!", LENGTH_SHORT).show();
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
