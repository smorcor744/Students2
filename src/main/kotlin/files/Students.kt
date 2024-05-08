package files// import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.material.Text
//import androidx.compose.ui.graphics.Color
//
//@Composable
//fun StudentList(students: MutableList<String>) {
//    Spacer(modifier = Modifier.height(16.dp))
//    students.forEachIndexed { index, student ->
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text(text = student)
//            IconButton(onClick = { students.removeAt(index) }) {
//                Icon(Icons.Filled.Delete, contentDescription = "Delete student")
//            }
//        }
//    }
//
//}
//
//
//@Composable
//fun StudentAdd(students: MutableList<String>, newStudentName: MutableState<String>) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        TextField(
//            modifier = Modifier.background(Color.White),
//            value = newStudentName.value,
//            onValueChange = { newStudentName.value = it },
//            label = { Text("New student name") })
//        Button(onClick = { students.add(newStudentName.value); newStudentName.value = "" }) {
//            Text("Add new student")
//        }
//    }
//}
//
//@Composable
//fun SaveButton() {
//    Button(
//        onClick = { /* Aquí puedes implementar la lógica para guardar los cambios */ }
//    ) {
//        Text("Save changes")
//    }
//}