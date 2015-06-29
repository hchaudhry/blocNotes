package fr.esgi.android.blocNotes.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Note;

public class CreateNoteActivity extends ActionBarActivity 
{


	private static final String TITLE_INPUT_DATA = "titleInputData";
	private static final String TEXT_INPUT_DATA = "textInputData";
	private EditText noteTitleEditText ,noteTextEditText;
	private boolean modifyFlag = false;
	private int categoryId;
	int noteId=0;
	private Context context;
	private TextView noteDateTextView;
	private RatingBar noteImportance;
	private float noteImportanceValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);
		
		context = this;

		//set title of this activity
		setTitle(R.string.newNoteScreenName);
		
		addListenerOnRatingBar();

		noteTitleEditText = (EditText) findViewById(R.id.noteTitleEditText);
		noteTitleEditText.setHint(R.string.inputNoteTitleHint);
		
		noteTextEditText = (EditText) findViewById(R.id.noteTextEditText);
		noteTextEditText.setHint(R.string.inputNoteTextHint);
		
		noteDateTextView = (TextView) findViewById(R.id.noteDateTextViewCreate);

		if(savedInstanceState != null)
		{
			String titleInputSaved = savedInstanceState.getString(TITLE_INPUT_DATA);
			noteTitleEditText.setText(titleInputSaved);
			
			String textInputSaved = savedInstanceState.getString(TITLE_INPUT_DATA);
			noteTextEditText.setText(textInputSaved);
		}
		
		if (this.getIntent().getStringExtra("noteName") != null ){
			
			String noteName = this.getIntent().getStringExtra("noteName");
			String noteTexte = this.getIntent().getStringExtra("noteText");
			String noteDate = this.getIntent().getStringExtra("noteDate");
			
			noteId = this.getIntent().getIntExtra("noteId", 1);
			
			Note noteRate = MyDatabaseHelper.getInstance(context).getNote(noteId);
			
			modifyFlag = this.getIntent().getBooleanExtra("modifyFlag", false);
			
			noteTextEditText.setText(noteTexte);
			noteTitleEditText.setText(noteName);
			noteDateTextView.append(noteDate);
			noteImportance.setRating(Float.parseFloat(noteRate.getRating()));
			
		}
		categoryId = this.getIntent().getIntExtra("categoryId", 1);
	}
	
	public void addListenerOnRatingBar() {

		noteImportance = (RatingBar) findViewById(R.id.noteRating);
	
		noteImportance.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
	
			public void onRatingChanged(RatingBar ratingBar, float rating,	boolean fromUser) {
	
				noteImportanceValue = rating;
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.note_add_menu, menu);
	    
	    MenuItem saveNote = menu.findItem(R.id.action_save_note);
	    saveNote.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    saveNote.setIcon(R.drawable.ic_done_black);
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_save_note:
			Note toCreate = new Note();
			toCreate.setTitle(noteTitleEditText.getText().toString());
			toCreate.setCategoryId(categoryId);
			toCreate.setText(noteTextEditText.getText().toString());
			toCreate.setRating(String.valueOf(noteImportanceValue));
			
			// update or add note
			if(modifyFlag==true){
				//update note
				toCreate.setId(noteId);
				MyDatabaseHelper.getInstance(context).updateNote(toCreate);
			}
			else{ 
				//Add note
				MyDatabaseHelper.getInstance(context).addNote(toCreate);
			}

			Intent intent = new Intent();
			setResult(2,intent);  
			finish();
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
