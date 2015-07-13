package fr.esgi.android.blocNotes.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import fr.esgi.android.blocNotes.R;

public class CategoryFragmentActivity extends ActionBarActivity {

	boolean detailPage = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.category_fragment);

		if (savedInstanceState == null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			CategoryListFragment listFragment = new CategoryListFragment();
			ft.add(R.id.displayCategoryList, listFragment, "List_Fragment");
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		CreateCategoryFragment myFragment = (CreateCategoryFragment) getFragmentManager().findFragmentByTag("Detail_fragment");
		if (myFragment != null && myFragment.isVisible() && keyCode == KeyEvent.KEYCODE_BACK) {
			((CreateCategoryFragment) myFragment).myOnKeyDown(keyCode);
			return false;
		}
		
		return super.onKeyDown(keyCode, event);
	}

}
