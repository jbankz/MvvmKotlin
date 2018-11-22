package bankzworld.com.date

import android.arch.persistence.room.TypeConverter

import java.util.Date
class DateConverter {
    @TypeConverter
    fun toDate(timeStamp: Long?): Date? {
        return if (timeStamp == null) null else Date(timeStamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

}
