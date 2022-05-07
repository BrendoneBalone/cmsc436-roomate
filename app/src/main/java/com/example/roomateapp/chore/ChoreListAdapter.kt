package com.example.roomateapp.chore


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.example.roomateapp.R

class ChoreListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<ChoreListAdapter.ViewHolder>() {

    private val mItems = ArrayList<ChoreItem>()

    // Add a ToDoItem to the adapter
    // Notify observers that the data set has changed

    fun add(item: ChoreItem) {
        mItems.add(item)
        notifyItemChanged(mItems.size)
    }

    // Clears the list adapter of all items.
    fun clear() {
        mItems.clear()
        notifyDataSetChanged()
    }

    fun getItem(pos: Int): Any {
        return mItems[pos - 1]
    }

    // Returns the number of ToDoItems

    override fun getItemCount(): Int {
        return mItems.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position==0) HEADER_VIEW_TYPE else TODO_VIEW_TYPE
    }

    // Retrieve the number of ToDoItems
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == HEADER_VIEW_TYPE) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.chore_header_view, parent, false)
            return ViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.chore_item, parent, false)

            // TODO - Inflate the View (defined in todo_item.xml) for this ToDoItem and store references in ViewHolder

            val vh = ViewHolder(v)

            vh.mDateView = v.findViewById<TextView>(R.id.dateView)
            vh.mTitleView = v.findViewById(R.id.chore_titleView)
            vh.mStatusView = v.findViewById(R.id.statusCheckBox)
            return vh
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        if (position == 0) {
            viewHolder.itemView.setOnClickListener {
                Log.i(ChoreManagerActivity.TAG, "Entered footerView.OnClickListener.onClick()")
                val options: Bundle? = null
                startActivityForResult(
                    mContext as Activity,
                    Intent(
                        mContext,
                        ChoreActivity::class.java
                    ),
                    ChoreManagerActivity.ADD_TODO_ITEM_REQUEST,
                    options
                )
            }
        } else {
            val toDoItem = mItems[position - 1]
            viewHolder.mTitleView!!.text = toDoItem.title
            //Initialize statusView's isChecked property
            viewHolder.mStatusView!!.isChecked = when(toDoItem.status.toString()) {
                "DONE" -> true
                else -> false
            }

            //Display Time and Date
            viewHolder.mDateView!!.text = toDoItem.date.toString()
        }
    }

    // Get the ID for the ToDoItem
    // In this case it's just the position

    override fun getItemId(pos: Int): Long {
        return pos.toLong() - 1
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mItemLayout: View = itemView
        var mTitleView: TextView? = null
        var mStatusView: CheckBox? = null
        var mDateView: TextView? = null
    }

    companion object {
        private const val TAG = "Lab-UserInterface"
        private const val HEADER_VIEW_TYPE = R.layout.chore_header_view
        private const val TODO_VIEW_TYPE = R.layout.chore_item
    }


}
