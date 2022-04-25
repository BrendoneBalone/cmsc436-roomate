package com.example.roomateapp.grocery

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GroceryListAdapter(private val mContext: Context) :
    RecyclerView.Adapter<GroceryListAdapter.ViewHolder> {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //TODO: Implement ViewInflater once layouts are created
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //TODO: Figure this out
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //TODO: Implement this when layouts are created
    }

    companion object {
        private const val TAG = "RoommateApp"
    }


}