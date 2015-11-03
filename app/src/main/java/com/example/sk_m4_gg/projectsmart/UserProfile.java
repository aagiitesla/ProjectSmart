package com.example.sk_m4_gg.projectsmart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Sk_m4_gg on 15.10.28.
 */
public class UserProfile  extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profille);
        Intent intent = getIntent();
        String username = intent.getStringExtra(MainActivity.USER_NAME);

        TextView textView = (TextView) findViewById(R.id.hellotextView3);

        textView.setText("Welcome " + username);
    }
}
