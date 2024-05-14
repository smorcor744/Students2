package db

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import dao.UserDAOH2
import files.IFiles
import files.IStudentViewModelFiles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class StudentViewModelDB(
    private val fileManagement: IFiles,
    private val studentsFile: File,
    private val studentsRepository: UserDAOH2
) : IStudentViewModelFiles {

    companion object {
        private const val MAXCHARACTERS = 10
        private const val MAXNUMSTUDENTSVISIBLE = 7
    }

    private var _newStudent = mutableStateOf("")
    override val newStudent: State<String> = _newStudent

    private val _students = mutableStateListOf<String>()
    override val students: List<String> = _students

    private val _infoMessage = mutableStateOf("")
    override val infoMessage: State<String> = _infoMessage

    private val _showInfoMessage = mutableStateOf(false)
    override val showInfoMessage: State<Boolean> = _showInfoMessage

    private val _selectedIndex = mutableStateOf(-1) // -1 significa que no hay selección
    override val selectedIndex: State<Int> = _selectedIndex

    override fun addStudent() {
        if (_newStudent.value.isNotBlank()) {
            _students.add(_newStudent.value.trim())
            _newStudent.value = ""
        }
    }

    override fun removeStudent(index: Int) {
        if (index in _students.indices) {
            _students.removeAt(index)
        }
    }

    override fun loadStudents() {
        val loadedStudents = studentsRepository
    }


    override fun saveStudents() {}

    override fun clearStudents() {
        _students.clear()
    }

    override fun showScrollStudentListImage() = _students.size > MAXNUMSTUDENTSVISIBLE

    override fun newStudentChange(name: String) {
        if (name.length <= MAXCHARACTERS) {
            _newStudent.value = name
        }
    }

    override fun studentSelected(index: Int) {
        _selectedIndex.value = index
    }

    private fun updateInfoMessage(message: String) {
        _infoMessage.value = message
        _showInfoMessage.value = true
        CoroutineScope(Dispatchers.Default).launch {
            delay(2000)
            _showInfoMessage.value = false
            _infoMessage.value = ""
        }
    }

    override fun showInfoMessage(show: Boolean) {
        _showInfoMessage.value = show
    }
}