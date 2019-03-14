/*
 * Copyright (C) 2018 Edouard Fouché
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/**
  * Created by fouchee on 01.06.17.
  */

import io.github.edouardfouche.generators.{DataGenerator, GeneratorFactory}
import com.typesafe.scalalogging.LazyLogging

// Some examples:
// java -jar target/scala-2.12/DataGenerator.jar -g l -n 1000 -d 2 -disc 0 -noise 0.1 -ntype gaussian -a 'write' --verbose
// java -jar target/scala-2.12/DataGenerator.jar -g l -n 1000 -d 2 -disc 0 -noise 0.1 -ntype gaussian -a 'plot' --verbose
// java -jar target/scala-2.12/DataGenerator.jar -g l -n 1000 -d 2 -disc 5 -noise 0.1 -ntype gaussian -a 'plot' --verbose
object Main extends LazyLogging {

  import org.apache.log4j.PropertyConfigurator

  val log4jConfPath = "src/main/resources/log4j.properties"
  PropertyConfigurator.configure(log4jConfPath)

  def main(args: Array[String]): Unit = {
    val vindex = (args indexWhere (_ == "--verbose")) + 1
    val verbose = if(vindex == 0) false else true

    val currentpath = System.getProperty("user.dir")
    if(verbose) info("Working directory: " + currentpath)
    if(verbose) info("Raw parameters given: " + args.map(s => "\"" + s + "\"").mkString("[", ", ", "]"))

    def parseArgument[T](name: String, default: T, convert: String => T, warnmessage: String): T = {
      val index = (args indexWhere (_ == name)) + 1
      if(index == 0 | (index == args.length)) {
        if(verbose)  warn(warnmessage)
        default
      } else {
        convert(args(index))
      }
    }

    val g = parseArgument("-g", "undefined", (_:String).toString, "Generator unspecified, performing operation for all generators")

    val n = parseArgument("-n", 1000, (_:String).toInt, "Number of observation n unspecified, using default 1000")
    val d = parseArgument("-d", 2, (_:String).toInt, "Number of dimensions d unspecified, using default 2")

    val noise = parseArgument("-noise", 0.0, (_:String).toDouble, "Noise level unspecified, using default 0")
    val ntype = parseArgument("-ntype", "gaussian", (_:String).toString, "Noise type unspecified, using default 'gaussian'")

    val disc = parseArgument("-disc", 0, (_:String).toInt, "Discretization level unspecified, using default 0 (no discretization)")

    // Not sure if that would be any interesting. Eveything is already normalized.
    // val norm = parseArgument("-norm", true, (_:String).toBoolean, "Normalization unspecified, using default true")

    val s = parseArgument("-s", None, (_:String).toDouble, "No generator-specific parameter specified, using default of the generator, if any")
    val special = s match {
      case None => None
      case _ => Some(s.asInstanceOf[Double])
    }

    val action = parseArgument("-a", "write", (_:String).toString, "Default action unspecified, default 'write'. Possible actions: 'write', 'plot', 'both'")

    val path = parseArgument("-p", currentpath, (_:String).toString, s"Output path unspecified, default current repository $currentpath")
    val filename = parseArgument("-o", "undefined", (_:String).toString, s"Output file name unspecified, default is the generator.id")

    def performaction(generator: DataGenerator, action: String): Unit = {
      if(action == "write") {
        if(verbose) info(s"Writing $n points of ${generator.id} at $path")
        if(filename == "undefined") generator.save(n, path) else generator.save(n, path, filename)
      }
      else if(action == "plot") {
        if(verbose) info(s"Ploting $n points of ${generator.id} at $path")
        if(filename == "undefined") generator.plot(n, path) else generator.plot(n, path, filename)
      }
      else if(action == "both") {
        if(verbose) info(s"Writing $n points of ${generator.id} at $path")
        if(filename == "undefined") generator.save(n, path) else generator.save(n, path, filename)
        if(verbose) info(s"Ploting $n points of ${generator.id} at $path")
        if(filename == "undefined") generator.plot(n, path) else generator.plot(n, path, filename)
      } else throw new Error(s"Action $action is unknown")
    }

    if(g == "undefined") {
      for{
        ge <- GeneratorFactory.correspondances.keys
      } {
        val generator = GeneratorFactory.get(ge, d, noise, ntype, disc, special)
        performaction(generator, action)
      }
    } else {
      val generator = GeneratorFactory.get(g, d, noise, ntype, disc, special)
      performaction(generator, action)
    }

    System.exit(0)
  }

  def info(s: String): Unit = logger.info(s)

  def warn(s: String): Unit = logger.warn(s)
}
