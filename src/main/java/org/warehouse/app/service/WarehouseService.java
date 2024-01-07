package org.warehouse.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.warehouse.app.dto.warehouse.WarehouseDto;
import org.warehouse.app.entity.Warehouse;
import org.warehouse.app.exception.WarehouseValidationException;
import org.warehouse.app.helpers.DeletionCountService;
import org.warehouse.app.repository.WarehouseRepository;

import javax.transaction.Transactional;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final DeletionCountService deletionCountService;

    private final MessageSource messageSource;

    @Autowired
    public WarehouseService(WarehouseRepository warehouseRepository, DeletionCountService deletionCountService,
                            MessageSource messageSource) {
        this.warehouseRepository = warehouseRepository;
        this.deletionCountService = deletionCountService;
        this.messageSource = messageSource;
    }

    public List<WarehouseDto> getWarehouses() {
        return warehouseRepository.findAllByDeleted(false)
                .stream()
                .map(WarehouseDto::create)
                .collect(Collectors.toList());
    }

    public WarehouseDto getWarehouse(String name) {
        Warehouse warehouse = getWarehouseByName(name);
        return WarehouseDto.create(warehouse);
    }

    public WarehouseDto addNewWarehouse(String name) {
        Optional<Warehouse> existingWarehouse = warehouseRepository.findWarehouseByName(name);
        if (existingWarehouse.isPresent()) {
            throw new WarehouseValidationException(messageSource.getMessage("warehouse.service.already.exists",
                    new Object[]{name}, LocaleContextHolder.getLocale()));
        }
        Warehouse warehouse = new Warehouse(name);
        warehouseRepository.save(warehouse);

        return WarehouseDto.create(warehouse);
    }

    @Transactional
    public WarehouseDto updateWarehouse(String oldName, String newName) {
        Warehouse warehouseForUpdating = getWarehouseByName(oldName);
        if (warehouseForUpdating.getName().equals(newName)) {
            throw new WarehouseValidationException(messageSource.getMessage("warehouse.service.invalid.new.name",
                    null, LocaleContextHolder.getLocale()));
        }

        warehouseForUpdating.setName(newName);
        warehouseRepository.save(warehouseForUpdating);

        return WarehouseDto.create(warehouseForUpdating);
    }

    public WarehouseDto deleteWarehouse(String name) {
        Warehouse warehouse = getWarehouseByName(name);
        String deletedName = deletionCountService.defineDeletedWarehouseName(warehouse);
        warehouse.setName(deletedName);
        warehouse.setDeleted(true);
        warehouseRepository.save(warehouse);

        return WarehouseDto.create(warehouse);
    }

    private Warehouse getWarehouseByName(String name) {
        return warehouseRepository.findWarehouseByName(name)
                .orElseThrow(() -> new WarehouseValidationException(messageSource.getMessage("warehouse.service.not.exists",
                        new Object[]{name}, LocaleContextHolder.getLocale())));
    }

}