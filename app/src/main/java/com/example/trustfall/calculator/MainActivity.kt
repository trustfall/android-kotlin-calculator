package com.example.trustfall.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.lang.NumberFormatException

import kotlinx.android.synthetic.main.activity_main.*

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"

class MainActivity : AppCompatActivity() {
//    // lateinit non-nullable variable but won't get value
//    // until later, can only be used with var
//    private lateinit var result: EditText
//    private lateinit var newNumber: EditText
//    // lazy delegate - defining a function that will be called
//    // to assign the value to the property. The function will be
//    // called the first time the property is accessed. Then the value is cached
//    // so the function isn't called again
//    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    // Variables to hold the operands
    private var operand1: Double? = null
    private var pendingOperation = "="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        // EditTexts
//        result = findViewById(R.id.result)
//        newNumber = findViewById(R.id.newNumber)
//
//        // Data input buttons
//        val button0: Button = findViewById(R.id.button0)
//        val button1: Button = findViewById(R.id.button1)
//        val button2: Button = findViewById(R.id.button2)
//        val button3: Button = findViewById(R.id.button3)
//        val button4: Button = findViewById(R.id.button4)
//        val button5: Button = findViewById(R.id.button5)
//        val button6: Button = findViewById(R.id.button6)
//        val button7: Button = findViewById(R.id.button7)
//        val button8: Button = findViewById(R.id.button8)
//        val button9: Button = findViewById(R.id.button9)
//        val buttonDecimal: Button = findViewById(R.id.buttonDecimal)
//
//        // Operation buttons
//        val buttonEquals = findViewById<Button>(R.id.buttonEquals)
//        val buttonMultiply = findViewById<Button>(R.id.buttonMultiply)
//        val buttonDivide = findViewById<Button>(R.id.buttonDivide)
//        val buttonSubtract = findViewById<Button>(R.id.buttonSubtract)
//        val buttonAdd = findViewById<Button>(R.id.buttonAdd)

        // new onClick Listener instance
        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }

        // set listener for each button
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDecimal.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }
            pendingOperation = op
            operation.text = pendingOperation
        }

        val negListener = View.OnClickListener {
            val newNumberString = newNumber.text.toString()

            if (newNumberString.isEmpty()) {
                newNumber.setText("-")
            } else {
                try {
                    var doubleValue = newNumberString.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch(e: NumberFormatException) {
                    newNumber.setText("")
                }
            }
        }

        buttonEquals.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonSubtract.setOnClickListener(opListener)
        buttonAdd.setOnClickListener(opListener)
        buttonNeg.setOnClickListener(negListener)
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN // handle attempt to divide by zero
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value // operand 1 and 2 are actually different types
                "-" -> operand1 = operand1!! - value // Double/Double? are not the same and !! returns
                "+" -> operand1 = operand1!! + value // the non-nullable value for operand1.
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    // save state for switching between portrait and landscape
    override fun onSaveInstanceState(outState: Bundle?) {
        if (operand1 != null) {
            outState?.putDouble(STATE_OPERAND1, operand1!!)
            outState?.putBoolean(STATE_OPERAND1_STORED, true)
        }

        outState?.putString(
            STATE_PENDING_OPERATION,
            pendingOperation
        )
        super.onSaveInstanceState(outState)
    }

    // restore state after rotation
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState?.getBoolean(STATE_OPERAND1_STORED, false)!!) {
            savedInstanceState.getDouble(STATE_OPERAND1)
        } else {
            null
        }
        pendingOperation = savedInstanceState?.getString(STATE_PENDING_OPERATION)!!

        operation.text = pendingOperation
    }
}

