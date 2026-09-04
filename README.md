# Foundational Parsing Exercises

This repository contains two standalone Java exercises demonstrating foundational string parsing, tokenization, and validation techniques. The programs manually process basic structured data formats (JSON and XML) directly from standard input without the use of external parsing libraries or regular expression engines.

## Algorithmic Approach

### 1. JSON Parser / Validator (`JSONP`)
This exercise implements a rudimentary recursive-descent approach to validate and count elements in a JSON string.
- **Tokenization & Validation:** The string is scanned character by character to identify data types (strings, numbers, booleans, and nulls) based on specific starting characters (e.g., `"` for strings, `t`/`f`/`n` for true/false/null, and digits/signs for numbers).
- **State Management:** Nesting of JSON objects and arrays is handled recursively. When a nested `{` or `[` is encountered, a substring representing the nested structure is delegated to the corresponding parsing method. Simple counters are used to ensure matching braces and brackets.
- **Element Counting:** The program manually tracks and counts keys and values by traversing backward through the string, maintaining an array of processed keys to handle nested or duplicated structures.

### 2. XML Parser / Validator (`XMLP`)
This exercise focuses on basic tag validation and nested structure matching for XML data.
- **Tokenization:** The input is tokenized by searching for `<` and `>` delimiters to isolate tag names from the enclosed data values.
- **State Management:** The program validates the hierarchical structure by matching opening tags with their corresponding closing tags (e.g., `<name>` must eventually be closed by `</name>`). It counts angle brackets to ensure tags are properly formed and strictly contain one opening and closing bracket pair per token.
- **Validation:** Text enclosed between tags is verified to be either a valid string literal (enclosed in quotes without improper escape sequences) or a valid numeric/boolean value.

## Getting Started

### Prerequisites
You need the Java Development Kit (JDK) installed to compile and run these exercises.

### Compile
You can compile all exercises using the standard `javac` command from the root of the repository:
```bash
javac src/*.java
```

### Run
To run an exercise, execute the main class with the `java` command, ensuring you include the `src` directory in the classpath:

**Run JSONP:**
```bash
java -cp src JSONP
```

**Run XMLP:**
```bash
java -cp src XMLP
```
