package com.example.loginapplication

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CalaculaterActivity : AppCompatActivity() {

    private lateinit var search_close_btn: TextView
    private lateinit var Expression: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calaculater)

        val mainView = findViewById<ConstraintLayout>(R.id.main)

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { view, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        search_close_btn = findViewById(R.id.search_close_btn)
        Expression = findViewById(R.id.expression)

        val btn0 = findViewById<CardView>(R.id.btn0)
        val btn1 = findViewById<CardView>(R.id.btn1)
        val btn2 = findViewById<CardView>(R.id.btn2)
        val btn3 = findViewById<CardView>(R.id.btn3)
        val btn4 = findViewById<CardView>(R.id.btn4)
        val btn5 = findViewById<CardView>(R.id.btn5)
        val btn6 = findViewById<CardView>(R.id.btn6)
        val btn7 = findViewById<CardView>(R.id.btn7)
        val btn8 = findViewById<CardView>(R.id.btn8)
        val btn9 = findViewById<CardView>(R.id.btn9)

        val btnDot = findViewById<CardView>(R.id.btnDot)
        val btnPlus = findViewById<CardView>(R.id.btnPlus)
        val btnMinus = findViewById<CardView>(R.id.btnMinus)
        val btnMultiply = findViewById<CardView>(R.id.btnMultiply)
        val btnDivide = findViewById<CardView>(R.id.btnDivide)
        val btnPercent = findViewById<CardView>(R.id.btnPercent)
        val btnAC = findViewById<CardView>(R.id.btnAC)
        val btnDelete = findViewById<CardView>(R.id.btnDelete)
        val btnEqual = findViewById<CardView>(R.id.btnEqual)

        btn0.setOnClickListener { addValue("0") }
        btn1.setOnClickListener { addValue("1") }
        btn2.setOnClickListener { addValue("2") }
        btn3.setOnClickListener { addValue("3") }
        btn4.setOnClickListener { addValue("4") }
        btn5.setOnClickListener { addValue("5") }
        btn6.setOnClickListener { addValue("6") }
        btn7.setOnClickListener { addValue("7") }
        btn8.setOnClickListener { addValue("8") }
        btn9.setOnClickListener { addValue("9") }

        btnDot.setOnClickListener { addValue(".") }
        btnPlus.setOnClickListener { addValue("+") }
        btnMinus.setOnClickListener { addValue("-") }
        btnMultiply.setOnClickListener { addValue("×") }
        btnDivide.setOnClickListener { addValue("÷") }
        btnPercent.setOnClickListener { addValue("%") }

        btnAC.setOnClickListener {
            Expression.text = "0"
        }

        btnDelete.setOnClickListener {
            val value = Expression.text.toString()

            if (value.length > 1) {
                Expression.text = value.substring(0, value.length - 1)
            } else {
                Expression.text = "0"
            }
        }

        btnEqual.setOnClickListener {
            calculate()
        }
    }

    private fun addValue(value: String) {
        val current = Expression.text.toString()

        if (current == "0") {
            if (value == "+" || value == "×" ||
                value == "÷" || value == "%"
            ) {
                return
            }

            Expression.text = value
        } else {
            Expression.append(value)
        }
    }

    private fun calculate() {
        try {
            val text = Expression.text.toString()
            val numbers = ArrayList<Double>()
            val operators = ArrayList<Char>()
            var number = ""

            for (i in text.indices) {
                val ch = text[i]

                if (ch.isDigit() || ch == '.') {
                    number += ch
                } else if (ch == '+' || ch == '-' ||
                    ch == '×' || ch == '÷'
                ) {
                    if (number == "") {
                        Toast.makeText(
                            this, "Invalid expression",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    numbers.add(number.toDouble())
                    operators.add(ch)
                    number = ""
                } else if (ch == '%') {
                    if (number == "") {
                        return
                    }

                    number = (number.toDouble() / 100).toString()
                }
            }

            if (number == "") {
                Toast.makeText(
                    this, "Invalid expression",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            numbers.add(number.toDouble())

            var i = 0

            while (i < operators.size) {
                if (operators[i] == '×' || operators[i] == '÷') {
                    val first = numbers[i]
                    val second = numbers[i + 1]

                    if (operators[i] == '÷' && second == 0.0) {
                        Toast.makeText(
                            this, "Cannot divide by zero",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    if (operators[i] == '×') {
                        numbers[i] = first * second
                    } else {
                        numbers[i] = first / second
                    }

                    numbers.removeAt(i + 1)
                    operators.removeAt(i)
                } else {
                    i++
                }
            }

            var answer = numbers[0]

            for (j in operators.indices) {
                if (operators[j] == '+') {
                    answer += numbers[j + 1]
                } else if (operators[j] == '-') {
                    answer -= numbers[j + 1]
                }
            }

            if (answer % 1.0 == 0.0) {
                Expression.text = answer.toLong().toString()
            } else {
                Expression.text = answer.toString()
            }

        } catch (e: Exception) {
            Toast.makeText(
                this, "Invalid expression",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
