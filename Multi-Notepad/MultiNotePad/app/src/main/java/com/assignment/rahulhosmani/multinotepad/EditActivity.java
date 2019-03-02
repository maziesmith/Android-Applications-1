package com.assignment.rahulhosmani.multinotepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;

public class EditActivity extends AppCompatActivity {

    EditText editTxtTitle;
    EditText editTxtNote;
    DateFormat dateFormat;
    String strCurrentDate;
    String noteTitle;
    String noteText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        dateFormat = new SimpleDateFormat("EEE MMM d, h:mm a");
        strCurrentDate = dateFormat.format(Calendar.getInstance().getTime());

        editTxtTitle = (EditText) findViewById(R.id.editTextTitle);
        editTxtNote = (EditText) findViewById(R.id.editTextNote);

        Intent intent = getIntent();
        noteTitle = intent.getStringExtra("NOTE_TITLE");
        noteText = intent.getStringExtra("NOTE_TEXT");

        editTxtTitle.setText(noteTitle);
        editTxtNote.setText(noteText);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(editTxtTitle.getText().toString().equals("") && editTxtNote.getText().toString().equals("")){
            finish();
        }
        else if(editTxtTitle.getText().toString().equals("") && !editTxtNote.getText().toString().equals("")){
            Toast.makeText(this,"Un-Titled Activity was not Saved",Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(editTxtTitle.getText().toString().equals(noteTitle) && editTxtNote.getText().toString().equals(noteText)){
            finish();
        }
        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent data = new Intent();
                            data.putExtra("NOTE_TITLE", editTxtTitle.getText().toString());
                            dateFormat = new SimpleDateFormat("EEE MMM d, h:mm a");
                            strCurrentDate = dateFormat.format(Calendar.getInstance().getTime());
                            data.putExtra("NOTE_DATE", strCurrentDate);
                            data.putExtra("NOTE_TEXT", editTxtNote.getText().toString());
                            setResult(RESULT_OK, data);
                            finish();
                        }
                    });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setMessage("Your note is not saved!" + "\nSave note \'" + editTxtTitle.getText() + "\'?");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.menuSave:
             try {
                 if (editTxtTitle.getText().toString().equals("")) {
                     Toast.makeText(this, "Un-Titled Activity was not Saved", Toast.LENGTH_SHORT).show();
                 } else if (editTxtTitle.getText().toString().equals(noteTitle) && editTxtNote.getText().toString().equals(noteText)) {
                     finish();
                 } else {
                     Intent data = new Intent();
                     data.putExtra("NOTE_TITLE", editTxtTitle.getText().toString());
                     dateFormat = new SimpleDateFormat("EEE MMM d, h:mm a");
                     strCurrentDate = dateFormat.format(Calendar.getInstance().getTime());
                     data.putExtra("NOTE_DATE", strCurrentDate);
                     data.putExtra("NOTE_TEXT", editTxtNote.getText().toString());
                     setResult(RESULT_OK, data);

                 }
                 finish();
                 return true;
             }catch (Exception e){
                 e.printStackTrace();
             }

            default: return super.onOptionsItemSelected(item);
        }


    }
}
