package com.ulan.ledcontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    protected TextView logs_field;
    protected RequestQueue queue;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonLights = findViewById(R.id.led_lights);
        Button buttonBlinkGreen = findViewById(R.id.led_blink_green);
        Button buttonBlinkRed = findViewById(R.id.led_blink_red);
        Button buttonRainbow = findViewById(R.id.led_rainbow);
        Button buttonRed = findViewById(R.id.red_light);
        Button buttonGreen = findViewById(R.id.green_light);
        Button buttonBlue = findViewById(R.id.blue_light);
        Button buttonCustom = findViewById(R.id.led_custom);
        final SeekBar seekLevel = findViewById(R.id.led_level);

        logs_field = findViewById(R.id.led_logs);
        queue = Volley.newRequestQueue(this);


        buttonLights.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queue.add(buildRequest("http://192.168.1.31:5002/SwitchLights"));
                    }
                });
        buttonBlinkGreen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queue.add(buildRequest("http://192.168.1.31:5002/GlowGreen"));
                    }
                });
        buttonBlinkRed.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queue.add(buildRequest("http://192.168.1.31:5002/FlashRed"));
                    }
                });
        buttonRainbow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queue.add(buildRequest("http://192.168.1.31:5002/Rainbow"));
                    }
                });

        buttonRed.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queue.add(buildRequest("http://192.168.1.31:5002/Red"));
                    }
                });

        buttonGreen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queue.add(buildRequest("http://192.168.1.31:5002/Green"));
                    }
                });

        buttonBlue.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        queue.add(buildRequest("http://192.168.1.31:5002/Blue"));
                    }
                });

        buttonCustom.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        seekLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                Globals g = Globals.getInstance();
                                g.setData(progress);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });
                        Globals g = Globals.getInstance();
                        JSONObject request = new JSONObject();
                        try {
                            request.put("level", g.getData());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        queue.add(buildPostRequest("http://192.168.1.31:5002/CustomWhite", request));

                    }
                });
    }

    private StringRequest buildRequest(String url) {
        logs_field.setText("Sending…");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        logs_field.setText("Response is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logs_field.setText("That didn't work!");
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return stringRequest;
    }

    private JsonObjectRequest buildPostRequest(String url, JSONObject requestBody) {
        logs_field.setText("Sending…");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        logs_field.setText("Response is: " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logs_field.setText("That didn't work!");
            }
        }) { @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }};
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return jsonObjectRequest;
    }
}
