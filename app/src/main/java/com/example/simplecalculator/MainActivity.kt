package com.example.simplecalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplecalculator.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

enum class Operator(val symbol: Char) {
    ADD('+'),
    SUBTRACT('-'),
    MULTIPLY('×'),
    DIVIDE('÷');

    companion object {
        fun fromSymbol(symbol: Char): Operator? =
            entries.find { it.symbol == symbol }
    }
}

class MainActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_INPUT = "0"
        private const val DECIMAL_POINT = "."
        private const val MAX_DIGITS = 15
        private const val ERROR_TEXT = "Error"
    }

    private lateinit var binding: ActivityMainBinding

    private var currentInput = DEFAULT_INPUT
    private var pendingOperand: BigDecimal? = null
    private var pendingOperator: Operator? = null
    private var isResultDisplayed = false
    private var operatorJustPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDigitButtons()
        setupOperatorButtons()
        setupActionButtons()
        updateDisplay()
    }

    private fun setupDigitButtons() {
        val digitButtons = mapOf(
            binding.btn0 to "0", binding.btn1 to "1", binding.btn2 to "2",
            binding.btn3 to "3", binding.btn4 to "4", binding.btn5 to "5",
            binding.btn6 to "6", binding.btn7 to "7", binding.btn8 to "8",
            binding.btn9 to "9"
        )
        digitButtons.forEach { (button, digit) ->
            button.setOnClickListener { onDigitEntered(digit) }
        }
        binding.btnDot.setOnClickListener { onDecimalEntered() }
    }

    private fun setupOperatorButtons() {
        binding.btnAdd.setOnClickListener { onOperatorEntered(Operator.ADD) }
        binding.btnSubtract.setOnClickListener { onOperatorEntered(Operator.SUBTRACT) }
        binding.btnMultiply.setOnClickListener { onOperatorEntered(Operator.MULTIPLY) }
        binding.btnDivide.setOnClickListener { onOperatorEntered(Operator.DIVIDE) }
        binding.btnEquals.setOnClickListener { onEqualsPressed() }
    }

    private fun setupActionButtons() {
        binding.btnAC.setOnClickListener { onAllClear() }
        binding.btnCE.setOnClickListener { onClearEntry() }
    }

    private fun onDigitEntered(digit: String) {
        operatorJustPressed = false
        currentInput = when {
            isResultDisplayed -> {
                isResultDisplayed = false
                digit
            }
            currentInput == DEFAULT_INPUT -> digit
            isAtDigitLimit() -> return
            else -> currentInput + digit
        }
        updateDisplay()
    }

    private fun onDecimalEntered() {
        operatorJustPressed = false
        currentInput = when {
            isResultDisplayed -> {
                isResultDisplayed = false
                "0$DECIMAL_POINT"
            }
            currentInput.contains(DECIMAL_POINT) -> return
            else -> currentInput + DECIMAL_POINT
        }
        updateDisplay()
    }

    private fun onOperatorEntered(operator: Operator) {
        if (operatorJustPressed) {
            pendingOperator = operator
            updateFormulaDisplay()
            return
        }

        val currentValue = currentInput.toBigDecimalOrNull() ?: return

        pendingOperand = if (pendingOperator != null && !isResultDisplayed) {
            calculate(requireNotNull(pendingOperand), currentValue, requireNotNull(pendingOperator))
        } else {
            currentValue
        }

        pendingOperator = operator
        operatorJustPressed = true
        isResultDisplayed = false
        currentInput = DEFAULT_INPUT
        updateFormulaDisplay()
    }

    private fun onEqualsPressed() {
        val operator = pendingOperator ?: return
        val operand = pendingOperand ?: return
        val currentValue = currentInput.toBigDecimalOrNull() ?: return

        if (isDivisionByZero(operator, currentValue)) {
            showDivisionByZeroError()
            return
        }

        val result = calculate(operand, currentValue, operator)
        binding.tvFormula.text =
            "${formatNumber(operand)} ${operator.symbol} ${formatNumber(currentValue)} ="

        currentInput = formatNumber(result)
        isResultDisplayed = true
        pendingOperator = null
        pendingOperand = null
        operatorJustPressed = false
        updateDisplay()
    }

    private fun onAllClear() {
        currentInput = DEFAULT_INPUT
        pendingOperand = null
        pendingOperator = null
        isResultDisplayed = false
        operatorJustPressed = false
        binding.tvFormula.text = ""
        updateDisplay()
    }

    private fun onClearEntry() {
        currentInput = DEFAULT_INPUT
        isResultDisplayed = false
        updateDisplay()
    }

    private fun calculate(a: BigDecimal, b: BigDecimal, operator: Operator): BigDecimal {
        return when (operator) {
            Operator.ADD -> a.add(b)
            Operator.SUBTRACT -> a.subtract(b)
            Operator.MULTIPLY -> a.multiply(b)
            Operator.DIVIDE -> a.divide(b, MathContext(MAX_DIGITS, RoundingMode.HALF_UP))
        }
    }

    private fun isDivisionByZero(operator: Operator, value: BigDecimal): Boolean {
        return operator == Operator.DIVIDE && value.compareTo(BigDecimal.ZERO) == 0
    }

    private fun showDivisionByZeroError() {
        binding.tvDisplay.text = ERROR_TEXT
        binding.tvFormula.text = ""
        currentInput = DEFAULT_INPUT
        pendingOperator = null
        pendingOperand = null
        isResultDisplayed = true
        operatorJustPressed = false
    }

    private fun isAtDigitLimit(): Boolean {
        return currentInput.replace("-", "").replace(DECIMAL_POINT, "").length >= MAX_DIGITS
    }

    private fun formatNumber(value: BigDecimal): String {
        if (value.compareTo(BigDecimal.ZERO) == 0) return DEFAULT_INPUT
        val stripped = value.stripTrailingZeros()
        return if (stripped.scale() <= 0) {
            stripped.toBigInteger().toString()
        } else {
            stripped.toPlainString()
        }
    }

    private fun updateFormulaDisplay() {
        val operand = pendingOperand ?: return
        val operator = pendingOperator ?: return
        binding.tvFormula.text = "${formatNumber(operand)} ${operator.symbol}"
    }

    private fun updateDisplay() {
        binding.tvDisplay.text = currentInput
    }

    private fun String.toBigDecimalOrNull(): BigDecimal? {
        return try {
            BigDecimal(this)
        } catch (e: NumberFormatException) {
            null
        }
    }
}