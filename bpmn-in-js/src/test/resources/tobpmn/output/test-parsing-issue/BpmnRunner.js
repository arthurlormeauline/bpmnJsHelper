// Classes de contexte
class ExecutionContext {
    constructor() {
        this.variables = new Map();
    }

    getVariable(key) {
        return this.variables.get(key);
    }

    setVariable(key, value) {
        this.variables.set(key, value);
    }

    getVariables() {
        return Object.fromEntries(this.variables);
    }

    getCurrentActivityId() {
        return 'current-activity-id';
    }
}

class ConnectorContext {
    constructor() {
        this.variables = new Map();
    }

    getVariable(key) {
        return this.variables.get(key);
    }

    setVariable(key, value) {
        this.variables.set(key, value);
    }

    getParentVariableScope() {
        return execution;
    }
}

// Variables globales pour compatibilité avec les scripts BPMN
const execution = new ExecutionContext();
const connector = new ConnectorContext();

function print(message) {
    console.log(message);
}

// Variables globales pour rendre les objets disponibles dans les scripts
global.execution = execution;
global.connector = connector;
global.print = print;

class BpmnRunner {
    constructor() {
        this.execution = execution;
        this.connector = connector;
    }

    print(message) {
        console.log(message);
    }

//<main>

//<function id=simple-function-1>
simpleFunction_0() {
//NEW :
print('This is a simple function');
execution.setVariable('test', 'value1');
}
//</function>

//<function id=simple-function-2>
simpleFunction_1() {
//NEW :
var result = execution.getVariable('test');
print('Result: ' + result);
}
//</function>

//<function id=problematic-function>
problematicFunction_0() {
//NEW :
print('Starting problematic function');
if (execution.getVariable('condition')) {
    // Commentaire with } closing brace in comment
    var obj = { key: "value", nested: { inner: true } };
    execution.setVariable('result', obj);
}
/* Another comment with } brace */
execution.setVariable('state', 'completed');
}
//</function>

//<function id=complex-json-function>
complexJsonFunction_0() {
//NEW :
var complexObj = {
    "data": {
        "items": [
            { "id": 1, "name": "item1" },
            { "id": 2, "name": "item2" }
        ],
        "metadata": {
            "count": 2,
            "filters": { "active": true }
        }
    }
};
execution.setVariable('complexData', JSON.stringify(complexObj));
}
//</function>


//</main>
}

module.exports = BpmnRunner;
