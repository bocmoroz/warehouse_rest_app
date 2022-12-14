package org.test.dto.warehouse;

import lombok.Data;
import org.test.entity.Warehouse;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class WarehouseDto {

    private String warehouseName;
    private List<WarehouseProductDto> products;

    public static WarehouseDto create(Warehouse warehouse) {
        WarehouseDto warehouseDto = new WarehouseDto();
        warehouseDto.setWarehouseName(warehouse.getName());
        warehouseDto.setProducts(warehouse.getProducts().stream().map(
                productCount -> {
                    WarehouseProductDto warehouseProductDto = new WarehouseProductDto();
                    warehouseProductDto.setArticul(productCount.getProduct().getArticul());
                    warehouseProductDto.setName(productCount.getProduct().getName());
                    warehouseProductDto.setCount(productCount.getCount());
                    return warehouseProductDto;
                }).collect(Collectors.toList()));

        return warehouseDto;

    }

}
