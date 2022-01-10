package com.eygraber.date_input.xml_coroutines

import com.eygraber.date_input.xml.DateInputView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun DateInputView.dateInputChangeFlow() = callbackFlow {
  val listener = DateInputView.OnDateChangedListener { date ->
    trySend(date)
  }

  addOnDateChangedListener(listener)

  awaitClose {
    removeOnDateChangedListener(listener)
  }
}
