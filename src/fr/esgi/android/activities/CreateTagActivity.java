package fr.esgi.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.talsoft.organizeme.R;

import fr.esgi.android.datas.MyDatabaseHelper;
import fr.esgi.android.models.Tag;

public class CreateTagActivity extends Activity {

	private EditText tagName;
	private Button categoryAdd;
	private Button categoryEdit;
	private Button categoryDelete;
	private MyDatabaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_tag);

		// set title of this activity
		setTitle("Catégorie");

		tagName = (EditText) findViewById(R.id.tagNameEditText);
		
		if (this.getIntent().getStringExtra("tagName") != null) {
			String tagNameString = this.getIntent().getStringExtra("tagName");
			tagName.setText(tagNameString);
		}
		

		categoryAdd = (Button) findViewById(R.id.categoryAdd);
		categoryEdit = (Button) findViewById(R.id.categoryEdit);
		categoryDelete = (Button) findViewById(R.id.categoryDelete);
		
		db = new MyDatabaseHelper(this);

		categoryAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Tag toCreate = new Tag();
				toCreate.setName(tagName.getText().toString());

				// add task created
				// OrganizeMeDataBase.addTag(toCreate);
				db.addTag(toCreate);
				
				Intent intent = new Intent();
				setResult(2,intent);  
	            finish();
			}
		});
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// // Inflate the menu items for use in the action bar
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.create_tag_activity_action, menu);
	// return super.onCreateOptionsMenu(menu);
	// }

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle presses on the action bar items
//		switch (item.getItemId()) {
//		case R.id.action_create_tag:
//
//			// create the task
//			Tag toCreate = new Tag();
//
//			// set fields
//			toCreate.setName(tagName.getText().toString());
//
//			// add task created
//			// OrganizeMeDataBase.addTag(toCreate);
//			MyDatabaseHelper db = new MyDatabaseHelper(this);
//			db.addTag(toCreate);
//
//			// TODO: this line might genrate a bug, activity is started but not
//			// stopped TO CHECK
//			startActivity(new Intent(CreateTagActivity.this,
//					TagListActivity.class));
//
//			finish();
//			return true;
//
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
}
