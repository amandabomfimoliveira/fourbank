package br.com.fourbank.fourbank;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FourbankApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void deveCadastrarLogarEConsultarUsuarioAutenticado() throws Exception {
		String cadastroJson = """
				{
				  "nome": "Amanda",
				  "email": "amanda@example.com",
				  "senha": "senha-segura-123"
				}
				""";

		String cadastroResponse = mockMvc.perform(post("/api/auth/register")
					.contentType(MediaType.APPLICATION_JSON)
					.content(cadastroJson))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.tipo").value("Bearer"))
				.andExpect(jsonPath("$.token").isNotEmpty())
				.andReturn()
				.getResponse()
				.getContentAsString();

		String token = JsonPath.read(cadastroResponse, "$.token");

		mockMvc.perform(get("/api/users/me")
					.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Amanda"))
				.andExpect(jsonPath("$.email").value("amanda@example.com"))
				.andExpect(jsonPath("$.perfil").value("USER"));

		String loginJson = """
				{
				  "email": "amanda@example.com",
				  "senha": "senha-segura-123"
				}
				""";

		mockMvc.perform(post("/api/auth/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(loginJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").isNotEmpty());
	}

	@Test
	void deveRecusarRotaProtegidaSemToken() throws Exception {
		mockMvc.perform(get("/api/users/me"))
				.andExpect(status().isUnauthorized());
	}

}
