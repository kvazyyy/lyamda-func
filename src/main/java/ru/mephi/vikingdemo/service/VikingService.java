package ru.mephi.vikingdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class VikingService {

    private final VikingFactory vikingFactory;
    private final VikingStorage vikingStorage;

    @Autowired
    public VikingService(
            VikingFactory vikingFactory,
            VikingStorage vikingStorage
    ) {
        this.vikingFactory = vikingFactory;
        this.vikingStorage = vikingStorage;
    }

    public List<Viking> findAll() {
        return vikingStorage.findAll();
    }

    public Viking createRandomViking() {
        Viking viking = vikingFactory.createRandomViking();
        return vikingStorage.save(viking);
    }

    public List<Viking> generateRandomVikings(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количество викингов должно быть больше 0");
        }

        return IntStream.range(0, count)
                .mapToObj(index -> vikingFactory.createRandomViking())
                .map(vikingStorage::save)
                .toList();
    }

    public Viking addViking(Viking viking) {
        return vikingStorage.save(viking);
    }

    public Viking updateViking(int id, Viking viking) {
        return vikingStorage.updateById(id, viking);
    }

    public Viking deleteById(int id) {
        return vikingStorage.deleteById(id);
    }
}
