package fr.esgi.android.blocNotes.adapters;

import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.esgi.android.blocNotes.R;
import fr.esgi.android.blocNotes.models.Note;

public class NoteListAdapter  extends BaseAdapter
{
	private Context context;
	private List<Note> notes;
	private LayoutInflater inflater;
	
	public NoteListAdapter(Context context, List<Note> notes)
	{
		this.context = context;
		this.notes = notes;
		this.inflater = LayoutInflater.from(context); 
	}


	@Override
	public int getCount() 
	{
		return notes.size();
	}


	@Override
	public Object getItem(int position) 
	{
		return notes.get(position);
	}


	@Override
	public long getItemId(int position) 
	{
		return 0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		final Note note = (Note) getItem(position);
		
		
		convertView = inflater.inflate(R.layout.list_view_note,null);
		
		TextView noteTitle = (TextView) convertView.findViewById(R.id.noteTitleTextView);
		noteTitle.setText(context.getResources().getText(R.string.titlePrefixe) + " " + note.getTitle());
		
		TextView noteText = (TextView) convertView.findViewById(R.id.noteTextTextView);
		noteText.setText(context.getResources().getText(R.string.textPrefixe) + " " + note.getText());
		
		TextView noteDate = (TextView) convertView.findViewById(R.id.noteDateTextView);
		noteDate.setText(context.getResources().getText(R.string.dateTextPrefixe) + " " + dateTimeAsString(note.getDate()));
		;
		return convertView;
	}
	
	
	private String dateTimeAsString(DateTime dateTime)
	{
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM yyyy");
		formatter.withLocale(Locale.FRENCH);
		return formatter.print(dateTime);	
	}
}
