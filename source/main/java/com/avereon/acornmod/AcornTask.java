package com.avereon.acornmod;

import com.avereon.acorn.AcornChecker;
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
