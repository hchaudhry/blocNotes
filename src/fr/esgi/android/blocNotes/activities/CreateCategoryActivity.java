package fr.esgi.android.blocNotes.activities;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Category;

public class CreateCategoryActivity extends Fragment {

	private static final String NAME_INPUT_DATA = "nameInputData";
	private EditText categoryName;
	private boolean modifyFlag = false;
	int categoryIdFromList = 0;
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		context = getActivity().getApplicationContext();

		categoryName = (EditText) getActivity().findViewById(R.id.tagNameEditText);
		categoryName.setHint(R.string.inputCategoryHint);
		
		if(savedInstanceState != null)
		{
			String nameInputSaved = savedInstanceState.getString(NAME_INPUT_DATA);
			categoryName.setText(nameInputSaved);
		}
		
		if (getArguments().getString("categoryName") != null) {
			String categoryNameFromList = getArguments().getString("categoryName");
			categoryIdFromList = getArguments().getInt("categoryId", 1);
			modifyFlag = getArguments().getBoolean("modifyFlag", false);
			categoryName.setText(categoryNameFromList);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (container == null) {
			return null;
		}
		View view = inflater.inflate(R.layout.activity_create_category, container, false);
		return view;
	}
	
	
	@Override
    public void onSaveInstanceState(Bundle savedInstanceState) 
    {
    	savedInstanceState.putString(NAME_INPUT_DATA, categoryName.getText().toString());
    	super.onSaveInstanceState(savedInstanceState);
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    
	    inflater.inflate(R.menu.category_add_menu, menu);
	    
	    MenuItem saveCategory = menu.findItem(R.id.action_save_category);
	    saveCategory.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    saveCategory.setIcon(R.drawable.ic_done_black);
	    
	    super.onCreateOptionsMenu(menu, inflater);
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

			getFragmentManager().popBackStack();
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
