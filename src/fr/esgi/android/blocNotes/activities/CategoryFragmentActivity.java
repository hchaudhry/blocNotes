package fr.esgi.android.blocNotes.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

}
