# XML / JSON Converter with JavaCC

A Java desktop application for bidirectional XML ↔ JSON conversion using JavaCC-generated parsers and a Swing GUI.

## Overview

The project focuses on parsing structured XML and JSON documents with custom grammars, generating parser code with JavaCC, and presenting conversion operations through a Swing desktop interface.

## Technical Concepts

- XML lexical analysis and parsing
- JSON lexical analysis and parsing
- JavaCC grammar files (`.jj`)
- BNF / EBNF grammar definitions
- Generated parser and lexer classes
- Java Swing GUI
- Bidirectional XML ↔ JSON conversion

## Project Structure

```text
converter/
└── src/
    └── converter/
        ├── XMLToJSONConverter.java
        ├── XMLParser.jj
        └── JsonParser.jj
```

## Build and Run

The repository currently does not expose a Maven build descriptor in the inspected root, so build commands depend on the JavaCC setup used for the project. Run the existing JavaCC generation step first, then compile the generated and application sources with a compatible JDK.

## GUI

The Swing interface provides file loading, conversion controls, output display, and saving of generated content.

## Limitations

The parser implementation has limited XML namespace support, limited mixed-content handling, and no XML-comment preservation in generated output, as documented by the existing project behavior.
