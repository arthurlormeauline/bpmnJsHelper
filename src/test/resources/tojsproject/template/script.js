const BpmnRunner = require('./bpmnRunner.js');

// Créer une instance du runner (les variables globales sont déjà définies dans le module)
const runner = new BpmnRunner();

// Définir quelques variables de test par défaut
execution.setVariable('duration', 'PT30S');
execution.setVariable('url_device', 'http://localhost:8080/api/');
execution.setVariable('constructorId', 'camera-001');
execution.setVariable('deviceType', 'camera');
execution.setVariable('mode', 'total');
execution.setVariable('status', 'inactive');

console.log('BpmnRunner initialisé avec des variables de test par défaut');
console.log('Variables disponibles:', execution.getVariables());
console.log('');

// Si un nom de méthode est passé en argument, l'exécuter
const methodName = process.argv[2];

if (methodName) {
    if (typeof runner[methodName] === 'function') {
        console.log(`🚀 Exécution de la méthode: ${methodName}`);
        console.log('=====================================');
        
        // Afficher les variables avant exécution
        console.log('Variables AVANT exécution:', execution.getVariables());
        console.log('');
        
        try {
            // Point d'arrêt potentiel pour le debugger
            debugger;
            
            runner[methodName]();
            
            // Afficher les variables après exécution
            console.log('');
            console.log('Variables APRÈS exécution:', execution.getVariables());
        } catch (error) {
            console.error('❌ Erreur lors de l\'exécution:', error.message);
            console.error('Stack trace:', error.stack);
        }
    } else {
        console.error(`❌ Méthode '${methodName}' non trouvée.`);
        console.log('📋 Méthodes disponibles:');
        Object.getOwnPropertyNames(Object.getPrototypeOf(runner))
            .filter(method => method !== 'constructor' && method !== 'print' && typeof runner[method] === 'function')
            .forEach(method => console.log(`- ${method}`));
    }
} else {
    console.log('Usage: node script.js <nom_de_methode>');
    console.log('');
    console.log('Exemples:');
    console.log('  node script.js increment_retries');
    console.log('  node script.js event_04bifcz');
    console.log('  node script.js default_delay_d_finition');
}