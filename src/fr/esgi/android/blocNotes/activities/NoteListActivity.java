package fr.esgi.android.blocNotes.activities;

import android.app.Activity;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.adapters.NoteListAdapter;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Note;

public class NoteListActivity extends Fragment implements OnItemSelectedListener
{

	private NoteListAdapter noteAdapter;
	private int categoryId; 
	private Intent createNoteIntent;
	private Context  context;
	private Spinner triSpinner;
	
	private ListView lv;
	private Note note;
	private ActionMode mActionMode;
	
	private OnItemSelectedListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		registerForContextMenu(lv);
		noteSearchForCategory();
		
		triSpinner.setOnItemSelectedListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (container == null) {
			return null;
		}
		View view = inflater.inflate(R.layout.activity_note_list, container, false);
		
		context = getActivity().getApplicationContext();

		//get previous' categoryId
		categoryId = getActivity().getIntent().getIntExtra("categoryId", 1);
		
		lv = (ListView) view.findViewById(R.id.note_list);

		triSpinner = (Spinner) view.findViewById(R.id.triSpinnerNote);
		
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Note not = (Note) parent.getItemAtPosition(position);

				listener.onItemSelected(not);

			}
		});
		
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				note = (Note) parent.getItemAtPosition(position);

				ActionBarActivity activity = (ActionBarActivity) getActivity();
				mActionMode = activity
						.startSupportActionMode(new ActionBarCallBack());
				return true;
			}
		});
		
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		
		super.onCreateOptionsMenu(menu, inflater);
		
		inflater.inflate(R.menu.note_list_menu, menu);

		SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search_note)
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
				noteSearch(arg0);
				return true;
			}
		});

		MenuItem addCategory = menu.findItem(R.id.action_add_note);
		addCategory.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		addCategory.setIcon(R.drawable.ic_add_black);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_add_note:
	        	
	        	CreateNoteActivity noteCreate = new CreateNoteActivity();
	        	
	        	Bundle bundle = new Bundle();
	        	bundle.putInt("categoryId", categoryId);
	        	noteCreate.setArguments(bundle);
	        	
	        	FragmentTransaction transaction = getFragmentManager().beginTransaction();
	        	transaction.replace(R.id.displayNoteList, noteCreate);
	        	transaction.addToBackStack(null);
	        	transaction.commit();
	        	
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@SuppressWarnings("static-access")
	protected void noteSearchForCategory(){
		noteAdapter = new NoteListAdapter(getActivity(), MyDatabaseHelper.getInstance(context).getAllNotesForCategory(categoryId));
		lv.setAdapter(noteAdapter);
	}

	@SuppressWarnings("static-access")
	private void noteSearch(String string) {

		noteAdapter = new NoteListAdapter(getActivity(), MyDatabaseHelper.getInstance(context).getNotesForSearch(string,categoryId));
		lv.setAdapter(noteAdapter);

	}

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			setResult(2,intent);  
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}*/

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2) {
			noteAdapter = new NoteListAdapter(getActivity(), MyDatabaseHelper.getInstance(context).getAllNotesForCategory(categoryId));
			lv.setAdapter(noteAdapter);
		}
	}

	@SuppressWarnings("static-access")
	private void deleteNote(Note not) {

		MyDatabaseHelper.getInstance(context).deleteNote(not);

		noteAdapter = new NoteListAdapter(getActivity(),
				MyDatabaseHelper.getInstance(context).getAllNotesForCategory(categoryId));

		lv.setAdapter(noteAdapter);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		String item = parent.getItemAtPosition(position).toString();
		
		String date = "Date";
		String titre = "Titre";
		String rating = "Importance";
		
		if (item.equals(date)) {
			noteAdapter = new NoteListAdapter(getActivity(),
					MyDatabaseHelper.getInstance(context).getNotesOrderByDate(categoryId));

			lv.setAdapter(noteAdapter);
			
		}
		
		if (item.equals(titre)) {
			noteAdapter = new NoteListAdapter(getActivity(),
					MyDatabaseHelper.getInstance(context).getAllNotesForCategory(categoryId));

			lv.setAdapter(noteAdapter);
			
		}
		
		if (item.equals(rating)) {
			noteAdapter = new NoteListAdapter(getActivity(),
					MyDatabaseHelper.getInstance(context).getNotesOrderByRating(categoryId));

			lv.setAdapter(noteAdapter);
			
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
	
	class ActionBarCallBack implements ActionMode.Callback {

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_delete_note:
				deleteNote(note);
				mode.finish();
				break;
			}
			return false;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.getMenuInflater().inflate(R.menu.note_select_menu, menu);
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
	
	public interface OnItemSelectedListener {
		public void onItemSelected(Note n);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnItemSelectedListener) activity;
	    } catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString() + " must implement OnItemSelectedListener");
	    }
	}
	
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		lv.setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}
}
