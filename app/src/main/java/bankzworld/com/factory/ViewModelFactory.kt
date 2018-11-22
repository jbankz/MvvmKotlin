package bankzworld.com.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import bankzworld.com.ui.AddNoteViewModel
import bankzworld.com.database.NoteDatabase

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val mDb: NoteDatabase, private val mTaskId: Int) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddNoteViewModel(mDb, mTaskId) as T
    }
}
