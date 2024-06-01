// EventAdapter.kt
package com.example.myapplication.view.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.data.dto.EventHeader
import com.example.myapplication.R

class EventAdapterList(
    private val context: Context,
    private var events: List<EventHeader>,
    private val nestedScrollView: NestedScrollView, // 스크롤뷰 추가
    private val fromFragmentId: Int // 추가

) : BaseAdapter() {

    override fun getCount(): Int {
        return events.size
    }

    override fun getItem(position: Int): EventHeader {
        return events[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_event, parent, false)
        } else {
            view = convertView
        }

        val event = getItem(position)

        val titleTextView = view.findViewById<TextView>(R.id.eventTitle)
        val managerTextView = view.findViewById<TextView>(R.id.eventManager)
        val imageView = view.findViewById<ImageView>(R.id.eventImage)

        titleTextView.text = event.title
        managerTextView.text = event.startAt

        Glide.with(context)
            .load(event.imageUrl)
            .into(imageView)

        view.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("selectedEvent", event)
            }
            if (fromFragmentId == R.id.eventFragment) {
                view.findNavController().navigate(R.id.action_eventFragment_to_eventDescriptionFragment, bundle)
            } else if (fromFragmentId == R.id.homeFragment) {
                view.findNavController().navigate(R.id.action_homeFragment_to_eventDescriptionFragment, bundle)
            }
        }

        view.setOnTouchListener { v, event ->
            nestedScrollView.requestDisallowInterceptTouchEvent(true)
            false
        }

        return view
    }

    fun updateData(newEvents: List<EventHeader>) {
        events = newEvents
        notifyDataSetChanged()
    }
}
