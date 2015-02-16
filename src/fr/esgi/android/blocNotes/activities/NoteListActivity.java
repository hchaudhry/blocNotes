package fr.esgi.android.blocNotes.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.adapters.NoteListAdapter;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Category;
import fr.esgi.android.blocNotes.models.Note;

public class NoteListActivity extends ListActivity
{
	private NoteListAdapter noteAdapter;
	private MyDatabaseHelper db;
	private int categoryId;
	private Button noteButton;
	private Intent createNoteIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_list);

		//set title of this activity
		setTitle("Mes notes");

		//get previous' tag's name
		//String tagName = this.getIntent().getStringExtra("tagName");
		categoryId = this.getIntent().getIntExtra("categoryId", 1);

		db = new MyDatabaseHelper(this);
		noteAdapter = new NoteListAdapter(NoteListActivity.this,db.getAllNotesForCategory(categoryId));
		setListAdapter(noteAdapter);

		noteButton = (Button) findViewById(R.id.add_new_note);


		registerForContextMenu(getListView());
		//event on categoryButton
		noteButton.setOnClickListener(new OnClickListener() 
		{	

			public void onClick(View v) 
			{
				createNoteIntent = new Intent(NoteListActivity.this, CreateNoteActivity.class);
				createNoteIntent.putExtra("categoryId", categoryId);
				startActivityForResult(createNoteIntent, 2);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2) {
			noteAdapter = new NoteListAdapter(NoteListActivity.this,db.getAllNotesForCategory(categoryId));
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
		case R.id.modifyListNote:
			Log.i("modifier","11111");
			Intent modifyNoteIntent = new Intent(NoteListActivity.this, CreateNoteActivity.class);
			modifyNoteIntent.putExtra("noteName", not.getTitle());
			modifyNoteIntent.putExtra("noteId", not.getId());
			modifyNoteIntent.putExtra("modifyFlag", true);
			startActivityForResult(modifyNoteIntent, 2);

			return true;
		case R.id.deleteListNote:
			Log.i("delete","");
			deleteNote(not);
			Log.i("e1","");
			return true;
		default:
			Log.i("default","");
			return super.onContextItemSelected(item);
		}
	}

	private void deleteNote(Note not) {

		db.deleteNote(not);
		Log.i("e1","");
		noteAdapter = new NoteListAdapter(NoteListActivity.this,
				db.getAllNotesForCategory(not.getCategoryId()));
		Log.i("e1","");
		setListAdapter(noteAdapter);

		Log.i("e1","");

	}




}
