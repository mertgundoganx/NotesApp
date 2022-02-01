package com.mertgundoganx.notesapp.data.repository

import com.mertgundoganx.notesapp.data.local.NoteDao
import com.mertgundoganx.notesapp.data.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    fun searchNote(query: String): Flow<List<Note>> = noteDao.searchNote(query)

    suspend fun saveNote(note: Note) {
        noteDao.saveNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

}