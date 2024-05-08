package BaseDatos

import java.sql.Connection

class StudentRepo {

    fun getAllStudents(): Result<List<String>> {
        return try {
            val connectionDb = Database.getConnection()
            val students = mutableListOf<String>()
            connectionDb.use { conn ->
                conn.createStatement().use { stmt ->
                    stmt.executeQuery("SELECT name FROM students").use { rs ->
                        while (rs.next()) {
                            students.add(rs.getString("name"))
                        }
                    }
                }
            }
            Result.success(students)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun updateStudents(students: List<String>): Result<Unit> {
        var connectionDb : Connection? = null
        return try {
            connectionDb = Database.getConnection()
            connectionDb.autoCommit = false
            connectionDb.createStatement().use { stmt ->
                stmt.execute("DELETE FROM students")
            }
            connectionDb.prepareStatement("INSERT INTO students (name) VALUES (?)").use { ps ->
                for (student in students) {
                    ps.setString(1, student)
                    ps.executeUpdate()
                }
            }
            connectionDb.commit()
            Result.success(Unit)
        } catch (e: Exception) {
            connectionDb?.rollback()
            Result.failure(e)
        } finally {
            connectionDb?.autoCommit = true
            connectionDb?.close()
        }
    }
//    fun getAllStudents(): Result<MutableList<String>> {
//        var connectionBD: Connection? = null
//        var stmt: Statement? = null
//
//        try {
//            //cogemos la bd
//            connectionBD = BaseDatos.Database.getConnection()
//            val students = mutableListOf<String>()
//
//            stmt = connectionBD.createStatement()
//            val query = "SELECT name FROM students"
//            val rs = stmt.executeQuery(query)
//
//            while (rs.next()){
//                students.add(rs.getString("name"))
//            }
//            stmt?.close()
//            connectionBD.close()
//
//            return Result.success(students)
//        } catch (e: Exception){
//            stmt?.close()
//            connectionBD?.close()
//            return Result.failure(e)
//        }
//    }
//
//    fun upgradeStudents(stedunts:List<String >): Result<Unit> {
//        var connectionBD: Connection? = null
//        var stmt: Statement? = null
//        var error: Exception? = null
//
//        try {
//            //cogemos la bd
//            connectionBD = BaseDatos.Database.getConnection()
//            connectionBD.autoCommit = false
//            val students = mutableListOf<String>()
//
//            stmt = connectionBD.createStatement()
//            val query = "DELETE FROM students"
//            val rs = stmt.executeQuery(query)
//
//            while (rs.next()){
//                students.add(rs.getString("name"))
//            }
//
//        } catch (e: Exception){
//            error = e
//            try {
//                connectionBD?.rollback()
//            } catch (e: Exception){
//                error = e
//            }
//        } finally {
//            connectionBD?.autoCommit = false
//            stmt?.close()
//            connectionBD?.close()
//        }
//
//        if (error != null){
//            return Result.failure(error)
//        }
//        return Result.success(Unit)
//    }
}