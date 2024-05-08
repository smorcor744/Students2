import BaseDatos.IRepo
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class StudentVMBD : IRepo {
    private var _url: MutableState<String> = mutableStateOf("")
    val url: MutableState<String> get() = _url

    private var _usuario: MutableState<String> = mutableStateOf("")
    val usuario: MutableState<String> get() = _usuario

    private var _passwd: MutableState<String> = mutableStateOf("")
    val passwd: MutableState<String> get() = _passwd

    fun setUrl(value: String) {
        _url.value = value
    }

    fun setUsuario(value: String) {
        _usuario.value = value
    }

    fun setPasswd(value: String) {
        _passwd.value = value
    }
}
