package uz.iskandarbek.phone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.iskandarbek.phone.R

class CallHistoryAdapter(
    private var history: MutableList<String>,
    private val onItemDeleted: (String) -> Unit
) : RecyclerView.Adapter<CallHistoryAdapter.CallHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return CallHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallHistoryViewHolder, position: Int) {
        holder.bind(history[position])
    }

    override fun getItemCount(): Int = history.size

    fun removeItem(position: Int) {
        onItemDeleted(history[position])
        history.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class CallHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvHistoryItem: TextView = itemView.findViewById(R.id.tvHistoryItem)
        private val ivDelete: ImageView = itemView.findViewById(R.id.delete)

        fun bind(phoneNumber: String) {
            tvHistoryItem.text = phoneNumber
            ivDelete.setOnClickListener {
                removeItem(adapterPosition)
            }
        }
    }
}
