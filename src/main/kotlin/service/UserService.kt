package service

import entity.UserEntity
import java.util.*

interface UserService {
    fun create(user: UserEntity): UserEntity?
    fun getById(id: UUID): UserEntity?
    fun update(user: UserEntity): UserEntity?
    fun delete(id: UUID): Boolean
    fun getAll(): List<UserEntity>?
}
