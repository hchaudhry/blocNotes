package fr.esgi.android.blocNotes.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.adapters.CategoryListAdapter;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Category;

public class CategoryListFragment extends Fragment {


	private CategoryListAdapter categoryAdapter;
	private Intent createCategoryIntent;
	private Context context;
	private ListView lv;
	
	private ActionMode mActionMode;
	private int tagId;
	private Category category;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		context = getActivity().getApplicationContext();
	
		lv = (ListView) getView().findViewById(R.id.list_category);

		registerForContextMenu(lv);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Category category = (Category) parent.getItemAtPosition(position);
				tagId = category.getId();

				Intent i = new Intent(getActivity(), NoteFragmentActivity.class);
				i.putExtra("categoryId", tagId);
				startActivityForResult(i, 2);
			}
		});
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				category = (Category) parent.getItemAtPosition(position);
				tagId = category.getId();
				
				ActionBarActivity activity = (ActionBarActivity) getActivity();
				
				mActionMode = activity
						.startSupportActionMode(new ActionBarCallBack());
				return true;
			}
		});
		
		SearchAllCategory();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (container == null) {
			return null;
		}
		View view = inflater.inflate(R.layout.activity_category_list, container, false);
		return view;
	}
	
	@Override
    public void onSaveInstanceState(Bundle savedInstanceState) 
    {
//    	savedInstanceState.putString(SEARCH_INPUT_DATA, categorySearchInput.getText().toString());
//    	savedInstanceState.putString(SEARCH_BTN_DATA, categorySearchButton.getText().toString());
    	
    	super.onSaveInstanceState(savedInstanceState);
    }

	@SuppressWarnings("static-access")
	protected void SearchAllCategory() {
		categoryAdapter = new CategoryListAdapter(getActivity(),
				MyDatabaseHelper.getInstance(context).getAllCategories());
		lv.setAdapter(categoryAdapter);
		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.category_list_menu, menu);

		SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getActivity().getComponentName()));
		searchView.setSubmitButtonEnabled(true);
	    
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String arg0) {
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String arg0) {
				categorySearch(arg0);
				return true;
			}
		});

		MenuItem addCategory = menu.findItem(R.id.action_add_category);
		addCategory.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		addCategory.setIcon(R.drawable.ic_add_black);

		super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_add_category:
	        	
	        	CreateCategoryFragment categoryCreate = new CreateCategoryFragment();
	        	
	        	FragmentTransaction transaction = getFragmentManager().beginTransaction();
	        	transaction.replace(R.id.displayCategoryList, categoryCreate);
	        	transaction.addToBackStack(null);
	        	transaction.commit();
	        	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2) {
			categoryAdapter = new CategoryListAdapter(
					getActivity(), MyDatabaseHelper.getInstance(context).getAllCategories());
			lv.setAdapter(categoryAdapter);
		}
	}

	@SuppressWarnings("static-access")
	private void deleteCategory(int idCategory) {

		MyDatabaseHelper.getInstance(context).deleteCategory(idCategory);

		categoryAdapter = new CategoryListAdapter(getActivity(),
				MyDatabaseHelper.getInstance(context).getAllCategories());
		lv.setAdapter(categoryAdapter);
	}
	
	@SuppressWarnings("static-access")
	private void categorySearch(String categoryName){
		System.out.println(categoryName);
		categoryAdapter = new CategoryListAdapter(getActivity(), MyDatabaseHelper.getInstance(context).getCategoriesForSearch(categoryName));
		lv.setAdapter(categoryAdapter);
	}
	
	class ActionBarCallBack implements ActionMode.Callback {

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_delete:
				deleteCategory(tagId);
				mode.finish();
				break;
			case R.id.action_edit:
				Bundle bundle = new Bundle();
				bundle.putString("categoryName", category.getName());
				bundle.putInt("categoryId", category.getId());
				bundle.putBoolean("modifyFlag", true);
				
				CreateCategoryFragment detailFragment = new CreateCategoryFragment();
				detailFragment.setArguments(bundle);
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.displayCategoryList, detailFragment, "Detail_Fragment2");
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.addToBackStack(null);
				ft.commit();
				
				break;
			}
			return false;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.getMenuInflater().inflate(R.menu.category_select_menu, menu);
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}
	}

}
