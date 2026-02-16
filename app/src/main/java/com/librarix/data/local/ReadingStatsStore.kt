package com.librarix.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class ReadingSession(
    val id: String = UUID.randomUUID().toString(),
    val date: Long = System.currentTimeMillis(),
    val pagesDelta: Int
)

@Singleton
class ReadingStatsStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("librarix_reading_stats", Context.MODE_PRIVATE)

    private val _sessions = MutableStateFlow<List<ReadingSession>>(emptyList())
    val sessions: StateFlow<List<ReadingSession>> = _sessions.asStateFlow()

    private val _yearlyGoals = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val yearlyGoals: StateFlow<Map<Int, Int>> = _yearlyGoals.asStateFlow()

    init {
        load()
    }

    fun logSession(pagesDelta: Int, date: Long = System.currentTimeMillis()) {
        if (pagesDelta <= 0) return
        val session = ReadingSession(date = date, pagesDelta = pagesDelta)
        val updated = (_sessions.value + session)
            .sortedByDescending { it.date }
            .take(2000)
        _sessions.value = updated
        save()
    }

    fun setGoal(year: Int, goal: Int) {
        _yearlyGoals.value = _yearlyGoals.value + (year to maxOf(0, goal))
        save()
    }

    fun goal(forYear: Int): Int {
        return _yearlyGoals.value[forYear] ?: 30
    }

    fun dayStreak(now: Long = System.currentTimeMillis()): Int {
        val calendar = Calendar.getInstance()
        val sessionDays = _sessions.value.map { session ->
            calendar.timeInMillis = session.date
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.timeInMillis
        }.toSet()

        if (sessionDays.isEmpty()) return 0

        calendar.timeInMillis = now
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        var cursor = calendar.timeInMillis

        // If no session today, allow yesterday anchor
        if (!sessionDays.contains(cursor)) {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterday = calendar.timeInMillis
            if (sessionDays.contains(yesterday)) {
                cursor = yesterday
            } else {
                return 0
            }
        }

        var streak = 0
        calendar.timeInMillis = cursor
        while (sessionDays.contains(calendar.timeInMillis)) {
            streak++
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
        return streak
    }

    fun pagesThisWeek(now: Long = System.currentTimeMillis()): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = now
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val weekStart = calendar.timeInMillis
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        val weekEnd = calendar.timeInMillis

        return _sessions.value
            .filter { it.date in weekStart until weekEnd }
            .sumOf { it.pagesDelta }
    }

    private fun load() {
        try {
            val sessionsJson = prefs.getString("sessions", null)
            if (sessionsJson != null) {
                val array = JSONArray(sessionsJson)
                val list = mutableListOf<ReadingSession>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(ReadingSession(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        date = obj.getLong("date"),
                        pagesDelta = obj.getInt("pagesDelta")
                    ))
                }
                _sessions.value = list
            }
        } catch (_: Exception) {
            _sessions.value = emptyList()
        }

        try {
            val goalsJson = prefs.getString("yearly_goals", null)
            if (goalsJson != null) {
                val obj = JSONObject(goalsJson)
                val map = mutableMapOf<Int, Int>()
                obj.keys().forEach { key -> map[key.toInt()] = obj.getInt(key) }
                _yearlyGoals.value = map
            }
        } catch (_: Exception) {
            _yearlyGoals.value = emptyMap()
        }
    }

    private fun save() {
        val sessionsArray = JSONArray()
        _sessions.value.forEach { s ->
            sessionsArray.put(JSONObject().apply {
                put("id", s.id)
                put("date", s.date)
                put("pagesDelta", s.pagesDelta)
            })
        }

        val goalsObj = JSONObject()
        _yearlyGoals.value.forEach { (year, goal) -> goalsObj.put(year.toString(), goal) }

        prefs.edit()
            .putString("sessions", sessionsArray.toString())
            .putString("yearly_goals", goalsObj.toString())
            .apply()
    }
}
