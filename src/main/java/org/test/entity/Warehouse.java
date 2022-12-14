package org.test.entity;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @UpdateTimestamp
    private Date lastChangeDate;

    @Column(nullable = false)
    private Boolean deleted;

    @ElementCollection
    @CollectionTable(name = "Warehouse_products_count",
            joinColumns = @JoinColumn(name = "warehouse_id", nullable = false))
    @AttributeOverrides({
            @AttributeOverride(name = "product",
                    column = @Column(name = "product_id")),
            @AttributeOverride(name = "count",
                    column = @Column(name = "count"))
    })
    private List<ProductCount> products;

    public Warehouse() {
    }

    public Warehouse(String name) {
        this.name = name;
    }

}
