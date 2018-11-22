package bankzworld.com.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import bankzworld.com.database.NoteDatabase
import bankzworld.com.model.Note

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val entries: LiveData<List<Note>>

    init {
        val noteDatabase = NoteDatabase.getsInstance(this.getApplication())
        entries = noteDatabase!!.journalDao().readNotes()
    }

    companion object {

        private val TAG = "MainViewModel"
    }

}
