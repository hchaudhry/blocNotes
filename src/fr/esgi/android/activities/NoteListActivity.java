package fr.esgi.android.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.talsoft.organizeme.R;

import fr.esgi.android.adapters.NoteListAdapter;
import fr.esgi.android.adapters.TagListAdapter;
import fr.esgi.android.datas.MyDatabaseHelper;
import fr.esgi.android.datas.OrganizeMeDataBase;

public class NoteListActivity extends ListActivity
{
	private NoteListAdapter noteAdapter;
	private MyDatabaseHelper db;
	private int tagId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_list);
		
		//set title of this activity
		setTitle("Mes notes");
		
		//get previous' tag's name
		String tagName = this.getIntent().getStringExtra("tagName");
		tagId = this.getIntent().getIntExtra("tagId", 1);
		
//		noteAdapter = new NoteListAdapter(NoteListActivity.this, OrganizeMeDataBase.getAllNotesWithTagName(tagName));
//		setListAdapter(noteAdapter);	
		
		db = new MyDatabaseHelper(this);
		noteAdapter = new NoteListAdapter(NoteListActivity.this,db.getAllNotesForCategory(tagId));
		setListAdapter(noteAdapter);
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.note_list_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    // Handle presses on the action bar items
	    switch (item.getItemId()) 
	    {
	        case R.id.action_add_note:
	            
	        	Intent i = new Intent(NoteListActivity.this, CreateNoteActivity.class);
	        	
	        	//get previous' tag's name
	    		String tagName = this.getIntent().getStringExtra("tagName");	
	    		
	    		//put in the intent to star activity
    			i.putExtra("tagName", tagName);
	    		i.putExtra("tagId", tagId);
    			
	    		// launch activity for create a note	    		
	        	startActivityForResult(i, 2);
	        	
//	        	finish();
	            return true;
	      
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
