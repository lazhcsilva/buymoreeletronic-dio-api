package controller;

import builder.ElectronicDTOBuilder;
import com.dititalinnovation.buymore.controller.ElectronicController;
import com.dititalinnovation.buymore.dto.ElectronicDTO;
import com.dititalinnovation.buymore.dto.QuantityDTO;
import com.dititalinnovation.buymore.exception.ElectronicNotFoundException;
import com.dititalinnovation.buymore.service.ElectronicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.JsonConvertionUtils.asJsonString;

@ExtendWith(MockitoExtension.class)
public class ElectronicControllerTest {

    private static final String ELECTRONIC_API_URL_PATH = "/api/v1/electronics";
    private static final long VALID_ELECTRONIC_ID = 1L;
    private static final long INVALID_ELECTRONIC_ID = 2l;
    private static final String ELECTRONIC_API_SUBPATH_INCREMENT_URL = "/increment";
    private static final String ELECTRONIC_API_SUBPATH_DECREMENT_URL = "/decrement";

    private MockMvc mockMvc;

    @Mock
    private ElectronicService electronicService;

    @InjectMocks
    private ElectronicController electronicController;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(electronicController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, Locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenABeerIsCreated() throws Exception{

        //given
        ElectronicDTO electronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();

        //when
        when(electronicService.createElectronic(electronicDTO)).thenReturn(electronicDTO);

        //then
        mockMvc.perform(post(ELECTRONIC_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(electronicDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(electronicDTO.getName())))
                .andExpect(jsonPath("$.brand", is(electronicDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(electronicDTO.getType().toString())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenAnErrorIsReturned() throws Exception {
        // given
        ElectronicDTO electronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();
        electronicDTO.setBrand(null);

        // then
        mockMvc.perform(post(ELECTRONIC_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(electronicDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        ElectronicDTO electronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();

        //when
        when(electronicService.findByName(electronicDTO.getName())).thenReturn(electronicDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(ELECTRONIC_API_URL_PATH + "/" + electronicDTO.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(electronicDTO.getName())))
                .andExpect(jsonPath("$.brand", is(electronicDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(electronicDTO.getType().toString())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        ElectronicDTO electronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();

        //when
        when(electronicService.findByName(electronicDTO.getName())).thenThrow(ElectronicNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(ELECTRONIC_API_URL_PATH + "/" + electronicDTO.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETListWithBeersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        ElectronicDTO electronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();

        //when
        when(electronicService.listAll()).thenReturn(Collections.singletonList(electronicDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(ELECTRONIC_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(electronicDTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(electronicDTO.getBrand())))
                .andExpect(jsonPath("$[0].type", is(electronicDTO.getType().toString())));
    }

    @Test
    void whenGETListWithoutBeersIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        ElectronicDTO electronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();

        //when
        when(electronicService.listAll()).thenReturn(Collections.singletonList(electronicDTO));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(ELECTRONIC_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        ElectronicDTO electronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();

        //when
        doNothing().when(electronicService).deleteById(electronicDTO.getId());

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(ELECTRONIC_API_URL_PATH + "/" + electronicDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        //when
        doThrow(ElectronicNotFoundException.class).when(electronicService).deleteById(INVALID_ELECTRONIC_ID);

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(ELECTRONIC_API_URL_PATH + "/" + INVALID_ELECTRONIC_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPATCHIsCalledToIncrementDiscountThenOKstatusIsReturned() throws Exception {
        QuantityDTO quantityDTO = QuantityDTO.builder()
                .quantity(10)
                .build();

        ElectronicDTO electronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();
        electronicDTO.setQuantity(electronicDTO.getQuantity() + quantityDTO.getQuantity());

        when(electronicService.increment(VALID_ELECTRONIC_ID, quantityDTO.getQuantity())).thenReturn(electronicDTO);

        mockMvc.perform(MockMvcRequestBuilders.patch(ELECTRONIC_API_URL_PATH + "/" + VALID_ELECTRONIC_ID + ELECTRONIC_API_SUBPATH_INCREMENT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(quantityDTO))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(electronicDTO.getName())))
                .andExpect(jsonPath("$.brand", is(electronicDTO.getBrand())))
                .andExpect(jsonPath("$.type", is(electronicDTO.getType().toString())))
                .andExpect(jsonPath("$.quantity", is(electronicDTO.getQuantity())));
    }

}
