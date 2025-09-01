// Test 1: Token spéciaux simples dans un WORD
un/test

// Test 2: Plusieurs tokens dans un WORD  
un/autre=test

// Test 3: Succession de tokens spéciaux
//</function>

// Test 4: Token au début d'un WORD
/test

// Test 5: Token à la fin d'un WORD
test/

// Test 6: Token au milieu avec texte des deux côtés
avant/milieu/après

// Test 7: Tokens multiples avec equals
var=value/other=test

// Test 8: Pattern BPMN typique (ouverture)
//<function

// Test 9: Pattern BPMN typique (fermeture)
//</function>

// Test 10: Pattern complexe avec ID
id=abc123/def>

// Test 11: Cas avec plusieurs equals
a=b=c/d=e

// Test 12: Slashes multiples
path//to///resource

// Test 13: Mélange avec caractères spéciaux
test/path?param=value&other=test