package com.mertgundoganx.notesapp.ui.fragment.addnote

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mertgundoganx.notesapp.R
import com.mertgundoganx.notesapp.data.model.Note
import com.mertgundoganx.notesapp.databinding.AddNoteFragmentBinding
import com.mertgundoganx.notesapp.ui.viewmodel.SharedViewModel
import com.mertgundoganx.notesapp.utils.UtilFunctions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment : Fragment() {

    private val viewModel: SharedViewModel by viewModels()
    private var _binding: AddNoteFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddNoteFragmentBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveNote -> {
                val title: String = binding.textInputTitle.text.toString()
                val description: String = binding.textInputDescription.text.toString()

                if (UtilFunctions.titleEmptyControl(title)) {
                    binding.textInputLayoutTitle.error = null
                    val newNote = Note(
                        title = title,
                        description = description
                    )
                    viewModel.saveNote(newNote)
                    actionToNotesFragment()
                } else {
                    binding.textInputLayoutTitle.error = "Title is not empty."
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_note_menu, menu)
    }

    private fun actionToNotesFragment() {
        val action = AddNoteFragmentDirections.actionAddNoteFragmentToNotesFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}