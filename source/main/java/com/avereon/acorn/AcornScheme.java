package com.avereon.acorn;

import com.avereon.xenon.Xenon;
import com.avereon.xenon.asset.Resource;
import com.avereon.xenon.scheme.ProductScheme;

public class AcornScheme extends ProductScheme {

	public static final String ID = "acorn";

	public AcornScheme( Xenon program ) {
		super( program, ID );
	}

	@Override
	public boolean exists( Resource resource ) {
		String ssp = resource.getUri().getSchemeSpecificPart();
		return ssp.equals( "tester" ) || ssp.equals( "acorn" );
	}

	@Override
	public boolean canLoad( Resource resource ) {
		return true;
	}

}
