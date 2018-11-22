package bankzworld.com.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import bankzworld.com.model.Note

@Dao
interface NoteDao {

    // inserts into the database
    @Insert
    fun createNote(note: Note)

    // gets all items from the database
    @Query("SELECT * FROM Note ORDER BY id")
    fun readNotes(): LiveData<List<Note>>

    // selects a particular item that needs to be updated
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateNote(note: Note)

    // deletes an item from the database
    @Delete
    fun deleteNote(note: Note)

    // deletes all items from the database
    @Query("DELETE FROM Note")
    fun deleteAllNotes()

    // retrieves a single item
    @Query("SELECT * FROM Note WHERE id = :id")
    fun loadSingleNoteById(id: Int): LiveData<Note>
}
