package fr.esgi.android.blocNotes.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.adapters.NoteListAdapter;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Category;
import fr.esgi.android.blocNotes.models.Note;

public class NoteListActivity extends ListActivity implements OnItemSelectedListener
{
	private static final String SEARCH_INPUT_DATA = "searchInputData";
	private static final String SEARCH_BTN_DATA = "searchBtnData";
	private NoteListAdapter noteAdapter;
	private int categoryId;
	private Button noteAddButton;
	private EditText noteSearchInput;
	private Button noteSearchButton; 
	private Intent createNoteIntent;
	private Context  context;
	private Spinner triSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_list);

		//set title of this activity
		setTitle(R.string.noteScreenName);

		//get previous' categoryId
		categoryId = this.getIntent().getIntExtra("categoryId", 1);


		
		noteSearchButton = (Button) findViewById(R.id.noteBtnSearch);
		noteSearchButton.setText(R.string.noteSearchBtnTitle);
		
		noteSearchInput = (EditText) findViewById(R.id.searchNoteInput);
		noteSearchInput.setHint(R.string.searchInputNoteHint);

		noteAddButton = (Button) findViewById(R.id.add_new_note);
		noteAddButton.setText(R.string.newNoteBtnTitle);
		
		
		if(savedInstanceState != null)
		{
			String searchInputSaved = savedInstanceState.getString(SEARCH_INPUT_DATA);
			noteSearchInput.setText(searchInputSaved);
			
			String searchBtnTextSaved = savedInstanceState.getString(SEARCH_BTN_DATA);
			noteSearchButton.setText(searchBtnTextSaved);
			
			if(noteSearchButton.getText().equals(getResources().getString(R.string.searchBtnTitle)))
			{
				noteSearchForCategory();
				Toast.makeText(getApplicationContext(), "Les nombres d'enregistrement : "+noteAdapter.getCount(), Toast.LENGTH_SHORT).show();
			}
			else if (noteSearchButton.getText().equals(getResources().getString(R.string.cancelBtnTitle)))
			{
				noteSearch(searchInputSaved);
				Toast.makeText(getApplicationContext(), "Les nombres d'enregistrement : "+noteAdapter.getCount(), Toast.LENGTH_SHORT).show();
			}
		}
		
		
		
		noteSearchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(noteSearchButton.getText().equals(getResources().getString(R.string.searchBtnTitle))){
					noteSearch(noteSearchInput.getText().toString());
					noteSearchButton.setText(getResources().getString(R.string.cancelBtnTitle));
					Toast.makeText(getApplicationContext(), "Les nombres d'enregistrement : "+noteAdapter.getCount(), Toast.LENGTH_SHORT).show();
				}else if (noteSearchButton.getText().equals(getResources().getString(R.string.cancelBtnTitle))){
					noteSearchForCategory();
					noteSearchButton.setText(getResources().getString(R.string.searchBtnTitle));
					Toast.makeText(getApplicationContext(), "Les nombres d'enregistrement : "+noteAdapter.getCount(), Toast.LENGTH_SHORT).show();
				}

			}


		});

		registerForContextMenu(getListView());
		//event on categoryButton
		noteAddButton.setOnClickListener(new OnClickListener() 
		{	

			public void onClick(View v) 
			{
				createNoteIntent = new Intent(NoteListActivity.this, CreateNoteActivity.class);
				createNoteIntent.putExtra("categoryId", categoryId);
				startActivityForResult(createNoteIntent, 2);
			}
		});
		
		noteSearchForCategory();

		
		triSpinner = (Spinner) findViewById(R.id.triSpinnerNote);
		triSpinner.setOnItemSelectedListener(this);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Note not = (Note) l.getItemAtPosition(position);

		Intent modifyNoteIntent = new Intent(NoteListActivity.this, CreateNoteActivity.class);
		modifyNoteIntent.putExtra("noteName", not.getTitle());
		modifyNoteIntent.putExtra("noteText", not.getText());
		modifyNoteIntent.putExtra("noteId", not.getId());
		modifyNoteIntent.putExtra("noteDate", not.getDate());
		modifyNoteIntent.putExtra("modifyFlag", true);
		startActivityForResult(modifyNoteIntent, 2);

	}
	
	@Override
    public void onSaveInstanceState(Bundle savedInstanceState) 
    {
    	savedInstanceState.putString(SEARCH_INPUT_DATA, noteSearchInput.getText().toString());
    	savedInstanceState.putString(SEARCH_BTN_DATA, noteSearchButton.getText().toString());
    	
    	super.onSaveInstanceState(savedInstanceState);
    	
    	
    }

	@SuppressWarnings("static-access")
	protected void noteSearchForCategory(){
		noteAdapter = new NoteListAdapter(NoteListActivity.this,MyDatabaseHelper.getInstance(context).getAllNotesForCategory(categoryId));
		setListAdapter(noteAdapter);
	}

	@SuppressWarnings("static-access")
	private void noteSearch(String string) {

		noteAdapter = new NoteListAdapter(NoteListActivity.this, MyDatabaseHelper.getInstance(context).getNotesForSearch(string,categoryId));
		setListAdapter(noteAdapter);

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

	@SuppressWarnings("static-access")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2) {
			noteAdapter = new NoteListAdapter(NoteListActivity.this,MyDatabaseHelper.getInstance(context).getAllNotesForCategory(categoryId));
			setListAdapter(noteAdapter);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		Log.i("e1","");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.note_list_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Note not = (Note) getListAdapter().getItem(info.position);

		switch (item.getItemId()) {
//		case R.id.modifyListNote:
//
//			Intent modifyNoteIntent = new Intent(NoteListActivity.this, CreateNoteActivity.class);
//			modifyNoteIntent.putExtra("noteName", not.getTitle());
//			modifyNoteIntent.putExtra("noteText", not.getText());
//			modifyNoteIntent.putExtra("noteId", not.getId());
//			modifyNoteIntent.putExtra("noteDate", not.getDate());
//			modifyNoteIntent.putExtra("modifyFlag", true);
//			startActivityForResult(modifyNoteIntent, 2);
//
//			return true;
		case R.id.deleteListNote:
			deleteNote(not);
			return true;
		default:

			return super.onContextItemSelected(item);
		}
	}

	@SuppressWarnings("static-access")
	private void deleteNote(Note not) {

		MyDatabaseHelper.getInstance(context).deleteNote(not);

		noteAdapter = new NoteListAdapter(NoteListActivity.this,
				MyDatabaseHelper.getInstance(context).getAllNotesForCategory(categoryId));

		setListAdapter(noteAdapter);


	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		String item = parent.getItemAtPosition(position).toString();
		
		String date = "Date";
		String titre = "Titre";
		String rating = "Importance";
		
		if (item.equals(date)) {
			noteAdapter = new NoteListAdapter(NoteListActivity.this,
					MyDatabaseHelper.getInstance(context).getNotesOrderByDate(categoryId));

			setListAdapter(noteAdapter);
			
		}
		
		if (item.equals(titre)) {
			noteAdapter = new NoteListAdapter(NoteListActivity.this,
					MyDatabaseHelper.getInstance(context).getAllNotesForCategory(categoryId));

			setListAdapter(noteAdapter);
			
		}
		
		if (item.equals(rating)) {
			noteAdapter = new NoteListAdapter(NoteListActivity.this,
					MyDatabaseHelper.getInstance(context).getNotesOrderByRating(categoryId));

			setListAdapter(noteAdapter);
			
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
