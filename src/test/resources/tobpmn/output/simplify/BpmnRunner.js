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

//<880ed997-78b2-4ff6-bb02-9eeadfa6bcb7>
Delay_definition_0() {
NEW : delay definition script
}
//<880ed997-78b2-4ff6-bb02-9eeadfa6bcb7/>

//<74b70353-a19e-4ad7-9838-5251ab67d8a6>
Get_device_by_name_0() {
NEW : get device by name url script
}
//<74b70353-a19e-4ad7-9838-5251ab67d8a6/>

//<a96073f8-befb-430d-b8a0-466100ca5bc9>
Get_device_by_name_1() {
NEW : get device by name output script
}
//<a96073f8-befb-430d-b8a0-466100ca5bc9/>

//<da508040-ebc4-4267-82fb-d7d4576e136e>
Event_1u1d1qi_0() {
NEW : start event script
}
//<da508040-ebc4-4267-82fb-d7d4576e136e/>


//<MAIN/>
}

module.exports = BpmnRunner;
