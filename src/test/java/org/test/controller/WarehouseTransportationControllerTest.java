package org.test.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.test.dto.ResponseDto;
import org.test.dto.transportation.WarehouseTransportationBuilderDto;
import org.test.dto.transportation.WarehouseTransportationDto;
import org.test.entity.Warehouse;
import org.test.entity.WarehouseTransportation;
import org.test.exception.WarehouseTransportationValidationException;
import org.test.helpers.DocumentRequestValidationService;
import org.test.service.WarehouseTransportationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doNothing;

@SpringBootTest(classes = {WarehouseTransportationController.class})
@RunWith(SpringRunner.class)
public class WarehouseTransportationControllerTest {

    @Autowired
    private WarehouseTransportationController warehouseTransportationController;

    @MockBean
    private WarehouseTransportationService warehouseTransportationService;

    @MockBean
    private DocumentRequestValidationService requestValidationService;

    @Test
    public void getWarehouseTransportationsTestSuccess() {
        WarehouseTransportation warehouseTransportation = new WarehouseTransportation();
        warehouseTransportation.setWarehouseFrom(new Warehouse());
        warehouseTransportation.setWarehouseTo(new Warehouse());
        warehouseTransportation.setProducts(new ArrayList<>());

        Mockito.when(warehouseTransportationService.getWarehouseTransportations())
                .thenReturn(Collections.singletonList(warehouseTransportation));

        ResponseEntity<ResponseDto<List<WarehouseTransportationDto>>> response =
                warehouseTransportationController.getWarehouseTransportations();

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(0, response.getBody().getStatus());
        Assert.assertEquals("?????????????????????? ?????????????????? ?????????????? ????????????????!", response.getBody().getMessage());
        Assert.assertEquals(1, response.getBody().getBody().size());
    }

    @Test
    public void getWarehouseTransportationsTestFailNotFound() {
        Mockito.when(warehouseTransportationService.getWarehouseTransportations())
                .thenThrow(new WarehouseTransportationValidationException("?? ???? ?????? ?????????????????????? ??????????????????!"));

        ResponseEntity<ResponseDto<List<WarehouseTransportationDto>>> response = warehouseTransportationController.getWarehouseTransportations();

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(1, response.getBody().getStatus());
        Assert.assertEquals("?? ???? ?????? ?????????????????????? ??????????????????!", response.getBody().getMessage());
        Assert.assertNull(response.getBody().getBody());
    }

    @Test
    public void getWarehouseTransportationsTestFailInternalError() {
        Mockito.when(warehouseTransportationService.getWarehouseTransportations()).thenThrow(new RuntimeException());

        ResponseEntity<ResponseDto<List<WarehouseTransportationDto>>> response =
                warehouseTransportationController.getWarehouseTransportations();

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(2, response.getBody().getStatus());
        Assert.assertEquals("???????????????????? ????????????, ?????????????????????? ?????????????????? ???? ????????????????!", response.getBody().getMessage());
        Assert.assertNull(response.getBody().getBody());
    }

    @Test
    public void getWarehouseTransportationByIdTestSuccess() {
        WarehouseTransportation warehouseTransportation = new WarehouseTransportation();
        warehouseTransportation.setWarehouseFrom(new Warehouse());
        warehouseTransportation.setWarehouseTo(new Warehouse());
        warehouseTransportation.setProducts(new ArrayList<>());
        WarehouseTransportationDto warehouseTransportationDto = WarehouseTransportationDto.create(warehouseTransportation);

        Mockito.when(warehouseTransportationService.getWarehouseTransportationById(warehouseTransportation.getId()))
                .thenReturn(warehouseTransportation);

        ResponseEntity<ResponseDto<WarehouseTransportationDto>> response =
                warehouseTransportationController.getWarehouseTransportationById(warehouseTransportation.getId());

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(0, response.getBody().getStatus());
        Assert.assertEquals("?????????????????????? ?????????????????? ?????????????? ????????????????!", response.getBody().getMessage());
        Assert.assertEquals(response.getBody().getBody(), warehouseTransportationDto);
    }

    @Test
    public void getWarehouseTransportationByIdTestFailNotFound() {
        WarehouseTransportation warehouseTransportation = new WarehouseTransportation();

        Mockito.when(warehouseTransportationService.getWarehouseTransportationById(warehouseTransportation.getId()))
                .thenThrow(new WarehouseTransportationValidationException(
                        "?????????????????????? ?????????????? ?? ?????????????? " + warehouseTransportation.getId() + " ???? ????????????????????!"));

        ResponseEntity<ResponseDto<WarehouseTransportationDto>> response =
                warehouseTransportationController.getWarehouseTransportationById(warehouseTransportation.getId());

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(1, response.getBody().getStatus());
        Assert.assertEquals("?????????????????????? ?????????????? ?? ?????????????? " + warehouseTransportation.getId() + " ???? ????????????????????!",
                response.getBody().getMessage());
        Assert.assertNull(response.getBody().getBody());
    }

    @Test
    public void getWarehouseTransportationByIdTestFailInternalError() {
        Mockito.when(warehouseTransportationService.getWarehouseTransportationById(Mockito.any()))
                .thenThrow(new RuntimeException("???????????????????? ????????????"));

        ResponseEntity<ResponseDto<WarehouseTransportationDto>> response =
                warehouseTransportationController.getWarehouseTransportationById(Mockito.any());

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(2, response.getBody().getStatus());
        Assert.assertEquals("???????????????????? ????????????, ?????????????????????? ?????????????????? ???? ????????????????!", response.getBody().getMessage());
        Assert.assertNull(response.getBody().getBody());
    }

    @Test
    public void addNewWarehouseTransportationTestSuccess() {
        WarehouseTransportation warehouseTransportation = new WarehouseTransportation();
        warehouseTransportation.setWarehouseFrom(new Warehouse());
        warehouseTransportation.setWarehouseTo(new Warehouse());
        warehouseTransportation.setProducts(new ArrayList<>());
        WarehouseTransportationDto warehouseTransportationDto = WarehouseTransportationDto.create(warehouseTransportation);

        Mockito.when(warehouseTransportationService.addNewWarehouseTransportation(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(warehouseTransportation);

        ResponseEntity<ResponseDto<WarehouseTransportationDto>> response =
                warehouseTransportationController.addNewWarehouseTransportation(new WarehouseTransportationBuilderDto());

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(0, response.getBody().getStatus());
        Assert.assertEquals("?????????????????????? ?????????????????? ?????????????? ??????????????????!", response.getBody().getMessage());
        Assert.assertEquals(response.getBody().getBody(), warehouseTransportationDto);
    }

    @Test
    public void addNewWarehouseTransportationTestFailWarehouseNotExist() {
        WarehouseTransportation warehouseTransportation = new WarehouseTransportation();
        warehouseTransportation.setWarehouseFrom(new Warehouse());
        warehouseTransportation.setWarehouseTo(new Warehouse());
        warehouseTransportation.setProducts(new ArrayList<>());
        WarehouseTransportationDto warehouseTransportationDto = WarehouseTransportationDto.create(warehouseTransportation);

        Mockito.when(warehouseTransportationService.addNewWarehouseTransportation(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new WarehouseTransportationValidationException(
                        "???????????? ?? ???????????? "
                                + warehouseTransportationDto.getWarehouseNameFrom()
                                + " ???? ????????????????????!"));

        ResponseEntity<ResponseDto<WarehouseTransportationDto>> response =
                warehouseTransportationController.addNewWarehouseTransportation(new WarehouseTransportationBuilderDto());

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(1, response.getBody().getStatus());
        Assert.assertEquals("???????????? ?? ???????????? " + warehouseTransportationDto.getWarehouseNameFrom() + " ???? ????????????????????!",
                response.getBody().getMessage());
        Assert.assertNull(response.getBody().getBody());

    }

    @Test
    public void addNewWarehouseTransportationTestFailInternalError() {
        Mockito.when(warehouseTransportationService.addNewWarehouseTransportation(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new RuntimeException("???????????????????? ????????????"));

        ResponseEntity<ResponseDto<WarehouseTransportationDto>> response =
                warehouseTransportationController.addNewWarehouseTransportation(new WarehouseTransportationBuilderDto());

        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(2, response.getBody().getStatus());
        Assert.assertEquals("???????????????????? ????????????, ?????????????????????? ?????????????????? ???? ??????????????????!", response.getBody().getMessage());
        Assert.assertNull(response.getBody().getBody());

    }

}