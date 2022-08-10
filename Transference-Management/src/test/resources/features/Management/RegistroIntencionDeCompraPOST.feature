# language: es
#Author: ccubides@bancolombia.com.co - emacias@bancolombia.com.co - ceatobon@bancolombia.com.co

Característica: Realizo el registro de una intencion de compra para pagar a traves de Boton Bancolombia

  Escenario: Realizo el registro de una intención de compra de un cliente a partir de todos los datos
    Dado Soy Cliente y quiero registrar una intención de compra
    Cuando solicito el registro de la intencion de compra e ingreso todos los datos correctamente
    Entonces El servicio retorna intención de compra registrada

  Escenario: Realizo el registro de una intención de compra de un cliente a partir de todos los datos
    Dado Soy Cliente y quiero registrar una intención de compra
    Cuando Ingreso correctamente los parametros de entrada incluyendo caracteres especiales
    Entonces El servicio retorna intención de compra registrada

  Escenario: Realizo el registro de una intención de compra de un cliente a partir de informacion erronea en el hash
    Dado Soy Cliente y quiero registrar una intención de compra
    Cuando Ingreso incorrectamente el hash del Botón
    Entonces El servicio retorna una excepción de negocio indicando que el botón No existe

  Escenario:Realizo el registro de una intención de compra de un cliente a partir de informacion erronea en el amount
    Dado Soy Cliente y quiero registrar una intención de compra
    Cuando Ingreso los datos para realizar la compra y supera el tope maximo de compra
    Entonces El servicio retorna una excepción de negocio indicando que el valor de la transacción excede el monto maximo permitido

  Escenario: Obligatoriedad de campos, registro de intencion de compra con una peticion sin enviar campos obligatorios de la peticion
    Dado Soy Cliente y quiero registrar una intención de compra
    Cuando el mensaje de peticion de registrar intencion de compra no se le envia algun campo obligatorio
      | commerceTransferButtonId | transferReference  | transferDescription	  | transferAmount  | commerceUrl | confirmationURL |
      | 	                     | 100234567811111111 | Prueba compra         |        10000    |  https://gateway.com/payment/route?commerce=Telovendo   | https://gateway.com/payment/route?commerce=Telovendo|
      | w0mp1B0toN	             |     	              | Prueba compra         |        10000    |  https://gateway.com/payment/route?commerce=Telovendo   | https://gateway.com/payment/route?commerce=Telovendo|
      | w0mp1B0toN	             | 100234567811111    | Prueba compra         |                 |  https://gateway.com/payment/route?commerce=Telovendo   | https://gateway.com/payment/route?commerce=Telovendo|
      | w0mp1B0toN	             | 1002345678111111   | Prueba compra         |        10000    |                                                         | https://gateway.com/payment/route?commerce=Telovendo|
    Entonces recibo una excepcion de negocio indicando que los campos de la peticion de registrar intencion de compra no cumplen con la especificacion de la firma

  Escenario: Longitud de datos supera la maxima definida, registro de intencion de compra con una peticion enviando campos superando la longitud permitida
    Dado Soy Cliente y quiero registrar una intención de compra
    Cuando el mensaje de peticion de registrar intencion de compra tiene campos con longitud mayor a la definida
      | commerceTransferButtonId | transferReference  | transferDescription	  | transferAmount  | commerceUrl | confirmationURL |
      | 	w0mp1B0toNw0mp1B0toNw0mp1B0toNw0mp1B0toNw0mp1B0toN1        | 100234567811111111 | Prueba compra         |        10000    |  https://gateway.com/payment/route?commerce=Telovendo   | https://gateway.com/payment/route?commerce=Telovendo|
      | w0mp1B0toN	             | 100234567811111111100234567811111111100234567811111111100234567811111111100234567811111111100234567811111111 | Prueba compra         |        10000    |  https://gateway.com/payment/route?commerce=Telovendo   | https://gateway.com/payment/route?commerce=Telovendo|
      | w0mp1B0toN	             | 100234567811111    | Prueba compra100234567811111111100234567811111111100234567811111111100234567811111111100234567811111111100234567811111111Prueba compra10023456781111111110023456781111111110023456781111111110023456781111111110023456781111111110023456781111111167811111111100234567811111111     |        10000    |  https://gateway.com/payment/route?commerce=Telovendo   | https://gateway.com/payment/route?commerce=Telovendo|
      | w0mp1B0toN	             | 1002345678111111   | Prueba compra         |        10000000000000000000    |  https://gateway.com/payment/route?commerce=Telovendo           | https://gateway.com/payment/route?commerce=Telovendo|
    Entonces recibo una excepcion de negocio indicando que los campos del registro de intencion de compra no cumplen con la especificacion de la firma
