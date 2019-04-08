
/**
  * Import Generator classes
  */

import io.github.edouardfouche.generators._

/**
  * Create Generator instance. See Readme for all generator classes.
  *
  * @nDim:Int No. of dimensions
  * @noise:Double Noise level (see also Readme "Adding noise")
  * @noisetype:String "gaussian" or "uniform"
  * @discretize:Int If values should be discretized (>0) or not (=0). Value signifies on how many distinct points discretization
  *                 takes place (e.g. 1 means all dataobjects get discretized on one point). (see also Readme "Discretization")
  *
  *
  * Special Parameter: Certain dependencies take optional dependency specific parameters such as Sine (No of periods),
  * passed as an Option[Double]. If none is given it defaults.
  * The Readme contains information on special parameters for a given dependency.
  */

val sine_generator = Sine(nDim = 2, noise = 0.01, noisetype = "gaussian", discretize = 0)(period = Some(3))

/**
  * Generate n Dataobjects with generator
  */

val sine: Array[Array[Double]] = sine_generator.generate(n = 100000) // Dim: 100000 x 5


/**
  * Save n (required) Dataobjects to csv at path as filename. path & filename default to s"${System.getProperty("user.dir")}/"
  * & independent_generator.id respectively.
  */

val path:String  = s"${System.getProperty("user.dir")}/"
val filename:String = sine_generator.id

sine_generator.save(n = 100, path = path, filename = filename)


/**
  * Save a plot of n (required) Dataobjects to png at path as filename. path & filename default to s"${System.getProperty("user.dir")}/"
  * & independent_generator.id respectively.
  *
  * Plotting is only supported for 2 to 3 dimensions
  */

sine_generator.plot(n = 10000, path = path, filename = filename)
