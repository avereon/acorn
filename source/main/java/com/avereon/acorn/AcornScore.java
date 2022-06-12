package com.avereon.acorn;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class AcornScore extends Label {

	private final BooleanProperty left;

	private final long score;

	public AcornScore( boolean left, long score ) {
		this( left, score, null );
	}

	public AcornScore( boolean left, long score, String text ) {
		this( left, score, text, null );
	}

	public AcornScore( boolean left, long score, String text, Node graphic ) {
		super( text, graphic );
		this.score = score;
		this.left = new SimpleBooleanProperty( left );
	}

	public BooleanProperty leftProperty() {
		return left;
	}

	public long getScore() {
		return score;
	}

}
