package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.Arrays;
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

    public Optional<Viking> findRandomVikingHigherThan180() {
        List<Viking> vikings = vikingStorage.findAll().stream()
                .filter(viking -> viking.heightCm() > 180)
                .toList();

        return vikings.isEmpty()
                ? Optional.empty()
                : vikings.stream()
                .skip(random.nextInt(vikings.size()))
                .findFirst();
    }

    public List<Viking> findAllWithLegendaryEquipment() {
        return vikingStorage.findAll().stream()
                .filter(this::hasLegendaryEquipment)
                .toList();
    }

    public List<Viking> findRedBeardedSortedByAge() {
        return vikingStorage.findAll().stream()
                .filter(viking -> viking.hairColor() == HairColor.Red)
                .sorted(Comparator.comparingInt(Viking::age))
                .toList();
    }

    public Optional<Integer> findMaxIdFromArray() {
        Integer[] ids = vikingStorage.findAll().stream()
                .map(Viking::id)
                .filter(id -> id != null)
                .toArray(Integer[]::new);

        return Arrays.stream(ids)
                .max(Integer::compareTo);
    }

    public List<Integer> findEvenIdsFromArray() {
        Integer[] ids = vikingStorage.findAll().stream()
                .map(Viking::id)
                .filter(id -> id != null)
                .toArray(Integer[]::new);

        return Arrays.stream(ids)
                .filter(id -> id % 2 == 0)
                .toList();
    }

    private long countByCondition(Predicate<Viking> condition) {
        return vikingStorage.findAll().stream()
                .filter(condition)
                .count();
    }

    private long countAxes(Viking viking) {
        return viking.equipment().stream()
                .map(EquipmentItem::name)
                .filter(name -> name != null)
                .filter(name -> name.toLowerCase().contains(AXE_NAME))
                .count();
    }

    private boolean hasLegendaryEquipment(Viking viking) {
        return viking.equipment().stream()
                .map(EquipmentItem::quality)
                .filter(quality -> quality != null)
                .anyMatch(LEGENDARY_QUALITY::equalsIgnoreCase);
    }
}
