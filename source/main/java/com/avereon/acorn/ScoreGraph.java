package com.avereon.acorn;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.HashSet;
import java.util.Set;

public class ScoreGraph extends Pane {

	private static final long TOP_LIMIT = 10000;

	private final ObservableSet<AcornScore> scores;

	private final Set<Node> scoreNodes;

	private final Line divider;

	public ScoreGraph() {
		scoreNodes = new HashSet<>();

		scores = FXCollections.observableSet( new HashSet<>() );
		scores.addListener( this::onChanged );

		divider = new Line();
		divider.getStyleClass().add( "divider" );

		AcornScore top = addScore( new AcornScore( true, TOP_LIMIT, String.valueOf( TOP_LIMIT ) ) );
		AcornScore base = addScore( new AcornScore( true, 0, "0" ) );

		int exp = 1;
		long value = (long)Math.pow(10, exp);
		while( value < TOP_LIMIT ) {
			addScore( new AcornScore( true, value, String.valueOf( value ) ) );
			value = (long)Math.pow(10, ++exp);
		}

		getStyleClass().add( "score-graph" );

		divider.startXProperty().bind( this.widthProperty().multiply( 0.5 ) );
		divider.endXProperty().bind( this.widthProperty().multiply( 0.5 ) );
		divider.startYProperty().bind( this.layoutYProperty().add( Bindings.selectDouble( this.paddingProperty(), "top" ) ).add( Bindings.multiply( 0.5, top.heightProperty() ) ) );
		divider.endYProperty().bind( this.heightProperty().subtract( Bindings.selectDouble( this.paddingProperty(), "bottom" ) ).subtract( Bindings.multiply( 0.5, base.heightProperty() ) ) );

		getChildren().addAll( divider );

		addScore( new AcornScore( false, 270, "16-Core AMD Ryzen 9 5950X One Thread" ) );
		addScore( new AcornScore( false, 20, "4-Core Raspberry PI 3 All Threads" ) );
		addScore( new AcornScore( false, 6, "4-Core Raspberry PI 3 One Thread" ) );
	}

	ObservableSet<AcornScore> scoresProperty() {
		return scores;
	}

	public AcornScore addScore( AcornScore score ) {
		if( score == null ) return null;
		scoresProperty().add( score );
		return score;
	}

	public AcornScore removeScore( AcornScore score ) {
		if( score == null ) return null;
		scoresProperty().remove( score );
		return score;
	}

	private void onChanged( SetChangeListener.Change<? extends AcornScore> change ) {
		// Remove all old ones
		getChildren().removeAll( scoreNodes );
		scoreNodes.clear();

		change.getSet().forEach( n -> {
			n
				.layoutXProperty()
				.bind( Bindings
					.when( n.leftProperty() )
					.then( this.widthProperty().multiply( 0.5 ).subtract( n.widthProperty() ).subtract( n.getGraphicTextGap() ) )
					.otherwise( this.widthProperty().multiply( 0.5 ).add( n.getGraphicTextGap() ) ) );
			n
				.layoutYProperty()
				.bind( divider
					.endYProperty()
					.subtract( Bindings.multiply( logScore( n.getScore() ), Bindings.subtract( divider.endYProperty(), divider.startYProperty() ) ) )
					.subtract( Bindings.multiply( 0.5, n.heightProperty() ) ) );
		} );

		// Add new ones
		scoreNodes.addAll( change.getSet() );
		getChildren().addAll( scoreNodes );
	}

	private double logScore( long score ) {
		return Math.log( 1 + score ) / Math.log( TOP_LIMIT );
	}

}
