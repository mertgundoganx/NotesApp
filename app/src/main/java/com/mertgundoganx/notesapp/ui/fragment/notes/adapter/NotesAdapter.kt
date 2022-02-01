package com.mertgundoganx.notesapp.ui.fragment.notes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mertgundoganx.notesapp.data.model.Note
import com.mertgundoganx.notesapp.databinding.ItemNoteBinding
import com.mertgundoganx.notesapp.ui.fragment.notes.NotesFragmentDirections

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    var noteList = ArrayList<Note>()

    class ViewHolder(val itemNoteBinding: ItemNoteBinding) : RecyclerView.ViewHolder(itemNoteBinding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNote = noteList[position]
        holder.itemNoteBinding.apply {
            note = currentNote
            root.setOnClickListener {
                val action = NotesFragmentDirections.actionNotesFragmentToUpdateNoteFragment(currentNote)
                root.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = noteList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateNoteList(newNoteList: List<Note>) {
        CustomDiffUtil(noteList, newNoteList).calculateAndDispatch(this)
        noteList = newNoteList as ArrayList<Note>
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(position: Int) {
        if (noteList.isNotEmpty() && noteList.size > position) {
            noteList.removeAt(position)
            notifyItemRemoved(position)
        }
        if (noteList.size == 0) {
            noteList.clear()
        }
    }

}