import com.avereon.acorn.AcornMod;

module com.avereon.acorn {

	// Compile-time only
	requires static lombok;

	// Both compile-time and run-time
	requires com.avereon.acorncli;
	requires com.avereon.xenon;
	requires java.management;

	opens com.avereon.acorn.bundles;
	opens com.avereon.acorn.settings;

	exports com.avereon.acorn to com.avereon.xenon, com.avereon.zarra;

	provides com.avereon.xenon.Mod with AcornMod;

}
