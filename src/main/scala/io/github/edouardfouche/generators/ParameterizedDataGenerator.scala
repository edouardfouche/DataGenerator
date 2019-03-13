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

trait ParameterizedDataGenerator extends DataGenerator {
  val nDim: Int
  val noise: Double
  val noisetype: String
  val discretize: Int
  val param: Double // I don't like because actually some take an int, but yeah...

  val name: String // A full name of the dependency

  override lazy val id: String = s"$name-${shortname}_$param-$nDim-$noise-$noisetype-$discretize" // A String that says everything about this dependency

  def getPoints(n: Int): Array[Array[Double]]
}
