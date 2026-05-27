package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingLambdaService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings/lambda")
@Tag(name = "Viking Lambda Service", description = "Операции с викингами через лямбда-функции")
public class VikingLambdaController {

    private final VikingLambdaService vikingLambdaService;

    public VikingLambdaController(VikingLambdaService vikingLambdaService) {
        this.vikingLambdaService = vikingLambdaService;
    }

    @GetMapping("/count/age/greater-than/{age}")
    @Operation(summary = "Посчитать викингов старше указанного возраста", operationId = "countByAgeGreaterThan")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Количество успешно рассчитано")})
    public long countByAgeGreaterThan(@PathVariable int age) {
        return vikingLambdaService.countByAgeGreaterThan(age);
    }

    @GetMapping("/count/age/less-than/{age}")
    @Operation(summary = "Посчитать викингов младше указанного возраста", operationId = "countByAgeLessThan")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Количество успешно рассчитано")})
    public long countByAgeLessThan(@PathVariable int age) {
        return vikingLambdaService.countByAgeLessThan(age);
    }

    @GetMapping("/count/age/in-range")
    @Operation(summary = "Посчитать викингов в диапазоне возраста", operationId = "countByAgeInRange")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Количество успешно рассчитано")})
    public long countByAgeInRange(@RequestParam int fromAge, @RequestParam int toAge) {
        return vikingLambdaService.countByAgeInRange(fromAge, toAge);
    }

    @GetMapping("/count/age/out-of-range")
    @Operation(summary = "Посчитать викингов вне диапазона возраста", operationId = "countByAgeOutOfRange")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Количество успешно рассчитано")})
    public long countByAgeOutOfRange(@RequestParam int fromAge, @RequestParam int toAge) {
        return vikingLambdaService.countByAgeOutOfRange(fromAge, toAge);
    }

    @GetMapping("/count/appearance")
    @Operation(summary = "Посчитать викингов по форме бороды и цвету волос одновременно", operationId = "countByAppearance")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Количество успешно рассчитано")})
    public long countByBeardStyleAndHairColor(@RequestParam BeardStyle beardStyle, @RequestParam HairColor hairColor) {
        return vikingLambdaService.countByBeardStyleAndHairColor(beardStyle, hairColor);
    }

    @GetMapping("/count/one-or-two-axes")
    @Operation(summary = "Посчитать викингов, имеющих один или два топора", operationId = "countWithOneOrTwoAxes")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Количество успешно рассчитано")})
    public long countWithOneOrTwoAxes() {
        return vikingLambdaService.countWithOneOrTwoAxes();
    }

    @GetMapping("/screen/random-higher-than-180")
    @Operation(summary = "Получить случайного викинга ростом выше 180", operationId = "findRandomVikingHigherThan180")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг найден"),
            @ApiResponse(responseCode = "204", description = "Подходящих викингов нет")
    })
    public ResponseEntity<Viking> findRandomVikingHigherThan180() {
        return vikingLambdaService.findRandomVikingHigherThan180()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/screen/legendary-equipment")
    @Operation(summary = "Получить всех викингов с легендарным снаряжением", operationId = "findAllWithLegendaryEquipment")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Список успешно получен")})
    public List<Viking> findAllWithLegendaryEquipment() {
        return vikingLambdaService.findAllWithLegendaryEquipment();
    }

    @GetMapping("/screen/red-bearded-sorted-by-age")
    @Operation(summary = "Получить сортированный по возрасту список рыжебородых викингов", operationId = "findRedBeardedSortedByAge")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Список успешно получен")})
    public List<Viking> findRedBeardedSortedByAge() {
        return vikingLambdaService.findRedBeardedSortedByAge();
    }

    @GetMapping("/ids/max")
    @Operation(summary = "Найти последнюю запись по массиву ID", operationId = "findMaxIdFromArray")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ID найден"),
            @ApiResponse(responseCode = "204", description = "ID отсутствуют")
    })
    public ResponseEntity<Integer> findMaxIdFromArray() {
        return vikingLambdaService.findMaxIdFromArray()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/ids/even")
    @Operation(summary = "Получить все четные ID из массива", operationId = "findEvenIdsFromArray")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Список успешно получен")})
    public List<Integer> findEvenIdsFromArray() {
        return vikingLambdaService.findEvenIdsFromArray();
    }
}
