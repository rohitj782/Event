package com.geu.quiz.Activities.Adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.event_view.view.*

class EventAdapter(internal var view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
    override fun onClick(p0: View?) {

    }

    fun bindView(title:String?,category: String?   ) {

        view.textViewTitle.text=title+category
    }




}
