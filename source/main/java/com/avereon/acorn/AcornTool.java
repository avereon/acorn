package com.avereon.acorn;

import com.avereon.product.Rb;
import com.avereon.xenon.ProgramProduct;
import com.avereon.xenon.ProgramTool;
import com.avereon.xenon.RbKey;
import com.avereon.xenon.asset.Asset;
import com.avereon.xenon.asset.OpenAssetRequest;
import com.avereon.xenon.task.TaskEvent;
import com.avereon.xenon.workpane.ToolException;
import com.avereon.zarra.javafx.Fx;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.CustomLog;

import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@CustomLog
public class AcornTool extends ProgramTool {

	private static final Timer timer = new Timer( true );

	private final Consumer<Double> cpuLoadListener;

	private final Label result;

	private final ProgressIndicator progress;

	private SystemCpuLoadCheck cpuLoadCheck;

	private final Button button;

	private final Label message;

	private final ScoreGraph scoreGraph;

	private AcornTask checker;

	private AcornScore allThreadScore;

	private AcornScore oneThreadScore;

	public AcornTool( ProgramProduct product, Asset asset ) {
		super( product, asset );
		addStylesheet( AcornMod.STYLESHEET );
		getStyleClass().addAll( "acorn-tool" );
		setIcon( "acorn" );

		String waitingText = Rb.text( "message", "waiting-to-start" );

		cpuLoadListener = d -> log.atFine().log( "cpu=%s", d );

		Label icon = new Label( "One Thread", getProgram().getIconLibrary().getIcon( "acorn", 32 ) );
		icon.getStyleClass().addAll( "icon" );
		result = new Label( "----" );
		result.getStyleClass().addAll( "result" );
		button = new Button();
		//button.getStyleClass().addAll( "button" );
		progress = new ProgressBar( 0 );
		progress.getStyleClass().addAll( "progress" );
		message = new Label( waitingText );
		message.getStyleClass().addAll( "message" );

		button.setOnAction( e -> toggle() );
		updateButtonState();

		scoreGraph = new ScoreGraph();

		HBox test = new HBox( icon, button, progress, result );
		test.getStyleClass().addAll( "layout" );

		VBox box = new VBox( test );
		box.getStyleClass().addAll( "layout" );
		HBox.setHgrow( box, Priority.ALWAYS );

		HBox parts = new HBox( box, scoreGraph );
		parts.getStyleClass().addAll( "layout" );

		getChildren().add( parts );
	}

	@Override
	protected void allocate() throws ToolException {
		cpuLoadCheck = new SystemCpuLoadCheck();
		cpuLoadCheck.addListener( cpuLoadListener );
		timer.schedule( cpuLoadCheck, 0, 1000 );
	}

	@Override
	protected void ready( OpenAssetRequest request ) throws ToolException {
		super.ready( request );
		setTitle( request.getAsset().getName() );
	}

	@Override
	protected void deallocate() throws ToolException {
		cpuLoadCheck.cancel();
		cpuLoadCheck.removeListener( cpuLoadListener );
	}

	private void setScore( long score ) {
		Fx.run( () -> {
			scoreGraph.removeScore( oneThreadScore );
			result.setText( String.valueOf( score ) );
			scoreGraph.addScore( oneThreadScore = new AcornScore( false, score, "Your Computer One Thread" ) );
		} );
	}

	private boolean isRunning() {
		return !(checker == null || checker.isDone());
	}

	private void toggle() {
		if( isRunning() ) {
			checker.cancel( true );
		} else {
			start();
		}
	}

	private void updateButtonState() {
		String startText = Rb.text( RbKey.LABEL, "start" );
		String cancelText = Rb.text( RbKey.LABEL, "cancel" );
		Node pauseIcon = getProgram().getIconLibrary().getIcon( "pause" );
		Node playIcon = getProgram().getIconLibrary().getIcon( "play" );
		Fx.run( () -> {
			button.setGraphic( isRunning() ? pauseIcon : playIcon );
			//button.setText( isRunning() ? cancelText : startText );
		} );
	}

	private void start() {
		checker = new AcornTask();
		checker.register( TaskEvent.SUBMITTED, e -> {
			Fx.run( () -> progress.setProgress( 0 ) );
			updateButtonState();
		} );
		checker.register( TaskEvent.PROGRESS, e -> Fx.run( () -> progress.setProgress( e.getTask().getPercent() ) ) );
		checker.register( TaskEvent.SUCCESS, e -> Fx.run( () -> {
			try {
				setScore( checker.get() );
			} catch( InterruptedException | ExecutionException exception ) {
				log.atWarning().withCause( exception ).log( "Error computing acorn count" );
			}
		} ) );
		checker.register( TaskEvent.CANCEL, e -> Fx.run( () -> progress.setProgress( 0 ) ) );
		checker.register( TaskEvent.FINISH, e -> updateButtonState() );

		getProgram().getTaskManager().submit( checker );
	}

}
