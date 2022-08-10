package co.com.bancolombia.integracion.stepdefinitions;

import com.bancolombia.integracion.citrus.AbstractStepDefinition;
import com.consol.citrus.message.MessageType;
import cucumber.api.java.es.Cuando;
import cucumber.api.java.es.Entonces;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class AutorizacionAPI_TransferenceManagement extends AbstractStepDefinition {

	private final String OPERATION = "v3/operations/cross-product/payments/payment-order/transfer/action/registry";
	@Cuando("el mensaje de petición no indica client id")
	public void el_mensaje_de_petición_no_indica_client_id() {
		runner.echo("El endpoint responde...");
		String requestPath = "messages/request.json";
		Map<String, Object> headers = apiHeaders();
		headers.remove("x-ibm-client-id");
		runner.http(action -> action.client("gateway")
				.send()
				.post(getApiUrl(OPERATION))
				.messageType(MessageType.JSON)
				.payload(new ClassPathResource(requestPath), getCharset())
				.headers(headers)
		);
	}

	@Entonces("la API responde un error de autorizacion")
	public void la_API_responde_un_error_de_autorizacion() {
		runner.echo("Recibir la respuesta y validar... Esperamos un error!");
		runner.http(action -> action.client("gateway")
				.receive()
				.response(HttpStatus.UNAUTHORIZED)
		);
	}

	@Cuando("el mensaje de petición no indica client secret")
	public void el_mensaje_de_petición_no_indica_client_secret() {
		runner.echo("El endpoint responde...");
		String requestPath = "messages/request.json";
		Map<String, Object> headers = apiHeaders();
		headers.remove("x-ibm-client-secret");
		runner.http(action -> action.client("gateway")
				.send()
				.post(getApiUrl(OPERATION))
				.messageType(MessageType.JSON)
				.payload(new ClassPathResource(requestPath), getCharset())
				.headers(headers)
		);
	}

	@Cuando("los campos clientid y client secret no son correctos")
	public void los_campos_clientid_y_client_secret_no_son_correctos() {
		runner.echo("El endpoint responde...");
		String requestPath = "messages/request.json";
		Map<String, Object> headers = apiHeaders();
		headers.remove("x-ibm-client-id");
		headers.remove("x-ibm-client-secret");
		runner.http(action -> action.client("gateway")
				.send()
				.post(getApiUrl(OPERATION))
				.messageType(MessageType.JSON)
				.payload(new ClassPathResource(requestPath), getCharset())
				.headers(headers)
		);
	}
}