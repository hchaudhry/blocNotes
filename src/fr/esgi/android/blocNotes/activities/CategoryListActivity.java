package fr.esgi.android.blocNotes.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.adapters.CategoryListAdapter;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Category;

public class CategoryListActivity extends ListActivity
{
	
	private CategoryListAdapter categoryAdapter;
	private Button categoryButton;
	private Intent createCategoryIntent;
	private MyDatabaseHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_list);
		
		//set title of this activity
		setTitle("Mes Catégories");
		
		db = new MyDatabaseHelper(this);
		categoryAdapter = new CategoryListAdapter(CategoryListActivity.this,db.getAllTags());
		setListAdapter(categoryAdapter);
		
		categoryButton = (Button) findViewById(R.id.add_new_caterory);
		
		//event on categoryButton
		categoryButton.setOnClickListener(new OnClickListener() 
		{	
			public void onClick(View v) 
			{
				createCategoryIntent = new Intent(CategoryListActivity.this, CreateCategoryActivity.class);
				startActivityForResult(createCategoryIntent, 2);
			}
		});
		
		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Category category = (Category) parent.getItemAtPosition(position);
				
				Intent i = new Intent(CategoryListActivity.this, CreateCategoryActivity.class);
				i.putExtra("categoryName", category.getName());
				i.putExtra("categoryId", category.getId());
				startActivityForResult(i, 2);
				
				return true;
			}
		});
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id)
	{	
		Category tag = (Category) l.getItemAtPosition(position);
		//String tagNameValue = tag.getName();
		int tagId = tag.getId();
	
		Intent i = new Intent(CategoryListActivity.this, NoteListActivity.class);
//		i.putExtra("tagName", tagNameValue);
		i.putExtra("categoryId", tagId);
		startActivityForResult(i, 2);

		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 2) {
			categoryAdapter = new CategoryListAdapter(CategoryListActivity.this,db.getAllTags());
			setListAdapter(categoryAdapter);
		}
	}
}
