package com.ismaelsouza.registerapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class IncluirUsuarioActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private EditText inputEmail, inputSenha;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incluir_usuario);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.sign_up_button);

        inputEmail = findViewById(R.id.email);
        inputSenha = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String senha = inputSenha.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Digite um email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(senha)) {
                    Toast.makeText(getApplicationContext(), "Digite uma senha!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (senha.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Senha deve conter no minimo 6 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                mAuth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(IncluirUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String retorno;
                                if(task.isSuccessful()){
                                    retorno = "foi";
                                } else {
                                    retorno = "não foi";
                                }

                                Toast.makeText(IncluirUsuarioActivity.this, "Usuario " + retorno + " criado com sucesso:" , Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                                if (!task.isSuccessful()) {
                                    Toast.makeText(IncluirUsuarioActivity.this, "Email ja está sendo utilizado em outra Conta. \n\n" + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(IncluirUsuarioActivity.this, task.getResult().getUser().getUid(), Toast.LENGTH_LONG).show();

                                    startActivity(new Intent(IncluirUsuarioActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
