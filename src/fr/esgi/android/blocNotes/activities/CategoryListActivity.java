package fr.esgi.android.blocNotes.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.adapters.CategoryListAdapter;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Category;

public class CategoryListActivity extends ListActivity {

	private static final String SEARCH_INPUT_DATA = "searchInputData";
	private static final String SEARCH_BTN_DATA = "searchBtnData";
	private CategoryListAdapter categoryAdapter;
	private Button categoryButton;
	private Button categorySearchButton;
	private Intent createCategoryIntent;
	private EditText categorySearchInput;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list);
		
		context = this;

		// set title of this activity
		setTitle(R.string.categoryScreenName);
	

		categoryButton = (Button) findViewById(R.id.add_new_caterory);
		categoryButton.setText(R.string.newCategorieBtnTitle);

		// event on categoryButton
		categoryButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createCategoryIntent = new Intent(CategoryListActivity.this,
						CreateCategoryActivity.class);
				
				startActivityForResult(createCategoryIntent, 2);
			}
		});

		registerForContextMenu(getListView());
		
		categorySearchButton = (Button) findViewById(R.id.categoryBtnSearch);
		categorySearchButton.setText(R.string.searchBtnTitle);
		categorySearchInput = (EditText) findViewById(R.id.categorySearchInput);
		categorySearchInput.setHint(R.string.searchInputCategoryHint);
		
		if(savedInstanceState != null)
		{
			String searchInputSaved = savedInstanceState.getString(SEARCH_INPUT_DATA);
			categorySearchInput.setText(searchInputSaved);
			
			String searchBtnTextSaved = savedInstanceState.getString(SEARCH_BTN_DATA);
			categorySearchButton.setText(searchBtnTextSaved);
			
			if(categorySearchButton.getText().equals(getResources().getString(R.string.searchBtnTitle)))
			{
				SearchAllCategory();
				Toast.makeText(getApplicationContext(), "Les nombres d'enregistrement : "+categoryAdapter.getCount(), Toast.LENGTH_SHORT).show();
			}
			else if (categorySearchButton.getText().equals(getResources().getString(R.string.cancelBtnTitle)))
			{
				categorySearch(searchInputSaved);
				Toast.makeText(getApplicationContext(), "Les nombres d'enregistrement : "+categoryAdapter.getCount(), Toast.LENGTH_SHORT).show();
			}
		}
		
		categorySearchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String searchText = getResources().getString(R.string.searchBtnTitle);
				String searchTextCancel = getResources().getString(R.string.cancelBtnTitle);
				
				if(categorySearchButton.getText().equals(searchText))
				{
					categorySearch(categorySearchInput.getText().toString());
					categorySearchButton.setText(searchTextCancel);
					Toast.makeText(getApplicationContext(), "Les nombres d'enregistrement : "+categoryAdapter.getCount(), Toast.LENGTH_SHORT).show();
				}
				else if (categorySearchButton.getText().equals(searchTextCancel))
				{
					SearchAllCategory();
					categorySearchButton.setText(searchText);
					Toast.makeText(getApplicationContext(), "Les nombres d'enregistrement : "+categoryAdapter.getCount(), Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		
		
		
		
	}
	
	@Override
    public void onSaveInstanceState(Bundle savedInstanceState) 
    {
    	savedInstanceState.putString(SEARCH_INPUT_DATA, categorySearchInput.getText().toString());
    	savedInstanceState.putString(SEARCH_BTN_DATA, categorySearchButton.getText().toString());
    	
    	super.onSaveInstanceState(savedInstanceState);
    	
    	
    }
	
	/*@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{

	    super.onRestoreInstanceState(savedInstanceState);
	   
	    if(savedInstanceState != null)
	    {
	    	String searchInputSaved = savedInstanceState.getString(SEARCH_INPUT_DATA);
	    	categorySearchInput.setText(searchInputSaved);
		
	    	String searchBtnSaved = savedInstanceState.getString(SEARCH_BTN_DATA);
			categorySearchButton.setText(searchBtnSaved);
			
	    }
	}*/
	
	
	

	@SuppressWarnings("static-access")
	protected void SearchAllCategory() {
		categoryAdapter = new CategoryListAdapter(CategoryListActivity.this,
				MyDatabaseHelper.getInstance(context).getAllCategories());
		setListAdapter(categoryAdapter);
		
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		Category category = (Category) l.getItemAtPosition(position);
		int tagId = category.getId();

		Intent i = new Intent(CategoryListActivity.this, NoteListActivity.class);
		i.putExtra("categoryId", tagId);
		startActivityForResult(i, 2);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.category_list_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Category cat = (Category) getListAdapter().getItem(info.position);

		switch (item.getItemId()) {
		case R.id.modifyCategory:
			
			Intent modifyCategoryIntent = new Intent(CategoryListActivity.this, CreateCategoryActivity.class);
			modifyCategoryIntent.putExtra("categoryName", cat.getName());
			modifyCategoryIntent.putExtra("categoryId", cat.getId());
			modifyCategoryIntent.putExtra("modifyFlag", true);
			startActivityForResult(modifyCategoryIntent, 2);
			
			return true;
		case R.id.deleteCategory:
			deleteCategory(cat.getId());
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2) {
			categoryAdapter = new CategoryListAdapter(
					CategoryListActivity.this, MyDatabaseHelper.getInstance(context).getAllCategories());
			setListAdapter(categoryAdapter);
		}
	}

	@SuppressWarnings("static-access")
	private void deleteCategory(int idCategory) {

		MyDatabaseHelper.getInstance(context).deleteCategory(idCategory);

		categoryAdapter = new CategoryListAdapter(CategoryListActivity.this,
				MyDatabaseHelper.getInstance(context).getAllCategories());
		setListAdapter(categoryAdapter);
	}
	
	@SuppressWarnings("static-access")
	private void categorySearch(String categoryName){
		System.out.println(categoryName);
		categoryAdapter = new CategoryListAdapter(CategoryListActivity.this, MyDatabaseHelper.getInstance(context).getCategoriesForSearch(categoryName));
		setListAdapter(categoryAdapter);
	}
}
