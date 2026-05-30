package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

@Service
public class VikingLambdaService {

    private static final String AXE_NAME = "axe";
    private static final String LEGENDARY_QUALITY = "Legendary";

    private final VikingStorage vikingStorage;
    private final Random random = new Random();

    public VikingLambdaService(VikingStorage vikingStorage) {
        this.vikingStorage = vikingStorage;
    }

    public long countByAgeGreaterThan(int age) {
        return countByCondition(viking -> viking.age() > age);
    }

    public long countByAgeLessThan(int age) {
        return countByCondition(viking -> viking.age() < age);
    }

    public long countByAgeInRange(int fromAge, int toAge) {
        int minAge = Math.min(fromAge, toAge);
        int maxAge = Math.max(fromAge, toAge);
        return countByCondition(viking -> viking.age() >= minAge && viking.age() <= maxAge);
    }

    public long countByAgeOutOfRange(int fromAge, int toAge) {
        int minAge = Math.min(fromAge, toAge);
        int maxAge = Math.max(fromAge, toAge);
        return countByCondition(viking -> viking.age() < minAge || viking.age() > maxAge);
    }

    public long countByBeardStyleAndHairColor(BeardStyle beardStyle, HairColor hairColor) {
        return countByCondition(viking -> viking.beardStyle() == beardStyle && viking.hairColor() == hairColor);
    }

    public long countWithOneOrTwoAxes() {
        return countByCondition(viking -> countAxes(viking) == 1 || countAxes(viking) == 2);
    }

    public Optional<Viking> findRandomVikingHigherThan(int minHeight) {
        List<Viking> vikings = vikingStorage.findAll().stream()
                .filter(viking -> viking.heightCm() > minHeight)
                .toList();

        return vikings.isEmpty()
                ? Optional.empty()
                : vikings.stream()
                .skip(random.nextInt(vikings.size()))
                .findFirst();
    }

    public Optional<Viking> findRandomVikingHigherThan180() {
        return findRandomVikingHigherThan(180);
    }

    public List<Viking> findAllWithEquipmentQuality(String quality) {
        return vikingStorage.findAll().stream()
                .filter(viking -> hasEquipmentQuality(viking, quality))
                .toList();
    }

    public List<Viking> findAllWithLegendaryEquipment() {
        return findAllWithEquipmentQuality(LEGENDARY_QUALITY);
    }

    public List<Viking> findByHairColorSortedByAge(HairColor hairColor) {
        return vikingStorage.findAll().stream()
                .filter(viking -> viking.hairColor() == hairColor)
                .sorted(Comparator.comparingInt(Viking::age))
                .toList();
    }

    public List<Viking> findRedBeardedSortedByAge() {
        return findByHairColorSortedByAge(HairColor.Red);
    }

    public Optional<Integer> findMaxIdFromArray() {
        Integer[] ids = getIdsArray();
        return Arrays.stream(ids)
                .max(Integer::compareTo);
    }

    public List<Integer> findEvenIdsFromArray() {
        Integer[] ids = getIdsArray();
        return Arrays.stream(ids)
                .filter(id -> id % 2 == 0)
                .toList();
    }

    private Integer[] getIdsArray() {
        return vikingStorage.findAll().stream()
                .map(Viking::id)
                .filter(id -> id != null)
                .toArray(Integer[]::new);
    }

    private long countByCondition(Predicate<Viking> condition) {
        return vikingStorage.findAll().stream()
                .filter(condition)
                .count();
    }

    private long countAxes(Viking viking) {
        return getEquipment(viking).stream()
                .map(EquipmentItem::name)
                .filter(name -> name != null)
                .filter(name -> name.toLowerCase().contains(AXE_NAME))
                .count();
    }

    private boolean hasEquipmentQuality(Viking viking, String quality) {
        return getEquipment(viking).stream()
                .map(EquipmentItem::quality)
                .filter(itemQuality -> itemQuality != null)
                .anyMatch(itemQuality -> itemQuality.equalsIgnoreCase(quality));
    }

    private List<EquipmentItem> getEquipment(Viking viking) {
        return viking.equipment() == null ? Collections.emptyList() : viking.equipment();
    }
}
