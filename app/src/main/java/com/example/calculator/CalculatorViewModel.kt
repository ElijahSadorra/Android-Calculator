package com.example.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel(){
    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction){
        when (action) {
            is CalculatorAction.Number -> enterNum(action.number)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> state= CalculatorState()
            is CalculatorAction.Operation -> enterOp(action.operation)
            is CalculatorAction.Calculate -> enterCalc()
            is CalculatorAction.Delete -> performDel()
        }
    }

    private fun performDel() {
        when {
            state.num2.isNotBlank() -> state = state.copy(
                num2 = state.num2.dropLast(1)
            )
            state.operation != null -> state = state.copy(
                operation = null
            )
            state.num1.isNotBlank() -> state = state.copy(
                num1 = state.num1.dropLast(1)
            )
        }
    }

    private fun enterCalc() {
        val num1 = state.num1.toDoubleOrNull()
        val num2 = state.num2.toDoubleOrNull()

        if (num1 == null || num2 == null){
            return
        }

        val result = when(state.operation){
            is CalculatorOperation.Add -> num1 + num2
            is CalculatorOperation.Subtract -> num1 - num2
            is CalculatorOperation.Divide -> num1 / num2
            is CalculatorOperation.Multiply -> num1 * num2
            null -> return
        }

        state = state.copy(
            num1 = result.toString().take(15),
            num2 = "",
            operation = null
        )
    }

    private fun enterOp(operation: CalculatorOperation) {
        if (state.num1.isNotBlank()){
            state = state.copy(operation = operation)
        }
    }

    private fun enterDecimal() {
        if (state.operation == null &&!state.num1.contains(".")
            && state.num1.isNotBlank()){
            state = state.copy(
                num1 = state.num1 + "."
            )
            return
        }
        if (!state.num2.contains(".")
            && state.num2.isNotBlank()){
            state = state.copy(
                num2 = state.num2 + "."
            )
            return
        }
    }

    private fun enterNum(number: Int) {
        if (state.operation == null){
            if (state.num1.length >= MAX_NUM_LENGTH){
                return
            }
            state = state.copy(num1 = state.num1 + number)
            return
        }
        if (state.operation != null){
            if (state.num2.length >= MAX_NUM_LENGTH){
                return
            }
            state = state.copy(num2 = state.num2 + number)
            return
        }
    }

    companion object{
        private const val MAX_NUM_LENGTH = 8
    }
}