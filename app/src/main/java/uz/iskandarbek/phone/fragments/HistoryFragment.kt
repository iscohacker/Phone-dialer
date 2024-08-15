package uz.iskandarbek.phone.fragments



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.iskandarbek.phone.R
import uz.iskandarbek.phone.adapters.CallHistoryAdapter
import uz.iskandarbek.phone.models.CallHistoryManager

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var callHistoryAdapter: CallHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val callHistoryManager = CallHistoryManager(requireContext())
        val history = callHistoryManager.getCallHistory().toMutableList()

        callHistoryAdapter = CallHistoryAdapter(history) { phoneNumber ->
            callHistoryManager.removeCallFromHistory(phoneNumber)
        }
        recyclerView.adapter = callHistoryAdapter

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.LEFT, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                callHistoryAdapter.removeItem(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}