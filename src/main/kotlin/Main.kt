import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun StudentList(students: SnapshotStateList<String>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn(
            modifier = Modifier.verticalScroll(rememberScrollState()).height(300.dp).width(150.dp)
        ) {
            items(students) { student ->
                StudentItem(student = student, onDelete = { students.remove(student) })
            }
        }
    }
}

@Composable
fun StudentItem(student: String, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = student, modifier = Modifier.weight(1f))
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete student")
        }
    }
}


@Composable
fun StudentAdd(students: SnapshotStateList<String>, newStudentName: MutableState<String>, newStudentFocusRequester: FocusRequester) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            modifier = Modifier.focusRequester(newStudentFocusRequester),
            value = newStudentName.value,
            onValueChange = { newStudentName.value = it },
            label = { Text("New student name") })
        Button(onClick = { students.add(newStudentName.value); newStudentName.value = "" }) {
            Text("Add new student")
        }
    }
}

@Composable
fun SaveButton(students: SnapshotStateList<String>, file: File) {
    Button(
        onClick = {
            file.bufferedWriter().use { out ->
                students.forEach { out.write("$it\n") }
            }

        }
    ) {
        Text("Save changes")
    }
}


@Composable
fun DeleteButton(students: SnapshotStateList<String>) {
    Button(
        onClick = {
            students.clear()
        }
    ) {
        Text("Clear all")
    }
}

@Composable
fun Students(file: File): SnapshotStateList<String> {
    val student = remember { mutableStateListOf<String>() }
    file.forEachLine { linea ->
        if (linea != "") {
            linea.split(" ").map { student.add(it.trim()) }
        }
    }
    return student
}


@Preview
@Composable
fun MainScreen(file: File, students: SnapshotStateList<String>) {
    val newStudentName = remember { mutableStateOf("") }
    val number by remember { mutableStateOf(students.size) }

    var showInfoMessage by remember { mutableStateOf(false) }
    var infoMessage by remember { mutableStateOf("") }

    val newStudentFocusRequester = remember { FocusRequester() }



    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 160.dp)

                ) {
                    StudentAdd(students, newStudentName,newStudentFocusRequester)
                }

                Box(
                    modifier = Modifier.fillMaxSize().padding(vertical = 20.dp),
                    contentAlignment = BottomCenter
                ) {
//                    SaveButton(students, file)
                    Button(
                        onClick = {
                            file.bufferedWriter().use { out ->
                                students.forEach { out.write("$it\n") }
                            }
                            infoMessage = "Fichero Guardado"
                            showInfoMessage = true


                        }
                    ) {
                        Text("Save changes")
                    }
                }

            }

        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp)

        ) {
            Spacer(modifier = Modifier.width(20.dp))

            StudentText(number)
            Box(
                modifier = Modifier
                    .padding(end = 160.dp)
                    .background(Color.White)
                    .border(width = 2.dp, color = Color.Black)
            )
            {
                StudentList(students)
            }
            DeleteButton(students)
        }


    }
    // Gestión de la visibilidad del mensaje informativo
    if (showInfoMessage) {
        InfoMessage(
            message = infoMessage,
            onCloseInfoMessage = {
                showInfoMessage = false
                infoMessage = ""
                newStudentFocusRequester.requestFocus()
            }
        )
    }

    // Automáticamente oculta el mensaje después de un retraso
    LaunchedEffect(showInfoMessage) {
        if (showInfoMessage) {
            delay(2000)
            showInfoMessage = false
            infoMessage = ""
            newStudentFocusRequester.requestFocus()
        }
    }
}


@Composable
fun StudentText(number: Int) {
    Text("Students: $number")
}

@Composable
fun InfoMessage(message: String, onCloseInfoMessage: () -> Unit) {
    DialogWindow(
        icon = painterResource("img.png"),
        title = "Atención",
        resizable = false,
        onCloseRequest = onCloseInfoMessage
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text(message)
        }
    }
}

fun main() = application {
    val file = File("Students.txt")
    val students = Students(file)
    Window(onCloseRequest = ::exitApplication) {
        MainScreen(file, students)
    }
}


