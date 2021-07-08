package com.avereon.acorn;

import com.avereon.acorncli.AcornChecker;
import com.avereon.xenon.task.Task;

public class AcornTask extends Task<Long> {

	private final AcornChecker checker;

	public AcornTask() {
		checker = new AcornChecker();
		setTotal( checker.getStepCount() );
		checker.addListener( this::setProgress );
	}

	@Override
	public Long call() throws Exception {
		return checker.call();
	}

}
