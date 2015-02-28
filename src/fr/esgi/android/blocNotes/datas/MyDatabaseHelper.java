package fr.esgi.android.blocNotes.datas;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import fr.esgi.android.blocNotes.models.Category;
import fr.esgi.android.blocNotes.models.Note;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	
	
	private static  MyDatabaseHelper instance;
	private static Context ctx;

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Notes.db";

	private static final String TABLE_CATEGORIES = "categories";
	private static final String KEY_ID_CATEGORY = "id";
	private static final String KEY_NAME_CATEGORY = "name";
	private static final String[] CATEGORIES_COLUMNS = { KEY_ID_CATEGORY,
		KEY_NAME_CATEGORY };

	private static final String TABLE_NOTES = "notes";
	private static final String KEY_ID_NOTES = "id";
	private static final String KEY_TITLE_NOTES = "title";
	private static final String KEY_TEXT_NOTES = "text";
	private static final String KEY_DATE_NOTES = "date";
	private static final String KEY_RATING_NOTES = "rating";
	private static final String KEY_ID_CATEGORY_NOTES = "categoryId";
	private static final String[] NOTES_COLUMNS = { KEY_ID_NOTES,
		KEY_TITLE_NOTES,KEY_TEXT_NOTES, KEY_DATE_NOTES, KEY_RATING_NOTES, KEY_ID_CATEGORY_NOTES };

	private MyDatabaseHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		ctx = context;
	}
	
	public static MyDatabaseHelper getInstance(Context context)
	{	
		if (instance == null)
		{
			synchronized (MyDatabaseHelper.class) 
			{
				instance = new MyDatabaseHelper(context);
			}
		}
		
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES
				+ " ( " + KEY_ID_CATEGORY
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME_CATEGORY
				+ " TEXT )";

		String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + " ( "
				+ KEY_ID_NOTES + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_TITLE_NOTES + " TEXT," + KEY_TEXT_NOTES + " TEXT,"
				+ KEY_DATE_NOTES + " TEXT," + KEY_RATING_NOTES + " TEXT,"
				+ KEY_ID_CATEGORY_NOTES + " INTEGER, " + " FOREIGN KEY ( "
				+ KEY_ID_CATEGORY_NOTES + " ) REFERENCES " + TABLE_CATEGORIES
				+ " ( " + KEY_ID_CATEGORY + " ))";

		db.execSQL(CREATE_CATEGORIES_TABLE);
		db.execSQL(CREATE_NOTES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS categories");
		db.execSQL("DROP TABLE IF EXISTS notes");
		this.onCreate(db);
	}

	// CRUD Category

	public static void addTag(Category category) {
		//SQLiteDatabase db = this.getWritableDatabase();

		SQLiteDatabase db = getInstance(ctx).getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_NAME_CATEGORY, category.getName());

		db.insert(TABLE_CATEGORIES, null, values);

		db.close();
	}

	public static Category getCategory(int id) {
		//SQLiteDatabase db = this.getReadableDatabase();
		SQLiteDatabase db = getInstance(ctx).getReadableDatabase();

		Cursor cursor = db.query(TABLE_CATEGORIES, CATEGORIES_COLUMNS,
				" id = ?", new String[] { String.valueOf(id) }, null, null,
				null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Category category = new Category();
		category.setId(Integer.parseInt(cursor.getString(0)));
		category.setName(cursor.getString(1));

		return category;
	}

	public static List<Category> getAllCategories() {
		List<Category> categories = new LinkedList<Category>();

		String query = "SELECT  * FROM " + TABLE_CATEGORIES +" ORDER BY name ";

		//SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = getInstance(ctx).getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);

		Category category = null;
		if (cursor.moveToFirst()) {
			do {
				category = new Category();
				category.setId(Integer.parseInt(cursor.getString(0)));
				category.setName(cursor.getString(1));

				categories.add(category);
			} while (cursor.moveToNext());
		}

		return categories;
	}

	public static int updateCategory(Category category) {
		//SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = getInstance(ctx).getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("name", category.getName());

		int i = db.update(TABLE_CATEGORIES, values, KEY_ID_CATEGORY + " = ?",
				new String[] { String.valueOf(category.getId()) });

		db.close();

		return i;

	}

	public static void deleteCategory(int idCategory) {
		//SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = getInstance(ctx).getWritableDatabase();

		// Notes
		db.delete(TABLE_NOTES, KEY_ID_CATEGORY_NOTES + " = ?",
				new String[] { String.valueOf(idCategory) });
		// Category
		db.delete(TABLE_CATEGORIES, KEY_ID_CATEGORY + " = ?",
				new String[] { String.valueOf(idCategory) });
		db.close();

	}

	public static List<Category> getCategoriesForSearch(String categoryName) {
		List<Category> categories = new LinkedList<Category>();

		String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE "+KEY_NAME_CATEGORY+" LIKE '%" + categoryName + "%'";

		//SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = getInstance(ctx).getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);

		Category category = null;
		if (cursor.moveToFirst()) {
			do {
				category = new Category();
				category.setId(Integer.parseInt(cursor.getString(0)));
				category.setName(cursor.getString(1));
				

				categories.add(category);
			} while (cursor.moveToNext());
		}

		return categories;
	}
	// CRUD Notes

	public static void addNote(Note note) {
		//SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = getInstance(ctx).getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE_NOTES, note.getTitle());
		values.put(KEY_TEXT_NOTES,note.getText());

		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat day = new java.text.SimpleDateFormat("dd");
		SimpleDateFormat month = new java.text.SimpleDateFormat("MM");
		SimpleDateFormat year = new java.text.SimpleDateFormat("yyyy");
		
		values.put(KEY_DATE_NOTES, "" + day.format(rightNow.getTime()) + "/" + month.format(rightNow.getTime()) + "/" + year.format(rightNow.getTime()) +"" );
		
		values.put(KEY_RATING_NOTES, note.getRating());
		
		values.put(KEY_ID_CATEGORY_NOTES, note.getCategoryId());

		db.insert(TABLE_NOTES, null, values);

		db.close();
	}

	public static Note getNote(int id) {
		//SQLiteDatabase db = this.getReadableDatabase();
		SQLiteDatabase db = getInstance(ctx).getReadableDatabase();

		Cursor cursor = db.query(TABLE_NOTES, NOTES_COLUMNS, " id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Note note = new Note();
		note.setId(Integer.parseInt(cursor.getString(0)));
		note.setTitle(cursor.getString(1));
		note.setText(cursor.getString(2));
		note.setRating(cursor.getString(4));
		
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat day = new java.text.SimpleDateFormat("dd");
		SimpleDateFormat month = new java.text.SimpleDateFormat("MM");
		SimpleDateFormat year = new java.text.SimpleDateFormat("yyyy");
		note.setDate(day.format(rightNow.getTime()) + "/" + month.format(rightNow.getTime()) + "/" + year.format(rightNow.getTime()));
		
		return note;
	}
	public static List<Note> getAllNotesForCategory(int categoryId) {
		List<Note> notes = new LinkedList<Note>();

		//SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = getInstance(ctx).getWritableDatabase();

		Cursor cursor = db.query(TABLE_NOTES, NOTES_COLUMNS, " categoryId = ?",
				new String[] { String.valueOf(categoryId) }, null, null, KEY_TITLE_NOTES);

		Note note = null;
		if (cursor.moveToFirst()) {
			do {
				note = new Note();
				note.setId(Integer.parseInt(cursor.getString(0)));
				note.setTitle(cursor.getString(1));
				note.setText(cursor.getString(2));
				note.setDate(cursor.getString(3));
				notes.add(note);
			} while (cursor.moveToNext());
		}

		return notes;
	}

	public static int updateNote(Note note) {

		//SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = getInstance(ctx).getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE_NOTES, note.getTitle());
		values.put(KEY_TEXT_NOTES, note.getText());
		
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat day = new java.text.SimpleDateFormat("dd");
		SimpleDateFormat month = new java.text.SimpleDateFormat("MM");
		SimpleDateFormat year = new java.text.SimpleDateFormat("yyyy");
		
		values.put(KEY_DATE_NOTES, "" + day.format(rightNow.getTime()) + "/" + month.format(rightNow.getTime()) + "/" + year.format(rightNow.getTime()) +"" );
		
		
		int i = db.update(TABLE_NOTES, values, KEY_ID_NOTES + " = ?",
				new String[] { String.valueOf(note.getId()) });

		db.close();

		return i;

	}

	public static void deleteNote(Note note) {

		//SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = getInstance(ctx).getWritableDatabase();
		
		db.delete(TABLE_NOTES, KEY_ID_NOTES + " = ?",
				new String[] { String.valueOf(note.getId()) });


		db.close();
	}

	public static List<Note> getNotesForSearch(String titleNote, int categoryId) {
		List<Note> notes = new LinkedList<Note>();

		String query = "SELECT  * FROM " + TABLE_NOTES + " where " +KEY_ID_CATEGORY_NOTES+" = "+categoryId+""
				+ " AND "+KEY_TITLE_NOTES+" LIKE '%" + titleNote + "%' ORDER BY "+KEY_TITLE_NOTES;

		//SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = getInstance(ctx).getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);

		Note note = null;
		if (cursor.moveToFirst()) {
			do {
				note = new Note();
				note.setId(Integer.parseInt(cursor.getString(0)));
				note.setTitle(cursor.getString(1));
				note.setText(cursor.getString(2));

				notes.add(note);
			} while (cursor.moveToNext());
		}

		return notes;
	}
	
	public static List<Note> getNotesOrderByDate(int categoryId) {
		List<Note> notes = new LinkedList<Note>();

		String query = "SELECT  * FROM " + TABLE_NOTES + " where " +KEY_ID_CATEGORY_NOTES+" = "+categoryId+""
				+ " ORDER BY " + KEY_DATE_NOTES;

		//SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = getInstance(ctx).getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);

		Note note = null;
		if (cursor.moveToFirst()) {
			do {
				note = new Note();
				note.setId(Integer.parseInt(cursor.getString(0)));
				note.setTitle(cursor.getString(1));
				note.setText(cursor.getString(2));
				note.setDate(cursor.getString(3));
				note.setRating(cursor.getString(4));

				notes.add(note);
			} while (cursor.moveToNext());
		}

		return notes;
	}
	
	public static List<Note> getNotesOrderByRating(int categoryId) {
		List<Note> notes = new LinkedList<Note>();

		String query = "SELECT  * FROM " + TABLE_NOTES + " where " +KEY_ID_CATEGORY_NOTES+" = "+categoryId+""
				+ " ORDER BY " + KEY_RATING_NOTES + " DESC";

		//SQLiteDatabase db = this.getWritableDatabase();
		SQLiteDatabase db = getInstance(ctx).getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);

		Note note = null;
		if (cursor.moveToFirst()) {
			do {
				note = new Note();
				note.setId(Integer.parseInt(cursor.getString(0)));
				note.setTitle(cursor.getString(1));
				note.setText(cursor.getString(2));
				note.setDate(cursor.getString(3));
				note.setRating(cursor.getString(4));

				notes.add(note);
			} while (cursor.moveToNext());
		}

		return notes;
	}


}
