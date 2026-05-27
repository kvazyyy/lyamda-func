package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private final VikingListener vikingListener;

    public VikingController(VikingService vikingService, VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
    }

    @GetMapping
    @Operation(summary = "Получить список созданных викингов", operationId = "getAllVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<Viking> getAllVikings() {
        System.out.println("GET /api/vikings called");
        return vikingService.findAll();
    }

    @GetMapping("/test")
    @Operation(summary = "Получить список тестовых викингов", operationId = "getTest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список успешно получен")
    })
    public List<String> test() {
        System.out.println("GET /api/vikings/test called");
        return List.of("Ragnar", "Bjorn");
    }

    @PostMapping("/post")
    @Operation(summary = "Создать викинга со случайными параметрами", operationId = "createRandomViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно создан")
    })
    public Viking addRandomViking() {
        System.out.println("POST /api/vikings/post called");
        return vikingListener.createRandomViking();
    }

    @PostMapping("/generate/{count}")
    @Operation(summary = "Массово сгенерировать случайных викингов", operationId = "generateRandomVikings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинги успешно сгенерированы"),
            @ApiResponse(responseCode = "400", description = "Передано некорректное количество")
    })
    public List<Viking> generateRandomVikings(@PathVariable int count) {
        System.out.println("POST /api/vikings/generate/" + count + " called");
        return vikingListener.generateRandomVikings(count);
    }

    @PostMapping
    @Operation(summary = "Добавить конкретного викинга", operationId = "addSpecificViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Переданы некорректные параметры викинга")
    })
    public Viking addSpecificViking(@RequestBody Viking viking) {
        System.out.println("POST /api/vikings called");
        return vikingListener.addViking(viking);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Перезаписать параметры конкретного викинга", operationId = "updateViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Викинг с указанным id не найден или переданы некорректные параметры")
    })
    public Viking updateViking(@PathVariable int id, @RequestBody Viking viking) {
        System.out.println("PUT /api/vikings/" + id + " called");
        return vikingListener.updateViking(id, viking);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить викинга из таблицы по id", operationId = "deleteViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно удалён"),
            @ApiResponse(responseCode = "400", description = "Викинг с указанным id не найден")
    })
    public Viking deleteViking(@PathVariable int id) {
        System.out.println("DELETE /api/vikings/" + id + " called");
        return vikingListener.deleteViking(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
