package bankzworld.com.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import bankzworld.com.model.Note
import bankzworld.com.database.NoteDatabase

class AddNoteViewModel(noteDatabase: NoteDatabase, id: Int) : ViewModel() {

    val task: LiveData<Note>

    init {
        task = noteDatabase.journalDao().loadSingleNoteById(id)
    }
}
