package com.eygraber.date_time_input.xml_coroutines

import com.eygraber.date_time_input.common.TimeResult
import com.eygraber.date_time_input.xml.TimeInputView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

fun TimeInputView.timeResultChangeFlow(emitInitialValue: Boolean = true) = callbackFlow {
  val listener = TimeInputView.OnTimeChangedListener { time ->
    trySend(time)
  }

  addOnTimeChangedListener(listener)

  if(emitInitialValue) listener.onTimeChanged(selectedTimeResult)

  awaitClose {
    removeOnTimeChangedListener(listener)
  }
}

fun TimeInputView.timeChangeOrNullFlow(emitInitialValue: Boolean = true) =
  timeResultChangeFlow(emitInitialValue)
    .map { result ->
      (result as? TimeResult.Success)?.time
    }
