package com.freesourfruit.kotlinfun

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById(R.id.editText) as EditText
        val textView = findViewById(R.id.textView) as TextView

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                textView.text = editText.text.toString().toIntOrNull()?.toEnglish() ?: "Sorry, that's greater than the maximum integer."
                true
            } else {
                false
            }
        }

        if (editText.requestFocus()) window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private fun String.collapseSpaces() = this.replace("""\s+""".toRegex(), " ")

    private fun Int.toEnglish(): String = when(this.toString().length) {
        // max int is 2,147,483,647
              1 -> this.digitToEnglish()
              2 -> this.tensToEnglish()
              3 -> this.hundredsToEnglish().collapseSpaces()
        in 4..6 -> this.thousandsToEnglish().collapseSpaces()
        in 7..9 -> this.millionsToEnglish().collapseSpaces()
             10 -> this.billionsToEnglish().collapseSpaces()
           else -> "ERROR"
    }

    private fun Int.digitToEnglish(): String = when(this % 10) {
        0 -> "zero"
        1 -> "one"
        2 -> "two"
        3 -> "three"
        4 -> "four"
        5 -> "five"
        6 -> "six"
        7 -> "seven"
        8 -> "eight"
        9 -> "nine"
        else -> "ERROR"
    }

    private fun Int.tensToEnglish(): String = when(this % 100) {
        0 -> ""
        in 1..9 -> this.digitToEnglish()
        10 -> "ten"
        11 -> "eleven"
        12 -> "twelve"
        13 -> "thirteen"
        14 -> "fourteen"
        15 -> "fifteen"
        16 -> "sixteen"
        17 -> "seventeen"
        18 -> "eighteen"
        19 -> "nineteen"
        20 -> "twenty"
        30 -> "thirty"
        40 -> "forty"
        50 -> "fifty"
        60 -> "sixty"
        70 -> "seventy"
        80 -> "eighty"
        90 -> "ninety"
        in 21..29,
        in 31..39,
        in 41..49,
        in 51..59,
        in 61..69,
        in 71..79,
        in 81..89,
        in 91..99 -> (this - this % 10).tensToEnglish().plus(" ").plus(this.digitToEnglish())
        else -> "ERROR"
    }

    private fun Int.magnitudeToEnglish(myMagnitude: Int, nextHigherMagnitude: Long, myName: String,
                                       toSayMyMagnitude: Int.() -> String, toSayLowerMagnitudes: Int.() -> String): String {

        val digitsICareAbout = (this % nextHigherMagnitude).toInt() // if it's billions, nextHigherMagnitude could be a Long
        val myMagnitudeDigits = (digitsICareAbout - digitsICareAbout % myMagnitude) / myMagnitude
        val myMagnitudeEnglish = if (myMagnitudeDigits > 0) myMagnitudeDigits.toSayMyMagnitude().plus(" $myName ") else ""
        return myMagnitudeEnglish.plus(digitsICareAbout.toSayLowerMagnitudes())
    }

    private fun Int.hundredsToEnglish()  = this.magnitudeToEnglish(100, 1000, "hundred", { digitToEnglish() }, { tensToEnglish() })

    private fun Int.thousandsToEnglish() = this.magnitudeToEnglish(1000, 1000000, "thousand", { hundredsToEnglish() }, { hundredsToEnglish() })

    private fun Int.millionsToEnglish()  = this.magnitudeToEnglish(1000000, 1000000000, "million", { hundredsToEnglish() }, { thousandsToEnglish() })

    private fun Int.billionsToEnglish()  = this.magnitudeToEnglish(1000000000, 10000000000L, "billion", { digitToEnglish() }, { millionsToEnglish() })
}
