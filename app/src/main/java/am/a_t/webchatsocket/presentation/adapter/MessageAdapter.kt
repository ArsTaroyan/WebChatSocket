package am.a_t.webchatsocket.presentation.adapter

import am.a_t.webchatsocket.databinding.ItemMyMessageBinding
import am.a_t.webchatsocket.databinding.ItemYourMessageBinding
import am.a_t.webchatsocket.model.User
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(var myName: String): ListAdapter<User, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    inner class MyMessageHolder(val binding: ItemMyMessageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.myMessage.text = user.message
            binding.userName.text = user.userName
        }
    }

    inner class YourMessageHolder(val binding: ItemYourMessageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.yourMessage.text = user.message
            binding.userName.text = user.userName
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).userName) {
            myName -> 1
            else -> 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            1 -> MyMessageHolder(
                ItemMyMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else ->YourMessageHolder(
                ItemYourMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItem(position).userName) {
            myName -> {
                with(holder as MyMessageHolder) {
                    this.bind(getItem(position))
                }
            }
            else -> {
                with(holder as YourMessageHolder) {
                    this.bind(getItem(position))
                }
            }
        }
    }

}

class DiffUtilCallback: DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem.userName == newItem.userName

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem
}