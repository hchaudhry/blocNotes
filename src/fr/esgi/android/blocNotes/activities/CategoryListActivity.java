package fr.esgi.android.blocNotes.activities;

import android.app.ListActivity;
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
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.adapters.CategoryListAdapter;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Category;

public class CategoryListActivity extends ListActivity {

	private CategoryListAdapter categoryAdapter;
	private Button categoryButton;
	private Button categorySearchButton;
	private Intent createCategoryIntent;
	private MyDatabaseHelper db;
	private EditText categorySearchInput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list);

		// set title of this activity
		setTitle("Mes Catégories");

		db = new MyDatabaseHelper(this);
		categoryAdapter = new CategoryListAdapter(CategoryListActivity.this,
				db.getAllCategories());
		setListAdapter(categoryAdapter);

		categoryButton = (Button) findViewById(R.id.add_new_caterory);

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
		categorySearchInput = (EditText) findViewById(R.id.categorySearchInput);
		
		categorySearchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				categorySearch(categorySearchInput.getText().toString());
				
			}
		});
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		Category tag = (Category) l.getItemAtPosition(position);
		// String tagNameValue = tag.getName();
		int tagId = tag.getId();

		Intent i = new Intent(CategoryListActivity.this, NoteListActivity.class);
		// i.putExtra("tagName", tagNameValue);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2) {
			categoryAdapter = new CategoryListAdapter(
					CategoryListActivity.this, db.getAllCategories());
			setListAdapter(categoryAdapter);
		}
	}

	private void deleteCategory(int idCategory) {

		db.deleteCategory(idCategory);

		categoryAdapter = new CategoryListAdapter(CategoryListActivity.this,
				db.getAllCategories());
		setListAdapter(categoryAdapter);
	}
	
	private void categorySearch(String categoryName){
		
		categoryAdapter = new CategoryListAdapter(CategoryListActivity.this, db.getCategoriesForSearch(categoryName));
		setListAdapter(categoryAdapter);
	}
}
