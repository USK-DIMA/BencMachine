package com.bench.service.interfaces;

import com.bench.service.entity.WorkPackage;

/**
 * Интерфейс взаимодействия между View и BenchManager
 */
public interface IBench {

    /**
     * Устанавливает обработчик сигнала окончания работы.
     * После окончания работы объект IBench вызовит метод EndWorkListener#end,
     * символезируя этим окончаниЛе работы станка.
     */
    void setEndWorkListener(EndWorkListener endWorkListener);

    /**
     * Передача всей необходимой информации для начала работы о конфигурации бруса и станка.
     * @param workPackage
     */
    void startWork(WorkPackage workPackage);

    /**
     * Останавливает работу станка
     */
    void stopWork();

    /**
     * Меняет режим работы станка с автоматического на ручной и обратно.
     * При ручном режиме работы станка, необходимо давать сигнал для совершения следующего шага.
     * При автоматическом режиме станок совершает все шаги самостоятельно, пока работа не будет завершена или
     * режим не будет сменён на ручной
     * @param isAuto true, если переводим в автоматический режим работы, false, если переводим в ручной режим.
     */
    void changeModeWork(boolean isAuto);

    /**
     * Иничиализирует объект IBench.
     */
    void start();

    /**
     * Выполняет следующий шаг работы станка. Используется при ручном режиме работы
     */
    void step();

    @FunctionalInterface
    interface EndWorkListener {
        void end(String message);
    }
}
