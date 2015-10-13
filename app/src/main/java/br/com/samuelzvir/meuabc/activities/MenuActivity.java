/*
 * Copyright 2015 Samuel Yuri Zvir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.samuelzvir.meuabc.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.raizlabs.android.dbflow.config.FlowManager;

import br.com.samuelzvir.meuabc.R;

public class MenuActivity extends Activity {

    private static final String TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        FlowManager.init(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onToWords(View view){
        Intent intent = new Intent(this, CreateTextActivity.class);
        startActivity(intent);
    }

    public void onToCamera(View view){
        Intent intent = new Intent(this, TakePhotoActivity.class);
        startActivity(intent);
    }

    public void onToProfiles(View view){
        Intent intent = new Intent(this, ProfilesActivity.class);
        startActivity(intent);
    }

    public void onToDataAnalysis(View view){
        Intent intent = new Intent(this, DataAnalysisActivity.class);
        startActivity(intent);
    }

    public void onToSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onToABC(View view){
        Intent intent = new Intent(this, ABCActivity.class);
        startActivity(intent);
    }

    public void onPlayChallenge(View view){
        Intent intent = new Intent(this, ScrabbleActivity.class);
        startActivity(intent);
    }
}
