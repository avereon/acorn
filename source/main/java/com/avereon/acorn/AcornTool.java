package com.avereon.acorn;

import com.avereon.xenon.ProgramProduct;
import com.avereon.xenon.ProgramTool;
import com.avereon.xenon.asset.Asset;
import com.avereon.xenon.asset.OpenAssetRequest;
import com.avereon.xenon.workpane.ToolException;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.CustomLog;

import java.util.Timer;
import java.util.function.Consumer;

@CustomLog
public class AcornTool extends ProgramTool {

	private static final Timer timer = new Timer( true );

	private final Consumer<Double> cpuLoadListener;

	private SystemCpuLoadCheck cpuLoadCheck;

	private final ScoreGraph scoreGraph;

	public AcornTool( ProgramProduct product, Asset asset ) {
		super( product, asset );
		addStylesheet( AcornMod.STYLESHEET );
		getStyleClass().addAll( "acorn-tool" );
		setIcon( "acorn" );

		cpuLoadListener = d -> log.atFine().log( "cpu=%s", d );

		scoreGraph = new ScoreGraph();

		int threads = Runtime.getRuntime().availableProcessors();

		AcornTest oneThreadTest = new AcornTest( this, "One Thread", 1 );
		AcornTest allThreadsTest = new AcornTest( this, "All Threads", threads );
		VBox box = new VBox( oneThreadTest, allThreadsTest );
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

	ScoreGraph getScoreGraph() {
		return scoreGraph;
	}

}
