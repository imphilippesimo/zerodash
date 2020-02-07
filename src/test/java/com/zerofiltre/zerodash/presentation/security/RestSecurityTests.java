package com.zerofiltre.zerodash.presentation.security;

import com.fasterxml.jackson.databind.*;
import com.zerofiltre.zerodash.model.*;
import com.zerofiltre.zerodash.service.*;
import com.zerofiltre.zerodash.utils.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.mock.web.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.annotation.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class RestSecurityTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    JwtConfig jwtConfig;

    String token;


    private MvcResult mvcResult;


    @Test
    @WithMockUser(username = Constants.TEST_EMAIL, password = Constants.TEST_PASSWORD)
    public void shouldGenerateTheToken() throws Exception {
        ZDUser user = new ZDUser();
        user.setEmail(Constants.TEST_EMAIL);
        user.setPhoneNumber(Constants.TEST_PHONE_NUMBER);
        user.setPassword(Constants.TEST_PASSWORD);
        userService.createUser(user);
        //override the encoded returned password
        user.setPassword(Constants.TEST_PASSWORD);

        connect(user, mvc).andExpect(status().isOk())
                .andDo(mvcResult -> {
                    token = extractToken(mvcResult.getResponse(), jwtConfig);
                });

        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(token).isNotBlank();
        Assertions.assertThat(token).startsWith(jwtConfig.getPrefix());
        //userService.delete(user.getId());
    }


    @Test
    public void shouldNotGenerateToken() throws Exception {
        ZDUser unknown = new ZDUser();
        unknown.setEmail(Constants.TEST_UNKNOWN_EMAIL);
        unknown.setPassword(Constants.TEST_PASSWORD);
        connect(unknown, mvc).andExpect(status().isUnauthorized());
    }

    public static ResultActions connect(ZDUser user, MockMvc mvc) throws Exception {
        String url = "/auth";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(user);
        ResultActions mvcResult = mvc.perform(post(url).contentType(APPLICATION_JSON).content(requestJson));
        return mvcResult;
    }

    public static String extractToken(MockHttpServletResponse response, JwtConfig jwtConfig) {
        if (response != null) {
            return response.getHeader(jwtConfig.getHeader());
        }
        return null;
    }

}
