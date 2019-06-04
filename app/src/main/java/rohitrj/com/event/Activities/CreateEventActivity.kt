package rohitrj.com.event.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_create_event.*
import rohitrj.com.event.Models.Event
import rohitrj.com.event.R

class CreateEventActivity : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth
    private lateinit var databaseReference:DatabaseReference
    private lateinit var myEvent:Event
    private val TAG="createeventactivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myEvent= intent.getParcelableExtra("EVENT")
        setTitle(myEvent.title)

        Log.i(TAG,myEvent.id)

        setContentView(R.layout.activity_create_event)

        mAuth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().reference.child(mAuth.uid.toString())

        imageViewSend.setOnClickListener {
            uploadText()
        }

    }

    fun uploadText(){


        if(editTextNotes.text==null){

            editTextNotes.setError("Type something...")
            editTextNotes.requestFocus()
            return
        }

        val textUploadQuery=databaseReference.child("notes").orderByChild("id")
            .equalTo(myEvent.id)
        textUploadQuery.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                if(p0.exists()){


                }else{
                    val textUploadDb=databaseReference.child("notes")
                    val id=textUploadDb.push().key

                    id?.let { textUploadDb.child(it).child("id").setValue(myEvent.id)
                        textUploadDb.child(it).child("text").setValue(editTextNotes.text.toString())

                    }

                }

            }

            override fun onCancelled(p0: DatabaseError) {
                //TODO

                }

        })



    }

}
