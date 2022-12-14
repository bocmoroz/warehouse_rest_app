package org.test.dto.sale;

import lombok.Data;

import java.util.List;

@Data
public class WarehouseSaleBuilderDto {

    private String warehouseName;
    private List<ProductSaleDto> products;

}
