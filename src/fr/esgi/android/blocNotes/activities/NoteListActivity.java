package fr.esgi.android.blocNotes.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.activities.CategoryListActivity.ActionBarCallBack;
import fr.esgi.android.blocNotes.adapters.NoteListAdapter;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Category;
import fr.esgi.android.blocNotes.models.Note;

public class NoteListActivity extends ActionBarActivity implements OnItemSelectedListener
{
//	private static final String SEARCH_INPUT_DATA = "searchInputData";
//	private static final String SEARCH_BTN_DATA = "searchBtnData";
	private NoteListAdapter noteAdapter;
	private int categoryId;
//	private Button noteAddButton;
//	private EditText noteSearchInput;
//	private Button noteSearchButton; 
	private Intent createNoteIntent;
	private Context  context;
	private Spinner triSpinner;
	
	private ListView lv;
	private Note note;
	private ActionMode mActionMode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_list);

		//set title of this activity
		setTitle(R.string.noteScreenName);

		//get previous' categoryId
		categoryId = this.getIntent().getIntExtra("categoryId", 1);

		lv = (ListView) findViewById(R.id.note_list);
		
		registerForContextMenu(lv);
		
		noteSearchForCategory();

		triSpinner = (Spinner) findViewById(R.id.triSpinnerNote);
		triSpinner.setOnItemSelectedListener(this);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Note not = (Note) parent.getItemAtPosition(position);

				Intent modifyNoteIntent = new Intent(NoteListActivity.this, CreateNoteActivity.class);
				modifyNoteIntent.putExtra("noteName", not.getTitle());
				modifyNoteIntent.putExtra("noteText", not.getText());
				modifyNoteIntent.putExtra("noteId", not.getId());
				modifyNoteIntent.putExtra("noteDate", not.getDate());
				modifyNoteIntent.putExtra("modifyFlag", true);
				startActivityForResult(modifyNoteIntent, 2);
			}
		});
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				note = (Note) parent.getItemAtPosition(position);

				mActionMode = NoteListActivity.this
						.startSupportActionMode(new ActionBarCallBack());
				return true;
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.note_list_menu, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search_note)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setSubmitButtonEnabled(true);
	    
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String arg0) {
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				noteSearch(arg0);
				return true;
			}
		});

		MenuItem addCategory = menu.findItem(R.id.action_add_note);
		addCategory.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		addCategory.setIcon(R.drawable.ic_add_black);

		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_add_note:
	        	createNoteIntent = new Intent(NoteListActivity.this, CreateNoteActivity.class);
				createNoteIntent.putExtra("categoryId", categoryId);
				startActivityForResult(createNoteIntent, 2);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@SuppressWarnings("static-access")
	protected void noteSearchForCategory(){
		noteAdapter = new NoteListAdapter(NoteListActivity.this,MyDatabaseHelper.getInstance(context).getAllNotesForCategory(categoryId));
		lv.setAdapter(noteAdapter);
	}

	@SuppressWarnings("static-access")
	private void noteSearch(String string) {

		noteAdapter = new NoteListAdapter(NoteListActivity.this, MyDatabaseHelper.getInstance(context).getNotesForSearch(string,categoryId));
		lv.setAdapter(noteAdapter);

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
			lv.setAdapter(noteAdapter);
		}
	}

	@SuppressWarnings("static-access")
	private void deleteNote(Note not) {

		MyDatabaseHelper.getInstance(context).deleteNote(not);

		noteAdapter = new NoteListAdapter(NoteListActivity.this,
				MyDatabaseHelper.getInstance(context).getAllNotesForCategory(categoryId));

		lv.setAdapter(noteAdapter);
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

			lv.setAdapter(noteAdapter);
			
		}
		
		if (item.equals(titre)) {
			noteAdapter = new NoteListAdapter(NoteListActivity.this,
					MyDatabaseHelper.getInstance(context).getAllNotesForCategory(categoryId));

			lv.setAdapter(noteAdapter);
			
		}
		
		if (item.equals(rating)) {
			noteAdapter = new NoteListAdapter(NoteListActivity.this,
					MyDatabaseHelper.getInstance(context).getNotesOrderByRating(categoryId));

			lv.setAdapter(noteAdapter);
			
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
	
	class ActionBarCallBack implements ActionMode.Callback {

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_delete_note:
				deleteNote(note);
				mode.finish();
				break;
			}
			return false;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.getMenuInflater().inflate(R.menu.note_select_menu, menu);
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}
	}
}
