package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingLambdaService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import java.util.stream.Collectors;

public class VikingLambdaInfoFrame extends JFrame {

    private final VikingLambdaService vikingLambdaService;
    private final JTextArea outputArea = new JTextArea();

    public VikingLambdaInfoFrame(VikingLambdaService vikingLambdaService) {
        this.vikingLambdaService = vikingLambdaService;

        setTitle("Lambda Viking Information");
        setSize(new Dimension(900, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh lambda information");
        refreshButton.addActionListener(event -> refreshReport());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshReport();
    }

    private void refreshReport() {
        String report = """
                LAMBDA SERVICE REPORT
                =====================

                1) Оценка объема выборки по условиям
                """
                + "Возраст больше 30: " + vikingLambdaService.countByAgeGreaterThan(30) + "\n"
                + "Возраст меньше 30: " + vikingLambdaService.countByAgeLessThan(30) + "\n"
                + "Возраст в диапазоне 25-40: " + vikingLambdaService.countByAgeInRange(25, 40) + "\n"
                + "Возраст вне диапазона 25-40: " + vikingLambdaService.countByAgeOutOfRange(25, 40) + "\n"
                + "Форма бороды LONG и цвет волос Red одновременно: "
                + vikingLambdaService.countByBeardStyleAndHairColor(BeardStyle.LONG, HairColor.Red) + "\n"
                + "Имеют один или два топора: " + vikingLambdaService.countWithOneOrTwoAxes() + "\n\n"
                + "2) Информация для вывода на экран\n"
                + "Случайный викинг ростом выше 180:\n"
                + vikingLambdaService.findRandomVikingHigherThan180()
                .map(this::formatViking)
                .orElse("Подходящий викинг не найден") + "\n\n"
                + "Все викинги с легендарным снаряжением:\n"
                + formatVikingList(vikingLambdaService.findAllWithLegendaryEquipment()) + "\n\n"
                + "Сортированный по возрасту список рыжебородых викингов:\n"
                + formatVikingList(vikingLambdaService.findRedBeardedSortedByAge()) + "\n\n"
                + "3) Операции с массивом ID\n"
                + "Последняя запись (max ID): "
                + vikingLambdaService.findMaxIdFromArray()
                .map(String::valueOf)
                .orElse("ID отсутствуют") + "\n"
                + "Все четные ID: " + vikingLambdaService.findEvenIdsFromArray() + "\n";

        outputArea.setText(report);
        outputArea.setCaretPosition(0);
    }

    private String formatVikingList(List<Viking> vikings) {
        if (vikings.isEmpty()) {
            return "Подходящие викинги не найдены";
        }

        return vikings.stream()
                .map(this::formatViking)
                .collect(Collectors.joining("\n"));
    }

    private String formatViking(Viking viking) {
        return "ID: " + viking.id()
                + ", Name: " + viking.name()
                + ", Age: " + viking.age()
                + ", Height: " + viking.heightCm()
                + ", Hair: " + viking.hairColor()
                + ", Beard: " + viking.beardStyle()
                + ", Equipment: " + formatEquipment(viking.equipment());
    }

    private String formatEquipment(List<EquipmentItem> equipment) {
        return equipment.stream()
                .map(item -> item.name() + " [" + item.quality() + "]")
                .collect(Collectors.joining(", "));
    }
}
