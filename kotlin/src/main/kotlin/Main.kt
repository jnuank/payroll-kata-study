package org.example

fun payroll(): List<Pair<String, Int>> {
    val rows = input.lines().map { it.split(",") }
    return rows.map {
        it[0] to it[2].toInt() * it[3].toInt()
    }
}

val input = """
Alice,Hourly,2000,40
Bob,Hourly,2000,20
""".trimIndent()
