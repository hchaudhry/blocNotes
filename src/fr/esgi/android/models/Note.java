package fr.esgi.android.models;

import java.io.Serializable;

import android.graphics.Bitmap;

public class Note implements Serializable
{
	private int id;
	private String title;
//	private String categoryName;
	private Bitmap photo;
	private String audioFileName;
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


//	public String getCategoryName() 
//	{
//		return categoryName;
//	}
//
//
//	public void setCategoryName(String categoryName) 
//	{
//		this.categoryName = categoryName;
//	}


	public Bitmap getPhoto() 
	{
		return photo;
	}


	public void setPhoto(Bitmap photo) 
	{
		this.photo = photo;
	}


	public String getAudioFileName() 
	{
		return audioFileName;
	}


	public void setAudioFileName(String audioFileName) 
	{
		this.audioFileName = audioFileName;
	}
	
	public int getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	
	
	
}
