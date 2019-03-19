package test.elevator.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import test.elevator.config.ConfStore;
import test.elevator.config.PropertiesConfiguration;
import test.elevator.core.cabin.Cabin;
import test.elevator.core.cabin.CabinFactory;
import test.elevator.core.callmanagement.StrategyFactory;
import test.elevator.core.callmanagement.Transporter;
import test.elevator.gui.*;
import test.elevator.impl.updown.UpDownOnlyStrategyFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FxApp extends Application {
    private static final String PASSENGER_ELEVATOR = "passenger";
    private static final String CARGO_ELEVATOR = "cargo";
    private static final ApplicationContext CONTEXT = new ApplicationContext();
    private static final List<Runnable> tasks = new ArrayList<>();
    private static final Map<String, FxElevatorLoader> loaders = new HashMap<>();
    private static final Map<String, FxElevator> fxElevators = new HashMap<>();
    private ExecutorService service;


    public static void main(String[] args) {
        ResourceBundle configurationBundle = ResourceBundle.getBundle("config", Locale.ENGLISH);
        ConfStore.setConfiguration(new PropertiesConfiguration(configurationBundle));

        int defaultPosition = ConfStore.getConf().getIntParam(ConfStore.DEFAULT_POSITION);
        final int bottomFloor = ConfStore.getConf().getIntParam(ConfStore.BOTTOM_FLOOR);
        final int topFloor = ConfStore.getConf().getIntParam(ConfStore.TOP_FLOOR);

        StrategyFactory strategyFactory = new UpDownOnlyStrategyFactory();

        Cabin liteCabin = CabinFactory.createCabin(CabinFactory.CabinType.LITE);
        Transporter liteTransporter = new Transporter(defaultPosition);
        CONTEXT.creteElevator(PASSENGER_ELEVATOR, liteCabin, liteTransporter, strategyFactory);

        loaders.put(PASSENGER_ELEVATOR, () -> {
            final FacadeController controller = new FacadeController(CONTEXT.getFacadeOf(PASSENGER_ELEVATOR));
            return FxElevatorBuilder.createElevator(bottomFloor, topFloor, controller);
        });
        tasks.add(CONTEXT.getTaskOf(PASSENGER_ELEVATOR));


        Cabin heavyCabin = CabinFactory.createCabin(CabinFactory.CabinType.HEAVY);
        Transporter heavyTransporter = new Transporter(defaultPosition);
        CONTEXT.creteElevator(CARGO_ELEVATOR, heavyCabin, heavyTransporter, strategyFactory);

        loaders.put(CARGO_ELEVATOR, () -> {
            final FacadeController controller = new FacadeController(CONTEXT.getFacadeOf(CARGO_ELEVATOR));
            return FxElevatorBuilder.createElevator(bottomFloor, topFloor, controller);
        });
        tasks.add(CONTEXT.getTaskOf(CARGO_ELEVATOR));

        tasks.add(new FxPollingTask());
        FxApp.launch();
    }

    @Override
    public void start(Stage primaryStage) {
        final HBox root = new HBox();
        primaryStage.setTitle("Elevator. test example");
        loaders.forEach((name, loader) -> fxElevators.put(name, loader.loadElevator()));
        fxElevators.forEach((name, elevator) -> root.getChildren().add(elevator.mainPane));

        primaryStage.setScene(new Scene(root, 300, 400));
        primaryStage.show();

        service = Executors.newFixedThreadPool(tasks.size());
        tasks.forEach(service::submit);
    }

    @Override
    public void stop() throws Exception {
        service.shutdownNow();
        super.stop();
    }

    private static class FxPollingTask implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    fxElevators.forEach((name, fxElevator) -> {
                        int position = CONTEXT.getFacadeOf(name).getPosition();
                        Platform.runLater(() -> fxElevator.changePosition(position));
                    });
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
