package fr.esgi.android.blocNotes.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	
	private EditText noteTitle;
	private Note toCreate;
	private Button noteAdd;
	private MyDatabaseHelper db;
	private int categoryId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);

		//set title of this activity
		setTitle("Créer une note");
		
		toCreate = new Note();
				
		noteTitle = (EditText) findViewById(R.id.noteTitleEditText);
		
		noteAdd = (Button) findViewById(R.id.btnCreateNote);
		
		db = new MyDatabaseHelper(this);

		categoryId = this.getIntent().getIntExtra("categoryId", 1);
		
		noteAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Note toCreate = new Note();
				toCreate.setTitle(noteTitle.getText().toString());
				toCreate.setCategoryId(categoryId);
				// add task created
				db.addNote(toCreate);
				
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
