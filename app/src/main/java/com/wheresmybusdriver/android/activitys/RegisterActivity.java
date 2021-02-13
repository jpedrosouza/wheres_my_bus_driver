package com.wheresmybusdriver.android.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wheresmybusdriver.android.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText confirmeSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nome = (EditText)findViewById(R.id.editName);
        email = (EditText)findViewById(R.id.editEmail);
        senha = (EditText)findViewById(R.id.editPassword);
        confirmeSenha = (EditText)findViewById(R.id.editConfirmPassword);
    }

    public void entrarLogin(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void mensagemCadastro(View view) {
        if (senha.getText() == confirmeSenha.getText()) {
            Toast.makeText(RegisterActivity.this, "Cadastrado com Sucesso!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(RegisterActivity.this, "Informe senhas iguais!", Toast.LENGTH_SHORT).show();
        }
    }
}
