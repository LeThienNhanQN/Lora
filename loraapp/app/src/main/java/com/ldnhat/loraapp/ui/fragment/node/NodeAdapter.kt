package com.ldnhat.loraapp.ui.fragment.node

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ldnhat.loraapp.common.model.Node
import com.ldnhat.loraapp.databinding.ItemNodeBinding

class NodeAdapter(private val clickListener: OnClickListener) :
    ListAdapter<Node, NodeAdapter.NodeViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeViewHolder {
        return NodeViewHolder(ItemNodeBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
        val node = getItem(position)

        holder.itemView.setOnClickListener {
            clickListener.onclick(node)
        }
        holder.bind(node)
    }

    class NodeViewHolder(private var binding: ItemNodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(node: Node) {
            binding.node = node
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Node>() {
        override fun areItemsTheSame(oldItem: Node, newItem: Node): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Node, newItem: Node): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class OnClickListener(val clickListener: (node: Node) -> Unit) {
        fun onclick(node: Node) = clickListener(node)
    }
}