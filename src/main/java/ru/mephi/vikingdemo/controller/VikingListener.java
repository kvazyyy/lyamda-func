package ru.mephi.vikingdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mephi.vikingdemo.gui.VikingDesktopFrame;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.SwingUtilities;
import java.util.List;

@Component
public class VikingListener {

    private final VikingService service;
    private VikingDesktopFrame gui;

    @Autowired
    public VikingListener(VikingService service) {
        this.service = service;
    }

    public void setGui(VikingDesktopFrame gui) {
        this.gui = gui;
    }

    public Viking createRandomViking() {
        Viking viking = service.createRandomViking();

        if (gui != null) {
            SwingUtilities.invokeLater(() -> gui.addNewViking(viking));
        }

        return viking;
    }

    public List<Viking> generateRandomVikings(int count) {
        List<Viking> vikings = service.generateRandomVikings(count);

        if (gui != null) {
            SwingUtilities.invokeLater(() -> vikings.forEach(gui::addNewViking));
        }

        return vikings;
    }

    public Viking addViking(Viking viking) {
        Viking addedViking = service.addViking(viking);

        if (gui != null) {
            SwingUtilities.invokeLater(() -> gui.addNewViking(addedViking));
        }

        return addedViking;
    }

    public Viking updateViking(int id, Viking viking) {
        Viking updatedViking = service.updateViking(id, viking);

        if (gui != null) {
            SwingUtilities.invokeLater(() -> gui.updateVikingById(id, updatedViking));
        }

        return updatedViking;
    }

    public Viking deleteViking(int id) {
        Viking deletedViking = service.deleteById(id);

        if (gui != null) {
            SwingUtilities.invokeLater(() -> gui.deleteVikingById(id));
        }

        return deletedViking;
    }
}
