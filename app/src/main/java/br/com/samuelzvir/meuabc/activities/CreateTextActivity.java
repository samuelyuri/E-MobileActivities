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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

import br.com.samuelzvir.meuabc.R;
import br.com.samuelzvir.meuabc.entities.Challenge;

public class CreateTextActivity extends Activity {

    private static final String TAG = "CreateTextActivity";
    private ImageView image;
    private String appPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);
        appPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("path");
        if(imagePath != null){
            setImageView(imagePath);
        }
    }

    public void onToCamera(View view){
        Intent intent = new Intent(this, TakePhotoActivity.class);
        startActivity(intent);
    }

    public void saveWord(View view){
        EditText messageView = (EditText) findViewById(R.id.word);
        String word = messageView.getText().toString();

        Challenge challenge = new Challenge();
        challenge.setName(word);
        challenge.setText(word);
        //TODO add image path
        challenge.save();
    }

    private void setImageView(String path) {
        Log.i(TAG, "Adding image " + path);
        File imgFile = new File(path);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView imageView = (ImageView) findViewById(R.id.imageViewPicture);
            imageView.setImageBitmap(bitmap);
        }
    }
}