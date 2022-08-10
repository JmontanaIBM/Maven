#language: es
#Author: ccubides@bancolombia.com.co - emacias@bancolombia.com.co - ceatobon@bancolombia.com.co

Característica: Consultar el estado de una transferencia

  Escenario: Consultar el estado de una transferencia a partir del identificador de la transferencia
    Dado Soy un cliente Bancolombia y quiero consultar el estado de una transacción
    Cuando Ingreso Correctamente el Identificador único de la transferencia
    Entonces El Servicio Retorna el estado de la transferencia asociado al identificador ingresado

  Escenario: Consultar el estado de una transferencia a partir de informacion incorrecta en el identificador de la transferencia
    Dado Soy un cliente Bancolombia y quiero consultar el estado de una transacción
    Cuando Ingreso incorrectamente el Identificador único de una transferencia
    Entonces El servicio retorna una excepción de negocio la intencion de compra NO existe

