package fr.esgi.android.blocNotes.activities;

import java.util.Calendar;

import org.joda.time.DateTime;

import android.app.Activity;
import android.content.Context;
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


	private static final String TITLE_INPUT_DATA = "titleInputData";
	private static final String TEXT_INPUT_DATA = "textInputData";
	private EditText noteTitleEditText ,noteTextEditText;
	private Button noteAddBtn;
	private boolean modifyFlag = false;
	private int categoryId;
	int noteId=0;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);
		
		context = this;

		//set title of this activity
		setTitle(R.string.newNoteScreenName);
		

		noteTitleEditText = (EditText) findViewById(R.id.noteTitleEditText);
		noteTitleEditText.setHint(R.string.inputNoteTitleHint);
		
		noteTextEditText = (EditText) findViewById(R.id.noteTextEditText);
		noteTextEditText.setHint(R.string.inputNoteTextHint);
		
		

		if(savedInstanceState != null)
		{
			String titleInputSaved = savedInstanceState.getString(TITLE_INPUT_DATA);
			noteTitleEditText.setText(titleInputSaved);
			
			String textInputSaved = savedInstanceState.getString(TITLE_INPUT_DATA);
			noteTextEditText.setText(textInputSaved);
		}
		
		noteAddBtn = (Button) findViewById(R.id.btnCreateNote);
		noteAddBtn.setText(R.string.noteCreateBtnTitle);

		if (this.getIntent().getStringExtra("noteName") !=null){
			String noteName = this.getIntent().getStringExtra("noteName");
			String noteTexte = this.getIntent().getStringExtra("noteText");
			noteId=this.getIntent().getIntExtra("noteId", 1);
			modifyFlag=this.getIntent().getBooleanExtra("modifyFlag", false);
			noteTextEditText.setText(noteTexte);
			noteTitleEditText.setText(noteName);
			noteAddBtn.setText(R.string.modifyNoteBtnTitle);
		}
		

		categoryId = this.getIntent().getIntExtra("categoryId", 1);

		noteAddBtn.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				Note toCreate = new Note();
				toCreate.setTitle(noteTitleEditText.getText().toString());
				toCreate.setCategoryId(categoryId);
				toCreate.setText(noteTextEditText.getText().toString());
				toCreate.setDate(new DateTime());
				// update or add note
				if(modifyFlag==true){
					//update note
					toCreate.setId(noteId);
					MyDatabaseHelper.getInstance(context).updateNote(toCreate);


				}
				else{ 
					//Add note
					MyDatabaseHelper.getInstance(context).addNote(toCreate);
					
					Calendar calendrier = Calendar.getInstance();
					Log.i("Time","l'ann�e :"+Integer.toString(calendrier.get(Calendar.YEAR))+" et le mois "+Integer.toString(calendrier.get(Calendar.MONTH))+" et le jour "+Integer.toString(calendrier.get(Calendar.DAY_OF_MONTH)));

				}

				Intent intent = new Intent();
				setResult(2,intent);  
				finish();
			}
		});

	}
	
	
	@Override
    public void onSaveInstanceState(Bundle savedInstanceState) 
    {
    	savedInstanceState.putString(TITLE_INPUT_DATA, noteTitleEditText.getText().toString());
    	savedInstanceState.putString(TEXT_INPUT_DATA, noteTextEditText.getText().toString());
    	
    	
    	super.onSaveInstanceState(savedInstanceState);
    	
    	
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
