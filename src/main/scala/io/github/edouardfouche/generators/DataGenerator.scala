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

import java.io.{BufferedWriter, File, FileWriter}

import breeze.stats.distributions.{Gaussian, Uniform}
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.{ChartFactory, ChartUtilities}
import org.jfree.data.xy.{XYSeries, XYSeriesCollection}
import org.jzy3d.chart.{AWTChart, ChartLauncher}
import org.jzy3d.colors.Color
import org.jzy3d.maths.Coord3d
import org.jzy3d.plot3d.primitives.Scatter
import org.jzy3d.plot3d.rendering.canvas.Quality


trait DataGenerator {
  val nDim: Int
  val noise: Double
  val noisetype: String
  val discretize: Int

  val name: String // A full name of the dependency

  lazy val shortname: String = GeneratorFactory.correspondances.getOrElse(name.toLowerCase, name.toLowerCase) // A short name for the dependency
  lazy val id = s"$name-$shortname-$nDim-$noise-$noisetype-$discretize" // A String that says everything about this dependency

  require(nDim > 0, "Number of dimension required to be strictly greater than 0")
  require(noise >= 0, "Noise required to be greater or equal to 0")
  require(noisetype == "gaussian" | noisetype ==  "uniform", s"Noise type $noisetype is unknown. Currently supported: 'gaussian', 'uniform'")
  require(discretize >= 0, "Discretization level should be greater or equal 0 (0 means no discretization)")

  def getPoints(n: Int): Array[Array[Double]]

  def generate(n: Int): Array[Array[Double]] = {
    Discretizer.discretize(postprocess(getPoints(n: Int), noise, noisetype), discretize)
  }

  def postprocess(data: Array[Array[Double]], noise: Double, t: String = "gaussian"): Array[Array[Double]] = {
    t match {
      case "gaussian" => data.map(y => y.map(x => addGaussianNoise(x, noise)))
      case "uniform" => data.map(y => y.map(x => addUniformNoise(x, noise)))
    }
  }

  def addGaussianNoise(x: Double, noise: Double): Double = x + Gaussian(0, noise).draw()

  def addUniformNoise(x: Double, noise: Double): Double = x + Uniform(-noise / 2.0, noise / 2.0).draw()

  def linearNormalization(x: Double): Double = (x + noise / 2.0) / (1 + 2 * (noise / 2))

  def noiselessPowerNormalization(x: Double, pow: Double): Double = {
    var t = Array(1.0)
    for (y <- 2 to nDim) {
      t = t ++ Array(math.pow(t.sum, pow))
    }
    val max = t.last
    x / max
  }

  def powerNormalization(x: Double, pow: Double): Double = {
    var t = Array(1.0)
    for (y <- 2 to nDim) {
      t = t ++ Array(math.pow(t.sum, pow))
    }
    val max = t.last
    (x + noise / 2.0) / (max + 2 * (noise / 2))
  }

  def save(n: Int, path: String = System.getProperty("user.dir"), filename: String = this.id): Unit = {
    val data = this.generate(n)

    def saveDataSet[T](res: Array[Array[T]], path: String): Unit = {
      val file = new File(path)
      val bw = new BufferedWriter(new FileWriter(file))
      bw.write(s"${(1 to res(0).length) mkString ","}\n") // a little header
      res.foreach(x => bw.write(s"${x mkString ","}\n"))
      bw.close()
    }

    saveDataSet(data, path + "/" + filename + ".csv")
  }

  def plot(n: Int, path: String = System.getProperty("user.dir") + this.id, filename: String = this.id): Unit = {
    val data = this.generate(n)
    val fullpath = path + "/" + filename + ".png"

    if (nDim == 2) {
      val points2d = new XYSeriesCollection()
      val series = new XYSeries(id)
      data.foreach(x => series.add(x(0), x(1)))
      points2d.addSeries(series)

      val chart2d = ChartFactory.createScatterPlot(
        s"$id-$discretize", // chart title
        "X", // x axis label
        "Y", // y axis label
        points2d, // data
        PlotOrientation.VERTICAL,
        true, // include legend
        true, // tooltips
        false // urls
      )

      ChartUtilities.saveChartAsPNG(new File(fullpath), chart2d, 500, 500)
    } else if (nDim == 3) {
      // maybe some help here https://github.com/jzy3d/jzy3d-api/issues/53
      val points3d = data.map(x => new Coord3d(x(0).toFloat, x(1).toFloat, x(2).toFloat))
      val scatter3d = new Scatter(points3d)
      scatter3d.setWidth(5)
      scatter3d.setColor(Color.RED.alphaSelf(0.3.toFloat))
      val chart = new AWTChart(Quality.Advanced, "offscreen")
      chart.getScene.add(scatter3d)
      ChartLauncher.screenshot(chart, fullpath)
    } else {
      throw new Error(s"Plotting is not supported with more than 3 dimensions. (${this.id})")
    }
  }



}
