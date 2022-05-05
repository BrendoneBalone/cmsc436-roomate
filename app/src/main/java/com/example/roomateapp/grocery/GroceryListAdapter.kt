package com.example.roomateapp.grocery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.CheckBox
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.example.roomateapp.R

class GroceryListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<GroceryListAdapter.ViewHolder>() {

    private val mItems = ArrayList<GroceryItem>()


    // Adds a GroceryItem to the adapter, and notify observers of dataset change
    fun add(item: GroceryItem) {
        mItems.add(item)
        notifyItemChanged(mItems.size)
    }


    // Clears the GroceryList adapter of all items
    fun clear() {
        mItems.clear()
        notifyDataSetChanged()
    }


    // Gets specific item
    fun getItem(pos: Int): Any {
        return mItems[pos - 1]
    }


    // Gets the total number of GroceryItems in list
    override fun getItemCount(): Int {
        return mItems.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) HEADER_VIEW_TYPE else GROCERY_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == HEADER_VIEW_TYPE) {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.grocery_header_view, parent, false)
            return ViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.grocery_item, parent, false)
            val viewHolder = ViewHolder(v)

            viewHolder.name = viewHolder.itemLayout.findViewById<TextView>(R.id.name)
            viewHolder.checkbox = viewHolder.itemLayout.findViewById<CheckBox>(R.id.status_checkbox)

            return viewHolder
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        if (position == 0) {
            viewHolder.itemView.setOnClickListener {
                Log.i(TAG, "Entered footerView onClick")
                val options: Bundle? = null
                startActivityForResult(
                    mContext as Activity,
                    Intent(
                        mContext,
                        AddGroceryActivity::class.java
                    ),
                    GroceryManagerActivity.ADD_GROCERY_ITEM_REQUEST,
                    options
                )
            }
        } else {
            val groceryItem: GroceryItem = mItems[position - 1]

            Log.i(TAG, "In onBindViewHolder for ${viewHolder.name.toString()}")

            viewHolder.name!!.text = groceryItem.name
            viewHolder.checkbox!!.isChecked = false

            //TODO: Implement onCheckedListener to delete the item from the grocery list
            viewHolder.checkbox!!.setOnCheckedChangeListener { compoundButton, b ->  }
        }
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong() - 1
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemLayout: View = itemView
        var name: TextView? = null
        var checkbox: CheckBox? = null
    }

    companion object {
        private const val TAG = "RoommateApp"
        private const val HEADER_VIEW_TYPE = R.layout.grocery_header_view
        private const val GROCERY_VIEW_TYPE = R.layout.grocery_item
    }


}