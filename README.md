# XML to JSON and JSON to XML Converter

A Java-based XML to JSON and JSON to XML converter using JavaCC parser generator with a Swing GUI interface. This tool allows bidirectional conversion between XML and JSON formats, providing a user-friendly interface for file loading, conversion, and saving.

## Project Structure
```
converter/
├── src/
│   └── converter/
│       ├── XMLToJSONConverter.java  # Main GUI application
│       ├── XMLParser.jj            # JavaCC grammar for XML to JSON conversion
│       ├── JsonParser.jj           # JavaCC grammar for JSON to XML conversion
└── README.md
```

## Grammar Definitions

### XML to JSON Grammar (XMLParser.jj)

#### BNF (Backus-Naur Form)

```bnf
<document> ::= <element> EOF
<element>  ::= "<" <name> ">" <content> "</" <name> ">" 
             | "<" <name> "/>"
<content>  ::= (<text> | <element>)*
<name>     ::= [a-zA-Z_] [a-zA-Z0-9_-]*
<text>     ::= [^<>]+
```

EBNF (Extended Backus-Naur Form)

```ebnf
document = element, EOF ;
element  = "<", name, ">", content, "</", name, ">" 
         | "<", name, "/>" ;
content  = { text | element } ;
name     = letter, { letter | digit | "_" | "-" } ;
text     = character, { character - "<" - ">" } ;
letter   = "A" | "B" | ... | "Z" | "a" | "b" | ... | "z" | "_" ;
digit    = "0" | "1" | ... | "9" ;
```

#### Token Definitions

```java
SKIP : {
    " "
    | "\t"
    | "\n"
    | "\r"
    | < XML_COMMENT: "<!--" (~["-"])* "-->" >
}

TOKEN : {
    < OPEN_TAG: "<" >
    | < CLOSE_TAG: ">" >
    | < END_TAG: "</" >
    | < SELF_CLOSE: "/>" >
    | < NAME: ["a"-"z", "A"-"Z", "_"](["a"-"z", "A"-"Z", "0"-"9", "_", "-", ":", "."])* >
    | < ATTRIBUTE_VALUE: "\"" (~["\""])* "\"" >
    | < TEXT: (~["<", ">", "@", "=", "\""])+ >
}
```

### JSON to XML Grammar (JsonParser.jj)

#### BNF (Backus-Naur Form)

```bnf
<document> ::= <object> | <array> EOF
<object>   ::= "{" <pair> ("," <pair>)* "}"
<pair>     ::= <string> ":" <value>
<value>    ::= <string> | <number> | <object> | <array> | "true" | "false" | "null"
<array>    ::= "[" <value> ("," <value>)* "]"
<string>   ::= "\"" (~["\"", "\\"] | "\\" ~[])* "\""
<number>   ::= ["-"]? (["0"-"9"])+ ("." (["0"-"9"])+)? (["e","E"] (["+","-"])? (["0"-"9"])+)?
```

#### Token Definitions

```java
SKIP : {
    " "
    | "\t"
    | "\n"
    | "\r"
}

TOKEN : {
    < OPEN_BRACE: "{" >
    | < CLOSE_BRACE: "}" >
    | < OPEN_BRACKET: "[" >
    | < CLOSE_BRACKET: "]" >
    | < COLON: ":" >
    | < COMMA: "," >
    | < STRING: "\"" (~["\"", "\\"] | "\\" ~[])* "\"" >
    | < NUMBER: (["-"])? (["0"-"9"])+ ("." (["0"-"9"])+)? (["e","E"] (["+","-"])? (["0"-"9"])+)? >
    | < TRUE: "true" >
    | < FALSE: "false" >
    | < NULL: "null" >
}
```

## Functional Description

### Parser Components

#### XML to JSON Parser (XMLParser)

- Tokenizes XML input using JavaCC-generated lexer.
- Validates XML structure during parsing.
- Converts XML elements to JSON objects, handling nested structures and attributes.
- Maintains proper indentation and escapes special characters.

#### JSON to XML Parser (JsonParser)

- Tokenizes JSON input using JavaCC-generated lexer.
- Validates JSON structure during parsing.
- Converts JSON objects to XML elements, handling nested objects and arrays.
- Maintains XML structure and escapes special characters.

### GUI Component (XMLToJSONConverter)

#### User Interface

- Split pane layout for input/output.
- XML input text area.
- JSON output text area.
- Control buttons for operations.

#### File Operations

- Load XML or JSON files.
- Save JSON or XML output.
- File chooser dialogs for file selection.

#### Conversion Features

- Real-time bidirectional conversion between XML and JSON.
- Error reporting with descriptive messages.
- Format preservation during conversion.

### Input XML with Comments

### Input XML

```xml
<!-- This is a comment -->
<root>
    <person>
        <name>John Doe</name>
        <age>30</age>
    </person>
</root>

```

### Output JSON

```json
{
"root": {
  "person": {
    "name": {
      "John Doe"    
    },
    "age": {
      "30"    
    }  
  }
}
}

```


### Input JSON

```json
{
  "root": {
    "person": {
      "name": "John"
    }
  }
}
```

### Output XML

```xml
<root>
  <person>
    <name>
      John
    </name>
  </person>
</root>

```

## Limitations and Future Improvements

### Current Limitations

- Limited support for XML namespaces.
- No support for XML comments in the output.
- Limited handling of mixed content (text and elements) in XML.

### Planned Improvements

- Add support for XML namespaces.
- Implement syntax highlighting in the GUI.
- Enhance error reporting with line numbers and detailed messages.
- Add support for XML comments and CDATA sections.
- Improve handling of mixed content in XML.

## How to Use

### Load XML/JSON File

- Use the "Load XML" or "Load JSON" button to load a file into the input text area.

### Convert

- Use the "Convert to JSON" or "Convert to XML" button to perform the conversion.

### Save Output

- Use the "Save JSON" or "Save XML" button to save the converted output to a file.

## Dependencies

- Java Development Kit (JDK) 8 or higher.
- JavaCC for parser generation.
- Swing for the graphical user interface.
