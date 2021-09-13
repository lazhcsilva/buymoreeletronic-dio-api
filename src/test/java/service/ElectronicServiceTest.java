package service;

import builder.ElectronicDTOBuilder;
import com.dititalinnovation.buymore.dto.ElectronicDTO;
import com.dititalinnovation.buymore.entity.Electronic;
import com.dititalinnovation.buymore.exception.BuyMoreExceededException;
import com.dititalinnovation.buymore.exception.ElectronicAlreadyRegisteredException;
import com.dititalinnovation.buymore.exception.ElectronicNotFoundException;
import com.dititalinnovation.buymore.mapper.ElectronicMapper;
import com.dititalinnovation.buymore.repository.ElectronicRepository;
import com.dititalinnovation.buymore.service.ElectronicService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElectronicServiceTest {

    private static final long INVALID_BEER_ID = 1L;

    @Mock
    private ElectronicRepository electronicRepository;

    private ElectronicMapper electronicMapper = ElectronicMapper.INSTANCE;

    @InjectMocks
    private ElectronicService electronicService;

    @Test
    void whenElectronicInformedThenItShouldBeCreated() throws ElectronicAlreadyRegisteredException {
        // given
        ElectronicDTO expectedElectronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();
        Electronic expectedSavedElectronic = electronicMapper.toModel(expectedElectronicDTO);

        // when
        when(electronicRepository.findByName(expectedElectronicDTO.getName())).thenReturn(Optional.empty());
        when(electronicRepository.save(expectedSavedElectronic)).thenReturn(expectedSavedElectronic);

        //then
        ElectronicDTO createdElectronicDTO = electronicService.createElectronic(expectedElectronicDTO);

        assertThat(createdElectronicDTO.getId(), is(equalTo(expectedElectronicDTO.getId())));
        assertThat(createdElectronicDTO.getName(), is(equalTo(expectedElectronicDTO.getName())));
        assertThat(createdElectronicDTO.getQuantity(), is(equalTo(expectedElectronicDTO.getQuantity())));
    }

    @Test
    void whenAlreadyRegisteredElectronicInformedThenAnExceptionShouldBeThrown() {
        // given
        ElectronicDTO expectedElectronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();
        Electronic duplicatedElectronic = electronicMapper.toModel(expectedElectronicDTO);

        // when
        when(electronicRepository.findByName(expectedElectronicDTO.getName())).thenReturn(Optional.of(duplicatedElectronic));

        // then
        assertThrows(ElectronicAlreadyRegisteredException.class, () -> electronicService.createElectronic(expectedElectronicDTO));
    }

    @Test
    void whenValidBeerNameIsGivenThenReturnABeer() throws ElectronicNotFoundException {
        // given
        ElectronicDTO expectedFoundElectronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();
        Electronic expectedFoundElectronic = electronicMapper.toModel(expectedFoundElectronicDTO);

        // when
        when(electronicRepository.findByName(expectedFoundElectronic.getName())).thenReturn(Optional.of(expectedFoundElectronic));

        // then
        ElectronicDTO foundElectronicDTO = electronicService.findByName(expectedFoundElectronicDTO.getName());

        assertThat(foundElectronicDTO, is(equalTo(expectedFoundElectronicDTO)));
    }

    @Test
    void whenNotRegisteredElectronicNameIsGivenThenThrowAnException() {
        // given
        ElectronicDTO expectedFoundElectronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();

        // when
        when(electronicRepository.findByName(expectedFoundElectronicDTO.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(ElectronicNotFoundException.class, () -> electronicService.findByName(expectedFoundElectronicDTO.getName()));
    }

    @Test
    void whenListElectronicIsCalledThenReturnAListOfElectronics() {
        // given
        ElectronicDTO expectedFoundElectronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();
        Electronic expectedFoundElectronic = electronicMapper.toModel(expectedFoundElectronicDTO);

        //when
        when(electronicRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundElectronic));

        //then
        List<ElectronicDTO> foundListBeersDTO = electronicService.listAll();

        assertThat(foundListBeersDTO, is(not(empty())));
        assertThat(foundListBeersDTO.get(0), is(equalTo(expectedFoundElectronicDTO)));
    }

    @Test
    void whenListElectronicIsCalledThenReturnAnEmptyListOfElectronics() {
        //when
        when(electronicRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        //then
        List<ElectronicDTO> foundListBeersDTO = electronicService.listAll();

        assertThat(foundListBeersDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAElectronicShouldBeDeleted() throws ElectronicNotFoundException{
        // given
        ElectronicDTO expectedDeletedElectronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();
        Electronic expectedDeletedElectronic = electronicMapper.toModel(expectedDeletedElectronicDTO);

        // when
        when(electronicRepository.findById(expectedDeletedElectronicDTO.getId())).thenReturn(Optional.of(expectedDeletedElectronic));
        doNothing().when(electronicRepository).deleteById(expectedDeletedElectronicDTO.getId());

        // then
        electronicService.deleteById(expectedDeletedElectronicDTO.getId());

        verify(electronicRepository, times(1)).findById(expectedDeletedElectronicDTO.getId());
        verify(electronicRepository, times(1)).deleteById(expectedDeletedElectronicDTO.getId());
    }

    @Test
    void whenIncrementIsCalledThenIncrementBuyMore() throws ElectronicNotFoundException, BuyMoreExceededException {
        //given
        ElectronicDTO expectedElectronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();
        Electronic expectedElectronic = electronicMapper.toModel(expectedElectronicDTO);

        //when
        when(electronicRepository.findById(expectedElectronicDTO.getId())).thenReturn(Optional.of(expectedElectronic));
        when(electronicRepository.save(expectedElectronic)).thenReturn(expectedElectronic);

        int quantityToIncrement = 10;
        int expectedQuantityAfterIncrement = expectedElectronicDTO.getQuantity() + quantityToIncrement;

        // then
        ElectronicDTO incrementedElectronicDTO = electronicService.increment(expectedElectronicDTO.getId(), quantityToIncrement);

        assertThat(expectedQuantityAfterIncrement, equalTo(incrementedElectronicDTO.getQuantity()));
        assertThat(expectedQuantityAfterIncrement, lessThan(expectedElectronicDTO.getMax()));
    }

    @Test
    void whenIncrementIsGreatherThanMaxThenThrowException() {
        ElectronicDTO expectedElectronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();
        Electronic expectedElectronic = electronicMapper.toModel(expectedElectronicDTO);

        when(electronicRepository.findById(expectedElectronicDTO.getId())).thenReturn(Optional.of(expectedElectronic));

        int quantityToIncrement = 80;
        assertThrows(BuyMoreExceededException.class, () -> electronicService.increment(expectedElectronicDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementAfterSumIsGreatherThanMaxThenThrowException() {
        ElectronicDTO expectedElectronicDTO = ElectronicDTOBuilder.builder().build().toElectronicDTO();
        Electronic expectedBeer = electronicMapper.toModel(expectedElectronicDTO);

        when(electronicRepository.findById(expectedElectronicDTO.getId())).thenReturn(Optional.of(expectedBeer));

        int quantityToIncrement = 45;
        assertThrows(BuyMoreExceededException.class, () -> electronicService.increment(expectedElectronicDTO.getId(), quantityToIncrement));
    }

    @Test
    void whenIncrementIsCalledWithInvalidIdThenThrowException() {
        int quantityToIncrement = 10;

        when(electronicRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

        assertThrows(ElectronicNotFoundException.class, () -> electronicService.increment(INVALID_BEER_ID, quantityToIncrement));
    }

}
