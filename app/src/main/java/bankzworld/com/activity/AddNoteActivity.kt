package bankzworld.com.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import bankzworld.com.R
import bankzworld.com.background.AppExecutor
import bankzworld.com.database.NoteDatabase
import bankzworld.com.factory.ViewModelFactory
import bankzworld.com.model.Note
import bankzworld.com.ui.AddNoteViewModel
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    private lateinit var mEnterTitle: EditText
    private lateinit var mEnterDetails: EditText
    private lateinit var mShowTitle: TextView
    private lateinit var mShowDetails: TextView
    private lateinit var mSaveButton: Button

    private var noteDatabase: NoteDatabase? = null
    private var mTaskID = DEFAULT_ID
    private var toUpdate = false
    private var receiveIntent: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // find views by id
        mEnterTitle = findViewById(R.id.et_add_title)
        mEnterDetails = findViewById(R.id.et_add_details)
        mShowTitle = findViewById(R.id.txt_show_title)
        mShowDetails = findViewById(R.id.txt_show_details)
        mSaveButton = findViewById(R.id.btn_add)


        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskID = savedInstanceState.getInt(
                INSTANCE_TASK_ID,
                DEFAULT_ID
            )
        }

        // gets the database instance
        noteDatabase = NoteDatabase.getsInstance(applicationContext)

        mSaveButton.setOnClickListener { saveNote() }

        // add textWatchers
        mEnterTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.d(TAG, "beforeTextChanged: " + charSequence.toString())
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.d(TAG, "onTextChanged: " + charSequence.toString())
                mShowTitle.text = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {
                Log.d(TAG, "afterTextChanged: " + editable.toString())
            }
        })

        mEnterDetails.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.d(TAG, "beforeTextChanged: " + charSequence.toString())
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.d(TAG, "onTextChanged: " + charSequence.toString())
                mShowDetails.text = charSequence.toString()
            }

            override fun afterTextChanged(editable: Editable) {
                Log.d(TAG, "afterTextChanged: " + editable.toString())
            }
        })

        val intent = intent
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {

            mSaveButton.text = "Update"

            receiveIntent = getIntent().getParcelableExtra(EXTRA_TASK_ID)

            val factory = ViewModelFactory(noteDatabase!!, receiveIntent!!.id)

            val viewModel = ViewModelProviders.of(this, factory).get(AddNoteViewModel::class.java)

            if (mTaskID == DEFAULT_ID) {
                viewModel.task.observe(this, object : Observer<Note> {
                    override fun onChanged(note: Note?) {
                        viewModel.task.removeObserver(this)
                        // populates the UI
                        populateUI(note)
                        // set value of toUpdate to true
                        toUpdate = true
                    }
                })
            }
        }
    }

    fun saveNote() {
        val title = mEnterTitle.text.toString()
        val details = mEnterDetails.text.toString()
        val date = Date()

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(details)) {
            finish()
        } else {
            val entry = Note(title, details, date)
            AppExecutor.getsInstance().diskIO.execute {
                if (toUpdate) {
                    Log.d(TAG, "run: update this file")
                    entry.id = receiveIntent!!.id
                    noteDatabase!!.journalDao().updateNote(entry)
                } else {
                    Log.d(TAG, "run: insert new file")
                    noteDatabase!!.journalDao().createNote(entry)
                }
                finish()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(INSTANCE_TASK_ID, mTaskID)
        super.onSaveInstanceState(outState)
    }

    private fun populateUI(note: Note?) {
        if (note == null) {
            return
        }
        mEnterTitle.setText(note.title)
        mEnterDetails.setText(note.description)
    }

    companion object {

        private val TAG = "AddNoteActivity"
        private val DEFAULT_ID = 1
        private val EXTRA_TASK_ID = "extraTaskId"
        private val INSTANCE_TASK_ID = "instanceTaskId"
    }
}
