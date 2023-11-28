package com.avereon.acorn;

import com.avereon.product.Rb;
import com.avereon.xenon.RbKey;
import com.avereon.xenon.Xenon;
import com.avereon.xenon.XenonProgramProduct;
import com.avereon.xenon.asset.Asset;
import com.avereon.xenon.asset.AssetType;
import com.avereon.xenon.asset.Codec;
import com.avereon.xenon.asset.PlaceholderCodec;

public class AcornAssetType extends AssetType {

	private static final String URI_PATTERN = "acorn:tester";

	public static final java.net.URI URI = java.net.URI.create( URI_PATTERN );

	public AcornAssetType( XenonProgramProduct product ) {
		super( product, "acorn" );

		PlaceholderCodec codec = new PlaceholderCodec();
		codec.addSupported( Codec.Pattern.URI, URI_PATTERN );
		setDefaultCodec( codec );
	}

	@Override
	public String getKey() {
		return URI_PATTERN;
	}

	@Override
	public boolean assetOpen( Xenon program, Asset asset ) {
		asset.setUri( URI );
		asset.setName( Rb.text( RbKey.ASSET, "acorn-name") );

		// Setting the scheme when the asset is opened solves a bunch of "new" asset problems
		asset.setScheme( program.getAssetManager().getScheme( URI.getScheme() ) );

		asset.setModified( false );
		return true;
	}

}
