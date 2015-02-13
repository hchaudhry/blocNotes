package fr.esgi.android.blocNotes.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.models.Category;

public class CategoryListAdapter  extends BaseAdapter
{
	private Context context;
	private List<Category> categories;
	private LayoutInflater inflater;
	
	
	public CategoryListAdapter(Context context, List<Category> categories)
	{
		this.context = context;
		this.categories = categories;
		this.inflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public int getCount() 
	{
		return categories.size();
	}
	
	
	@Override
	public Object getItem(int position) 
	{
		return categories.get(position);
	}
	
	
	@Override
	public long getItemId(int position) 
	{
		return 0;
	}
	
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		Category tag = (Category) getItem(position);
		
		convertView = inflater.inflate(R.layout.list_view_category,null);
		
		TextView tagName = (TextView) convertView.findViewById(R.id.tagNameTextView);
		tagName.setText(tag.getName()); 
		
//		TextView tagId = (TextView) convertView.findViewById(R.id.tagIdTextView);
//		tagId.setText(String.valueOf(tag.getId()));
		
		return convertView;
	}
}
