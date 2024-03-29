package com.avereon.acorn;

import com.avereon.xenon.Xenon;
import com.avereon.xenon.asset.Asset;
import com.avereon.xenon.scheme.BaseScheme;

public class AcornScheme extends BaseScheme {

	public static final String ID = "acorn";

	public AcornScheme( Xenon program ) {
		super( program, ID );
	}

	@Override
	public boolean exists( Asset asset ) {
		String ssp = asset.getUri().getSchemeSpecificPart();
		return ssp.equals( "tester" ) || ssp.equals( "acorn" );
	}

	@Override
	public boolean canLoad( Asset asset ) {
		return true;
	}

}
