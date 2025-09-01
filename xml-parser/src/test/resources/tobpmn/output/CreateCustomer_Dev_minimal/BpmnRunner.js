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

//<function id=32cb633b-0bb2-4c27-89fd-8812410863c6>
retries_1_0() {
var retries = execution.getVariable("retries");

// Decrement
retries = retries - 1;

// Don't allow negative retries
if (retries < 0) {
  retries = 0;
}

execution.setVariable("retries", retries);
print('Retrying failed task and changing retries variable to ' + retries);
}
//</function>

//<function id=f15c404f-979e-440d-bbf0-04c98580d03b>
Create_Billing_Account_0() {
    var url_tb= execution.getVariable('url_crm_customer');
    url_tb+'/api/customer/v1/billingAccount/'
}
//</function>




//</main>
}

module.exports = BpmnRunner;