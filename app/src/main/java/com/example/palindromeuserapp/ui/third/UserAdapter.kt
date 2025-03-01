package com.example.palindromeuserapp.ui.third

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.palindromeuserapp.R
import com.example.palindromeuserapp.model.User

class UserAdapter(
    private val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val userList = mutableListOf<User>()

    fun setData(newUsers: List<User>) {
        val diffCallback = UserDiffCallback(userList, newUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        userList.clear()
        userList.addAll(newUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addData(moreUsers: List<User>) {
        val oldSize = userList.size
        userList.addAll(moreUsers)
        notifyItemRangeInserted(oldSize, moreUsers.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position], onItemClick)
    }

    override fun getItemCount(): Int = userList.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        private val tvFullName: TextView = itemView.findViewById(R.id.tvFullName)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)

        fun bind(user: User, onItemClick: (User) -> Unit) {
            val fullName = "${user.first_name} ${user.last_name}"
            tvFullName.text = fullName
            tvEmail.text = user.email

            Glide.with(itemView.context)
                .load(user.avatar)
                .circleCrop()
                .into(imgAvatar)

            itemView.setOnClickListener {
                onItemClick(user)
            }
        }
    }
}

class UserDiffCallback(
    private val oldList: List<User>,
    private val newList: List<User>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
