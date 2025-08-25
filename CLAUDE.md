# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

JsBpmnHelper is a bidirectional converter between BPMN (Business Process Model and Notation) files and JavaScript projects. The application can:
- Convert BPMN files to JavaScript projects (`-toJs` option)
- Convert JavaScript projects back to BPMN files (`-toBpmn` option)

## Commands

### Build and Compilation
```bash
mvn compile
mvn package
```

### Running the Application
```bash
java -jar target/JsBpmnHelper-1.0-SNAPSHOT.jar <processName> [-toJs | -toBpmn]
```

Examples:
- `java -jar target/JsBpmnHelper-1.0-SNAPSHOT.jar myProcess` (defaults to -toJs)
- `java -jar target/JsBpmnHelper-1.0-SNAPSHOT.jar myProcess -toJs`
- `java -jar target/JsBpmnHelper-1.0-SNAPSHOT.jar myProcess -toBpmn`

### Running Tests
```bash
mvn test
```

### Running a Single Test
```bash
mvn test -Dtest=BpmnToJSTest
```

## Architecture

### Core Architecture
The project follows a bidirectional conversion pattern with two main flows:

1. **BPMN → JS Project**: `BpmnToJS` → `BlockBuilder` → `JsProject`
2. **JS Project → BPMN**: `JsProjectParser` → `ElementBuilder` → BPMN update

### Key Packages

#### `com.protectline.tojsproject`
- `BpmnToJS`: Main class for converting BPMN files to JavaScript projects
- `model.element.Element`: Represents BPMN XML elements with name, attributes, children, and content
- `model.element.Attribute`: Renamed from `BpmnAttribute`, represents XML attributes
- `model.block.Block`: Represents JavaScript code blocks (currently minimal implementation)

#### `com.protectline.tobpmn`
- `JsProjectToBpmn`: Main class for converting JavaScript projects back to BPMN
- `jsprojectparser.JsProjectParser`: Parses JavaScript project structure

### Model Classes
- **Element**: Core model for BPMN XML structure with hierarchical element representation
- **Block**: Represents JavaScript code structures (implementation in progress)
- **Attribute**: Key-value pairs for XML attributes

### Dependencies
- **Java 21**: Target compilation version
- **JUnit Jupiter**: Testing framework
- **Jackson XML**: XML processing (`jackson-dataformat-xml`)
- **Commons IO**: File operations
- **Guava**: Google's core libraries

### Test Resources
The `src/test/resources/input/` directory contains various BPMN files for testing different scenarios including customer processes, sales pipelines, subscription workflows, and contract management processes.

### Development Notes
- The codebase appears to be in active development with some incomplete implementations (e.g., `Block` class is mostly empty)
- Missing implementations: `BlockSelector`, `ElementBuilder`, `ElementSelector`, `BlockBuilder`, `JsProject` classes are referenced but not yet implemented
- Test case in `BpmnToJSTest` outlines the expected roundtrip conversion workflow but is not yet implemented