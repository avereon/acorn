package com.avereon.acorn;

import com.avereon.acorncli.AcornMonitor;
import com.avereon.acorncli.AcornCounter;
import com.avereon.xenon.task.Task;

public class AcornTask extends Task<Long> {

	private final AcornCounter counter;

	public AcornTask( int threads ) {
		counter = new AcornMonitor( threads );
		setTotal( counter.getTotal() );
		counter.addListener( this::setProgress );
	}

	@Override
	public Long call() throws Exception {
		counter.start();
		counter.join();
		return counter.getScore();
	}

}
