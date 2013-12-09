package org.instorm.example.pmtool;

import org.instorm.example.R;
import org.instorm.example.R.id;
import org.instorm.example.R.layout;
import org.instorm.example.R.string;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	private static final String TAG = "LoginActivity";
	
	private EditText edtUsername;
	private EditText edtPassword;
	private Button btnLogin;
	private Button btnRegist;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.v(TAG, "-----OnClick Login-----");
				String username = edtUsername.getText().toString();
				String password = edtPassword.getText().toString();
				if(username != null && !username.equals("") && password != null && !password.equals("")){
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.putExtra("username", username);
					intent.putExtra("password", password);
					Log.v(TAG, "-----username " + username + "-----");
					Log.v(TAG, "-----password " + password + "-----");
					LoginActivity.this.startActivity(intent);
				} else {
					Builder dialog = new AlertDialog.Builder(LoginActivity.this);
					dialog.setTitle(R.string.prompt);
					dialog.setMessage(R.string.wrong_username_or_password);
					dialog.setNegativeButton(R.string.confirm, null);
					dialog.show();
				}
			}
		});
        btnRegist = (Button) findViewById(R.id.btn_regist);
    }
}
