package sample.util;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.concurrent.Task;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Triathlon extends Application {

  private final Random random = new Random();
  private final ExecutorService exec = Executors.newSingleThreadExecutor();

  @Override public void start(Stage stage) throws Exception {
    final TaskMonitor taskMonitor = new TaskMonitor();

    final ProgressIndicator progressIndicator = new ProgressIndicator();
    progressIndicator.progressProperty().bind(
        taskMonitor.currentTaskProgressProperty()
    );

    final Label currentRaceStage = new Label();
    currentRaceStage.textProperty().bind(
        taskMonitor.currentTaskNameProperty()
    );

    createMainLayout(
        stage,
        createStartRaceButton(
            exec,
            taskMonitor
        ),
        createRaceProgressView(
            taskMonitor,
            progressIndicator,
            currentRaceStage
        )
    );
  }

  @Override public void stop() throws Exception {
    exec.shutdownNow();
  }

  private Button createStartRaceButton(final ExecutorService exec, final TaskMonitor taskMonitor) {
    final Button startButton = new Button("Start Race");
    startButton.disableProperty().bind(taskMonitor.idleProperty().not());
    startButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        runRace(exec, taskMonitor);
      }
    });
    return startButton;
  }

  private HBox createRaceProgressView(final TaskMonitor taskMonitor, ProgressIndicator progressIndicator, Label currentRaceStage) {
    final HBox raceProgress = new HBox(10);
    raceProgress.getChildren().setAll(
      currentRaceStage,
      progressIndicator
    );
    raceProgress.setOpacity(0);
    raceProgress.setAlignment(Pos.CENTER);

    final FadeTransition fade = new FadeTransition(Duration.seconds(0.75), raceProgress);
    fade.setToValue(0);

    taskMonitor.idleProperty().addListener(new InvalidationListener() {
      @Override
      public void invalidated(Observable observable) {
        if (taskMonitor.idleProperty().get()) {
          fade.playFromStart();
        } else {
          fade.stop();
          raceProgress.setOpacity(1);
        }
      }
    });

    return raceProgress;
  }

  private void createMainLayout(Stage stage, Button startButton, HBox raceProgress) {
    final VBox layout = new VBox(10);
    layout.getChildren().setAll(
      raceProgress,
      startButton
    );
    layout.setAlignment(Pos.CENTER);
    layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10px;");
    stage.setScene(new Scene(layout, 200, 130));
    stage.show();
  }


  private void runRace(ExecutorService exec, TaskMonitor taskMonitor) {
    StageTask swimTask = new StageTask("Swim", 30,   40);
    StageTask bikeTask = new StageTask("Bike", 210, 230);
    StageTask runTask  = new StageTask("Run",  120, 140);

    taskMonitor.monitor(swimTask, bikeTask, runTask);

    exec.execute(swimTask);
    exec.execute(bikeTask);
    exec.execute(runTask);
  }

  class TaskMonitor {
    final private ReadOnlyObjectWrapper<StageTask> currentTask = new ReadOnlyObjectWrapper<>();
    final private ReadOnlyStringWrapper currentTaskName        = new ReadOnlyStringWrapper();
    final private ReadOnlyDoubleWrapper currentTaskProgress    = new ReadOnlyDoubleWrapper();
    final private ReadOnlyBooleanWrapper idle                  = new ReadOnlyBooleanWrapper(true);

    public void monitor(final StageTask task) {
      task.stateProperty().addListener(new ChangeListener<Task.State>() {
        @Override
        public void changed(ObservableValue<? extends Task.State> observableValue, Task.State oldState, Task.State state) {
          switch (state) {
            case RUNNING:
              currentTask.set(task);
              currentTaskProgress.unbind();
              currentTaskProgress.set(task.progressProperty().get());
              currentTaskProgress.bind(task.progressProperty());
              currentTaskName.set(task.nameProperty().get());
              idle.set(false);
              break;

            case SUCCEEDED:
            case CANCELLED:
            case FAILED:
              task.stateProperty().removeListener(this);
              idle.set(true);
              break;
          }
        }
      });
    }

    public void monitor(final StageTask... tasks) {
      for (StageTask task: tasks) {
        monitor(task);
      }
    }

    public ReadOnlyObjectProperty<StageTask> currentTaskProperty() {
      return currentTask.getReadOnlyProperty();
    }

    public ReadOnlyStringProperty currentTaskNameProperty() {
      return currentTaskName.getReadOnlyProperty();
    }

    public ReadOnlyDoubleProperty currentTaskProgressProperty() {
      return currentTaskProgress.getReadOnlyProperty();
    }

    public ReadOnlyBooleanProperty idleProperty() {
      return idle.getReadOnlyProperty();
    }
  }

  class StageTask extends Task<Duration> {
    final private ReadOnlyStringWrapper name;
    final private int minMinutesElapsed;
    final private int maxMinutesElapsed;

    public StageTask(String name, int minMinutesElapsed, int maxMinutesElapsed) {
      this.name = new ReadOnlyStringWrapper(name);
      this.minMinutesElapsed = minMinutesElapsed;
      this.maxMinutesElapsed = maxMinutesElapsed;
    }

    @Override protected Duration call() throws Exception {
      Duration duration = timeInRange(
        minMinutesElapsed, maxMinutesElapsed
      );

      for (int i = 0; i < 25; i++) {
        updateProgress(i, 25);
        Thread.sleep((int) (duration.toMinutes()));
      }
      updateProgress(25, 25);

      return duration;
    }

    private Duration timeInRange(int min, int max) {
      return Duration.minutes(
        random.nextDouble() * (max - min) + min
      );
    }

    public ReadOnlyStringProperty nameProperty() {
      return name.getReadOnlyProperty();
    }
  }

  public static void main(String[] args) {
    Application.launch(Triathlon.class);
  }
}
