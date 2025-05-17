package com.transition.ora.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.gson.Gson
import com.transition.ora.R
import com.transition.ora.enums.CardType
import com.transition.ora.receivers.CardNotificationReceiver
import com.transition.ora.receivers.FareNotificationReceiver
import com.transition.ora.types.Card
import com.transition.ora.types.Fare
import java.util.Calendar
import java.util.UUID
import kotlin.math.abs

private const val ARG_CARD = "card"
private const val ARG_TITLE = "title"
private const val ARG_MESSAGE = "message"
private const val MILLIS_IN_20MIN = 1200000
private const val MILLIS_IN_6HOURS = 21600000
private const val MILLIS_IN_A_DAY = 86400000


class NotificationScheduler {
    fun scheduleNotification(card: Card, context: Context) {
        val fareNotifiedAbout: Fare? = checkForUnlimitedFares(card, context)
        if (fareNotifiedAbout == null) checkForTicketFares(card, context)

        if (card.type == CardType.Opus) {
            scheduleCardNotificationAtTime(card, context, card.expiryDate.timeInMillis)
        }
    }

    fun removeScheduleNotification(card: Card, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val cardNotificationPendingIntent = PendingIntent.getBroadcast(
            context,
            UUID.nameUUIDFromBytes("${card.id}:card".toByteArray()).hashCode(),
            Intent(context, CardNotificationReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val fareNotificationPendingIntent = PendingIntent.getBroadcast(
            context,
            UUID.nameUUIDFromBytes("${card.id}:fare".toByteArray()).hashCode(),
            Intent(context, FareNotificationReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(cardNotificationPendingIntent)
        alarmManager.cancel(fareNotificationPendingIntent)
    }

    private fun scheduleCardNotificationAtTime(card: Card, context: Context, triggerTimeInMillis: Long) {
        val now = Calendar.getInstance()
        val timeUntilTrigger = triggerTimeInMillis - (now.timeInMillis + 60 * 1000)
        if (timeUntilTrigger < 0) return

        val title: String
        val message: String
        var triggerTimeUpdated = triggerTimeInMillis

        if (timeUntilTrigger >= MILLIS_IN_A_DAY) {
            triggerTimeUpdated = triggerTimeInMillis - MILLIS_IN_A_DAY
            title = context.getString(R.string.validity_notification_opus_title)
            message = context.getString(R.string.validity_notification_opus_message, card.id.toString())
        } else {
            title = context.getString(R.string.validity_ended_notification_opus_title)
            message = context.getString(R.string.validity_ended_notification_opus_message, card.id.toString())
        }

        val intent = Intent(context, CardNotificationReceiver::class.java)
        intent.putExtra(ARG_CARD, Gson().toJson(card))
        intent.putExtra(ARG_TITLE, title)
        intent.putExtra(ARG_MESSAGE, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            UUID.nameUUIDFromBytes("${card.id}:card".toByteArray()).hashCode(),
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

    private fun scheduleFareNotificationAtTime(card: Card, fare: Fare, context: Context, triggerTimeInMillis: Long) {
        val now = Calendar.getInstance()
        val timeUntilTrigger = triggerTimeInMillis - (now.timeInMillis + 60 * 1000)
        if (timeUntilTrigger < 0) return

        val title: String
        val message: String
        var triggerTimeUpdated = triggerTimeInMillis
        val fareName = CardContentConverter.getFareProductById(context, fare.operatorId, fare.typeId).getName(context)

        if (timeUntilTrigger >= MILLIS_IN_20MIN) {
            title = context.getString(R.string.validity_notification_title)

            val remainingTimeString: String
            if (timeUntilTrigger >= MILLIS_IN_A_DAY) {
                remainingTimeString = context.getString(R.string.validity_day)
                triggerTimeUpdated = triggerTimeInMillis - MILLIS_IN_A_DAY
            } else if (timeUntilTrigger >= MILLIS_IN_6HOURS) {
                remainingTimeString = context.getString(R.string.validity_hours, 6)
                triggerTimeUpdated = triggerTimeInMillis - MILLIS_IN_6HOURS
            } else {
                remainingTimeString = context.getString(R.string.validity_minutes, 20)
                triggerTimeUpdated = triggerTimeInMillis - MILLIS_IN_20MIN
            }

            message = if (fare.ticketCount != null) {
                when (fare.ticketCount) {
                    0u -> context.getString(R.string.validity_notification_ticket_fare_empty_message, fareName, remainingTimeString)
                    1u -> context.getString(R.string.validity_notification_ticket_fare_singular_message, fareName, remainingTimeString)
                    else -> context.getString(R.string.validity_notification_ticket_fare_plural_message, fareName, remainingTimeString, fare.ticketCount!!)
                }
            } else {
                context.getString(R.string.validity_notification_unlimited_fare_message, fareName, remainingTimeString)
            }
        } else {
            title = context.getString(R.string.validity_ended_notification_title)
            message = if (fare.ticketCount != null) {
                when (fare.ticketCount) {
                    0u -> context.getString(R.string.validity_ended_notification_ticket_fare_empty_message, fareName)
                    1u -> context.getString(R.string.validity_ended_notification_ticket_fare_singular_message, fareName)
                    else -> context.getString(R.string.validity_ended_notification_ticket_fare_plural_message, fareName, fare.ticketCount!!)
                }
            } else {
                context.getString(R.string.validity_ended_notification_unlimited_fare_message, fareName)
            }
        }

        val intent = Intent(context, FareNotificationReceiver::class.java)
        intent.putExtra(ARG_CARD, Gson().toJson(card))
        intent.putExtra(ARG_TITLE, title)
        intent.putExtra(ARG_MESSAGE, message)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            UUID.nameUUIDFromBytes("${card.id}:fare".toByteArray()).hashCode(),
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
            scheduleFareNotificationAtTime(card, fareToNotifyAbout!!, context, nearestValidityUntil!!.timeInMillis)
        }

        return fareToNotifyAbout
    }

    private fun checkForTicketFares(card: Card, context: Context): Fare? {
        val fares = card.getFares()
        val trips = card.getTrips()
        if (fares.isEmpty() || trips.isEmpty()) return null

        val now = Calendar.getInstance()
        val nearestValidityUntilInterval = fares.filter { fare -> fare.ticketCount == null }
            .minOfOrNull { fare -> abs(now.timeInMillis - (fare.validityUntilDate?.timeInMillis ?: 0)) }

        var nearestValidityUntil: Calendar? = null
        var fareToNotifyAbout: Fare? = null

        fares.filter { fare -> fare.ticketCount != null }
            .forEach { fare ->
                trips.filter { trip -> trip.fareIndex == fare.fareIndex }
                    .forEach { trip ->
                        val tripFirstUseDate = trip.firstUseDate
                        val endValidity = Calendar.getInstance()
                        endValidity.set(
                            tripFirstUseDate.get(Calendar.YEAR),
                            tripFirstUseDate.get(Calendar.MONTH),
                            tripFirstUseDate.get(Calendar.DATE),
                            tripFirstUseDate.get(Calendar.HOUR_OF_DAY),
                            tripFirstUseDate.get(Calendar.MINUTE) + getDefaultValidityMinutes(fare)
                        )

                        if (nearestValidityUntilInterval == null || nearestValidityUntilInterval > getDefaultValidityMillis(fare)) {
                            if (isCloserFutureToNow(now, endValidity, nearestValidityUntil)) {
                                nearestValidityUntil = endValidity
                                fareToNotifyAbout = fare
                            }
                        }
                    }
            }

        if (fareToNotifyAbout != null) {
            scheduleFareNotificationAtTime(card, fareToNotifyAbout!!, context, nearestValidityUntil!!.timeInMillis)
        }

        return fareToNotifyAbout
    }

    private fun isCloserFutureToNow(now: Calendar, challenger: Calendar?, currentBest: Calendar?): Boolean {
        if (challenger == null) return false
        if (currentBest == null) return challenger.timeInMillis > now.timeInMillis

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