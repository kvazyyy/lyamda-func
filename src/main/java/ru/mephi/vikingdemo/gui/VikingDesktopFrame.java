package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingLambdaService;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingLambdaService vikingLambdaService;
    private final VikingTableModel tableModel = new VikingTableModel();

    public VikingDesktopFrame(VikingService vikingService, VikingLambdaService vikingLambdaService) {
        this.vikingService = vikingService;
        this.vikingLambdaService = vikingLambdaService;

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1100, 470));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JButton createButton = new JButton("Create random viking");
        createButton.addActionListener(event -> onCreateViking());

        JButton generateButton = new JButton("Generate vikings");
        generateButton.addActionListener(event -> onGenerateVikings());

        JButton lambdaInfoButton = new JButton("Open lambda analytics");
        lambdaInfoButton.addActionListener(event -> onOpenLambdaPage());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        bottomPanel.add(generateButton);
        bottomPanel.add(lambdaInfoButton);
        add(bottomPanel, BorderLayout.SOUTH);

        onInit();
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }

    private void onGenerateVikings() {
        String value = JOptionPane.showInputDialog(this, "Введите количество викингов для генерации:", "20");

        if (value == null) {
            return;
        }

        try {
            int count = Integer.parseInt(value.trim());
            List<Viking> generatedVikings = vikingService.generateRandomVikings(count);
            generatedVikings.forEach(tableModel::addViking);
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Количество должно быть целым числом");
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
    }

    private void onOpenLambdaPage() {
        VikingLambdaInfoFrame frame = new VikingLambdaInfoFrame(vikingLambdaService);
        frame.setVisible(true);
    }

    public void addNewViking(Viking viking) {
        tableModel.addViking(viking);
    }

    public void deleteVikingById(int id) {
        tableModel.deleteVikingById(id);
    }

    public void updateVikingById(int id, Viking viking) {
        tableModel.updateVikingById(id, viking);
    }

    private void onInit() {
        List<Viking> all = vikingService.findAll();
        all.forEach(tableModel::addViking);
    }
}
