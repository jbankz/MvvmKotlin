package bankzworld.com.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bankzworld.com.R
import bankzworld.com.activity.AddNoteActivity
import bankzworld.com.model.Note

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var noteList: List<Note>? = null
        set(entryList) {
            field = entryList
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_layout, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.mTitle.text = this.noteList!![position].title
        holder.mDate.text = this.noteList!![position].date!!.toString()
    }

    override fun getItemCount(): Int {
        return if (this.noteList == null) 0 else this.noteList!!.size
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var mTitle: TextView = itemView.findViewById<View>(R.id.journal_title) as TextView
        internal var mDate: TextView = itemView.findViewById<View>(R.id.journal_date) as TextView

        init {
            // set click listener
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val context = view.context
            val note = noteList!!.get(layoutPosition)
            val intent = Intent(context.applicationContext, AddNoteActivity::class.java)
            intent.putExtra("extraTaskId", note)
            context.startActivity(intent)
        }
    }

    companion object {
        private val TAG = "NoteAdapter"
    }
}
