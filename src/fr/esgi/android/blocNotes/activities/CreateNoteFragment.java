package fr.esgi.android.blocNotes.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.datas.MyDatabaseHelper;
import fr.esgi.android.blocNotes.models.Note;

public class CreateNoteFragment extends Fragment 
{

	private static final String TITLE_INPUT_DATA = "titleInputData";
	private static final String TEXT_INPUT_DATA = "textInputData";
	private EditText noteTitleEditText ,noteTextEditText;
	private boolean modifyFlag = false;
	private int categoryId;
	int noteId=0;
	private Context context;
	private TextView noteDateTextView;
	private RatingBar noteImportance;
	private float noteImportanceValue;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		context = getActivity().getApplicationContext();
		
		addListenerOnRatingBar();

		noteTitleEditText = (EditText) getActivity().findViewById(R.id.noteTitleEditText);
		noteTitleEditText.setHint(R.string.inputNoteTitleHint);
		
		noteTextEditText = (EditText) getActivity().findViewById(R.id.noteTextEditText);
		noteTextEditText.setHint(R.string.inputNoteTextHint);
		
		noteDateTextView = (TextView) getActivity().findViewById(R.id.noteDateTextViewCreate);

		if(savedInstanceState != null)
		{
			String titleInputSaved = savedInstanceState.getString(TITLE_INPUT_DATA);
			noteTitleEditText.setText(titleInputSaved);
			
			String textInputSaved = savedInstanceState.getString(TITLE_INPUT_DATA);
			noteTextEditText.setText(textInputSaved);
		}
		
		if (getArguments().getString("noteName") != null ){
			
			String noteName = getArguments().getString("noteName");
			String noteTexte = getArguments().getString("noteText");
			String noteDate = getArguments().getString("noteDate");
			
			noteId = getArguments().getInt("noteId", 1);
			
			Note noteRate = MyDatabaseHelper.getInstance(context).getNote(noteId);
			
			modifyFlag = getArguments().getBoolean("modifyFlag", false);
			
			noteTextEditText.setText(noteTexte);
			noteTitleEditText.setText(noteName);
			noteDateTextView.append(noteDate);
			noteImportance.setRating(Float.parseFloat(noteRate.getRating()));
			
		}
		categoryId = getActivity().getIntent().getIntExtra("categoryId", 1);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (container == null) {
			return null;
		}
		View view = inflater.inflate(R.layout.activity_create_note, container, false);
		return view;
	}
	
	public void addListenerOnRatingBar() {

		noteImportance = (RatingBar) getActivity().findViewById(R.id.noteRating);
	
		noteImportance.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
	
			public void onRatingChanged(RatingBar ratingBar, float rating,	boolean fromUser) {
	
				noteImportanceValue = rating;
			}
		});
	}
	
	
	@Override
    public void onSaveInstanceState(Bundle savedInstanceState) 
    {
    	savedInstanceState.putString(TITLE_INPUT_DATA, noteTitleEditText.getText().toString());
    	savedInstanceState.putString(TEXT_INPUT_DATA, noteTextEditText.getText().toString());
    	
    	
    	super.onSaveInstanceState(savedInstanceState);
    	
    	
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.note_add_menu, menu);
	    
	    MenuItem saveNote = menu.findItem(R.id.action_save_note);
	    saveNote.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    saveNote.setIcon(R.drawable.ic_done_black);
	    
	    super.onCreateOptionsMenu(menu, inflater);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_save_note:
			Note toCreate = new Note();
			toCreate.setTitle(noteTitleEditText.getText().toString());
			toCreate.setCategoryId(categoryId);
			toCreate.setText(noteTextEditText.getText().toString());
			toCreate.setRating(String.valueOf(noteImportanceValue));
			
			// update or add note
			if(modifyFlag==true){
				//update note
				toCreate.setId(noteId);
				MyDatabaseHelper.getInstance(context).updateNote(toCreate);
			}
			else{ 
				//Add note
				MyDatabaseHelper.getInstance(context).addNote(toCreate);
			}

			getFragmentManager().popBackStack();
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void myOnKeyDown(int key_code) {

		NoteListFragment noteList = new NoteListFragment();

		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.displayNoteList, noteList,
				"List_Fragment");
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
