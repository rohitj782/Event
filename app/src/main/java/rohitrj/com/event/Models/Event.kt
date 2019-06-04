package rohitrj.com.event.Models

import android.os.Parcel
import android.os.Parcelable

class Event :Parcelable {

    var title:String?=null
    var id:String?=null
    var category:String?=null

    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        id = parcel.readString()
        category = parcel.readString()
    }

    constructor()  {}

    constructor(title:String,id:String,category:String) : this() {

        this.category=category
        this.id=id
        this.title=title
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(id)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}