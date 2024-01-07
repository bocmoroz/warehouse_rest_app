package org.warehouse.app.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
@EntityListeners(AuditingEntityListener.class)
public class WarehouseTransportation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private Date createDate;

    @OneToOne
    private Warehouse warehouseFrom;
    @OneToOne
    private Warehouse warehouseTo;

    @ElementCollection
    @CollectionTable(name = "transportation_products_count",
            joinColumns = @JoinColumn(name = "warehouse_transportation_id", nullable = false))
    private List<ProductCount> products;

}