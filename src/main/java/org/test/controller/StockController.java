package org.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.dto.ResponseDto;
import org.test.dto.leftover.StockReportDto;
import org.test.dto.warehouse.WarehouseDto;
import org.test.service.WarehouseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/stocks")
public class StockController {

    private final WarehouseService warehouseService;

    @Autowired
    public StockController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public ResponseDto<List<StockReportDto>> getStocks() {
        List<WarehouseDto> warehouseList = warehouseService.getWarehouses();

        List<StockReportDto> stockReportDtoList = new ArrayList<>();

        warehouseList.forEach(wh -> wh.getProducts().stream()
                .filter(product -> product.getCount() > 0)
                .forEach(warehouseProductDto -> {
                    Optional<StockReportDto> optionalLeftoverReportDto = stockReportDtoList.stream()
                            .filter(p -> p.getArticle().equals(warehouseProductDto.getArticle())).findAny();

                    if (optionalLeftoverReportDto.isPresent()) {
                        StockReportDto stockReportDto = optionalLeftoverReportDto.get();
                        stockReportDto.setCount(stockReportDto.getCount() + warehouseProductDto.getCount());
                    } else {
                        StockReportDto dto = new StockReportDto();
                        dto.setArticle(warehouseProductDto.getArticle());
                        dto.setName(warehouseProductDto.getName());
                        dto.setCount(warehouseProductDto.getCount());
                        stockReportDtoList.add(dto);
                    }
                }));

        return ResponseDto.success(stockReportDtoList);
    }
}
