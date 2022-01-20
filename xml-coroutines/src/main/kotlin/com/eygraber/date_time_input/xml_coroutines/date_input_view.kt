package com.eygraber.date_time_input.xml_coroutines

import com.eygraber.date_time_input.common.DateResult
import com.eygraber.date_time_input.xml.DateInputView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

fun DateInputView.dateResultChangeFlow(emitInitialValue: Boolean = true) = callbackFlow {
  val listener = DateInputView.OnDateChangedListener { date ->
    trySend(date)
  }

  addOnDateChangedListener(listener)

  if(emitInitialValue) listener.onDateChanged(selectedDateResult)

  awaitClose {
    removeOnDateChangedListener(listener)
  }
}

fun DateInputView.dateChangeOrNullFlow(emitInitialValue: Boolean = true) =
  dateResultChangeFlow(emitInitialValue)
    .map { result ->
      (result as? DateResult.Success)?.date
    }
