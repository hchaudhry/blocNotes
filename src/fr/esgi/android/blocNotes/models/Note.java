package fr.esgi.android.blocNotes.models;


public class Note
{
	private int id;
	private String title;
	private String text;
	private String date;

	private int categoryId;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() 
	{
		return title;
	}

	
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public String getText() 
	{
		return text;
	}

	
	public void setText(String text) 
	{
		this.text = text;
	}
	
	public int getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}
}
