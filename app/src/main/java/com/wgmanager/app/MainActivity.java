package com.wgmanager.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_ADD_SERVER = 100;
    private static final String PREFS_NAME = "wg_servers";
    private static final String KEY_SERVERS = "servers_list";
    private static final String KEY_SELECTED = "selected_index";
    private static final String KEY_CONNECTED = "is_connected";

    private TextView tvTitle, tvServerName, tvLocation, tvStatus, tvVpnText, tvTapHint;
    private FrameLayout btnVpn;
    private ImageButton btnAdd;
    private CardView cardServer;
    private View dotStatus;

    private List<Server> servers = new ArrayList<>();
    private int selectedIndex = 0;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTitle = findViewById(R.id.tvTitle);
        tvServerName = findViewById(R.id.tvServerName);
        tvLocation = findViewById(R.id.tvLocation);
        tvStatus = findViewById(R.id.tvStatus);
        tvVpnText = findViewById(R.id.tvVpnText);
        tvTapHint = findViewById(R.id.tvTapHint);
        btnVpn = findViewById(R.id.btnVpn);
        btnAdd = findViewById(R.id.btnAdd);
        cardServer = findViewById(R.id.cardServer);
        dotStatus = findViewById(R.id.dotStatus);

        loadServers();
        updateUI();

        btnVpn.setOnClickListener(v -> toggleVpn());

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddServerActivity.class);
            startActivityForResult(intent, REQ_ADD_SERVER);
        });

        cardServer.setOnClickListener(v -> {
            if (servers.size() > 1) {
                selectedIndex = (selectedIndex + 1) % servers.size();
                saveServers();
                updateUI();
            }
        });
    }

    private void toggleVpn() {
        isConnected = !isConnected;
        if (selectedIndex < servers.size()) {
            servers.get(selectedIndex).setConnected(isConnected);
        }
        saveServers();
        updateUI();

        String msg = isConnected ? "VPN подключён" : "VPN отключён";
        Snackbar.make(btnVpn, msg, Snackbar.LENGTH_SHORT).show();
    }

    private void updateUI() {
        if (servers.isEmpty()) {
            tvServerName.setText("Нет серверов");
            tvLocation.setText("Нажмите + чтобы добавить");
            tvStatus.setText(R.string.disconnected);
            tvStatus.setTextColor(getColor(R.color.status_disconnected));
            dotStatus.setBackgroundResource(R.drawable.dot_status);
            btnVpn.setBackgroundResource(R.drawable.ic_vpn_button_off);
            tvTapHint.setText(R.string.tap_to_connect);
            return;
        }

        Server s = servers.get(selectedIndex);
        tvServerName.setText(s.getName());
        tvLocation.setText(s.getLocation());

        if (isConnected) {
            tvStatus.setText(R.string.connected);
            tvStatus.setTextColor(getColor(R.color.status_connected));
            dotStatus.setBackgroundResource(R.drawable.dot_status_connected);
            btnVpn.setBackgroundResource(R.drawable.ic_vpn_button_on);
            tvTapHint.setText(R.string.tap_to_disconnect);
        } else {
            tvStatus.setText(R.string.disconnected);
            tvStatus.setTextColor(getColor(R.color.status_disconnected));
            dotStatus.setBackgroundResource(R.drawable.dot_status);
            btnVpn.setBackgroundResource(R.drawable.ic_vpn_button_off);
            tvTapHint.setText(R.string.tap_to_connect);
        }
    }

    private void loadServers() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = prefs.getString(KEY_SERVERS, "[]");
        selectedIndex = prefs.getInt(KEY_SELECTED, 0);
        isConnected = prefs.getBoolean(KEY_CONNECTED, false);

        try {
            JSONArray arr = new JSONArray(json);
            servers.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                Server s = new Server(o.getString("name"), o.getString("location"), o.getString("code"));
                s.setConnected(o.optBoolean("connected", false));
                servers.add(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Дефолтный сервер если пусто
        if (servers.isEmpty()) {
            servers.add(new Server("NL-AMS-01", "Netherlands, Amsterdam", "nl"));
            saveServers();
        }
    }

    private void saveServers() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        JSONArray arr = new JSONArray();
        for (Server s : servers) {
            JSONObject o = new JSONObject();
            try {
                o.put("name", s.getName());
                o.put("location", s.getLocation());
                o.put("code", s.getCountryCode());
                o.put("connected", s.isConnected());
                arr.put(o);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ed.putString(KEY_SERVERS, arr.toString());
        ed.putInt(KEY_SELECTED, selectedIndex);
        ed.putBoolean(KEY_CONNECTED, isConnected);
        ed.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADD_SERVER && resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("name");
            String location = data.getStringExtra("location");
            String code = data.getStringExtra("code");
            servers.add(new Server(name, location, code));
            selectedIndex = servers.size() - 1;
            isConnected = false;
            saveServers();
            updateUI();
            Toast.makeText(this, "Сервер добавлен", Toast.LENGTH_SHORT).show();
        }
    }
}
