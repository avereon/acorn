package com.avereon.acorn;

import com.avereon.acorncli.AcornMonitor;
import com.avereon.acorncli.AcornCounter;
import com.avereon.xenon.task.Task;

public class AcornTask extends Task<Long> {

	//private final AcornChecker checker;

	private final AcornCounter counterOne;

	public AcornTask() {
		counterOne = new AcornMonitor();
		setTotal( counterOne.getTotal() );
		counterOne.addListener( this::setProgress );
	}

	@Override
	public Long call() throws Exception {
		counterOne.start();
		counterOne.join();
		return counterOne.getScore();
	}

}
