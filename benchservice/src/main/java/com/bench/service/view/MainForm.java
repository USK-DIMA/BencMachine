package com.bench.service.view;

import com.bench.service.BenchManager;
import com.bench.service.interfaces.IBench;
import com.bench.service.entity.WorkPackage;
import javafx.geometry.Point3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Created by Dmitry on 08.04.2017.
 */
public class MainForm {
    private static final Logger logger = LoggerFactory.getLogger(MainForm.class);

    private JPanel viewPanel;
    private JPanel controlPanel;
    private JPanel mainPanel;
    private JButton runOrStopButton;
    private JCheckBox autoChechbox;
    private JButton stepButton;
    private JButton changeAutoStateButton;
    private JSpinner chamferXSpiner;
    private JSpinner weedXSpiner;
    private JSpinner weedYSpiner;
    private JSpinner weedZSpiner;
    private JSpinner chamferYSpiner;
    private JSpinner chamferZSpiner;
    private JSpinner timePauseSpiner;
    private JPanel infoLabelPanel;
    private JLabel infoLabel;
    private JPanel weedPanel;
    private JPanel chamferPanel;
    private JPanel machinePanel;
    private JFrame frame;
    private IBench benchManager;

    private boolean work = false; //запущен ли станок
    private boolean auto = false; //режим работы

    public MainForm(String frameName) {
        initFrame(frameName);
        initListeners();
        initSpinners();
        benchManager = new BenchManager(viewPanel);
        benchManager.setEndWorkListener(message -> {
            stopWork();
            info("Станок завершил работу");
        });

    }

    private void initSpinners() {
        //каждому свою модель
        weedXSpiner.setModel(new SpinnerNumberModel(150, 15, 1000, 1));
        weedYSpiner.setModel(new SpinnerNumberModel(150, 15, 1000, 1));
        weedZSpiner.setModel(new SpinnerNumberModel(15, 15, 1000, 1));

        chamferXSpiner.setModel(new SpinnerNumberModel(0, 0, 1000, 1));
        chamferYSpiner.setModel(new SpinnerNumberModel(10, 10, 1000, 1));
        chamferZSpiner.setModel(new SpinnerNumberModel(10, 1, 1000, 1));

        timePauseSpiner.setModel(new SpinnerNumberModel(1, 1, 3000, 1));
    }

    private void initFrame(String frameName) {
        frame = new JFrame(frameName);
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        setButtonsState();
    }

    public void start() {
        frame.setVisible(true);
        benchManager.start();
    }

    private void initListeners() {
        runOrStopButton.addActionListener(e -> {
            if (work) {
                stopWork();
            } else {
                startWork();
            }
        });

        changeAutoStateButton.addActionListener(e -> changeAutoState());

        stepButton.addActionListener(e -> benchManager.step());
    }

    private void changeAutoState() {
        auto = !auto;
        benchManager.changeModeWork(auto);
        setButtonsState();
        info("Изменён режим работы на " + (auto?"автоматический":"ручной"));
    }

    private void stopWork() {
        benchManager.stopWork();
        work = false;
        setButtonsState();
        setEnabledPackageWorkPanel(true);
        info("Работа станка остановлена");
    }

    private void setEnabledPackageWorkPanel(boolean enabled) {
        weedXSpiner.setEnabled(enabled);
        weedYSpiner.setEnabled(enabled);
        weedZSpiner.setEnabled(enabled);
        chamferXSpiner.setEnabled(enabled);
        chamferYSpiner.setEnabled(enabled);
        chamferZSpiner.setEnabled(enabled);
        timePauseSpiner.setEnabled(enabled);
        autoChechbox.setEnabled(enabled);
    }

    private void setButtonsState() {
        if (work) {
            runOrStopButton.setText("Остановить");
        } else {
            runOrStopButton.setText("Начать");
        }

        if (auto) {
            changeAutoStateButton.setText("Ручной режим");
        } else {
            changeAutoStateButton.setText("Автоматический режим");
        }

        stepButton.setEnabled(!auto && work);
        changeAutoStateButton.setEnabled(work);

    }


    private void startWork() {
        WorkPackage workPackage = collectWorkPackage();
        benchManager.startWork(workPackage);
        work = true;
        auto = workPackage.isAuto();
        setButtonsState();
        setEnabledPackageWorkPanel(false);
        info("Начало работы станка в " + (auto?"автоматическом":"ручном") + " режиме");
    }

    private WorkPackage collectWorkPackage() {
        WorkPackage workPackage = new WorkPackage();
        Integer wX = (Integer) weedXSpiner.getValue();
        Integer wY = (Integer) weedYSpiner.getValue();
        Integer wZ = (Integer) weedZSpiner.getValue();

        Integer cX = (Integer) chamferXSpiner.getValue();
        Integer cY = (Integer) chamferYSpiner.getValue();
        Integer cZ = (Integer) chamferZSpiner.getValue();

        Boolean auto = autoChechbox.isSelected();

        Integer timePause = (Integer) timePauseSpiner.getValue();
        workPackage.setChamferInfo(new Point3D(cX, cY, cZ));
        workPackage.setWoodSize(new Point3D(wX, wY, wZ));
        workPackage.setTimePause(timePause);
        workPackage.setAuto(auto);
        return workPackage;
    }

    private void info(String message) {
        infoLabel.setText(message);
    }
}