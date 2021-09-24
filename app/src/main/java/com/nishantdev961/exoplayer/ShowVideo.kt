package com.nishantdev961.exoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_show_video.*

class ShowVideo : AppCompatActivity() {

    val db by lazy {
        FirebaseDatabase.getInstance()
    }
    val dbRef by lazy {
        db.getReference("video")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_video)

        rvMain.setHasFixedSize(true)
        rvMain.layoutManager = LinearLayoutManager(this)

    }

    override fun onStart() {
        super.onStart()

        var options :FirebaseRecyclerOptions<Member> =
            FirebaseRecyclerOptions.Builder<Member>()
                    .setQuery(dbRef, Member::class.java)
                    .build()

        val adapter = object : FirebaseRecyclerAdapter<Member, ViewHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

                return ViewHolder(
                        LayoutInflater.from(parent.context)
                                .inflate(R.layout.item, parent, false)
                )
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Member) {
                holder.bind(application, model)
            }

        }

        adapter.startListening()
        rvMain.adapter = adapter
    }
}