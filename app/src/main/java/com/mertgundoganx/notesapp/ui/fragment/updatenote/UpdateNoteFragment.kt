package com.mertgundoganx.notesapp.ui.fragment.updatenote

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mertgundoganx.notesapp.R
import com.mertgundoganx.notesapp.data.model.Note
import com.mertgundoganx.notesapp.databinding.UpdateNoteFragmentBinding
import com.mertgundoganx.notesapp.ui.viewmodel.SharedViewModel
import com.mertgundoganx.notesapp.utils.UtilFunctions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateNoteFragment : Fragment() {

    private val viewModel: SharedViewModel by viewModels()
    private var _binding: UpdateNoteFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: UpdateNoteFragmentArgs by navArgs()
    private lateinit var note: Note

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UpdateNoteFragmentBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)
        note = args.note
        binding.note = note
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteNote -> {
                viewModel.deleteNote(note)
                actionToNotesFragment()
            }
            R.id.updateNote -> {
                val id = note.id
                val title = binding.textInputTitle.text.toString()
                val description = binding.textInputDescription.text.toString()

                if (UtilFunctions.titleEmptyControl(title)) {
                    binding.textInputLayoutTitle.error = null
                    val updatedNote = Note(
                        id = id,
                        title = title,
                        description = description
                    )
                    viewModel.updateNote(updatedNote)
                    actionToNotesFragment()
                } else {
                    binding.textInputLayoutTitle.error = "Title is not empty."
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_note_menu, menu)
    }

    private fun actionToNotesFragment() {
        val action = UpdateNoteFragmentDirections.actionUpdateNoteFragmentToNotesFragment()
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}