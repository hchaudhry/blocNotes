package fr.esgi.android.activities;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.talsoft.organizeme.R;

import fr.esgi.android.adapters.TagListAdapter;
import fr.esgi.android.datas.MyDatabaseHelper;
import fr.esgi.android.datas.OrganizeMeDataBase;
import fr.esgi.android.models.Tag;

public class TagListActivity extends ListActivity
{
	
	private TagListAdapter tagAdapter;
	private Button categoryButton;
	private Intent taskListIntent;
	private MyDatabaseHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_list);
		
		//set title of this activity
		setTitle("Mes tags");
		
//		tagAdapter = new TagListAdapter(TagListActivity.this,OrganizeMeDataBase.getAllTags());
		db = new MyDatabaseHelper(this);
		tagAdapter = new TagListAdapter(TagListActivity.this,db.getAllTags());
		setListAdapter(tagAdapter);
		
		categoryButton = (Button) findViewById(R.id.add_new_caterory);
		
		//event on categoryButton
		categoryButton.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				Toast.makeText(TagListActivity.this, "categoryButton OK", Toast.LENGTH_SHORT).show();	
				taskListIntent = new Intent(TagListActivity.this, CreateTagActivity.class);
				startActivityForResult(taskListIntent, 2);
			}
		});
		
		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Tag tag = (Tag) parent.getItemAtPosition(position);
				
				Intent i = new Intent(TagListActivity.this, CreateTagActivity.class);
				i.putExtra("tagName", tag.getName());
				i.putExtra("tagId", tag.getId());
				startActivityForResult(i, 2);
				
				return true;
			}
		});
	}
	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu)
//	{
//	    // Inflate the menu items for use in the action bar
//	    MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.tag_list_activity_actions, menu);
//	    return super.onCreateOptionsMenu(menu);
//	}
	
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) 
//	{
//	    // Handle presses on the action bar items
//	    switch (item.getItemId()) 
//	    {
//	        case R.id.action_add_tag:
//	            // open activity for create a tag
//	        	startActivity(new Intent(TagListActivity.this, CreateTagActivity.class));
//	        	finish();
//	            return true;
//	      
//	        default:
//	            return super.onOptionsItemSelected(item);
//	    }
//	}
	
	
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Toast.makeText(TagListActivity.this, "onListItemClick()", Toast.LENGTH_SHORT).show();
		
		Tag tag = (Tag) l.getItemAtPosition(position);
		String tagNameValue = tag.getName();
		int tagId = tag.getId();
		
		Intent i = new Intent(TagListActivity.this, NoteListActivity.class);
		i.putExtra("tagName", tagNameValue);
		i.putExtra("tagId", tagId);
		startActivityForResult(i, 2);
		
//    	finish();
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 2) {
			tagAdapter = new TagListAdapter(TagListActivity.this,db.getAllTags());
			setListAdapter(tagAdapter);
		}
	}
}
