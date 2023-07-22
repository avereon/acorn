package com.avereon.acorn;

import com.avereon.product.Rb;
import com.avereon.xenon.RbKey;
import com.avereon.xenon.Xenon;
import com.avereon.xenon.XenonProgramProduct;
import com.avereon.xenon.asset.*;
import com.avereon.xenon.asset.exception.AssetException;

public class AcornAssetType extends AssetType {

	private static final String uriPattern = "acorn:acorn";

	public static final java.net.URI URI = java.net.URI.create( uriPattern );

	public AcornAssetType( XenonProgramProduct product ) {
		super( product, "acorn" );

		PlaceholderCodec codec = new PlaceholderCodec();
		codec.addSupported( Codec.Pattern.URI, uriPattern );
		setDefaultCodec( codec );
	}

	@Override
	public String getKey() {
		return uriPattern;
	}

	@Override
	public boolean assetOpen( Xenon program, Asset asset ) throws AssetException {
		asset.setUri( URI );
		asset.setName( Rb.text( RbKey.ASSET, "acorn-name") );
		asset.setModified( false );
		return true;
	}

}
