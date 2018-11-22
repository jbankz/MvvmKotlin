package bankzworld.com.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

import java.util.Date

@Entity
class Note : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    @ColumnInfo(name = "title")
    var title: String? = null
        private set
    @ColumnInfo(name = "description")
    var description: String? = null
        private set
    @ColumnInfo(name = "date")
    var date: Date? = null
        private set

    @Ignore
    constructor(title: String, description: String, date: Date) {
        this.title = title
        this.description = description
        this.date = date
    }

    constructor(id: Int, title: String, description: String, date: Date) {
        this.id = id
        this.title = title
        this.description = description
        this.date = date
    }

    protected constructor(`in`: Parcel) {
        id = `in`.readInt()
        title = `in`.readString()
        description = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}
