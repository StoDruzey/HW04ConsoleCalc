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
            expression = expression.replace("""\s+""".toRegex(), "") //deleting all spaces
            expression = expression.replace("""[^0123456789.+-/*()]""".toRegex(), "") //deleting unsuitable symbols
            expression = expression.replace("""(?<!(\d+(\.\d+)?)|\))\-""".toRegex(), "~") //converting unary minus to ~

            println(expression)
            return expression
        }

        fun convertToRPN(expression: String): List<String> { //converts string to List of Reverse Polish Notation

            val rpn = mutableListOf<String>() //Reverse Polish Notation
            val stack = mutableListOf<String>()
            var flagDigit = false
            var unaryMinusFlag = false
            var tempStore = ""

            fun numberToRPN() { //frequently repeated block 1 - push number to rpn from temporary store
                if (flagDigit) {
                    rpn.add(tempStore)
                    tempStore = ""
                    flagDigit = false
                }
            }

            fun pushLastOperationFromStackToRPN () { //frequently repeated block 2 - push last operation to rpn from stack
                rpn.add(stack.last())
                stack.removeLast()
            }

            expression.forEach {
                when (it) {
                    '~' -> unaryMinusFlag = true
                    in "+-/*" -> {
                        numberToRPN()
                        //ИЛИ операция на вершине стека приоритетнее, или такого же уровня приоритета как o1
                        //… выталкиваем верхний элемент стека в выходную строку; помещаем операцию o1 в стек
                        if (stack.isEmpty()) { //if stack is empty don't check the ops priopity
                            stack.add(it.toString())
                        } else { //checking operation priority
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
                    in "0123456789." -> {
                        if (unaryMinusFlag) {
                            tempStore += "-"
                            unaryMinusFlag = false
                        }
                        tempStore += it
                        flagDigit = true
                    }
                }
            }
            numberToRPN() //adding number to output string if last

            while (stack.isNotEmpty()) { //when input string is over, push all the operations from stack to output string
                pushLastOperationFromStackToRPN()
            }

            rpn.forEach { print("$it ") }
            println()
            return rpn.toList()
        }

        fun computer(rpn: List<String>): String {
            val result: String = ""

            return result
        }
    }
}