package fr.esgi.android.blocNotes.activities;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Note;

public class CreateNoteActivity extends Activity 
{

	public static final int REQUEST_IMAGE_CAPTURE = 1;
	public static final int REQUEST_AUDIO_CAPTURE = 2;

	private EditText noteTitle ,noteText;
	private Note toCreate;
	private Button noteAdd;
	private MyDatabaseHelper db;
	private boolean modifyFlag = false;
	private int categoryId;
	int noteId=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);

		//set title of this activity
		setTitle("CrÃ©er une note");

		toCreate = new Note();

		noteTitle = (EditText) findViewById(R.id.noteTitleEditText);
		noteText = (EditText) findViewById(R.id.noteTextEditText);
		noteAdd = (Button) findViewById(R.id.btnCreateNote);

		if (this.getIntent().getStringExtra("noteName") !=null){
			String noteName = this.getIntent().getStringExtra("noteName");
			String noteTexte = this.getIntent().getStringExtra("noteText");
			noteId=this.getIntent().getIntExtra("noteId", 1);
			modifyFlag=this.getIntent().getBooleanExtra("modifyFlag", false);
			noteText.setText(noteTexte);
			noteTitle.setText(noteName);
			noteAdd.setText("modifier");
		}
		db = new MyDatabaseHelper(this);

		categoryId = this.getIntent().getIntExtra("categoryId", 1);

		noteAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Note toCreate = new Note();
				toCreate.setTitle(noteTitle.getText().toString());
				toCreate.setCategoryId(categoryId);
				toCreate.setText(noteText.getText().toString());
				// update or add note
				if(modifyFlag==true){
					//update note
					toCreate.setId(noteId);
					db.updateNote(toCreate);


				}
				else{ 
					//Add note
					db.addNote(toCreate);
					Calendar calendrier = Calendar.getInstance();
					Log.i("Time","l'année :"+Integer.toString(calendrier.get(Calendar.YEAR))+" et le mois "+Integer.toString(calendrier.get(Calendar.MONTH))+" et le jour "+Integer.toString(calendrier.get(Calendar.DAY_OF_MONTH)));

				}

				Intent intent = new Intent();
				setResult(2,intent);  
				finish();
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			setResult(2,intent);  
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}
}
