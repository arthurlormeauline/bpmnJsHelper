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

    //<MAIN>
}

module.exports = BpmnRunner;
