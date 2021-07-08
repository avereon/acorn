import com.avereon.acornmod.AcornMod;

module com.avereon.acornmod {

	// Compile-time only
	requires static lombok;

	// Both compile-time and run-time
	requires com.avereon.acorn;
	requires com.avereon.xenon;
	requires java.management;

	opens com.avereon.acornmod.bundles;
	opens com.avereon.acornmod.settings;

	exports com.avereon.acornmod to com.avereon.xenon, com.avereon.zerra;

	provides com.avereon.xenon.Mod with AcornMod;

}
