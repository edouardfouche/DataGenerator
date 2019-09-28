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

import breeze.stats.distributions.Uniform

case class LinearCat(nDim: Int, noise: Double, noisetype: String, discretize: Int) extends DataGenerator {
  val name = "linearcat"
  override lazy val shortname = "lc"

  def getPoints(n: Int): Array[Array[Double]] = {
    (1 to n).toArray.map { _ =>
      val x = Uniform(0, 1).draw()
      (1 to nDim).toArray.map(_ => x)
    }
  }

  override def generate(n: Int): Array[Array[Double]] = {
    Categorizer.categorize(postprocess(getPoints(n: Int), noise, noisetype), discretize)
  }
}
