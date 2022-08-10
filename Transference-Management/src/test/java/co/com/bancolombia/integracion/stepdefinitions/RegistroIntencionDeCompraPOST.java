package co.com.bancolombia.integracion.stepdefinitions;

import cucumber.api.java.es.Dado;
import io.cucumber.datatable.DataTable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import com.bancolombia.integracion.citrus.AbstractStepDefinition;
import com.consol.citrus.message.MessageType;

import cucumber.api.java.es.Cuando;
import cucumber.api.java.es.Entonces;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class RegistroIntencionDeCompraPOST extends AbstractStepDefinition {

    private final String OPERATION = "v3/operations/cross-product/payments/payment-order/transfer/action/registry";
    private final String OPERATIONOAUTH2 = "security/oauth-provider/oauth2/token";
    String uuid = java.util.UUID.randomUUID().toString();
    String accessToken;
    String transferCode;

    @Dado("Soy Cliente y quiero registrar una intención de compra")
    public void Soy_Cliente_y_quiero_registrar_una_intención_de_compra() {
        runner.echo("Armado y envio peticion Access SuccessMIRARAQUIOJO!!!!!!!!!");
        runner.echo(getApiUrl(OPERATIONOAUTH2));

        Map<String, Object> headers = apiHeaders();
        headers.put("content-type", getProperty("content-type"));
		headers.put("client-id", getProperty("x-ibm-client-id"));
		headers.put("client-secret", getProperty("x-ibm-client-secret"));

        runner.http(action -> action.client("seguridadGateway")
                .send()
                .post(getApiUrl(OPERATIONOAUTH2))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .payload(getProperty("payload"))
                .headers(headers));

        runner.echo("Response operacion Access Success");

        runner.http(action -> action.client("seguridadGateway")
                .receive()
                .response(HttpStatus.OK)
                .extractFromPayload("$.access_token", "accessToken"));

        runner.echo("------------------Imprimiendo Respuesta del token ---------------------------------");
        accessToken = "${accessToken}";
        runner.echo("------------------Imprimiendo el valor del token ---------------------------------" + accessToken);
    }

    @Cuando("solicito el registro de la intencion de compra e ingreso todos los datos correctamente")
    public void solicito_el_registro_de_la_intencion_de_compra_e_ingreso_todos_los_datos_correctamente() throws IOException {
        runner.echo("Datos del cliente ingresados...");

        String requestPath = "messages/request.json";
        Map<String, Object> headers = apiHeaders();
        headers.put("message-id", uuid);
        headers.put("Authorization", "Bearer " + accessToken);
        runner.http(action -> action.client("gateway")
                .send()
                .post(getApiUrl(OPERATION))
                .messageType(MessageType.JSON)
                .payload(new ClassPathResource(requestPath), getCharset())
                .headers(headers)
        );
    }

    @Cuando("Ingreso correctamente los parametros de entrada incluyendo caracteres especiales")
    public void Ingreso_correctamente_los_parametros_de_entrada_incluyendo_caracteres_especiales() throws IOException {
        runner.echo("Datos del cliente ingresados...");
        String requestPath = "messages/requestOp1.json";
        Map<String, Object> headers = apiHeaders();
        headers.put("message-id", uuid);
        headers.put("Authorization", "Bearer " + accessToken);

        setVariable("commerceTransferButtonId", "w0mp1B0toN");
        setVariable("transferReference", "100234567811");
        setVariable("transferDescription", "Prueba-compra_ con$ caracteres / especiales+");
        setVariable("transferAmount", "1200.33");
        setVariable("commerceUrl", "https://gateway.com/payment/route?commerce=Telovendo");
        setVariable("confirmationURL", "https://gateway.com/payment/route?commerce=Telovendo");

        runner.http(action -> action.client("gateway")
                .send()
                .post(getApiUrl(OPERATION))
                .messageType(MessageType.JSON)
                .payload(new ClassPathResource(requestPath), getCharset())
                .headers(headers)
        );
    }

    @Entonces("El servicio retorna intención de compra registrada")
    public void El_servicio_retorna_intención_de_compra_registrada() {
        runner.echo("Recibir los datos del registro de intencion de compra");
        runner.http(action -> action.client("gateway")
                .receive()
                .response(HttpStatus.OK)
                .schemaValidation(true)
                .jsonSchema("POSTResTransferRegistry")
        );
    }

    @Cuando("Ingreso incorrectamente el hash del Botón")
    public void Ingreso_incorrectamente_el_hash_del_Botón() throws IOException {
        runner.echo("Datos del cliente ingresados...");

        String requestPath = "messages/requestOp1.json";
        Map<String, Object> headers = apiHeaders();
        headers.put("message-id", uuid);
        headers.put("Authorization", "Bearer " + accessToken);

        setVariable("commerceTransferButtonId", "w0mp1B0toN1");
        setVariable("transferReference", "100234567811");
        setVariable("transferDescription", "Prueba-compra_ con$ caracteres / especiales+");
        setVariable("transferAmount", "1200.33");
        setVariable("commerceUrl", "https://gateway.com/payment/route?commerce=Telovendo");
        setVariable("confirmationURL", "https://gateway.com/payment/route?commerce=Telovendo");

        runner.http(action -> action.client("gateway")
                .send()
                .post(getApiUrl(OPERATION))
                .messageType(MessageType.JSON)
                .payload(new ClassPathResource(requestPath), getCharset())
                .headers(headers)
        );
    }

    @Entonces("El servicio retorna una excepción de negocio indicando que el botón No existe")
    public void El_servicio_retorna_una_excepción_de_negocio_indicando_que_el_botón_No_existe() {
        runner.echo("Esperamos una excepcion de negocio....!");
        runner.http(action -> action.client("gateway")
                .receive()
                .response(HttpStatus.BAD_REQUEST)
                .schemaValidation(true)
                .jsonSchema("errorResponse")
        );
    }

    @Cuando("Ingreso los datos para realizar la compra y supera el tope maximo de compra")
    public void Ingreso_los_datos_para_realizar_la_compra_y_supera_el_tope_maximo_de_compra() throws IOException {
        runner.echo("Datos del cliente ingresados...");
        String requestPath = "messages/requestOp1.json";
        Map<String, Object> headers = apiHeaders();
        headers.put("message-id", uuid);
        headers.put("Authorization", "Bearer " + accessToken);

        setVariable("commerceTransferButtonId", "w0mp1B0toN");
        setVariable("transferReference", "100234567811");
        setVariable("transferDescription", "Prueba compra");
        setVariable("transferAmount", "3458777777777.33");
        setVariable("commerceUrl", "https://gateway.com/payment/route?commerce=Telovendo");
        setVariable("confirmationURL", "https://gateway.com/payment/route?commerce=Telovendo");

        runner.http(action -> action.client("gateway")
                .send()
                .post(getApiUrl(OPERATION))
                .messageType(MessageType.JSON)
                .payload(new ClassPathResource(requestPath), getCharset())
                .headers(headers)
        );
    }

    @Entonces("El servicio retorna una excepción de negocio indicando que el valor de la transacción excede el monto maximo permitido")
    public void El_servicio_retorna_una_excepción_de_negocio_indicando_que_el_valor_de_la_transacción_excede_el_monto_maximo_permitido() {
        runner.echo("Esperamos una excepcion de negocio....!");
        runner.http(action -> action.client("gateway")
                .receive()
                .response(HttpStatus.BAD_REQUEST)
                .schemaValidation(true)
                .jsonSchema("errorResponse")
        );
    }

    @Cuando("el mensaje de peticion de registrar intencion de compra no se le envia algun campo obligatorio")
    public void el_mensaje_de_peticion_de_registrar_intencion_de_compra_no_se_le_envia_algun_campo_obligatorio(DataTable dt) throws IOException {
        runner.echo("Datos del cliente ingresados...");
        String requestPath = "messages/requestOp1.json";
        Map<String, Object> headers = apiHeaders();
        headers.put("message-id", uuid);
        headers.put("Authorization", "Bearer " + accessToken);

        List<Map<String, String>> list = dt.asMaps(String.class, String.class);

        for (int i = 0; i < list.size(); i++) {
            setVariable("commerceTransferButtonId", list.get(i).get("commerceTransferButtonId"));
            setVariable("transferReference", list.get(i).get("transferReference"));
            setVariable("transferDescription", list.get(i).get("transferDescription"));
            setVariable("transferAmount", list.get(i).get("transferAmount"));
            setVariable("commerceUrl", list.get(i).get("commerceUrl"));
            setVariable("confirmationURL", list.get(i).get("confirmationURL"));


            runner.http(action -> action.client("gateway")
                    .send()
                    .post(getApiUrl(OPERATION))
                    .messageType(MessageType.JSON)
                    .payload(new ClassPathResource(requestPath), getCharset())
                    .headers(headers)
            );
        }
    }

    @Entonces("recibo una excepcion de negocio indicando que los campos de la peticion de registrar intencion de compra no cumplen con la especificacion de la firma")
    public void recibo_una_excepcion_de_negocio_indicando_que_los_campos_de_la_peticion_de_registrar_intencion_de_compra_no_cumplen_con_la_especificacion_de_la_firma() {
        runner.echo("Esperamos una excepcion de negocio....!");
        runner.http(action -> action.client("gateway")
                .receive()
                .response(HttpStatus.BAD_REQUEST)
                .schemaValidation(true)
                .jsonSchema("errorResponse")
        );
    }

    @Cuando("el mensaje de peticion de registrar intencion de compra tiene campos con longitud mayor a la definida")
    public void el_mensaje_de_peticion_de_registrar_intencion_de_compra_tiene_campos_con_longitud_mayor_a_la_definida(DataTable dt) throws IOException {
        runner.echo("Datos del cliente ingresados...");
        String requestPath = "messages/requestOp1.json";
        Map<String, Object> headers = apiHeaders();
        headers.put("message-id", uuid);
        headers.put("Authorization", "Bearer " + accessToken);

        List<Map<String, String>> list = dt.asMaps(String.class, String.class);

        for (int i = 0; i < list.size(); i++) {
            setVariable("commerceTransferButtonId", list.get(i).get("commerceTransferButtonId"));
            setVariable("transferReference", list.get(i).get("transferReference"));
            setVariable("transferDescription", list.get(i).get("transferDescription"));
            setVariable("transferAmount", list.get(i).get("transferAmount"));
            setVariable("commerceUrl", list.get(i).get("commerceUrl"));
            setVariable("confirmationURL", list.get(i).get("confirmationURL"));

            runner.http(action -> action.client("gateway")
                    .send()
                    .post(getApiUrl(OPERATION))
                    .messageType(MessageType.JSON)
                    .payload(new ClassPathResource(requestPath), getCharset())
                    .headers(headers)
            );
        }
    }

    @Entonces("recibo una excepcion de negocio indicando que los campos del registro de intencion de compra no cumplen con la especificacion de la firma")
    public void recibo_una_excepcion_de_negocio_indicando_que_los_campos_del_registro_de_intencion_de_compra_no_cumplen_con_la_especificacion_de_la_firma() {
        runner.echo("Esperamos una excepcion de negocio....!");
        runner.http(action -> action.client("gateway")
                .receive()
                .response(HttpStatus.BAD_REQUEST)
                .schemaValidation(true)
                .jsonSchema("errorResponse")
        );
    }

    @Dado("Soy un cliente Bancolombia y quiero consultar el estado de una transacción")
    public void Soy_un_cliente_Bancolombia_y_quiero_consultar_el_estado_de_una_transacción() {
        runner.echo("Armado y envio peticion Access Success");
        runner.echo(getApiUrl(OPERATIONOAUTH2));

        Map<String, Object> headers = apiHeaders();
        headers.put("content-type", getProperty("content-type"));

        runner.http(action -> action.client("seguridadGateway")
                .send()
                .post(getApiUrl(OPERATIONOAUTH2))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .payload(getProperty("payload"))
                .headers(headers));

        runner.echo("Response operacion Access Success");

        runner.http(action -> action.client("seguridadGateway")
                .receive()
                .response(HttpStatus.OK)
                .extractFromPayload("$.access_token", "accessToken"));

        runner.echo("------------------Imprimiendo Respuesta del token ---------------------------------");
        accessToken = "${accessToken}";
        runner.echo("------------------Imprimiendo el valor del token ---------------------------------" + accessToken);

        runner.echo("Datos del cliente ingresados...");

        String requestPath = "messages/request.json";
        Map<String, Object> headers1 = apiHeaders();
        headers1.put("message-id", uuid);
        headers1.put("Authorization", "Bearer " + accessToken);
        runner.http(action -> action.client("gateway")
                .send()
                .post(getApiUrl(OPERATION))
                .messageType(MessageType.JSON)
                .payload(new ClassPathResource(requestPath), getCharset())
                .headers(headers1)
        );

        runner.echo("Recibir los datos del registro de intencion de compra");
        runner.http(action -> action.client("gateway")
                .receive()
                .response(HttpStatus.OK)
                .extractFromPayload("$.data[0].transferCode", "transferCode")
                .schemaValidation(true)
                .jsonSchema("POSTResTransferRegistry")
        );

        runner.echo("------------------Imprimiendo Respuesta del transferCode---------------------------------");
        transferCode = "v3/operations/cross-product/payments/payment-order/transfer/${transferCode}/action/validate";
        runner.echo("------------------Imprimiendo el valor del transferCode---------------------------------" + transferCode);
    }

    @Cuando("Ingreso Correctamente el Identificador único de la transferencia")
    public void Ingreso_Correctamente_el_Identificador_único_de_la_transferencia() throws IOException {
        runner.echo("Datos del cliente ingresados...");
        Map<String, Object> headers = apiHeaders();
        headers.put("message-id", uuid);
        headers.put("Authorization", "Bearer " + accessToken);
        runner.http(action -> action.client("gateway")
                .send()
                .get(getApiUrl(transferCode))
                .headers(headers)
        );
    }

    @Entonces("El Servicio Retorna el estado de la transferencia asociado al identificador ingresado")
    public void El_Servicio_Retorna_el_estado_de_la_transferencia_asociado_al_identificador_ingresado() {
        runner.echo("Recibimos la informacion de la transferencia");
        runner.http(action -> action.client("gateway")
                .receive()
                .response(HttpStatus.OK)
        );
    }

    @Cuando("Ingreso incorrectamente el Identificador único de una transferencia")
    public void Ingreso_incorrectamente_el_Identificador_único_de_una_transferencia() throws IOException {
        runner.echo("Datos del cliente ingresados...");
        Map<String, Object> headers = apiHeaders();
        headers.put("message-id", uuid);
        headers.put("Authorization", "Bearer " + accessToken);
        String OPERATION2 = "v3/operations/cross-product/payments/payment-order/transfer/_Y0GUTqnmFYe/action/validate";
        runner.http(action -> action.client("gateway").
                send()
                .get(getApiUrl(OPERATION2))
                .headers(headers)
        );
    }

    @Entonces("El servicio retorna una excepción de negocio la intencion de compra NO existe")
    public void El_servicio_retorna_una_excepción_de_negocio_la_intencion_de_compra_NO_existe() {
        runner.echo("Recibimos la informacion de la transferencia");
        runner.http(action -> action.client("gateway")
                .receive()
                .response(HttpStatus.BAD_REQUEST)
                .schemaValidation(true)
                .jsonSchema("errorResponse")
        );
    }

}
