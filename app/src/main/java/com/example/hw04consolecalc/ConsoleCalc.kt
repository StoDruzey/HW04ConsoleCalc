package com.example.hw04consolecalc

import androidx.core.text.isDigitsOnly

fun main() {

    val str = ConsoleCalc.getData()
    val rpnInStringList = ConsoleCalc.convertStringToRPNinList(str)
    println("Result = ${ConsoleCalc.computer(rpnInStringList)}")
}

class ConsoleCalc {

    companion object {

        fun getData(): String {
            println("Enter the expression: ")
            var expression: String? = readlnOrNull()
            expression = expression ?: ""
            expression = expression.replace("""[^0123456789.+-/*()]|\s+""".toRegex(), "")
            //deleting unsuitable symbols (if space or not digit, operation, bracket)
            expression = expression.replace("""(?<!(\d+(\.\d+)?)|\))\-""".toRegex(), "~")
            //converting unary minus to ~ (if before "-" not number or closing bracket)
            return expression
        }

        fun convertStringToRPNinList(expression: String): List<String> {
            //converts string to List of Reverse Polish Notation

            val rpn = mutableListOf<String>() //Reverse Polish Notation
            val stack = mutableListOf<String>() //Stack for operations
            var flagDigit = false //If number in string is detected
            var unaryMinusFlag = false //If unary minus before number or bracket
            var numberTempStore = "" //Temp store for digits

            fun pushNumberFromNumberTempStoreToRPN() {
                //frequently repeated block 1 - push number to rpn from temporary store
                if (flagDigit) {
                    rpn.add(numberTempStore)
                    numberTempStore = ""
                    flagDigit = false
                }
            }

            fun pushLastOperationFromStackToRPN () {
                //frequently repeated block 2 - push last operation to rpn from stack
                rpn.add(stack.removeLast())
            }

            expression.forEach {
                when (it) {
                    '~' -> unaryMinusFlag = true
                    in "0123456789." -> {
                        if (unaryMinusFlag) {
                            numberTempStore += "-"
                            unaryMinusFlag = false
                        }
                        numberTempStore += it
                        flagDigit = true
                    }
                    in "+-/*" -> {
                        pushNumberFromNumberTempStoreToRPN()
                        //while операция на вершине стека приоритетнее, или такого же уровня приоритета как o1
                        //… выталкиваем верхний элемент стека в выходную строку; помещаем операцию o1 в стек
                        while (!stack.isEmpty() && (stack.last() in "*/" || stack.last() in "+-" && it in "+-")) {
                            pushLastOperationFromStackToRPN()
                        }
                        stack.add(it.toString())
                    }
                    in "()" -> {
                        if (it == '(') {
                            if (unaryMinusFlag) {
                                stack.add("~")
                                unaryMinusFlag = false
                            }
                            stack.add(it.toString())
                        } else { // if it == ")"
                            pushNumberFromNumberTempStoreToRPN()
                            while (stack.last() != "(") {
                                pushLastOperationFromStackToRPN()
                            }
                            stack.removeLast() //removing "(" from the stack
                            if (stack.last() == "~") {
                                //if unary minus is before opening bracket, push it from the stack to rpn
                                pushLastOperationFromStackToRPN()
                            }
                        }
                    }
                }
            }
            pushNumberFromNumberTempStoreToRPN() //adding number to output string if last

            while (stack.isNotEmpty()) {
                //when input string is over, push all the operations from the stack to output string
                pushLastOperationFromStackToRPN()
            }

            rpn.forEach { print("$it ") }
            println()
            return rpn.toList()
        }

        fun computer(rpn: List<String>): String {
            val stack = emptyList<Double>().toMutableList() //Computer stack
            var operandRight: Double
            var operandLeft: Double
            var index = 0 //action's order
            rpn.forEach { token ->
                when (token) {
                    "-" -> {
                        operandRight = stack.removeLast()
                        operandLeft = stack.removeLast()
                        stack.add(operandLeft - operandRight)
                        print("${index + 1}) $operandLeft - $operandRight = ${operandLeft - operandRight}")
                        println()
                        ++index
                    }
                    "+" -> {
                        operandRight = stack.removeLast()
                        operandLeft = stack.removeLast()
                        stack.add(operandLeft + operandRight)
                        print("${index + 1}) $operandLeft + $operandRight = ${operandLeft + operandRight}")
                        println()
                        ++index
                    }
                    "*" -> {
                        operandRight = stack.removeLast()
                        operandLeft = stack.removeLast()
                        stack.add(operandLeft * operandRight)
                        print("${index + 1}) $operandLeft * $operandRight = ${operandLeft * operandRight}")
                        println()
                        ++index
                    }
                    "/" -> {
                        operandRight = stack.removeLast()
                        operandLeft = stack.removeLast()
                        stack.add(operandLeft / operandRight)
                        print("${index + 1}) $operandLeft / $operandRight = ${operandLeft / operandRight}")
                        println()
                        ++index
                    }
                    "~" -> {
                        operandRight = -stack.removeLast()
                        stack.add(operandRight)
                        print("${index + 1}) $operandRight = $operandRight")
                        println()
                        ++index
                    }
                    else -> { //if token is number
                        stack.add(token.toDouble())
                    }
                }
            }
            return stack.removeLast().toString()
        }
    }
}