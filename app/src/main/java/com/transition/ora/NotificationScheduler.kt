package com.transition.ora

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.gson.Gson
import java.util.Calendar
import kotlin.math.abs

private const val ARG_CARD = "card"
private const val ARG_MESSAGE = "message"
private const val MILLIS_IN_20MIN = 1200000
private const val MILLIS_IN_6HOURS = 21600000
private const val MILLIS_IN_A_DAY = 86400000


class NotificationScheduler {
    fun scheduleNotification(card: Card, context: Context) {
        val fareNotifiedAbout: Fare? = checkForUnlimitedFares(card, context)
        if (fareNotifiedAbout == null) checkForTicketFares(card, context)
    }

    private fun scheduleNotificationAtTime(card: Card, fare: Fare, context: Context, triggerTimeInMillis: Long) {
        val now = Calendar.getInstance()
        val timeUntilTrigger = triggerTimeInMillis - now.timeInMillis
        if (timeUntilTrigger <= 0) return

        var remainingTimeString = ""
        var triggerTimeUpdated = triggerTimeInMillis
        if (timeUntilTrigger >= MILLIS_IN_A_DAY) {
            remainingTimeString = context.getString(R.string.validity_day)
            triggerTimeUpdated = triggerTimeInMillis - MILLIS_IN_A_DAY
        } else if (timeUntilTrigger >= MILLIS_IN_6HOURS) {
            remainingTimeString = context.getString(R.string.validity_hours, 6)
            triggerTimeUpdated = triggerTimeInMillis - MILLIS_IN_6HOURS
        } else if (timeUntilTrigger >= MILLIS_IN_20MIN) {
            remainingTimeString = context.getString(R.string.validity_minutes, 20)
            triggerTimeUpdated = triggerTimeInMillis - MILLIS_IN_20MIN
        }

        val fareName = CardContentConverter.getFareProductById(context, fare.operatorId, fare.typeId).name
        val message = if (fare.ticketCount != null) {
            when (fare.ticketCount) {
                0u -> context.getString(R.string.validity_notification_ticket_fare_empty_message, fareName, remainingTimeString)
                1u -> context.getString(R.string.validity_notification_ticket_fare_singular_message, fareName, remainingTimeString)
                else -> context.getString(R.string.validity_notification_ticket_fare_plural_message, fareName, remainingTimeString, fare.ticketCount!!)
            }
        } else {
            context.getString(R.string.validity_notification_unlimited_fare_message, fareName, remainingTimeString)
        }

        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra(ARG_CARD, Gson().toJson(card))
        intent.putExtra(ARG_MESSAGE, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            card.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTimeUpdated, pendingIntent)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeUpdated, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeUpdated, pendingIntent)
        }
    }


    private fun checkForUnlimitedFares(card: Card, context: Context): Fare? {
        val fares = card.getFares()
        if (fares.isEmpty()) return null

        val now = Calendar.getInstance()
        var nearestValidityUntil: Calendar? = null
        var fareToNotifyAbout: Fare? = null
        fares.filter { it.ticketCount == null }
            .forEach {
                if (isCloserFutureToNow(now, it.validityUntilDate, nearestValidityUntil)) {
                    nearestValidityUntil = it.validityUntilDate
                    fareToNotifyAbout = it
                }
            }

        if (fareToNotifyAbout != null) {
            scheduleNotificationAtTime(card, fareToNotifyAbout!!, context, nearestValidityUntil!!.timeInMillis)
        }

        return fareToNotifyAbout
    }

    private fun checkForTicketFares(card: Card, context: Context): Fare? {
        val fares = card.getFares()
        val trips = card.getTrips()
        if (fares.isEmpty() || trips.isEmpty()) return null

        val now = Calendar.getInstance()
        val nearestValidityUntilInterval = fares.filter { it.ticketCount == null }
            .minOfOrNull { abs(now.timeInMillis - (it.validityUntilDate?.timeInMillis ?: 0)) }

        var nearestValidityUntil: Calendar? = null
        var fareToNotifyAbout: Fare? = null

        fares.filter { it.ticketCount != null }
            .forEach {
                val validityUntil = trips.last().firstUseDate
                validityUntil.set(
                    validityUntil.get(Calendar.YEAR),
                    validityUntil.get(Calendar.MONTH),
                    validityUntil.get(Calendar.DATE),
                    validityUntil.get(Calendar.HOUR_OF_DAY),
                    validityUntil.get(Calendar.MINUTE) + getDefaultValidityMinutes(it)
                )

                if (nearestValidityUntilInterval == null || nearestValidityUntilInterval > getDefaultValidityMillis(it)) {
                    if (isCloserFutureToNow(now, validityUntil, nearestValidityUntil)) {
                        nearestValidityUntil = validityUntil
                        fareToNotifyAbout = it
                    }
                }
            }

        if (fareToNotifyAbout != null) {
            scheduleNotificationAtTime(card, fareToNotifyAbout!!, context, nearestValidityUntil!!.timeInMillis)
        }

        return fareToNotifyAbout
    }

    private fun isCloserFutureToNow(now: Calendar, challenger: Calendar?, currentBest: Calendar?): Boolean {
        if (challenger == null) return false
        if (currentBest == null) return true

        return (challenger.timeInMillis < currentBest.timeInMillis
                && challenger.timeInMillis > now.timeInMillis)
    }

    private fun getDefaultValidityMinutes(fare: Fare?): Int {
        return if (fare?.operatorId == 5u || fare?.operatorId == 16u) 90 else 120
    }

    private fun getDefaultValidityMillis(fare: Fare?): Long {
        return getDefaultValidityMinutes(fare).toLong() * 60 * 1000
    }
}