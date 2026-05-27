package ru.mephi.vikingdemo.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.EquipmentItemEntity;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.VikingEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class VikingStorage {

    private final VikingRepository vikingRepository;
    private final EquipmentItemRepository equipmentItemRepository;
    private final VikingMapper vikingMapper;

    public VikingStorage(
            VikingRepository vikingRepository,
            EquipmentItemRepository equipmentItemRepository,
            VikingMapper vikingMapper
    ) {
        this.vikingRepository = vikingRepository;
        this.equipmentItemRepository = equipmentItemRepository;
        this.vikingMapper = vikingMapper;
    }

    @Transactional
    public Viking save(Viking viking) {
        validateViking(viking);

        Integer vikingId = vikingRepository.save(vikingMapper.toVikingEntity(viking));

        for (EquipmentItem item : viking.equipment()) {
            equipmentItemRepository.save(vikingMapper.toEquipmentItemEntity(vikingId, item));
        }

        return findById(vikingId);
    }

    public List<Viking> findAll() {
        List<VikingEntity> vikingEntities = vikingRepository.findAll();
        List<EquipmentItemEntity> equipmentEntities = equipmentItemRepository.findAll();

        Map<Integer, List<EquipmentItemEntity>> equipmentByVikingId = equipmentEntities.stream()
                .collect(Collectors.groupingBy(EquipmentItemEntity::vikingId));

        return vikingEntities.stream()
                .map(vikingEntity -> vikingMapper.toViking(
                        vikingEntity,
                        equipmentByVikingId.getOrDefault(vikingEntity.id(), List.of())
                ))
                .toList();
    }

    public Viking findById(int id) {
        VikingEntity vikingEntity = vikingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Викинг с id " + id + " не найден"));

        List<EquipmentItemEntity> equipment = equipmentItemRepository.findByVikingId(id);
        return vikingMapper.toViking(vikingEntity, equipment);
    }

    @Transactional
    public Viking updateById(int id, Viking viking) {
        validateViking(viking);
        findById(id);

        int updatedRows = vikingRepository.updateById(id, vikingMapper.toVikingEntity(viking));
        if (updatedRows == 0) {
            throw new IllegalArgumentException("Викинг с id " + id + " не найден");
        }

        equipmentItemRepository.deleteByVikingId(id);
        for (EquipmentItem item : viking.equipment()) {
            equipmentItemRepository.save(vikingMapper.toEquipmentItemEntity(id, item));
        }

        return findById(id);
    }

    @Transactional
    public Viking deleteById(int id) {
        Viking deletedViking = findById(id);
        int deletedRows = vikingRepository.deleteById(id);

        if (deletedRows == 0) {
            throw new IllegalArgumentException("Викинг с id " + id + " не найден");
        }

        return deletedViking;
    }

    private void validateViking(Viking viking) {
        if (viking == null) {
            throw new IllegalArgumentException("Викинг не должен быть null");
        }
        if (viking.name() == null || viking.name().isBlank()) {
            throw new IllegalArgumentException("Имя викинга не должно быть пустым");
        }
        if (viking.hairColor() == null) {
            throw new IllegalArgumentException("Цвет волос викинга должен быть указан");
        }
        if (viking.beardStyle() == null) {
            throw new IllegalArgumentException("Форма бороды викинга должна быть указана");
        }
        if (viking.equipment() == null) {
            throw new IllegalArgumentException("Список снаряжения не должен быть null");
        }
    }
}
