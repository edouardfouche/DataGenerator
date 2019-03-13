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
package io.github.edouardfouche.generators

object GeneratorFactory {
  val correspondances: Map[String, String] = Map(
    "cross" -> "cr",
    "cubic" -> "cu",
    "doublelinear" -> "dl",
    "hourglass" -> "ho",
    "hypercube" -> "hc",
    "hypercubegraph" -> "hcg",
    "hypersphere" -> "hs",
    "independent" -> "i",
    "linear" -> "l",
    "linearperiodic" -> "lp",
    "linearstairs" -> "lsa",
    "linearsteps" -> "lse",
    "linearthendummy" -> "ltd",
    "linearthennoise" -> "ltn",
    "noncoexistence" -> "nc",
    "parabola" -> "p",
    "randomsteps" -> "rs",
    "root" -> "ro",
    "sine" -> "si",
    "star" -> "st",
    "straightlines" -> "sl",
    "z" -> "z",
    "zinv" -> "zi"
  )

  def get(name: String, nDim: Int, noise: Double, noisetype: String, discretize: Int, param: Option[Double] = None): DataGenerator = {
    correspondances.getOrElse(name.toLowerCase, name.toLowerCase) match {
      case "cr"  => Cross(nDim, noise, noisetype, discretize)
      case "cu"  => Cubic(nDim, noise, noisetype, discretize)(param)
      case "dl"  => DoubleLinear(nDim, noise, noisetype, discretize)(param)
      case "ho"  => Hourglass(nDim, noise, noisetype, discretize)
      case "hc"  => Hypercube(nDim, noise, noisetype, discretize)
      case "hcg" => HypercubeGraph(nDim, noise, noisetype, discretize)
      case "hs"  => HyperSphere(nDim, noise, noisetype, discretize)
      case "i"   => Independent(nDim, noise, noisetype, discretize)
      case "l"   => Linear(nDim, noise, noisetype, discretize)
      case "lp"  => LinearPeriodic(nDim, noise, noisetype, discretize)(param)
      case "lsa" => LinearStairs(nDim, noise, noisetype, discretize)(param)
      case "lse" => LinearSteps(nDim, noise, noisetype, discretize)(param)
      case "ltd" => LinearThenDummy(nDim, noise, noisetype, discretize)
      case "ltn" => LinearThenNoise(nDim, noise, noisetype, discretize)
      case "nc"  => NonCoexistence(nDim, noise, noisetype, discretize)
      case "p"   => Parabola(nDim, noise, noisetype, discretize)(param)
      case "rs"  => RandomSteps(nDim, noise, noisetype, discretize)(param)
      case "ro"  => Root(nDim, noise, noisetype, discretize)(param)
      case "si"  => Sine(nDim, noise, noisetype, discretize)(param)
      case "st"  => Star(nDim, noise, noisetype, discretize)
      case "sl"  => StraightLines(nDim, noise, noisetype, discretize)
      case "z"   => Z(nDim, noise, noisetype, discretize)
      case "zi"  => Zinv(nDim, noise, noisetype, discretize)
      case _ => throw new Error(s"Unknown generator name $name. " +
        s"FYI, valid (names,shortnames): ${correspondances.map({case (k,v) => s"($k,$v)"}) mkString ";"} ")
    }
 }
}