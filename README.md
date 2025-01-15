# XML to JSON Parser

A Java-based XML to JSON converter using JavaCC parser generator with a Swing GUI interface.

## Project Structure
```
converter/
├── src/
│   └── converter/
│      │
│      └── XMLToJSONConverter.java
│      │
│      └── XMLParser.jj
│     
└── README.md
```

## Grammar Definition

### BNF (Backus-Naur Form)
```bnf
<document> ::= <element> EOF
<element>  ::= "<" <name> ">" <content> "</" <name> ">" 
             | "<" <name> "/>"
<content>  ::= (<text> | <element>)*
<name>     ::= [a-zA-Z_] [a-zA-Z0-9_-]*
<text>     ::= [^<>]+
```

### EBNF (Extended Backus-Naur Form)
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

## Token Definitions
```
SKIP : {
    " "
    | "\t"
    | "\n"
    | "\r"
}

TOKEN : {
    < OPEN_TAG: "<" >
    | < CLOSE_TAG: ">" >
    | < END_TAG: "</" >
    | < SELF_CLOSE: "/>" >
    | < NAME: ["a"-"z", "A"-"Z", "_"](["a"-"z", "A"-"Z", "0"-"9", "_", "-"])* >
    | < CONTENT: (~["<", ">"])+ >
}
```

## Functional Description

### Parser Component (`XMLParser`)
1. **Input Processing**
   - Tokenizes XML input using JavaCC-generated lexer
   - Validates XML structure during parsing
   - Maintains tag stack for nested element validation

2. **JSON Conversion**
   - Converts XML elements to JSON objects
   - Handles nested structures
   - Maintains proper indentation
   - Escapes special characters

3. **Error Handling**
   - Validates matching tags
   - Reports syntax errors with descriptive messages
   - Maintains parsing state for error recovery

### GUI Component (`XMLToJSONConverter`)
1. **User Interface**
   - Split pane layout for input/output
   - XML input text area
   - JSON output text area
   - Control buttons for operations

2. **File Operations**
   - Load XML files
   - Save JSON output
   - File chooser dialogs

3. **Conversion Features**
   - Real-time conversion
   - Error reporting
   - Format preservation

## Sample Input/Output

### Input XML
```xml
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
        "content": "John Doe"
      },
      "age": {
        "content": "30"
      }
    }
  }
}
```

## Limitations and Future Improvements
1. Currently does not support:
   - XML attributes
   - CDATA sections
   - XML declarations
   - DTD validation
   - Namespaces

2. Planned improvements:
   - Add support for XML attributes
   - Implement syntax highlighting
   - Add validation for special XML characters
   - Support for XML comments
   - Enhanced error reporting with line numbers
