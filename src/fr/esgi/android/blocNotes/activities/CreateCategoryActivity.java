package fr.esgi.android.blocNotes.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Category;

public class CreateCategoryActivity extends Activity {

	private EditText categoryName;
	private Button categoryAdd;
	private MyDatabaseHelper db;
	private boolean modifyFlag = false;
	int categoryIdFromList = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_category);

		// set title of this activity
		setTitle(R.string.newCategoryScreenName);

		categoryName = (EditText) findViewById(R.id.tagNameEditText);
		categoryName.setHint(R.string.inputCategoryHint);
		
		categoryAdd = (Button) findViewById(R.id.categoryAdd);
		categoryAdd.setText(R.string.createBtnTitle);
		
		
		if (this.getIntent().getStringExtra("categoryName") != null) {
			String categoryNameFromList = this.getIntent().getStringExtra("categoryName");
			categoryIdFromList = this.getIntent().getIntExtra("categoryId", 1);
			modifyFlag = this.getIntent().getBooleanExtra("modifyFlag", false);
			categoryName.setText(categoryNameFromList);
			
			categoryAdd.setText(R.string.modifyBtnTitle);
		}
		
		db = new MyDatabaseHelper(this);

		categoryAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Category toCreate = new Category();
				toCreate.setName(categoryName.getText().toString());

				if (modifyFlag == true) {
					toCreate.setId(categoryIdFromList);
					// update
					db.updateCategory(toCreate);
				}
				else {
					// add task created
					db.addTag(toCreate);
				}
				
				Intent intent = new Intent();
				setResult(2,intent);  
	            finish();
			}
		});
	}

}
