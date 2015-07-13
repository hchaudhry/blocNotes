package fr.esgi.android.blocNotes.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.models.Note;

public class NoteFragmentActivity extends ActionBarActivity implements NoteListFragment.OnItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.note_fragment);
		
		if (savedInstanceState == null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			NoteListFragment listFragment = new NoteListFragment();
			ft.add(R.id.displayNoteList, listFragment, "List_Fragment");
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		}
	}

	@Override
	public void onItemSelected(Note n) {
		Bundle bundle = new Bundle();
		bundle.putString("noteName", n.getTitle());
		bundle.putString("noteText", n.getText());
		bundle.putString("noteDate", n.getDate());
		bundle.putInt("noteId", n.getId());
		bundle.putBoolean("modifyFlag", true);

		CreateNoteFragment detailFragment = new CreateNoteFragment();
		detailFragment.setArguments(bundle);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.displayNoteList, detailFragment, "Detail_fragment");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		CreateNoteFragment myFragment = (CreateNoteFragment) getFragmentManager().findFragmentByTag("Detail_fragment");
		if (myFragment != null && myFragment.isVisible() && keyCode == KeyEvent.KEYCODE_BACK) {
			((CreateNoteFragment) myFragment).myOnKeyDown(keyCode);
			return false;
		}
		
		return super.onKeyDown(keyCode, event);
	}
}
