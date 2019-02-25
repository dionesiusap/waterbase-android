package com.example.waterbase.pbd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutUs extends Fragment {
    private EditText serverIp;
    private TextView serverIpInfo;
    private CheckBox useHttps;

    public AboutUs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.

        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button about = view.findViewById(R.id.aboutUs_button);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.acyril.com";
                Uri webpage = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        });

        Button resetFloor = view.findViewById(R.id.resetFloor_button);
        resetFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentActivity.setGroundFloorBaseline(BarometerReaderService.getSensorValue());
                Log.d("SETTINGS", "GF baseline: " + FragmentActivity.getGroundFloorBaseline());
            }
        });

        Button saveConfig = view.findViewById(R.id.saveConfig_button);
        serverIp = view.findViewById(R.id.serverIp_field);
        serverIpInfo = view.findViewById(R.id.serverIp_info);
        useHttps = view.findViewById(R.id.serverIp_useHttps);
        serverIpInfo.setText("Current Address: " + FragmentActivity.getServerUrl());
        saveConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverIpValue = serverIp.getText().toString();

                serverIpValue.replace("https://", "");
                serverIpValue.replace("http://", "");

                if (useHttps.isChecked()) {
                    serverIpValue = "https://" + serverIpValue;
                }
                else {
                    serverIpValue = "http://" + serverIpValue;
                }

                if (!serverIpValue.equals(new String())) {
                    FragmentActivity.setServerUrl(serverIpValue);
                }

                serverIpInfo.setText("Current Address: " + FragmentActivity.getServerUrl());

                Log.d("SETTINGS", "Server IP Value: " + FragmentActivity.getServerUrl());
            }
        });
    }
}
