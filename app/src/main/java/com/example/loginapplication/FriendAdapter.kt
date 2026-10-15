package com.example.loginapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendAdapter(private val friendList: List<Friend>) :
    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendName = itemView.findViewById<TextView>(R.id.fname)
        val time = itemView.findViewById<TextView>(R.id.time)
        val mutual = itemView.findViewById<TextView>(R.id.mutual)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friendList[position]
        holder.friendName.text = friend.name
        holder.time.text = friend.time
        holder.mutual.text = friend.mutual
    }

    override fun getItemCount(): Int {
        return friendList.size
    }
}
