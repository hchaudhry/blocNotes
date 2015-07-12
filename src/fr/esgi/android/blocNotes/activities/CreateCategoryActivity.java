package fr.esgi.android.blocNotes.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Category;

public class CreateCategoryActivity extends ActionBarActivity {

	private static final String NAME_INPUT_DATA = "nameInputData";
	private EditText categoryName;
	private boolean modifyFlag = false;
	int categoryIdFromList = 0;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_category);
		
		context = this;

		// set title of this activity
		setTitle(R.string.newCategoryScreenName);

		categoryName = (EditText) findViewById(R.id.tagNameEditText);
		categoryName.setHint(R.string.inputCategoryHint);
		
		if(savedInstanceState != null)
		{
			String nameInputSaved = savedInstanceState.getString(NAME_INPUT_DATA);
			categoryName.setText(nameInputSaved);
		}
		
		if (this.getIntent().getStringExtra("categoryName") != null) {
			String categoryNameFromList = this.getIntent().getStringExtra("categoryName");
			categoryIdFromList = this.getIntent().getIntExtra("categoryId", 1);
			modifyFlag = this.getIntent().getBooleanExtra("modifyFlag", false);
			categoryName.setText(categoryNameFromList);
		}
	}
	
	
	@Override
    public void onSaveInstanceState(Bundle savedInstanceState) 
    {
    	savedInstanceState.putString(NAME_INPUT_DATA, categoryName.getText().toString());
    	super.onSaveInstanceState(savedInstanceState);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.category_add_menu, menu);
	    
	    MenuItem saveCategory = menu.findItem(R.id.action_save_category);
	    saveCategory.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    saveCategory.setIcon(R.drawable.ic_done_black);
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_save_category:
			Category toCreate = new Category();
			toCreate.setName(categoryName.getText().toString());

			if (modifyFlag == true) {
				toCreate.setId(categoryIdFromList);
				// update
				MyDatabaseHelper.getInstance(context).updateCategory(toCreate);
			} else {
				// add task created
				MyDatabaseHelper.getInstance(context).addTag(toCreate);
			}

			Intent intent = new Intent();
			setResult(2, intent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
