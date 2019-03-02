package com.assignment.rahulhosmani.multinotepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private List<Notes> notesList = new ArrayList<>();
    private  static final int BIND_req=1;
    private static final int EDIT_req=2;
    private RecyclerView recyclerView;
    private NotesAdapter nAdapter;

    JsonReader jsonread;
    JsonWriter jsonwrite;

    String strTitle;
    String strText;
    String strDate;

    int editPosition;

    ArrayList<Notes> jsonlist = new ArrayList<>();

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            recyclerView = (RecyclerView) findViewById(R.id.recycler);
            nAdapter = new NotesAdapter(notesList, this);
            recyclerView.setAdapter(nAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            loader();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addnote:
                Intent editIntent = new Intent(this,EditActivity.class);
                startActivityForResult(editIntent,BIND_req);
                return true;

            case R.id.about:
                Intent aboutIntent=new Intent(this,AboutActivity.class);
                startActivity(aboutIntent);
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildLayoutPosition(v);
        editPosition = position;
        Notes n = notesList.get(position);
        Intent intentEdit = new Intent(this,EditActivity.class);
        intentEdit.putExtra("NOTE_TITLE",jsonlist.get(position).getTitle());
        intentEdit.putExtra("NOTE_DATE",jsonlist.get(position).getDate());
        intentEdit.putExtra("NOTE_TEXT",jsonlist.get(position).getContent());

        startActivityForResult(intentEdit, EDIT_req);
    }

    @Override
    public boolean onLongClick(View v) {
        final int position = recyclerView.getChildLayoutPosition(v);
        String title = jsonlist.get(position).getTitle();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                jsonlist.remove(position);
                if(!notesList.isEmpty()){
                    notesList.remove(position);
                    nAdapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setMessage("Delete Note \'"+title+"\'?");
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    public void loader(){
        notesList.clear();
        jsonlist.clear();
        int object_counter=-1;

        try {

        jsonread = new JsonReader(new InputStreamReader(openFileInput("notes.json"),"UTF-8"));
        jsonread.beginObject();
        int temp=0;
        while(jsonread.hasNext()){
            String name = jsonread.nextName();
            if(name.equals("NOTE_TITLE")){
                strTitle=jsonread.nextString();
                temp++;
            }else if(name.equals("NOTE_DATE")){
                strDate=jsonread.nextString();
                temp++;
            } else  if(name.equals("NOTE_TEXT")){
                strText=jsonread.nextString();
                temp++;
            }

            else{
                jsonread.skipValue();
            }

            if(temp==3){
                object_counter++;
                Notes note = new Notes(strTitle,strDate,strText);
                jsonlist.add(note);
                if(strText.length()>80)
                    strText=strText.substring(0,79)+"...";
                notesList.add(object_counter,new Notes(strTitle,strDate,strText));
                nAdapter.notifyDataSetChanged();
                temp=0;
            }

        }
    }  catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            Notes temp;
            File file = new File("notes.json");
            file.delete();
            jsonwrite = new JsonWriter(new OutputStreamWriter(openFileOutput("notes.json", Context.MODE_PRIVATE), "UTF-8"));
            jsonwrite.beginObject();
            Iterator<Notes> iterator = jsonlist.iterator();
            while (iterator.hasNext()) {
                temp = iterator.next();
                jsonwrite.name("NOTE_TITLE").value(temp.getTitle().toString());
                jsonwrite.name("NOTE_DATE").value(temp.getDate().toString());
                jsonwrite.name("NOTE_TEXT").value(temp.getContent().toString());
            }
            jsonwrite.endObject();
            jsonwrite.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void doAsync(){
        new MyAsyncTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        try {

            Notes temp;
            if (requestCode == BIND_req) {
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra("NOTE_TITLE");
                    String date = data.getStringExtra("NOTE_DATE");
                    String text = data.getStringExtra("NOTE_TEXT");

                    Notes note = new Notes(title, date, text);
                    jsonlist.add(0, note);

                    if (text.length() > 80)
                        text = text.substring(0, 79) + "...";
                    notesList.add(0, new Notes(title, date, text));
                    nAdapter.notifyDataSetChanged();

                    Log.i("NOTE_TITLE: ", title);
                } else {
                    Log.i("Result code: ", String.valueOf(requestCode));
                }
            } else if (requestCode == EDIT_req) {
                if (resultCode == RESULT_OK) {
                    Notes note;
                    String title = data.getStringExtra("NOTE_TITLE");
                    String date = data.getStringExtra("NOTE_DATE");
                    String text = data.getStringExtra("NOTE_TEXT");

                    note = jsonlist.get(editPosition);
                    note = new Notes(title, date, text);
                    jsonlist.remove(editPosition);
                    nAdapter.notifyDataSetChanged();
                    jsonlist.add(0, note);
                    if (text.length() > 80)
                        text = text.substring(0, 79) + "...";
                    notesList.add(0, new Notes(title, date, text));
                    nAdapter.notifyDataSetChanged();

                }
            } else {
                Log.d("Request Code ", String.valueOf(requestCode));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class MyAsyncTask extends AsyncTask<Context, Integer, String> { //  <Parameter, Progress, Result>
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Context... contexts) {
        JsonReader jsonReader;
        JsonWriter jsonWriter;
        String title=null;
        String date=null;
        String text=null;

        ArrayList<Notes> jsonlist=new ArrayList<Notes>();
        int Edit_post;
        String filePath = contexts[0].getFilesDir().getPath().toString() + "/notes.json";
        File file =new File(filePath);

        jsonlist.clear();
        int object_counter=-1;
        try {
            jsonReader = new JsonReader(new InputStreamReader(contexts[0].openFileInput("notes.json"),"UTF-8"));
            jsonReader.beginObject();
            int temp=0;
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (name.equals("NOTES_TITLE")) {
                    title=jsonReader.nextString();
                    Log.i("NOTES TITLE: ",title);
                    temp++;
                }else if (name.equals("NOTES_DATE") ) {
                    date=jsonReader.nextString();
                    Log.i("NOTES DATE: ",date);
                    temp++;
                }
                else if (name.equals("NOTES_TEXT") ) {
                    text=jsonReader.nextString();
                    Log.i("NOTES TEXT: ",text);
                    temp++;
                }

                else{
                    jsonReader.skipValue();
                }
                if(temp==3) {
                    Notes note=new Notes(title,date,text);
                    jsonlist.add(note);
                    if (text.length()>80)
                        text=text.substring(0,79)+"...";
                    temp=0;
                }
            }
            return jsonlist.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return jsonlist.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}






