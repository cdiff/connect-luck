import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.dto.EventHeader
import com.example.data.dto.FoodTruckHeader
import com.example.myapplication.R

class EventAdapter(private val context: Context, private var eventList: List<EventHeader>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        Glide.with(context)
            .load(event.imageUrl)
            .into(holder.imageView)
        holder.textView.text = event.title
        holder.textView1.text =event.streetAddress
        holder.textView2.text =event.zipCode
        holder.textView3.text =event.startAt
        holder.textView4.text =event.endAt
        holder.textView5.text =event.status

    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.title1)
        val textView1: TextView = itemView.findViewById(R.id.streetAddress)
        val textView2: TextView = itemView.findViewById(R.id.zipcode)
        val textView3: TextView = itemView.findViewById(R.id.startAt)
        val textView4: TextView = itemView.findViewById(R.id.endAt)
        val textView5: TextView = itemView.findViewById(R.id.status)

    }
    fun updateData(newEventList: List<EventHeader>) {
        eventList = newEventList
        notifyDataSetChanged()
    }

}
