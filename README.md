<img width="720" height="1600" alt="WhatsApp Image 2026-09-26 at 16 55 40" src="https://github.com/user-attachments/assets/89594b96-749b-4860-b06c-417a602d4476" />
<h1 align="center">Simple Calculator</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white" />
</p>

<p align="center">My first real Kotlin/Android project — a calculator app, built while learning.</p>



## ✨ What it does
- Standard arithmetic: addition, subtraction, multiplication, division
- Chained calculations (e.g. `5 + 3 ×` computes `5 + 3` before continuing)
- Operator swapping — changing your mind mid-input doesn't trigger an early calculation
- Precision-safe math using `BigDecimal` instead of Double (avoids floating-point rounding errors)
- Divide-by-zero handling
- Clean number formatting (no trailing zeros)

## 🛠 Tech
| | |
|---|---|
| Language | Kotlin |
| UI | Android Views + ViewBinding |

## 📚 What I learned building this
- Managing multi-step UI state cleanly (not everything in one giant function)
- Handling edge cases: leading zeros, decimal limits, divide-by-zero, operator chaining
- Using Kotlin enums for type safety instead of raw chars/strings
- Why `BigDecimal` matters for anything involving money or exact math

