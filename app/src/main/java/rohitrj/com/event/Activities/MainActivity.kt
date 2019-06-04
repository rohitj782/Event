package rohitrj.com.event.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import rohitrj.com.event.Models.Event
import rohitrj.com.event.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.geu.quiz.Activities.Adapters.EventAdapter
import kotlinx.android.synthetic.main.event_chooser.view.*
import org.jetbrains.anko.*


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{

    private lateinit var mAuth: FirebaseAuth
    private  var databaseReference: DatabaseReference?=null
    private var TAG="mainactivity"
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open
            ,R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigation.setNavigationItemSelectedListener(this)

        mAuth = FirebaseAuth.getInstance()

        recyclerView=findViewById(R.id.recyclerViewEvent)

        val mLayoutManager = LinearLayoutManager(this@MainActivity)
        mLayoutManager.reverseLayout = true
        mLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = mLayoutManager

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        if (mAuth.currentUser == null) {
          login()
        }

        floatingButton.setOnClickListener {

            createEvent()
        }

    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        when (p0.itemId) {

            R.id.i1 -> {
                Toast.makeText(this@MainActivity, "lol1 ", Toast.LENGTH_SHORT).show()

            }
            R.id.i2 -> {
                Toast.makeText(this@MainActivity, "lol2 ", Toast.LENGTH_SHORT).show()

            }

            R.id.signout -> {
                googleSignInClient.signOut().addOnCompleteListener {
                    if (it.isSuccessful) {
                        mAuth.signOut()
                        Log.i(TAG, "LoggeD out")
                        login()
                    }
                }
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }



    override fun onStart() {
        super.onStart()
        updateUI()
    }

    private fun updateUI() {

        val query=FirebaseDatabase.getInstance().reference.child(mAuth.uid.toString()).child("event")
        val options = FirebaseRecyclerOptions.Builder<Event>()
            .setQuery(query, Event::class.java)
            .build()

        query.keepSynced(true)

        val adapter = object : FirebaseRecyclerAdapter<Event, EventAdapter>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.event_view, parent, false)

                return EventAdapter(view)
            }

            override fun onBindViewHolder(holder: EventAdapter, position: Int, model: Event) {

                holder.bindView(model.title,model.category)

            }

        }

        recyclerView.adapter=adapter
        adapter.startListening()
    }

    private fun login(){
        startActivity(intentFor<LoginActivity>().clearTask().clearTop().newTask())
    }

    private fun createEvent(){

        //Inflate the dialog with custom view
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.event_chooser, null)

        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        //show dialog
        val  mAlertDialog = mBuilder.show()

        // button click of custom layout
        mDialogView.buttonNext.setOnClickListener {

            val newEvent=Event("","","")
            newEvent.title=mDialogView.editTextTitle.text.toString().trim()
            newEvent.category=mDialogView.editTextCategory.text.toString().trim()


            mAuth=FirebaseAuth.getInstance()
            databaseReference= FirebaseDatabase.getInstance().reference.child(mAuth.uid.toString()).child("event")

            mAlertDialog.dismiss()
            val id= databaseReference!!.push().key
            databaseReference= databaseReference!!.child(id!!)
            newEvent.id=newEvent.title+newEvent.category

            databaseReference!!.setValue(newEvent)

            val createEventIntent=Intent(this@MainActivity, CreateEventActivity::class.java)
            createEventIntent.putExtra("EVENT",newEvent)
            startActivity(createEventIntent)

        }
    }

    override fun onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

}
