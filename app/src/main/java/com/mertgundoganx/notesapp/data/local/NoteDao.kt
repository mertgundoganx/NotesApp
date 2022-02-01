package com.mertgundoganx.notesapp.data.local

import androidx.room.*
import com.mertgundoganx.notesapp.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("select * from notes_table")
    fun getAllNotes(): Flow<List<Note>>

    @Query("select * from notes_table where title like :query or description like :query")
    fun searchNote(query: String): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

}