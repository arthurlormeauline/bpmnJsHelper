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

//<function id=46cf3a65-d793-4841-b8b1-e1889e9a9a1d>
Return_to_Last_Task_0() {
print('Return to flow is Running');
var errorTaskId = execution.getVariable('errorTaskId');
if(errorTaskId == null){
print('No where to go back to');
}else{          execution.getProcessEngineServices().
getRuntimeService().createProcessInstanceModification(execution.getProcessInstanceId()).startBeforeActivity(errorTaskId).
execute();
}
}
//</function>

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

//<function id=681dae6a-c400-4582-971a-eaf63d3af4df>
Create_Billing_Account_1() {
var order = S(execution.getVariable('subscription'));
var customerCategoryCode = order.prop("customerCategoryCode").value();
var date = order.prop("date").value();
var holder = order.prop("holder");
var channel = order.prop("channel");
var individual = holder.prop("individual");
var adress = individual.prop("adress");
var contactInformation = individual.prop("contactInformation");
var seller = channel.prop("organisationSellerId").value();
var externalRef = order.prop("contractId").value();
var title = individual.prop("title").value();
var partnerId = individual.prop("partnerId").value();
var firstName = individual.prop("firstName").value();
var lastName = individual.prop("lastName").value();
var description = partnerId + "." + firstName  + "." + lastName;
var birthDate = individual.prop("birthDate").value();
var birthLocality = individual.prop("birthLocality").value();
var birthCountry = individual.prop("birthCountry").value();
var locality = adress.prop("locality").value();
var zipCode = adress.prop("zipCode").value();
var countryName = adress.prop("countryName").value();
var addressLine1 = adress.prop("addressLine1").value();
var addressLine2 = adress.prop("addressLine2").value();
var addressLine3 = adress.prop("addressLine3").value();
var adressCustomer =
'{"locality":"' + locality + '"' +
',"postalCode":"' + zipCode + '"' +
',"countryName":"' + countryName + '"' +
',"addressLine1":"' + addressLine1 + '"' +
',"addressLine2":"' + addressLine2 + '"' +
',"addressLine3":"' + addressLine3 + '"}';
var email = contactInformation.prop("email").value();
var mobile = contactInformation.prop("mobile").value();
var phone = contactInformation.prop("phone").value();
var contactInformationCustomer =
'{"email":"' + email + '"' +
',"mobile":"' + mobile + '"' +
',"phone":"' + phone + '"}';

'{"subscriptionDate":"' + date + '"' +
',"nextInvoiceDate":"' + date + '"' +
',"crmAccountType":"ACCT_BA"' +
',"customerCategory":"' + customerCategoryCode + '"' +
',"code":"' + partnerId + '"' +
',"externalRef":"' + externalRef + '"' +
',"seller":"' + seller + '"' +
',"description":"' + description + '"' +
',"title":"' + title + '"' +
',"firstName":"' + firstName + '"' +
',"lastName":"' + lastName + '"' +
',"birthDate":"' + birthDate + '"' +
',"birthLocality":"' + birthLocality + '"' +
',"birthCountry":"' + birthCountry + '"' +
',"address":' + adressCustomer + ',' +
'"contactInformation":' + contactInformationCustomer + '}';
}
//</function>

//<function id=45dadd4e-a5bc-4b80-9843-5d48ff20f80d>
Create_Billing_Account_2() {
print('create billing account api');
var resp = connector.getVariable('response')
print('resp billing customer account' + resp);
var respStatusCode = connector.getVariable('statusCode')
print(respStatusCode );

var execution = connector.getParentVariableScope();
var activityId = execution.getCurrentActivityId();

if ( respStatusCode == 200 || respStatusCode == 201 || respStatusCode == 202 || respStatusCode == 204) {
 print('no error');

/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/
execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"SUCCESS")
execution.setVariable('responseMessage',response)
} else {
execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"FAIL")
execution.setVariable('responseMessage',response);

/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/
/* create incident*/

var IncidentEntity  = Java.type('org.camunda.bpm.engine.impl.persistence.entity.IncidentEntity');
	var IncidentContext = Java.type('org.camunda.bpm.engine.impl.incident.IncidentContext');
	var context = new IncidentContext();
  var parentScope = connector.getParentVariableScope()
	context.setActivityId(parentScope.getCurrentActivityId());
	context.setExecutionId(parentScope.getProcessInstanceId());
	context.setProcessDefinitionId(parentScope.getProcessDefinitionId());
	var newIncident  = IncidentEntity.createAndInsertIncident("Api Fail", context, response);
	newIncident.getId()
execution.setVariable('incidentId',newIncident.getId());
print('incientId'+newIncident.getId());
/*end create incident*/
 throw new org.camunda.bpm.engine.delegate.BpmnError('CheckError',activityId);
}
}
//</function>

//<function id=29f6386a-84b8-42ed-bc09-0f5e81dffa6c>
Create_User_Account_0() {
var url_tb= execution.getVariable('url_crm_customer');
url_tb+'/api/customer/v1/userAccount/'
}
//</function>

//<function id=d1a9f1d0-23d5-40b6-877b-c7eb92032c11>
Create_User_Account_1() {
var order = S(execution.getVariable('subscription'));
var customerCategoryCode = order.prop("customerCategoryCode").value();
var date = order.prop("date").value();
var holder = order.prop("holder");
var channel = order.prop("channel");
var individual = holder.prop("individual");
var adress = individual.prop("adress");
var contactInformation = individual.prop("contactInformation");
var seller = channel.prop("organisationSellerId").value();
var externalRef = order.prop("contractId").value();
var title = individual.prop("title").value();
var partnerId = individual.prop("partnerId").value();
var firstName = individual.prop("firstName").value();
var lastName = individual.prop("lastName").value();
var description = partnerId + "." + firstName  + "." + lastName;
var birthDate = individual.prop("birthDate").value();
var birthLocality = individual.prop("birthLocality").value();
var birthCountry = individual.prop("birthCountry").value();
var locality = adress.prop("locality").value();
var zipCode = adress.prop("zipCode").value();
var countryName = adress.prop("countryName").value();
var addressLine1 = adress.prop("addressLine1").value();
var addressLine2 = adress.prop("addressLine2").value();
var addressLine3 = adress.prop("addressLine3").value();
var adressCustomer =
'{"locality":"' + locality + '"' +
',"postalCode":"' + zipCode + '"' +
',"countryName":"' + countryName + '"' +
',"addressLine1":"' + addressLine1 + '"' +
',"addressLine2":"' + addressLine2 + '"' +
',"addressLine3":"' + addressLine3 + '"}';
var email = contactInformation.prop("email").value();
var mobile = contactInformation.prop("mobile").value();
var phone = contactInformation.prop("phone").value();
var contactInformationCustomer =
'{"email":"' + email + '"' +
',"mobile":"' + mobile + '"' +
',"phone":"' + phone + '"}';

'{"subscriptionDate":"' + date + '"' +
',"crmAccountType":"ACCT_UA"' +
',"customerCategory":"' + customerCategoryCode + '"' +
',"code":"' + partnerId + '"' +
',"externalRef":"' + externalRef + '"' +
',"seller":"' + seller + '"' +
',"description":"' + description + '"' +
',"title":"' + title + '"' +
',"firstName":"' + firstName + '"' +
',"lastName":"' + lastName + '"' +
',"birthDate":"' + birthDate + '"' +
',"birthLocality":"' + birthLocality + '"' +
',"birthCountry":"' + birthCountry + '"' +
',"address":' + adressCustomer + ',' +
'"contactInformation":' + contactInformationCustomer + '}';
}
//</function>

//<function id=71938128-416d-4500-8b83-f5d47e40a42c>
Create_User_Account_2() {
print('create USER ACCOUNT api');
var resp = connector.getVariable('response')
print('resp create USERACCOUNT' + resp);
var respStatusCode = connector.getVariable('statusCode')
print(respStatusCode );

var execution = connector.getParentVariableScope();
var activityId = execution.getCurrentActivityId();


if ( respStatusCode == 200 || respStatusCode == 201 || respStatusCode == 202 || respStatusCode == 204) {
 print('no error');

/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/

execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"SUCCESS")
execution.setVariable('responseMessage',response)
} else {
execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"FAIL")
execution.setVariable('responseMessage',response);

/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/
/* create incident*/

var IncidentEntity  = Java.type('org.camunda.bpm.engine.impl.persistence.entity.IncidentEntity');
	var IncidentContext = Java.type('org.camunda.bpm.engine.impl.incident.IncidentContext');
	var context = new IncidentContext();
  var parentScope = connector.getParentVariableScope()
	context.setActivityId(parentScope.getCurrentActivityId());
	context.setExecutionId(parentScope.getProcessInstanceId());
	context.setProcessDefinitionId(parentScope.getProcessDefinitionId());
	var newIncident  = IncidentEntity.createAndInsertIncident("Api Fail", context, response);
	newIncident.getId()
execution.setVariable('incidentId',newIncident.getId());
print('incientId'+newIncident.getId());
/*end create incident*/
 throw new org.camunda.bpm.engine.delegate.BpmnError('CheckError',activityId);
}
}
//</function>

//<function id=6e3cc2d0-39fb-48ea-bb0e-9fa3d964dd14>
Apply_discount_0() {
var url_tb= execution.getVariable('url_crm_customer');
var order = execution.getVariable('subscription');
var holder = S(order).prop("holder");
var individual = S(holder).prop("individual");
var billingAccountCode = S(individual).prop("partnerId").value();

url_tb+'/api/customer/v1/billingAccount/'+billingAccountCode+'/discounts'
}
//</function>

//<function id=e9ce7121-aab2-4c47-a0be-ddbcb34d863c>
Apply_discount_1() {
var order = execution.getVariable('subscription');
var discountCodes = S(order).prop("discountCodes").elements();
var discountPlanForInstantiation = S("[]");

for (var i=0; i < discountCodes.size(); i++) {
  var discount = discountCodes[i];
  var code = discount.prop("code").value();
  discount.remove("code");
  discount.prop("discountPlanid", code);

  discountPlanForInstantiation.append(discount);
}

'{"discountPlan":' + discountPlanForInstantiation.toString() +'}';
}
//</function>

//<function id=135db922-b959-46a2-8ee3-203aa9c33a93>
Apply_discount_2() {
print('Apply discount');
var resp = connector.getVariable('response')
print('resp Apply discount' + resp);
var respStatusCode = connector.getVariable('statusCode')
print(respStatusCode );

var execution = connector.getParentVariableScope();
var activityId = execution.getCurrentActivityId();

if ( respStatusCode == 200 || respStatusCode == 201 || respStatusCode == 202 || respStatusCode == 204) {
 print('no error');

/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/
execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"SUCCESS")
execution.setVariable('responseMessage',response)
} else {
execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"FAIL")
execution.setVariable('responseMessage',response);

/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/
/* create incident*/

var IncidentEntity  = Java.type('org.camunda.bpm.engine.impl.persistence.entity.IncidentEntity');
	var IncidentContext = Java.type('org.camunda.bpm.engine.impl.incident.IncidentContext');
	var context = new IncidentContext();
  var parentScope = connector.getParentVariableScope()
	context.setActivityId(parentScope.getCurrentActivityId());
	context.setExecutionId(parentScope.getProcessInstanceId());
	context.setProcessDefinitionId(parentScope.getProcessDefinitionId());
	var newIncident  = IncidentEntity.createAndInsertIncident("Api Fail", context, response);
	newIncident.getId()
execution.setVariable('incidentId',newIncident.getId());
print('incientId'+newIncident.getId());
/*end create incident*/
 throw new org.camunda.bpm.engine.delegate.BpmnError('CheckError',activityId);
}
}
//</function>

//<function id=8405558f-9726-4018-8e53-9d513bb4d4e8>
Refresh_Token_0() {
var url_tb= execution.getVariable('url_keycloack');
url_tb+'/realms/Protectline/protocol/openid-connect/token'
}
//</function>

//<function id=2e4a5a5b-3912-43f1-90cb-fcc23e0ca26e>
Refresh_Token_1() {
obj = JSON.parse(connector.getVariable('response'));
var execution = connector.getParentVariableScope();
execution.setVariable('token',obj.access_token);
}
//</function>

//<function id=ddde7429-6274-4bc3-a8b8-b883ef079d0a>
Create_Method_Of_Payment_0() {
var url_tb= execution.getVariable('url_crm_be');
url_tb+'/api/v1/orders/methodOfPayments'
}
//</function>

//<function id=8aa7bb64-6c56-4a10-88fa-7590fbee0bd7>
Create_Method_Of_Payment_1() {
execution.getVariable('subscription');
}
//</function>

//<function id=44e66867-f215-4e26-91f9-e6e8ba235cd9>
Create_Method_Of_Payment_2() {
print('create MethodOfPayment api');
var resp = connector.getVariable('response')
print('resp create methodOfPayment' + resp);
var respStatusCode = connector.getVariable('statusCode')
print(respStatusCode );

var execution = connector.getParentVariableScope();
var activityId = execution.getCurrentActivityId();


if ( respStatusCode == 200 || respStatusCode == 201 || respStatusCode == 202 || respStatusCode == 204) {
 print('no error');

/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/

execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"SUCCESS")
execution.setVariable('responseMessage',response)
} else {
execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"FAIL")
execution.setVariable('responseMessage',response);
/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/
/* create incident*/

var IncidentEntity  = Java.type('org.camunda.bpm.engine.impl.persistence.entity.IncidentEntity');
	var IncidentContext = Java.type('org.camunda.bpm.engine.impl.incident.IncidentContext');
	var context = new IncidentContext();
  var parentScope = connector.getParentVariableScope()
	context.setActivityId(parentScope.getCurrentActivityId());
	context.setExecutionId(parentScope.getProcessInstanceId());
	context.setProcessDefinitionId(parentScope.getProcessDefinitionId());
	var newIncident  = IncidentEntity.createAndInsertIncident("Api Fail", context, response);
execution.setVariable('incidentId',newIncident.getId());
print('incientId'+newIncident.getId());
/*end create incident*/
 throw new org.camunda.bpm.engine.delegate.BpmnError('CheckError',activityId);
}
}
//</function>

//<function id=8707715d-c6c9-4b8a-9db3-7b7e3c0c7935>
Create_Method_Of_Payment_3() {
var subscription = S(execution.getVariable('subscription'));
var payment = subscription.prop("payment");

if (payment.hasProp("bankAccount")) {
  var bankAccount = payment.prop("bankAccount");
  if (bankAccount.hasProp("iban")) {
    S(bankAccount).prop("iban", 'XXXXX');
  }
  if (bankAccount.hasProp("bic")) {
    S(bankAccount).prop("bic", 'XXXXX');
  }
}

subscription.toString();
}
//</function>

//<function id=9e430d3d-d7a7-440c-9823-7d2164fe01fd>
Create_Keycloak_Account_0() {
var url_tb= execution.getVariable('url_crm_customer');
url_tb+'/api/customer/v1/authenticationAccount'
}
//</function>

//<function id=ddf3bf50-54b6-4591-8587-a083693c12f0>
Create_Keycloak_Account_1() {
var order = S(execution.getVariable('subscription'));
var holder = order.prop("holder");
var channel = order.prop("channel");
var individual = holder.prop("individual");
var contactInformation = individual.prop("contactInformation");
var seller = channel.prop("organisationSellerId").value();
var partnerId = individual.prop("partnerId").value();
var firstName = individual.prop("firstName").value();
var lastName = individual.prop("lastName").value();
var email = contactInformation.prop("email").value();

'{"username":"' + partnerId + '"' +
',"email":"' + email + '"' +
',"firstName":"' + firstName + '"' +
',"lastName":"' + lastName + '"' +
',"seller":"' + seller + '"}';
}
//</function>

//<function id=bb888e2b-fba8-44f3-9dff-9475f24947aa>
Create_Keycloak_Account_2() {
print('create keycloak account api');
var resp = connector.getVariable('response')
print('resp create keycloak account' + resp);
var respStatusCode = connector.getVariable('statusCode')
print(respStatusCode );

/* get activity id from parent*/
var execution = connector.getParentVariableScope();
var activityId = execution.getCurrentActivityId();
/*end get activity id from parent*/


if ( respStatusCode == 200 || respStatusCode == 201 || respStatusCode == 202 || respStatusCode == 204) {
 print('no error');
execution.setVariable('keycloadUserPassword',resp);

/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/

execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"SUCCESS");
execution.setVariable('responseMessage',response);

// Clean up retries if successful
execution.removeVariable("retries");
} else{
execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"FAIL")
execution.setVariable('responseMessage',response);

/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/

/* create incident*/

var IncidentEntity  = Java.type('org.camunda.bpm.engine.impl.persistence.entity.IncidentEntity');
	var IncidentContext = Java.type('org.camunda.bpm.engine.impl.incident.IncidentContext');
	var context = new IncidentContext();
  var parentScope = connector.getParentVariableScope()
	context.setActivityId(parentScope.getCurrentActivityId());
	context.setExecutionId(parentScope.getProcessInstanceId());
	context.setProcessDefinitionId(parentScope.getProcessDefinitionId());
	var newIncident  = IncidentEntity.createAndInsertIncident("Api Fail", context, response);
execution.setVariable('incidentId',newIncident.getId());
print('incientId'+newIncident.getId());
/*end create incident*/

  var retries = execution.getVariable("retries");
  if (retries == null) {
    // First failure, set retries to 2 (out of 3)
    execution.setVariable("retries", 2);
  } else if (retries <= 0) {
    // No retries left - Do cleanup
    execution.removeVariable("retries");
  }

  throw new org.camunda.bpm.engine.delegate.BpmnError('CheckError',activityId);}
}
//</function>

//<function id=8785eb71-d317-4674-88d7-a1f4e22e0d9c>
Create_customer_0() {
var url_tb= execution.getVariable('url_crm_customer');
url_tb+'/api/customer/v1/customer/'
}
//</function>

//<function id=55282276-ab4b-4a86-97e3-e48209bc5165>
Create_customer_1() {
var order = S(execution.getVariable('subscription'));
var customerCategoryCode = order.prop("customerCategoryCode").value();
var holder = order.prop("holder");
var channel = order.prop("channel");
var individual = holder.prop("individual");
var adress = individual.prop("adress");
var contactInformation = individual.prop("contactInformation");
var seller = channel.prop("organisationSellerId").value();
var externalRef = order.prop("contractId").value();
var title = individual.prop("title").value();
var partnerId = individual.prop("partnerId").value();
var firstName = individual.prop("firstName").value();
var lastName = individual.prop("lastName").value();
var description = partnerId + "." + firstName  + "." + lastName;
var birthDate = individual.prop("birthDate").value();
var birthLocality = individual.prop("birthLocality").value();
var birthCountry = individual.prop("birthCountry").value();
var locality = adress.prop("locality").value();
var zipCode = adress.prop("zipCode").value();
var countryName = adress.prop("countryName").value();
var addressLine1 = adress.prop("addressLine1").value();
var addressLine2 = adress.prop("addressLine2").value();
var addressLine3 = adress.prop("addressLine3").value();
// Construire l'adresse du client
var adressCustomer = '{"locality":"' + locality + '",' +
                    '"postalCode":"' + zipCode + '",' +
                    '"countryName":"' + countryName + '",' +
                    '"addressLine1":"' + addressLine1 + '"';

if (addressLine2 !== null) {
    adressCustomer += ',"addressLine2":"' + addressLine2 + '"';
}

if (addressLine3 !== null) {
    adressCustomer += ',"addressLine3":"' + addressLine3 + '"';
}

adressCustomer += '}';

var email = contactInformation.prop("email").value();
var mobile = contactInformation.prop("mobile").value();
var phone = contactInformation.prop("phone").value();
// Construire les informations de contact du client
var contactInformationCustomer = '{"email":"' + email + '",' +
                                 '"mobile":"' + mobile + '"';

if (phone !== null) {
    contactInformationCustomer += ',"phone":"' + phone + '"';
}

contactInformationCustomer += '}';

// Récupérer la valeur de target
var target = order.prop("target").value();

// Initialiser le JSON commun
var json = '{"crmAccountType":"ACCT_CUST",' +
           '"customerCategory":"' + customerCategoryCode + '",' +
           '"code":"' + partnerId + '",' +
           '"externalRef":"' + externalRef + '",' +
           '"seller":"' + seller + '",' +
           '"description":"' + description + '",' +
           '"title":"' + title + '",' +
           '"firstName":"' + firstName + '",' +
           '"lastName":"' + lastName + '",' +
           '"birthDate":"' + birthDate + '",' +
           '"birthLocality":"' + birthLocality + '",';

if (order.hasProp('company') && order.prop('company') !== null) {
  var company = order.prop('company').toString();
  json += '"company":' + company + ',';
}

// Vérifier si birthCountry est null ou non
if (birthCountry === null) {
    json += '"birthCountry":' + birthCountry + ',';
} else {
    json += '"birthCountry":"' + birthCountry + '",';
}

// Ajouter l'adresse et les informations de contact
json += '"address":' + adressCustomer + ',' +
        '"contactInformation":' + contactInformationCustomer + ',';

// Vérifier si target est null ou non
if (target === null) {
    // Si target est null, ajouter target sans guillemets
    json += '"target":' + target + '}';
} else {
    // Si target n'est pas null, ajouter target avec guillemets
    json += '"target":"' + target + '"}';
}

// Utilisation de l'objet JSON
json
}
//</function>

//<function id=3acb3e8a-feb0-452c-a20d-c08e405779c5>
Create_customer_2() {
print('create customer api');
var resp = connector.getVariable('response')
print('resp create customer' + resp);
var respHeader = connector.getVariable('headers')
//print(respHeader);
var respStatusCode = connector.getVariable('statusCode')
print(respStatusCode );


var execution = connector.getParentVariableScope();
var activityId = execution.getCurrentActivityId();

if ( respStatusCode == 200 || respStatusCode == 201 || respStatusCode == 202 || respStatusCode == 204) {
 print('no error');

/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/

execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"SUCCESS")
execution.setVariable('responseMessage',response)
} else {
execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"FAIL")
execution.setVariable('responseMessage',response);
/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/
/* create incident*/

var IncidentEntity  = Java.type('org.camunda.bpm.engine.impl.persistence.entity.IncidentEntity');
	var IncidentContext = Java.type('org.camunda.bpm.engine.impl.incident.IncidentContext');
	var context = new IncidentContext();
  var parentScope = connector.getParentVariableScope()
	context.setActivityId(parentScope.getCurrentActivityId());
	context.setExecutionId(parentScope.getProcessInstanceId());
	context.setProcessDefinitionId(parentScope.getProcessDefinitionId());
	var newIncident  = IncidentEntity.createAndInsertIncident("Api Fail", context, response);
	newIncident.getId()
execution.setVariable('incidentId',newIncident.getId());
print('incientId'+newIncident.getId());
/*end create incident*/
throw new org.camunda.bpm.engine.delegate.BpmnError('CheckError',activityId);
}
}
//</function>

//<function id=7aa22ad8-031f-47ed-8abe-29da37f8319f>
Create_Customer_Account_0() {
var url_tb= execution.getVariable('url_crm_customer');
url_tb+'/api/customer/v1/customerAccount/'
}
//</function>

//<function id=d04a2515-5601-4534-8074-6babcb689335>
Create_Customer_Account_1() {
var order = S(execution.getVariable('subscription'));
var customerCategoryCode = order.prop("customerCategoryCode").value();
var holder = order.prop("holder");
var channel = order.prop("channel");
var individual = holder.prop("individual");
var adress = individual.prop("adress");
var contactInformation = individual.prop("contactInformation");
var seller = channel.prop("organisationSellerId").value();
var externalRef = order.prop("contractId").value();
var title = individual.prop("title").value();
var partnerId = individual.prop("partnerId").value();
var firstName = individual.prop("firstName").value();
var lastName = individual.prop("lastName").value();
var description = partnerId + "." + firstName  + "." + lastName;
var birthDate = individual.prop("birthDate").value();
var birthLocality = individual.prop("birthLocality").value();
var birthCountry = individual.prop("birthCountry").value();
var locality = adress.prop("locality").value();
var zipCode = adress.prop("zipCode").value();
var countryName = adress.prop("countryName").value();
var addressLine1 = adress.prop("addressLine1").value();
var addressLine2 = adress.prop("addressLine2").value();
var addressLine3 = adress.prop("addressLine3").value();
var adressCustomer =
'{"locality":"' + locality + '"' +
',"postalCode":"' + zipCode + '"' +
',"countryName":"' + countryName + '"' +
',"addressLine1":"' + addressLine1 + '"' +
',"addressLine2":"' + addressLine2 + '"' +
',"addressLine3":"' + addressLine3 + '"}';
var email = contactInformation.prop("email").value();
var mobile = contactInformation.prop("mobile").value();
var phone = contactInformation.prop("phone").value();
var contactInformationCustomer =
'{"email":"' + email + '"' +
',"mobile":"' + mobile + '"' +
',"phone":"' + phone + '"}';

'{"crmAccountType":"ACCT_CA"' +
',"customerCategory":"' + customerCategoryCode + '"' +
',"code":"' + partnerId + '"' +
',"externalRef":"' + externalRef + '"' +
',"seller":"' + seller + '"' +
',"description":"' + description + '"' +
',"title":"' + title + '"' +
',"firstName":"' + firstName + '"' +
',"lastName":"' + lastName + '"' +
',"birthDate":"' + birthDate + '"' +
',"birthLocality":"' + birthLocality + '"' +
',"birthCountry":"' + birthCountry + '"' +
',"address":' + adressCustomer + ',' +
'"contactInformation":' + contactInformationCustomer + '}';
}
//</function>

//<function id=3ea0492d-500f-4c53-b8fa-628d7e259c85>
Create_Customer_Account_2() {
print('create customer account api');
var resp = connector.getVariable('response')
print('resp create customer account' + resp);
var respStatusCode = connector.getVariable('statusCode')
print(respStatusCode );

var execution = connector.getParentVariableScope();
var activityId = execution.getCurrentActivityId();


if ( respStatusCode == 200 || respStatusCode == 201 || respStatusCode == 202 || respStatusCode == 204) {
 print('no error');

/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);}
/* end solving incident*/

execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"SUCCESS")
execution.setVariable('responseMessage',response)
} else {
execution.setVariable('httpCode',respStatusCode);
execution.setVariable('state',"FAIL")
execution.setVariable('responseMessage',response);
/*solve incident if exist*/
print('incientId : '+execution.getVariable('incidentId'));
if (execution.getVariable('incidentId')!=null)
{
execution.getProcessEngineServices().getRuntimeService().createIncidentQuery().incidentId(execution.getVariable('incidentId')).singleResult().resolve();
execution.setVariable('incidentId',null);
}
/* end solving incident*/
/* create incident*/

var IncidentEntity  = Java.type('org.camunda.bpm.engine.impl.persistence.entity.IncidentEntity');
	var IncidentContext = Java.type('org.camunda.bpm.engine.impl.incident.IncidentContext');
	var context = new IncidentContext();
  var parentScope = connector.getParentVariableScope()
	context.setActivityId(parentScope.getCurrentActivityId());
	context.setExecutionId(parentScope.getProcessInstanceId());
	context.setProcessDefinitionId(parentScope.getProcessDefinitionId());
	var newIncident  = IncidentEntity.createAndInsertIncident("Api Fail", context, response);
	newIncident.getId()
execution.setVariable('incidentId',newIncident.getId());
print('incientId'+newIncident.getId());
/*end create incident*/
 throw new org.camunda.bpm.engine.delegate.BpmnError('CheckError',activityId);
}
}
//</function>


//</main>
}

module.exports = BpmnRunner;
