package bankzworld.com.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import bankzworld.com.R
import bankzworld.com.adapter.NoteAdapter
import bankzworld.com.background.AppExecutor
import bankzworld.com.database.NoteDatabase
import bankzworld.com.ui.MainViewModel
import kotlinx.android.synthetic.main.empty_layout.*

class MainActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mFb: FloatingActionButton
    private var noteDatabase: NoteDatabase? = null
    private var noteAdapter: NoteAdapter? = null
    private var mainViewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mRecyclerView = findViewById(R.id.note_rv)
        mFb = findViewById(R.id.fab)

        // init view model
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // init journal database
        noteDatabase = NoteDatabase.getsInstance(applicationContext)

        // set recycler views needs
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        // initialising adapter
        noteAdapter = NoteAdapter()
        mRecyclerView.adapter = noteAdapter

        // set clickListener
        mFb.setOnClickListener { startActivity(Intent(this@MainActivity, AddNoteActivity::class.java)) }

        // initialise the delete function
        deleteItem()

        // set up the view model
        setUpViewModel()

    }

    private fun deleteItem() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AppExecutor.getsInstance().diskIO.execute {
                    val position = viewHolder.adapterPosition
                    val noteList = noteAdapter!!.noteList
                    noteDatabase!!.journalDao().deleteNote(noteList!![position])
                }
            }
        }).attachToRecyclerView(mRecyclerView)
    }

    private fun setUpViewModel() {
        mainViewModel!!.entries.observe(this, Observer { journalEntries ->
            Log.d(TAG, "onChanged: receiving data from view model")
            if (journalEntries!!.isEmpty()) {
                empty_list.visibility = View.VISIBLE
            } else {
                empty_list.visibility = View.INVISIBLE
                noteAdapter!!.noteList = journalEntries
                mRecyclerView.adapter = noteAdapter
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when (id) {
            R.id.action_delete_all -> AppExecutor.getsInstance().diskIO.execute {
                noteDatabase!!.journalDao().deleteAllNotes()
               // runOnUiThread {setUpViewModel()}
            }

        }

        return super.onOptionsItemSelected(item)
    }

    companion object {

        private val TAG = "MainActivity"
    }
}
