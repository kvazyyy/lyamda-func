package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.BeardStyle;
import ru.mephi.vikingdemo.model.EquipmentItem;    
import ru.mephi.vikingdemo.model.HairColor;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingLambdaService;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.stream.Collectors;

public class VikingLambdaInfoFrame extends JFrame {

    private final VikingLambdaService vikingLambdaService;

    private final JTextArea countOutputArea = createOutputArea();
    private final JTextArea screenOutputArea = createOutputArea();
    private final JTextArea idOutputArea = createOutputArea();

    private final JSpinner ageGreaterSpinner = createNumberSpinner(30, 0, 200);
    private final JSpinner ageLessSpinner = createNumberSpinner(30, 0, 200);
    private final JSpinner ageRangeFromSpinner = createNumberSpinner(25, 0, 200);
    private final JSpinner ageRangeToSpinner = createNumberSpinner(40, 0, 200);
    private final JSpinner ageOutRangeFromSpinner = createNumberSpinner(25, 0, 200);
    private final JSpinner ageOutRangeToSpinner = createNumberSpinner(40, 0, 200);
    private final JComboBox<BeardStyle> beardStyleBox = new JComboBox<>(BeardStyle.values());
    private final JComboBox<HairColor> hairColorBox = new JComboBox<>(HairColor.values());

    private final JSpinner minHeightSpinner = createNumberSpinner(180, 0, 250);
    private final JTextField qualityField = new JTextField("Legendary", 12);
    private final JComboBox<HairColor> sortedHairColorBox = new JComboBox<>(HairColor.values());

    public VikingLambdaInfoFrame(VikingLambdaService vikingLambdaService) {
        this.vikingLambdaService = vikingLambdaService;

        setTitle("Lambda analytics");
        setSize(new Dimension(980, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        beardStyleBox.setSelectedItem(BeardStyle.LONG);
        hairColorBox.setSelectedItem(HairColor.Red);
        sortedHairColorBox.setSelectedItem(HairColor.Red);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("1)", createCountTab());
        tabs.addTab("2)", createScreenInfoTab());
        tabs.addTab("3)", createIdArrayTab());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createCountTab() {
        JPanel tab = new JPanel(new BorderLayout(10, 10));
        JPanel controls = createVerticalPanel();

        JButton ageGreaterButton = new JButton("Посчитать");
        ageGreaterButton.addActionListener(event -> runAgeGreaterThan());
        controls.add(createRow(new JLabel("Возраст больше:"), ageGreaterSpinner, ageGreaterButton));

        JButton ageLessButton = new JButton("Посчитать");
        ageLessButton.addActionListener(event -> runAgeLessThan());
        controls.add(createRow(new JLabel("Возраст меньше:"), ageLessSpinner, ageLessButton));

        JButton ageInRangeButton = new JButton("Посчитать");
        ageInRangeButton.addActionListener(event -> runAgeInRange());
        controls.add(createRow(
                new JLabel("Возраст в диапазоне:"),
                new JLabel("от"),
                ageRangeFromSpinner,
                new JLabel("до"),
                ageRangeToSpinner,
                ageInRangeButton
        ));

        JButton ageOutOfRangeButton = new JButton("Посчитать");
        ageOutOfRangeButton.addActionListener(event -> runAgeOutOfRange());
        controls.add(createRow(
                new JLabel("Возраст вне диапазона:"),
                new JLabel("от"),
                ageOutRangeFromSpinner,
                new JLabel("до"),
                ageOutRangeToSpinner,
                ageOutOfRangeButton
        ));

        JButton appearanceButton = new JButton("Посчитать");
        appearanceButton.addActionListener(event -> runAppearanceCount());
        controls.add(createRow(
                new JLabel("Форма бороды:"),
                beardStyleBox,
                new JLabel("Цвет волос:"),
                hairColorBox,
                appearanceButton
        ));

        JButton axesButton = new JButton("Посчитать");
        axesButton.addActionListener(event -> runOneOrTwoAxesCount());
        controls.add(createRow(new JLabel("Имеют один или два топора:"), axesButton));

        JButton clearButton = new JButton("Очистить вывод");
        clearButton.addActionListener(event -> countOutputArea.setText(""));
        controls.add(createRow(clearButton));

        tab.add(controls, BorderLayout.NORTH);
        tab.add(new JScrollPane(countOutputArea), BorderLayout.CENTER);
        countOutputArea.setText("Нажми нужную кнопку, чтобы выполнить конкретную lambda-функцию.\n");
        return tab;
    }

    private JPanel createScreenInfoTab() {
        JPanel tab = new JPanel(new BorderLayout(10, 10));
        JPanel controls = createVerticalPanel();

        JButton randomByHeightButton = new JButton("Найти случайного");
        randomByHeightButton.addActionListener(event -> runRandomVikingByHeight());
        controls.add(createRow(
                new JLabel("Случайный викинг ростом выше:"),
                minHeightSpinner,
                randomByHeightButton
        ));

        JButton legendaryButton = new JButton("Показать");
        legendaryButton.addActionListener(event -> runVikingsByQuality());
        controls.add(createRow(
                new JLabel("Викинги со снаряжением качества:"),
                qualityField,
                legendaryButton
        ));

        JButton redBeardedButton = new JButton("Показать");
        redBeardedButton.addActionListener(event -> runRedBeardedSortedByAge());
        controls.add(createRow(
                new JLabel("Сортированный по возрасту список рыжебородых викингов:"),
                redBeardedButton
        ));

        JButton clearButton = new JButton("Очистить вывод");
        clearButton.addActionListener(event -> screenOutputArea.setText(""));
        controls.add(createRow(clearButton));

        tab.add(controls, BorderLayout.NORTH);
        tab.add(new JScrollPane(screenOutputArea), BorderLayout.CENTER);
        screenOutputArea.setText("Нажми кнопку, чтобы вывести нужную информацию на экран.\n");
        return tab;
    }

    private JPanel createIdArrayTab() {
        JPanel tab = new JPanel(new BorderLayout(10, 10));
        JPanel controls = createVerticalPanel();

        JButton maxIdButton = new JButton("Найти max ID");
        maxIdButton.addActionListener(event -> runMaxId());
        controls.add(createRow(new JLabel("Последняя запись:"), maxIdButton));

        JButton evenIdsButton = new JButton("Показать четные ID");
        evenIdsButton.addActionListener(event -> runEvenIds());
        controls.add(createRow(new JLabel("Все четные ID:"), evenIdsButton));

        JButton clearButton = new JButton("Очистить вывод");
        clearButton.addActionListener(event -> idOutputArea.setText(""));
        controls.add(createRow(clearButton));

        tab.add(controls, BorderLayout.NORTH);
        tab.add(new JScrollPane(idOutputArea), BorderLayout.CENTER);
        idOutputArea.setText("Нажми кнопку, чтобы выполнить операцию с массивом ID.\n");
        return tab;
    }

    private void runAgeGreaterThan() {
        int age = getSpinnerValue(ageGreaterSpinner);
        long result = vikingLambdaService.countByAgeGreaterThan(age);
        appendLine(countOutputArea, "Возраст больше " + age + ": " + result);
    }

    private void runAgeLessThan() {
        int age = getSpinnerValue(ageLessSpinner);
        long result = vikingLambdaService.countByAgeLessThan(age);
        appendLine(countOutputArea, "Возраст меньше " + age + ": " + result);
    }

    private void runAgeInRange() {
        int fromAge = getSpinnerValue(ageRangeFromSpinner);
        int toAge = getSpinnerValue(ageRangeToSpinner);
        long result = vikingLambdaService.countByAgeInRange(fromAge, toAge);
        appendLine(countOutputArea, "Возраст в диапазоне " + fromAge + "-" + toAge + ": " + result);
    }

    private void runAgeOutOfRange() {
        int fromAge = getSpinnerValue(ageOutRangeFromSpinner);
        int toAge = getSpinnerValue(ageOutRangeToSpinner);
        long result = vikingLambdaService.countByAgeOutOfRange(fromAge, toAge);
        appendLine(countOutputArea, "Возраст вне диапазона " + fromAge + "-" + toAge + ": " + result);
    }

    private void runAppearanceCount() {
        BeardStyle beardStyle = (BeardStyle) beardStyleBox.getSelectedItem();
        HairColor hairColor = (HairColor) hairColorBox.getSelectedItem();
        long result = vikingLambdaService.countByBeardStyleAndHairColor(beardStyle, hairColor);
        appendLine(countOutputArea, "Форма бороды " + beardStyle + " и цвет волос " + hairColor + " одновременно: " + result);
    }

    private void runOneOrTwoAxesCount() {
        long result = vikingLambdaService.countWithOneOrTwoAxes();
        appendLine(countOutputArea, "Имеют один или два топора: " + result);
    }

    private void runRandomVikingByHeight() {
        int minHeight = getSpinnerValue(minHeightSpinner);
        String result = vikingLambdaService.findRandomVikingHigherThan(minHeight)
                .map(this::formatViking)
                .orElse("Подходящий викинг не найден");
        appendBlock(screenOutputArea, "Случайный викинг ростом выше " + minHeight + ":\n" + result);
    }

    private void runVikingsByQuality() {
        String quality = qualityField.getText().trim();
        if (quality.isEmpty()) {
            quality = "Legendary";
            qualityField.setText(quality);
        }

        List<Viking> vikings = vikingLambdaService.findAllWithEquipmentQuality(quality);
        appendBlock(screenOutputArea, "Все викинги со снаряжением качества " + quality + ":\n" + formatVikingList(vikings));
    }

    private void runSortedByHairColor() {
        HairColor hairColor = (HairColor) sortedHairColorBox.getSelectedItem();
        List<Viking> vikings = vikingLambdaService.findByHairColorSortedByAge(hairColor);
        appendBlock(screenOutputArea, "Сортированный по возрасту список викингов с цветом волос " + hairColor + ":\n" + formatVikingList(vikings));
    }

    private void runMaxId() {
        String result = vikingLambdaService.findMaxIdFromArray()
                .map(String::valueOf)
                .orElse("ID отсутствуют");
        appendLine(idOutputArea, "Последняя запись (max ID): " + result);
    }

    private void runEvenIds() {
        appendLine(idOutputArea, "Все четные ID: " + vikingLambdaService.findEvenIdsFromArray());
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
        if (equipment == null || equipment.isEmpty()) {
            return "нет снаряжения";
        }

        return equipment.stream()
                .map(item -> item.name() + " [" + item.quality() + "]")
                .collect(Collectors.joining(", "));
    }

    private JTextArea createOutputArea() {
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        return outputArea;
    }

    private JSpinner createNumberSpinner(int value, int minimum, int maximum) {
        return new JSpinner(new SpinnerNumberModel(value, minimum, maximum, 1));
    }

    private JPanel createVerticalPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    private JPanel createRow(Component... components) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (Component component : components) {
            if (component instanceof JComponent jComponent) {
                jComponent.setAlignmentX(Component.LEFT_ALIGNMENT);
            }
            row.add(component);
        }
        return row;
    }

    private int getSpinnerValue(JSpinner spinner) {
        return ((Number) spinner.getValue()).intValue();
    }

    private void appendLine(JTextArea outputArea, String text) {
        outputArea.append(text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private void appendBlock(JTextArea outputArea, String text) {
        outputArea.append(text + "\n\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
    private void runRedBeardedSortedByAge() {
    List<Viking> vikings = lambdaService.findRedBeardedSortedByAge();

    StringBuilder result = new StringBuilder();
    result.append("Сортированный по возрасту список рыжебородых викингов:\n");

    if (vikings.isEmpty()) {
        result.append("Подходящие викинги не найдены");
    } else {
        vikings.forEach(viking ->
                result.append(formatViking(viking)).append("\n")
        );
    }

    outputArea.setText(result.toString());
}
}
