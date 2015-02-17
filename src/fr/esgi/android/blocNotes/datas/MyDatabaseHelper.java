package fr.esgi.android.blocNotes.datas;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fr.esgi.android.blocNotes.models.Category;
import fr.esgi.android.blocNotes.models.Note;

public class MyDatabaseHelper extends SQLiteOpenHelper {

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
	private static final String KEY_ID_CATEGORY_NOTES = "categoryId";
	private static final String[] NOTES_COLUMNS = { KEY_ID_NOTES,
		KEY_TITLE_NOTES, KEY_ID_CATEGORY_NOTES };

	public MyDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES
				+ " ( " + KEY_ID_CATEGORY
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME_CATEGORY
				+ " TEXT )";

		String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + " ( "
				+ KEY_ID_NOTES + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_TITLE_NOTES + " TEXT," + KEY_ID_CATEGORY_NOTES
				+ " INTEGER, " + " FOREIGN KEY ( " + KEY_ID_CATEGORY_NOTES
				+ " ) REFERENCES " + TABLE_CATEGORIES + " ( " + KEY_ID_CATEGORY
				+ " ))";

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

	public void addTag(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME_CATEGORY, category.getName());

		db.insert(TABLE_CATEGORIES, null, values);

		db.close();
	}

	public Category getCategory(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

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

	public List<Category> getAllCategories() {
		List<Category> categories = new LinkedList<Category>();

		String query = "SELECT  * FROM " + TABLE_CATEGORIES +" ORDER BY name ";

		SQLiteDatabase db = this.getWritableDatabase();
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

	public int updateCategory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("name", category.getName());

		int i = db.update(TABLE_CATEGORIES, values, KEY_ID_CATEGORY + " = ?",
				new String[] { String.valueOf(category.getId()) });

		db.close();

		return i;

	}

	public void deleteCategory(int idCategory) {
		SQLiteDatabase db = this.getWritableDatabase();

		// Notes
		db.delete(TABLE_NOTES, KEY_ID_CATEGORY_NOTES + " = ?",
				new String[] { String.valueOf(idCategory) });
		// Category
		db.delete(TABLE_CATEGORIES, KEY_ID_CATEGORY + " = ?",
				new String[] { String.valueOf(idCategory) });
		db.close();

	}

	public List<Category> getCategoriesForSearch(String categoryName) {
		List<Category> categories = new LinkedList<Category>();

		String query = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE "+KEY_NAME_CATEGORY+" LIKE '%" + categoryName + "%'";

		SQLiteDatabase db = this.getWritableDatabase();
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

	public void addNote(Note note) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE_NOTES, note.getTitle());
		values.put(KEY_ID_CATEGORY_NOTES, note.getCategoryId());

		db.insert(TABLE_NOTES, null, values);

		db.close();
	}

	public Note getNote(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NOTES, NOTES_COLUMNS, " id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Note note = new Note();
		note.setId(Integer.parseInt(cursor.getString(0)));
		note.setTitle(cursor.getString(1));

		return note;
	}
	public List<Note> getAllNotesForCategory(int categoryId) {
		List<Note> notes = new LinkedList<Note>();

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.query(TABLE_NOTES, NOTES_COLUMNS, " categoryId = ?",
				new String[] { String.valueOf(categoryId) }, null, null, KEY_TITLE_NOTES);

		Note note = null;
		if (cursor.moveToFirst()) {
			do {
				note = new Note();
				note.setId(Integer.parseInt(cursor.getString(0)));
				note.setTitle(cursor.getString(1));

				notes.add(note);
			} while (cursor.moveToNext());
		}

		return notes;
	}

	public int updateNote(Note note) {

		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("title", note.getTitle());

		int i = db.update(TABLE_NOTES, values, KEY_ID_NOTES + " = ?",
				new String[] { String.valueOf(note.getId()) });

		db.close();

		return i;

	}

	public void deleteNote(Note note) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTES, KEY_ID_NOTES + " = ?",
				new String[] { String.valueOf(note.getId()) });


		db.close();
	}

	public List<Note> getNotesForSearch(String titleNote, int categoryId) {
		List<Note> notes = new LinkedList<Note>();

		String query = "SELECT  * FROM " + TABLE_NOTES + " where " +KEY_ID_CATEGORY_NOTES+" = "+categoryId+""
				+ " AND "+KEY_TITLE_NOTES+" LIKE '%" + titleNote + "%' ORDER BY "+KEY_TITLE_NOTES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		Note note = null;
		if (cursor.moveToFirst()) {
			do {
				note = new Note();
				note.setId(Integer.parseInt(cursor.getString(0)));
				note.setTitle(cursor.getString(1));

				notes.add(note);
			} while (cursor.moveToNext());
		}

		return notes;
	}


}
