#language: es
# author: ccmoreno@bancolombia.com.co, ceatobon@bancolombia.com.co

Característica: BUTTON-TRANSFERENCE-MANAGEMENT Autenticacion de un cliente mediante flujo Aplication

  Escenario: Obligatoriedad de client id
    Cuando el mensaje de petición no indica client id
    Entonces la API responde un error de autorizacion

  Escenario: Obligatoriedad de client secret
    Cuando el mensaje de petición no indica client secret
    Entonces la API responde un error de autorizacion

  Escenario: Campos invalidos
    Cuando los campos clientid y client secret no son correctos
    Entonces la API responde un error de autorizacion
