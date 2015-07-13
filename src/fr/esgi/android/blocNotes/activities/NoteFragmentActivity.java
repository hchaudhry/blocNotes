package fr.esgi.android.blocNotes.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.models.Note;

public class NoteFragmentActivity extends ActionBarActivity implements NoteListActivity.OnItemSelectedListener {

	private boolean detailPage = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.note_fragment);
		
		if (savedInstanceState == null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			NoteListActivity listFragment = new NoteListActivity();
			ft.add(R.id.displayNoteList, listFragment, "List_Fragment");
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		}
		
		/*if (findViewById(R.id.displayNoteDetails) != null) {
			detailPage = true;
			getFragmentManager().popBackStack();

			CreateNoteActivity detailFragment = (CreateNoteActivity) getFragmentManager().findFragmentById(R.id.displayNoteDetails);
			
			if (detailFragment == null) {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				detailFragment = new CreateNoteActivity();
				ft.replace(R.id.displayNoteDetails, detailFragment, "Detail_Fragment1");
				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}
		}*/
	}

	@Override
	public void onItemSelected(Note n) {
		
		/*if (detailPage) {
			CreateNoteActivity detailFragment = (CreateNoteActivity) getFragmentManager()
					.findFragmentById(R.id.displayNoteDetails);
		} else {*/
			Bundle bundle = new Bundle();
			bundle.putString("noteName", n.getTitle());
			bundle.putString("noteText", n.getText());
			bundle.putString("noteDate", n.getDate());
			bundle.putInt("noteId", n.getId());
			bundle.putBoolean("modifyFlag", true);
			
			CreateNoteActivity detailFragment = new CreateNoteActivity();
			detailFragment.setArguments(bundle);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.displayNoteList, detailFragment, "Detail_Fragment2");
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(null);
			ft.commit();
//		}
	}
}
