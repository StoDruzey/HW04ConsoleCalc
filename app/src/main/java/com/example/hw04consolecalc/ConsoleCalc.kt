package com.example.hw04consolecalc

fun main() {

    val str = ConsoleCalc.getData()
    ConsoleCalc.convertToRPN(str)
}

class ConsoleCalc {

    companion object {

        fun getData(): String {
            println("Enter the expression: ")
            var expression: String? = readlnOrNull()
            expression = expression ?: ""
            println(expression)
            expression = expression.replace("""\s+""".toRegex(), "")
            expression = expression.replace("""[^0123456789,.+-/*()]""".toRegex(), "")

            println(expression)
            return expression
        }

        fun convertToRPN(expression: String) {

            val rpn = mutableListOf<String>() //Reverse Polish Notation
            val stack = mutableListOf<String>()
            var flagDigit = false
            var tempStore = ""

            fun numberToRPN() { //часто повторяющийся блок 1
                if (flagDigit) {
                    rpn.add(tempStore)
                    tempStore = ""
                    flagDigit = false
                }
            }

            fun pushLastOperationFromStackToRPN () { //часто повторяющийся блок 2
                rpn.add(stack.last())
                stack.removeLast()
            }

            expression.forEach {
                when (it) {
                    in "+-/*" -> {
                        numberToRPN()
                        //ИЛИ операция на вершине стека приоритетнее, или такого же уровня приоритета как o1
                        //… выталкиваем верхний элемент стека в выходную строку; помещаем операцию o1 в стек
                        if (stack.isEmpty()) {
                            stack.add(it.toString())
                        } else {
                            when (stack.last()) {
                                "(" -> stack.add(it.toString())
                                in "*/" -> {
                                    pushLastOperationFromStackToRPN()
                                    stack.add(it.toString())
                                }
                                in "+-" -> if (it in "+-") {
                                    pushLastOperationFromStackToRPN()
                                    stack.add(it.toString())
                                } else {
                                    stack.add(it.toString())
                                }
                            }
                        }
                    }
                    in "()" -> {
                        if (it == '(') {
                            stack.add(it.toString())
                        } else if (it == ')') {
                            numberToRPN()
                            while (stack.last() != "(") {
                                pushLastOperationFromStackToRPN()
                            }
                            stack.removeLast() //removing "(" from stack
                        }
                    }
                    in "0123456789.," -> {
                        tempStore += it
                        flagDigit = true
                    }
                }
//                stack.forEach { print("$it ") }
//                println()
//                rpn.forEach { print("$it ") }
//                println()
            }
            numberToRPN() //adding number to output string if last

            while (stack.isNotEmpty()) { //Когда входная строка закончилась, выталкиваем все символы из стека в выходную строку
                pushLastOperationFromStackToRPN()
            }
            stack.forEach { print("$it ") }
            println()
            rpn.forEach { print("$it ") }
        }
    }
}