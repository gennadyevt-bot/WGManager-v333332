package com.wgmanager.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddServerActivity extends AppCompatActivity {

    private EditText etServerName, etLocation, etCountryCode;
    private Button btnSave;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_server);

        etServerName = findViewById(R.id.etServerName);
        etLocation = findViewById(R.id.etLocation);
        etCountryCode = findViewById(R.id.etCountryCode);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String name = etServerName.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String code = etCountryCode.getText().toString().trim();

            if (name.isEmpty() || location.isEmpty() || code.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent result = new Intent();
            result.putExtra("name", name);
            result.putExtra("location", location);
            result.putExtra("code", code);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
