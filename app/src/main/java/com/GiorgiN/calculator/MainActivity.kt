package com.GiorgiN.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.view.View
import android.widget.Toast
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    lateinit var txtInput: TextView

    // Represent whether the lastly pressed key is numeric or not
    var lastNumeric: Boolean = false

    // Represent that current state is in error or not
    var stateError: Boolean = false

    // If true, do not allow to add another DOT
    var lastDot: Boolean = false
    var getValueFlag: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtInput = findViewById(R.id.txtInput)
    }

    /**
     * Append the Button.text to the TextView
     */
    fun onDigit(view: View) {
        if (stateError) {
            // If current state is Error, replace the error message
            txtInput.text = (view as Button).text
            stateError = false
        } else {
            // If not, already there is a valid expression so append to it]
            if( getValueFlag ){
                txtInput.text = ""
            }
            txtInput.append((view as Button).text)
            getValueFlag = false;
        }
        // Set the flag
        lastNumeric = true
    }

    /**
     * Append . to the TextView
     */
    fun onDecimalPoint(view: View) {
        if (lastNumeric && !stateError && !lastDot) {
            txtInput.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    /**
     * Append +,-,*,/ operators to the TextView
     */
    fun onOperator(view: View) {
        if (lastNumeric && !stateError) {
            txtInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false    // Reset the DOT flag
            getValueFlag =false
        }
    }


    /**
     * Clear the TextView
     */
    fun onClear(view: View) {
        this.txtInput.text = ""
        lastNumeric = false
        stateError = false
        lastDot = false
        getValueFlag = false
    }

    /**
     * Calculate the output using Exp4j
     */
    fun onEqual(view: View) {
        // If the current state is error, nothing to do.
        // If the last input is a number only, solution can be found.
        if (lastNumeric && !stateError) {
            // Read the expression
            val txt = txtInput.text.toString()
            // Create an Expression (A class from exp4j library)
            val expression = ExpressionBuilder(txt).build()
            try {
                // Calculate the result and display
                val result = expression.evaluate()
                txtInput.text = result.toString()
//                lastDot = true // Result contains a dot
                getValueFlag = true

            } catch (ex: ArithmeticException) {
                // Display an error message
                txtInput.text = "Error"
                stateError = true
                lastNumeric = false
                getValueFlag = false
            }
        }
    }

    fun onDelete(view: View) {
        if( !getValueFlag ) {
            val txt = txtInput.text.toString()
            if( txt.length>0)
                txtInput.text = txt.substring(0, txt.length - 1)
        }
    }
}
