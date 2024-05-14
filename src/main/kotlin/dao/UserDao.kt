package dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import entity.UserEntity
import output.IOutPutInfo
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource

interface UserDAO {
    fun create(user: UserEntity): UserEntity?
    fun getAll(): List<UserEntity>?
    fun getById(id: UUID): UserEntity?
    fun update(user: UserEntity): UserEntity?
    fun delete(id: UUID): Boolean
}


class UserDAOH2(
    private val dataSource: DataSource,
    private val console: IOutPutInfo
) : UserDAO {

    override fun create(user: UserEntity): UserEntity? {
        val sql = "INSERT INTO tuser (id, name, email) VALUES (?, ?, ?)"
        return try {
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, user.id.toString())
                    stmt.setString(2, user.name)
                    stmt.setString(3, user.email)
                    val rs = stmt.executeUpdate()
                    if (rs == 1) {
                        user
                    } else {
                        console.showMessage("*Error* insert query failed! ($rs records insert)")
                        null
                    }

                }
            }
        } catch (e: SQLException) {
            console.showMessage("*Error* insert query failed! ($e).")
            null
        }
    }

    override fun getById(id: UUID): UserEntity? {
        val sql = "SELECT * FROM tuser WHERE id = ?"
        return try {
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, id.toString())
                    val rs = stmt.executeQuery()
                    if (rs.next()) {
                        UserEntity(
                            id = UUID.fromString(rs.getString("id")),
                            name = rs.getString("name"),
                            email = rs.getString("email")
                        )
                    } else {
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            console.showMessage("*Error* insert query failed! ($e).")
            null
        }
    }

    override fun getAll(): List<UserEntity>? {
        val sql = "SELECT * FROM tuser"
        return try {
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    val rs = stmt.executeQuery()
                    val users = mutableListOf<UserEntity>()
                    while (rs.next()) {
                        users.add(
                            UserEntity(
                                id = UUID.fromString(rs.getString("id")),
                                name = rs.getString("name"),
                                email = rs.getString("email")
                            )
                        )
                    }
                    users
                }
            }
        } catch (e: SQLException) {
            console.showMessage("*Error* insert query failed! ($e).")
            null
        }
    }

    override fun update(user: UserEntity): UserEntity? {
        val sql = "UPDATE tuser SET name = ?, email = ? WHERE id = ?"
        return try {
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, user.name)
                    stmt.setString(2, user.email)
                    stmt.setString(3, user.id.toString())
                    val rs = stmt.executeUpdate()
                    if (rs == 1) {
                        user
                    } else {
                        console.showMessage("*Error* insert query failed! ($rs records insert)")
                        null
                    }
                }
            }
        } catch (e: SQLException) {
            console.showMessage("*Error* insert query failed! ($e).")
            null
        }
    }

    override fun delete(id: UUID): Boolean {
        val sql = "DELETE FROM tuser WHERE id = ?"
        return try {
            dataSource.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    stmt.setString(1, id.toString())
                    (stmt.executeUpdate() == 1)
                }
            }
        } catch (e: SQLException) {
            console.showMessage("*Error* insert query failed! ($e).")
            false
        }
    }
}


interface UserService {
    fun create(user: UserEntity): UserEntity?
    fun getById(id: UUID): UserEntity?
    fun update(user: UserEntity): UserEntity?
    fun delete(id: UUID): Boolean
    fun getAll(): List<UserEntity>?
}


class UserServiceImpl(private val userDao: UserDAO) : UserService {
    override fun create(user: UserEntity): UserEntity? {
        return userDao.create(user)
    }

    override fun getById(id: UUID): UserEntity? {
        return userDao.getById(id)
    }

    override fun update(user: UserEntity): UserEntity? {
        return userDao.update(user)
    }

    override fun delete(id: UUID): Boolean {
        return userDao.delete(id)
    }

    override fun getAll(): List<UserEntity>? {
        return userDao.getAll()
    }
}


object DataSourceFactory {
    enum class DataSourceType {
        HIKARI,
        JDBC
    }

    fun getDS(dataSourceType: DataSourceType): DataSource {
        return when (dataSourceType) {
            DataSourceType.HIKARI -> {
                val config = HikariConfig()
                config.jdbcUrl = "jdbc:h2:./default"
                config.username = "user"
                config.password = "user"
                config.driverClassName = "org.h2.Driver"
                config.maximumPoolSize = 10
                config.isAutoCommit = true
                config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                HikariDataSource(config)
            }

            DataSourceType.JDBC -> TODO()
        }
    }
}
