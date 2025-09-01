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

//<function id=435ec209-a46a-47b3-809a-fe263f4133a0>
    increment_retries_0() {
        var nbRetry= execution.getVariable("nbRetry");
        if(nbRetry!=null){
            nbRetry=nbRetry+1;
        }else{
            nbRetry=0;
        }
        var duration=null;
        switch(nbRetry){
            case 0:
                duration="PT0S";
                break;
            case 1:
                duration="PT15S";
                break;
            case 2:
                duration="PT1M";
                break;
            case 3:
                duration="PT2M"
                break;
        }
        execution.setVariable("status", "inactive");
        execution.setVariable("duration",duration);
        execution.setVariable("nbRetry", nbRetry);
        print("Retry number : "+ nbRetry)
    }
//</function>

//<function id=477d9866-772b-4634-a60d-544bf540c29e>
    Delay_definition_0() {
        var printDebug = (msg, execution) => {
            var cameraId = execution.getVariable("constructorId");
            var customerId = execution.getVariable("customerId");
            var today = new Date();
            var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
            if (execution.getVariable("debug") === true) {
                print("DEBUG FOR CUSTOMER : " + customerId + " AND CAMERA : " + cameraId + " TIME : " + time + " ... [" + msg + "]");
            } else {
                print(msg);
            }
        }
        printDebug("Delay definition", execution);
        var dateTimeGateway = execution.getVariable('dateTimeGateway');
        var time = Date.parse(dateTimeGateway);
        var actual = Date.now();
        const isCamExt = execution.getVariable('deviceType') == "cameraExt";
        const mode = execution.getVariable('mode');
        var desiredDelay = 300000;
        var exitDelay = execution.getVariable('exitDelay');
        var stayExitDelay = execution.getVariable('stayExitDelay');
        if (mode == "total") {
            desiredDelay = (exitDelay || exitDelay === 0) ? (exitDelay * 1000) : 60000;
            printDebug("Total mode. DesiredDelay : " + desiredDelay, execution);
        } else {
            desiredDelay = (stayExitDelay || stayExitDelay === 0) ? (exitDelay * 1000) : 60000;
            printDebug("Partial mode. DesiredDelay : " + desiredDelay, execution);
        }
        var diff = (actual - time) / 1000;
        printDebug("Diff : " + diff, execution);
        printDebug("remain = (desired - (actual -time))/1000", execution);
        var remain_delay = (desiredDelay - (actual - time)) / 1000;
        printDebug("RemainDelay : " + remain_delay, execution);
        if (remain_delay > desiredDelay) {
            remain_delay = 60;
        }
        if (remain_delay < 0) {
            remain_delay = 0;
        }
        var today = new Date();
        var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
        var iso = 'PT' + Math.floor(remain_delay) + 'S';
        if (isCamExt) {
            const mm = Math.floor(remain_delay / 60);
            const ss = Math.floor(remain_delay - mm * 60);
            iso = 'PT' + mm + 'M' + ss + 'S';
        }
        printDebug("Duration " + iso + "for the camera " + execution.getVariable('constructorId'), execution);
        printDebug("DELAY BEGIN", execution);
        execution.setVariable('duration', iso);
    }
//</function>

//<function id=a342aba1-e659-4d8c-9890-e7a7e4fe2e4b>
    Désactivation_caméra_0() {
        var url_device= execution.getVariable('url_device');
        var cameraId = execution.getVariable('constructorId');
        execution.setVariable('status', 'inactive');
        print('status of sercomm : ' +status);
        print('mac sercomm ' + cameraId);
        var today = new Date();
        var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
        print(time);
        url_device + 'gateways/' + cameraId + '/state?mode=total&status='+status;
    }
//</function>

//<function id=6f645d9a-f482-4a31-9323-149947b7d409>
    Désactivation_caméra_1() {
        /* get activity id from parent*/
        var execution = connector.getParentVariableScope();
        var activityId = execution.getCurrentActivityId();
        /*end get activity id from parent*/
        var resp = connector.getVariable('response');
        print('update camera Sercomm state to inactive : ' + resp);
        var respStatusCode = connector.getVariable('statusCode');
        print(respStatusCode);
        var today = new Date();
        var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
        print(time);
        if (respStatusCode == 200 || respStatusCode == 201 || respStatusCode == 202 || respStatusCode == 204) {
            execution.setVariable('httpCodeDevice', respStatusCode);
            execution.setVariable('stateDevice', "SUCCESS");
            execution.setVariable('deviceObject', response);
            var obj = JSON.parse(response);
        } else {
            execution.setVariable('httpCodeDevice', respStatusCode);
            print("Error to patch state");
        }
    }
//</function>

//<function id=a0d9eb43-9ffa-4138-9159-0b19d119132a>
    Send_push__0() {
        print("Send push when error to patch camera");
        function uuidv4() {
            return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
                var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            });
        }
        var deviceId = execution.getVariable('constructorId');
        var supplier = execution.getVariable('supplier');
        var customerId = execution.getVariable('customerId');
        var dateTimeGateway = execution.getVariable('dateTimeGateway');
        var location = execution.getVariable('location');
        var url = execution.getVariable('url_notif');
        var deviceType = execution.getVariable('deviceType');
        var status = execution.getVariable('status');
        if(status == "active"){
            execution.setVariable("typePush", "Activation");
        }else{
            execution.setVariable("typePush", "Désactivation");
        }
        var obj = {
            metadata:{
                messageId: uuidv4(),
                callerId: "teamUsages.camunda",
                messageType: "POST"
            },
            supplier: supplier,
            customerId: customerId,
            push: {
                eventType: "usagesErreur",
                event:{
                    gatewayId: deviceId,
                    deviceId: deviceId,
                    dateTimeGateway: dateTimeGateway,
                    type : execution.getVariable("typePush"),
                    label: "appearance",
                    location : location,
                    deviceType : deviceType
                }
            }
        }
        var json= JSON.stringify(obj);
        print(json);
        execution.setVariable("json", json);
        url + '/pushNotifs';
    }
//</function>

//<function id=e7b04604-b8ac-4979-ae36-2935e4858d2d>
    Send_push__1() {
        statusCode;
    }
//</function>

//<function id=694fc198-6394-450e-a263-e26fad43844d>
    Send_push__2() {
        var execution = connector.getParentVariableScope();
        if (statusCode != 200) {
            print("Push KO");
        } else {
            execution.setVariable('stateSendNotif', "SUCCESS");
            print("Push OK");
        }
    }
//</function>

//<function id=2e384222-a4c0-4f82-8bb4-35193218313f>
    Get_state_0() {
        var cameraId = execution.getVariable("constructorId");
        var customerId = execution.getVariable("customerId");
        var today = new Date();
        var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
        if (execution.getVariable("debug") === true) {
            print("DEBUG FOR CUSTOMER : " + customerId + " AND CAMERA : " + cameraId + " TIME : " + time + " ... [" + "DELAY END" + "]");
        } else {
            print("DELAY END");
        }
        var url_device = execution.getVariable('url_device');
        var cameraId = execution.getVariable('constructorId');
        var crowId = execution.getVariable('crowGatewayId');
        print("Get state");
        url_device + 'gateways/' + crowId + '/state';
    }
//</function>

//<function id=f6e9f40c-0a65-46a8-a001-2dd6ce10fc0b>
    Get_state_1() {
        /* get activity id from parent*/
        var execution = connector.getParentVariableScope();
        var activityId = execution.getCurrentActivityId();
        /*end get activity id from parent*/
        var respStatusCode = connector.getVariable('statusCode');
        print(respStatusCode);
        if (respStatusCode == 200 || respStatusCode == 201 || respStatusCode == 202 || respStatusCode == 204) {
            var resp = connector.getVariable('response');
            var obj = JSON.parse(resp);
            print("get status " + obj.status);
            execution.setVariable("status", obj.status);
            print('get camera state : ' + response);
            var today = new Date();
            var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
            print(time);
            execution.setVariable('httpCodeDevice', respStatusCode);
            execution.setVariable('stateDevice', "SUCCESS");
            execution.setVariable('deviceObject', response);
            var obj = JSON.parse(response);
        } else {
            print("Get State fail");
            execution.setVariable('httpCodeDevice', respStatusCode);
            execution.setVariable('stateDevice', "FAIL");
            execution.setVariable('deviceObject', null);
        }
    }
//</function>

//<function id=24f00cfd-ae4d-46f7-9442-4f7f2e46eea5>
    Patch_camera_state_0() {
        var url_device = execution.getVariable('url_device');
        var mode = execution.getVariable('mode');
        var status = execution.getVariable('status');
        if (status == "ongoing") {
            status = "active";
        }
        var cameraId = execution.getVariable('constructorId');
        print("Patch camera sate ; mode : " + mode + " status : " + status);
        url_device + 'gateways/' + cameraId + '/state?mode=' + mode + '&status=' + status;
    }
//</function>

//<function id=fdde8915-f4e4-45f4-ab7e-d3838bffb76f>
    Patch_camera_state_1() {
        /* get activity id from parent*/
        var execution = connector.getParentVariableScope();
        var activityId = execution.getCurrentActivityId();
        /*end get activity id from parent*/
        var resp = connector.getVariable('response');
        print('update camera state : ' + resp);
        var respStatusCode = connector.getVariable('statusCode');
        print(respStatusCode);
        var today = new Date();
        var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
        print(time);
        if (respStatusCode == 200 || respStatusCode == 201 || respStatusCode == 202 || respStatusCode == 204) {
            print("Patch camera state SUCCESS");
            execution.setVariable('httpCodeDevice', respStatusCode);
            execution.setVariable('stateDevice', "SUCCESS");
            execution.setVariable('deviceObject', response);
            var obj = JSON.parse(response);
        } else {
            print("Patch camera state FAIL");
            print("Error to patch state");
            throw new org.camunda.bpm.engine.delegate.BpmnError("errorPatch");
        }
    }
//</function>

//<function id=ab3b870e-62fd-40f7-a2ee-e6afcdfd8a7b>
    Get_state_0() {
        var url_device= execution.getVariable('url_device');
        var cameraId = execution.getVariable('constructorId');
        url_device + 'gateways/' + cameraId + '/state';
    }
//</function>

//<function id=0c393b07-6ad4-44f0-8a03-5219ba7c229b>
    Get_state_1() {
        /* get activity id from parent*/
        var execution = connector.getParentVariableScope();
        var activityId = execution.getCurrentActivityId();
        /*end get activity id from parent*/
        var resp = connector.getVariable('response');
        var respStatusCode = connector.getVariable('statusCode');
        var cameraId = execution.getVariable('constructorId')
        print("Get state "+ cameraId +" respStatusCode " + resp );
        var topic;
        var obj = JSON.parse(resp);
        if (respStatusCode == 200) {
            topic= 'teamusages.notification.push.error.desactivation.200';
        }
        if (respStatusCode == 504 && obj.errorCode == "50401") {
            topic= 'teamusages.notification.push.error.desactivation.504';
        }
        if (respStatusCode == 504 && obj.errorCode == "50404") {
            topic= 'teamusages.notification.push.error.desactivation.500';
        }
        if (respStatusCode == 500) {
            topic= 'teamusages.notification.push.error.desactivation.500';
        }
        execution.setVariable('bodyMessage', resp);
        execution.setVariable('topic', topic);
    }
//</function>

//<function id=aa8f4665-3b31-4d2b-af9e-96f7778ee77f>
    Send_message_Kafka_0() {
        '{"response": "'+ execution.getVariable('bodyMessage')+'"}'
    }
//</function>

//<function id=44d7074b-136b-4317-a3ed-2d13c0af02e1>
    Send_message_Kafka_1() {
        var resp = connector.getVariable('response')
        var respStatusCode = connector.getVariable('statusCode')
        print("Response status api send message kafka to TLS: "+respStatusCode + " " +resp);
    }
//</function>

//<function id=bacbb517-b08e-4ed6-a202-27ae46c0ad3d>
    Send_message_Kafka_0() {
        '{"gatewayId": "'+ execution.getVariable('constructorId')+'"}'
    }
//</function>

//<function id=efbf2e49-f128-4f97-b7d8-fec8212e7bf0>
    Send_message_Kafka_1() {
        var resp = connector.getVariable('response')
        var respStatusCode = connector.getVariable('statusCode')
        print("Response status api send message kafka to TLS: " + respStatusCode + " " + resp);
    }
//</function>

//<function id=2ebceee5-fd2e-4918-a0cd-0be1b946b5cd>
    Get_device_by_name_0() {
        var tbFacade_url = execution.getVariable('url_tbFacade');
        var deviceName = execution.getVariable("constructorId");
        print("Get device with name: ");
        tbFacade_url + '/api/device/' + deviceName;
    }
//</function>

//<function id=d251766d-421d-43c6-ba50-1f0bb07c34b7>
    Get_device_by_name_1() {
        /* get activity id from parent*/
        var execution = connector.getParentVariableScope();
        var activityId = execution.getCurrentActivityId();
        /*end get activity id from parent*/
        var respStatusCode = connector.getVariable('statusCode');
        print(respStatusCode);
        var printDebug = (msg, execution) => {
            var cameraId = execution.getVariable("constructorId");
            var customerId = execution.getVariable("customerId");
            var today = new Date();
            var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
            if (execution.getVariable("debug") === true) {
                print("DEBUG FOR CUSTOMER : " + customerId + " AND CAMERA : " + cameraId + " TIME : " + time + " ... [" + msg + "]");
            } else {
                print(msg);
            }
        }
        printDebug("GET DEVICE BY NAME", execution);
        var exitDelay = execution.getVariable('exitDelayTime');
        var stayExitDelay = execution.getVariable('stayExitDelayTime');
        printDebug("Default exitDelaytime from shared metadata : " + exitDelay, execution);
        printDebug("Default stayExitDelaytime from shared metadata : " + stayExitDelay, execution);
        if (respStatusCode == 200 || respStatusCode == 201 || respStatusCode == 202 || respStatusCode == 204) {
            var resp = connector.getVariable('response');
            var obj = JSON.parse(resp);
            if (obj.attributeData.exitDelay != undefined) {
                printDebug("Exit delay : " + obj.attributeData.exitDelay, execution);
                printDebug("Set exitDelay from device shared attributes", execution);
                exitDelay = obj.attributeData.exitDelay;
            } else {
                printDebug("No exit delay in device shared attributes, keep default value", execution);
            }
            if (obj.attributeData.stayExitDelay != undefined) {
                printDebug("Stay exit delay : " + obj.attributeData.stayExitDelay, execution);
                printDebug("Set stayExitDelay from device shared attributes", execution);
                stayExitDelay = obj.attributeData.stayExitDelay;
            } else {
                printDebug("No stay exit delay in device shared attributes, keep default value", execution);
            }
            execution.setVariable('httpCode=', respStatusCode);
            execution.setVariable('state=', "SUCCESS");
        } else {
            execution.setVariable('httpCodeDevice', respStatusCode);
            execution.setVariable('stateDevice', "FAIL");
            execution.setVariable('deviceObject', null);
            printDebug("Get device by name failed; use exitDelayTime and stayExitDelayTime from shared attributes", execution);
        }
        execution.setVariable("exitDelay", exitDelay);
        execution.setVariable("stayExitDelay", stayExitDelay);
    }
//</function>

//<function id=d05b1f29-fe9c-4ede-8ea3-b81ce6806bd3>
    Event_1u1d1qi_0() {
        var cameraId = execution.getVariable("constructorId");
        var customerId = execution.getVariable("customerId");
        var today = new Date();
        var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
        if (execution.getVariable("debug") === true) {
            print("DEBUG FOR CUSTOMER : " + customerId + " AND CAMERA : " + cameraId + " TIME : " + time + " ... [" + "Action combine : begin" + "]");
        } else {
            print("Action combine : begin");
        }
    }
//</function>


//</main>
}

module.exports = BpmnRunner;
