package com.example.v1.loginpage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SigninFragment extends Fragment {

    FragmentListener fragmentListener;

    Context mComtext;
    EditText editTextEmail;
    EditText editTextPassword;

    TextView textviewMessage;
    ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    private Button signin;
    private TextView to_signup;
    private TextView to_pw;

    private String email;
    private String password;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mComtext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_in_fragment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText)rootView.findViewById(R.id.editTextTextEmailAddress2);
        editTextPassword = (EditText)rootView.findViewById(R.id.editTextTextPassword2);
        signin= (Button)rootView.findViewById(R.id.button3);
        textviewMessage = (TextView)rootView.findViewById(R.id.textView2);
        to_pw = (TextView)rootView.findViewById(R.id.to_pw_from_signin);
        to_signup = (TextView)rootView.findViewById(R.id.to_signup_from_signin);
        progressDialog = new ProgressDialog(mComtext);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                userLogin();
            }
        });
        to_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fragmentListener.onToSignup();
            }
        });
        to_pw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fragmentListener.onToFindPassword();
            }
        });
        //textviewFindPassword.setOnClickListener(mComtext);

        return rootView;
    }

    private void userLogin(){
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(mComtext, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(mComtext, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("로그인중입니다. 잠시 기다려 주세요...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) mComtext, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            String email = editTextEmail.getText().toString();
                            fragmentListener.onToFragments(email);
                        } else {
                            Toast.makeText(mComtext.getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
                            textviewMessage.setText("로그인 실패 유형\n - password가 맞지 않습니다.\n -서버에러");
                        }
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
