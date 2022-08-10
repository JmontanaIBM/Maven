package co.com.bancolombia.integracion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.bancolombia.integracion.citrus.config.HTTPSEndpointConfig;
import com.bancolombia.integracion.citrus.config.JsonSchemaConfig;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.json.schema.SimpleJsonSchema;

@Configuration
@PropertySource("classpath:citrus.properties")
@Import({HTTPSEndpointConfig.class, JsonSchemaConfig.class})
public class TestSetup {
	
	@Autowired private HTTPSEndpointConfig httpConfig;
    @Autowired private JsonSchemaConfig schemaConfig;

	@Value("${api.host}") private String apiHost = "";
	@Value("${seguridad.host}") private String seguridadHost = "";
	@Value("${api.swagger.definition.op1Res}") private String schemaOp1Res = "";
	@Value("${api.swagger.definition.op2Res}") private String schemaOp2Res = "";

    @Bean(name = "gateway")
    public HttpClient getApiHost() {
        return httpConfig.getHttpsClient(apiHost);
    }
    @Bean(name = "seguridadGateway")
    public HttpClient getseguridadHost() {
        return httpConfig.getHttpsClient(seguridadHost);
    }
	
    @Bean(name = "POSTResTransferRegistry")
	public SimpleJsonSchema op1Res() {
		return schemaConfig.schemaFromSwagger(schemaOp1Res);
	}

	@Bean(name = "resTransfer")
	public SimpleJsonSchema op2Res() {
		return schemaConfig.schemaFromSwagger(schemaOp2Res);
	}

    @Bean(name = "errorResponse")
	public SimpleJsonSchema errorRes() {
		return schemaConfig.schemaFromSwagger("failure");
	}
}