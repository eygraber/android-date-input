package com.eygraber.date_input.xml_coroutines

import com.eygraber.date_input.common.DateResult
import com.eygraber.date_input.xml.DateInputView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

fun DateInputView.dateChangeFlow() = callbackFlow {
  val listener = DateInputView.OnDateChangedListener { date ->
    trySend(date)
  }

  addOnDateChangedListener(listener)

  awaitClose {
    removeOnDateChangedListener(listener)
  }
}

fun DateInputView.dateChangeOrNullFlow() = callbackFlow {
  val listener = DateInputView.OnDateChangedListener { date ->
    trySend(date)
  }

  addOnDateChangedListener(listener)

  awaitClose {
    removeOnDateChangedListener(listener)
  }
}.map { result ->
  (result as? DateResult.Success)?.date
}
