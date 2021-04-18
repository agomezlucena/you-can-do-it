package com.agomezlucena.youcandoit

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.agomezlucena.youcandoit.contact_managament.Contact
import com.agomezlucena.youcandoit.task_managament.Task
import kotlinx.coroutines.*
import java.time.*
import java.util.*

/**
 * Returns the translation for a specific day of the week
 */
fun translateDayOfWeek(context: Context, dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> context.getString(R.string.monday)
        DayOfWeek.TUESDAY -> context.getString(R.string.tuesday)
        DayOfWeek.WEDNESDAY -> context.getString(R.string.wednesday)
        DayOfWeek.THURSDAY -> context.getString(R.string.thursday)
        DayOfWeek.FRIDAY -> context.getString(R.string.friday)
        DayOfWeek.SATURDAY -> context.getString(R.string.saturday)
        DayOfWeek.SUNDAY -> context.getString(R.string.sunday)
    }
}

fun translateMonth(context: Context,month: Month):String{
    return when(month){
        Month.JANUARY -> context.getString(R.string.january)
        Month.FEBRUARY -> context.getString(R.string.febraury)
        Month.MARCH -> context.getString(R.string.march)
        Month.APRIL -> context.getString(R.string.april)
        Month.MAY -> context.getString(R.string.may)
        Month.JUNE -> context.getString(R.string.june)
        Month.JULY -> context.getString(R.string.july)
        Month.AUGUST -> context.getString(R.string.august)
        Month.SEPTEMBER -> context.getString(R.string.september)
        Month.OCTOBER -> context.getString(R.string.october)
        Month.NOVEMBER -> context.getString(R.string.november)
        Month.DECEMBER -> context.getString(R.string.december)
    }
}

/**
 * Return the translated abbreviation for a specific day of the week
 */
fun translateDayOfWeekAbbreviation(context: Context, dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> context.getString(R.string.monday_abbv)
        DayOfWeek.TUESDAY -> context.getString(R.string.tuesday_abbv)
        DayOfWeek.WEDNESDAY -> context.getString(R.string.wednesday_abbv)
        DayOfWeek.THURSDAY -> context.getString(R.string.thursday_abbv)
        DayOfWeek.FRIDAY -> context.getString(R.string.friday_abbv)
        DayOfWeek.SATURDAY -> context.getString(R.string.saturday_abbv)
        DayOfWeek.SUNDAY -> context.getString(R.string.sunday_abbv)
    }
}

fun translateMonthAbbreviation(context: Context,month: Month):String{
    return when(month){
        Month.JANUARY -> context.getString(R.string.jan_abbv)
        Month.FEBRUARY -> context.getString(R.string.feb_abbv)
        Month.MARCH -> context.getString(R.string.mar_abbv)
        Month.APRIL -> context.getString(R.string.apr_abbv)
        Month.MAY -> context.getString(R.string.may_abbv)
        Month.JUNE -> context.getString(R.string.jun_abbv)
        Month.JULY -> context.getString(R.string.jul_abbv)
        Month.AUGUST -> context.getString(R.string.aug_abbv)
        Month.SEPTEMBER -> context.getString(R.string.sep_abbv)
        Month.OCTOBER -> context.getString(R.string.oct_abbv)
        Month.NOVEMBER -> context.getString(R.string.nov_abbv)
        Month.DECEMBER -> context.getString(R.string.dec_abbv)
    }
}

/**
 * Returns the quantity of days between the day of parameter and the date who calls
 * this extension function
 * */
fun LocalDate.daysToDay(day: DayOfWeek) = (day.value - this.dayOfWeek.value).toLong()

/**
 * Returns a Pair of LocalDate and LocalTime with the values of date who calls this extension function
 */
fun LocalDateTime.toDateTimePair(): Pair<LocalDate, LocalTime> =
    Pair(
        LocalDate.of(this.year, this.month, this.dayOfMonth),
        LocalTime.of(this.hour, this.minute, 0, 0)
    )

/**
 * convert calling LocalDate to a Calendar
 */
fun LocalDate.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, this.year)
    calendar.set(Calendar.MONTH, this.monthValue - 1)
    calendar.set(Calendar.DAY_OF_MONTH, this.dayOfMonth)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar
}

/**
 * convert calling callendar to LocalDateTime
 */
fun Calendar.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.of(
        this.get(Calendar.YEAR),
        this.get(Calendar.MONTH) + 1,
        this.get(Calendar.DAY_OF_MONTH),
        this.get(Calendar.HOUR_OF_DAY),
        this.get(Calendar.MINUTE),
        0,
        0
    )
}

fun launchCoroutine(dispatcher: CoroutineDispatcher, block: suspend CoroutineScope.() -> Unit) =
    GlobalScope.launch(dispatcher) {
        block(this)
    }

/**
 * Task function builder
 * */
fun task(init: Task.() -> Unit): Task {
    val task = Task()
    task.init()
    return task
}

fun getEmergencyContact(context: Context):Contact{
    val sharedPreferences = context.getSharedPreferences("YouCanDoItSharedPreferences",MODE_PRIVATE)
    val contactName = sharedPreferences.getString("EMERGENCY_CONTACT_NAME",null)
    val contactPhone = sharedPreferences.getString("EMERGENCY_CONTACT_PHONE",null)
    return Contact(contactName,contactPhone)
}

fun writeContact(context: Context,contact: Contact):Boolean{
    val editor = context.getSharedPreferences("YouCanDoItSharedPreferences",MODE_PRIVATE).edit()
    editor.putString("EMERGENCY_CONTACT_NAME",contact.name)
    editor.putString("EMERGENCY_CONTACT_PHONE",contact.phoneNumber)
    return editor.commit()
}