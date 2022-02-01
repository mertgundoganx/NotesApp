package com.mertgundoganx.notesapp.ui.fragment.notes

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mertgundoganx.notesapp.R
import com.mertgundoganx.notesapp.data.model.Note
import com.mertgundoganx.notesapp.databinding.NotesFragmentBinding
import com.mertgundoganx.notesapp.ui.viewmodel.SharedViewModel
import com.mertgundoganx.notesapp.ui.fragment.notes.adapter.NotesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel: SharedViewModel by viewModels()
    private var _binding: NotesFragmentBinding? = null
    private val binding get() = _binding!!
    private val notesAdapter = NotesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NotesFragmentBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        observeData()

        binding.apply {
            buttonAddNote.setOnClickListener {
                actionToAddNoteFragment()
            }
        }

    }

    private fun setAdapter() {
        binding.recyclerViewNotes.apply {
            adapter = notesAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            swipeToDelete(this)
        }
    }

    private fun observeData() {
        viewModel.noteList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                notesAdapter.updateNoteList(arrayListOf())
                binding.imageViewEmptyNotes.visibility = View.VISIBLE
                binding.textViewEmptyNotes.visibility = View.VISIBLE
            } else {
                binding.imageViewEmptyNotes.visibility = View.GONE
                binding.textViewEmptyNotes.visibility = View.GONE
                notesAdapter.updateNoteList(it)
            }
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDelete = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = notesAdapter.noteList[viewHolder.adapterPosition]
                viewModel.deleteNote(itemToDelete)
                notesAdapter.deleteItem(viewHolder.adapterPosition)
                restoreNote(viewHolder.itemView, itemToDelete)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreNote(view: View, note: Note) {
        val snackBar = Snackbar.make(view, "Deleted ${note.title}", Snackbar.LENGTH_LONG)
        snackBar.setAction("undo") {
            viewModel.saveNote(note)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notes_menu, menu)
        val search = menu.findItem(R.id.searchNote)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search note..."
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(queryText: String?): Boolean {
        queryText?.let {
            searchSettings("%$queryText%")
        }
        return true
    }

    override fun onQueryTextChange(queryText: String?): Boolean {
        queryText?.let {
            searchSettings("%$queryText%")
        }
        return true
    }

    private fun searchSettings(queryText: String) {
        lifecycle.coroutineScope.launch {
            viewModel.searchNote(queryText).collect {
                if (it.isEmpty()) {
                    notesAdapter.updateNoteList(arrayListOf())
                    binding.imageViewEmptyNotes.visibility = View.VISIBLE
                    binding.textViewEmptyNotes.visibility = View.VISIBLE
                } else {
                    binding.imageViewEmptyNotes.visibility = View.GONE
                    binding.textViewEmptyNotes.visibility = View.GONE
                    notesAdapter.updateNoteList(it)
                }
            }
        }
    }

    private fun actionToAddNoteFragment() {
        val action = NotesFragmentDirections.actionNotesFragmentToAddNoteFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}