package output

import entity.UserEntity

interface IOutPutInfo {

    fun showMessage(message: String, lineBreak: Boolean = true)
    fun show(userList: List<UserEntity>?, message: String)
}