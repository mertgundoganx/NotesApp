package com.mertgundoganx.notesapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.mertgundoganx.notesapp.data.model.Note
import com.mertgundoganx.notesapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    val noteList = MutableLiveData<List<Note>>()

    init {
        viewModelScope.launch {
            noteRepository.getAllNotes().collect {
                noteList.postValue(it)
            }
        }
    }

    fun noteList(): Flow<List<Note>> = noteRepository.getAllNotes()

    fun searchNote(query: String): Flow<List<Note>> = noteRepository.searchNote(query)

    fun saveNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                noteRepository.saveNote(note)
            } catch (e: Exception) {
                Log.e("EXCEPTION", e.toString())
            }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                noteRepository.updateNote(note)
            } catch (e: Exception) {
                Log.e("EXCEPTION", e.toString())
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                noteRepository.deleteNote(note)
            } catch (e: Exception) {
                Log.e("EXCEPTION", e.toString())
            }
        }
    }

}