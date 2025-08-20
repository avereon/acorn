package com.avereon.acorn;

import com.avereon.xenon.ProgramTool;
import com.avereon.xenon.XenonProgramProduct;
import com.avereon.xenon.asset.Asset;
import com.avereon.xenon.asset.OpenAssetRequest;
import com.avereon.xenon.workpane.ToolException;
import javafx.scene.layout.HBox;
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

	public AcornTool( XenonProgramProduct product, Asset asset ) {
		super( product, asset );
		addStylesheet( AcornMod.STYLESHEET );
		getStyleClass().addAll( "acorn-tool" );
		setIcon( "acorn" );

		cpuLoadListener = d -> log.atFine().log( "cpu=%s", d );

		int threads = Runtime.getRuntime().availableProcessors();

		AcornTest allThreadsTest = new AcornTest( this, threads + " Threads", threads );
		AcornTest oneThreadTest = new AcornTest( this, "1 Thread", 1 );
		VBox testBox = new VBox( allThreadsTest, oneThreadTest );
		testBox.setPrefWidth( 700 );
		testBox.setMinWidth( 400 );

		scoreGraph = new ScoreGraph();
		scoreGraph.setPrefWidth( 300 );
		scoreGraph.setMinWidth( 300 );

		HBox parts = new HBox( testBox, scoreGraph );
		parts.setFillHeight( true );

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
