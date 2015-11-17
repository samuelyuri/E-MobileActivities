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

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.samuelzvir.meuabc.R;
import br.com.samuelzvir.meuabc.entities.Challenge;
import br.com.samuelzvir.meuabc.entities.Challenge$Table;
import br.com.samuelzvir.meuabc.entities.SimpleChallenge;
import br.com.samuelzvir.meuabc.entities.SimpleChallenge$Table;
import br.com.samuelzvir.meuabc.entities.Student;
import br.com.samuelzvir.meuabc.entities.Student$Table;

public class ABCActivity extends AppCompatActivity {
    private static final String TAG = "ABCActivity";
    final ArrayList<View> mCheckedViews = new ArrayList<View>();
    List<String> wordsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abc);
        List<Student> studentsList = new Select().from(Student.class).queryList();
        List<SimpleChallenge> simpleChallenges =  new Select().from(SimpleChallenge.class).queryList();
        final ListView users = (ListView) findViewById(R.id.studentslistView);
        final ListView words = (ListView) findViewById(R.id.wordsListView);

        @SuppressWarnings( "unused" )
        final CheckBox usePositionsCB = new CheckBox(getApplicationContext());

        List<String> studentsNamesList = new ArrayList<>();
        for (Student s : studentsList){ // populate the words
            studentsNamesList.add(s.getNickname());
        }

        for (SimpleChallenge s : simpleChallenges){
            wordsList.add(s.getWord());
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_single_choice,
                studentsNamesList);

        users.setAdapter(adapter);
        users.setItemsCanFocus(false);
        users.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        users.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                boolean checked = users.isItemChecked(position);
                if (checked) {
                    Log.i(TAG, "mCheckedViews.add(view);");
                    mCheckedViews.add(view);
                    fillCheckedWords();
                    words.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                            boolean checked = users.isItemChecked(position);
                            if (checked) {
                                Log.i(TAG, "mCheckedViews.add(view);");
                                mCheckedViews.add(view);
                            } else {
                                Log.i(TAG, "mCheckedViews.remove(view);");
                                mCheckedViews.remove(view);
                            }
                        }
                    });

                } else {
                    Log.i(TAG, "mCheckedViews.remove(view);");
                    mCheckedViews.remove(view);
                }
            }
        });
        mCheckedViews.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_abc, menu);
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

    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<>();

        public StableArrayAdapter(Context context,
                                  int textViewResourceId,
                                  List<String> objects)
        {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        public Integer getId(String word){
            return mIdMap.get(word);
        }

        @Override
        public long getItemId(int position) {
            try{
                String item = getItem(position);
                return mIdMap.get(item);
            }catch(Exception e){
                return 0l;
            }

        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

    public void saveGame(View view){
        Log.d(TAG,"Saving game");
        final ListView users = (ListView) findViewById(R.id.studentslistView);
        final ListView words = (ListView) findViewById(R.id.wordsListView);
        Object user = users.getAdapter().getItem(users.getCheckedItemPosition());
        Log.d(TAG, "For user " + user.toString());

        Student student = new Select().from(Student.class).where(Condition.column(Student$Table.NICKNAME).eq(user.toString())).querySingle();
        Log.d(TAG, "Words: " + user.toString());
        List<Challenge>  challenges = new ArrayList<>();
        int length = words.getCount();
        SparseBooleanArray checked = words.getCheckedItemPositions();
        for (int i = 0; i < length; i++) {
            if (checked.get(i)) {
                String word = wordsList.get(i);
                Log.d(TAG, word);
                SimpleChallenge tempChallenge = new Select().from(SimpleChallenge.class).where(Condition.column(SimpleChallenge$Table.WORD).eq(word)).querySingle();
                Challenge challenge = new Challenge();
                challenge.setName(tempChallenge.getWord());
                challenge.setImagePath(tempChallenge.getImagePath());
                challenge.setText(tempChallenge.getWord());
                challenge.save();
                challenge.setStudentRef(student.getId()); //TODO provisory until resolve foreign keys
                challenges.add(challenge);
            }
        }
        Log.d(TAG, "storing student " + student.getNickname());
        Log.d(TAG, "to save " + challenges.size() + " challenges.");
        student.setChallenges(challenges);
        student.update();
        long i =new Select().from(Student.class).where(Condition.column(Student$Table.NICKNAME).eq(student.getNickname())).querySingle().getMyChallenges().size();
        Log.d(TAG, "with " + i + " challenges saved.");
    }

    private void fillCheckedWords(){
        final ListView users = (ListView) findViewById(R.id.studentslistView);
        Object user = users.getAdapter().getItem(users.getCheckedItemPosition());
        Log.d(TAG, "For user " + user.toString());
        Student student = new Select().from(Student.class).where(Condition.column(Student$Table.NICKNAME).eq(user.toString())).querySingle();

        final ListView words = (ListView) findViewById(R.id.wordsListView);
        //TODO check saved items.
        final StableArrayAdapter adapter3 = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice,
                wordsList);
        words.setAdapter(adapter3);
        words.setItemsCanFocus(false);
        words.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }
}
