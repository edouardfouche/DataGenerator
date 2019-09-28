import io.github.edouardfouche.generators.Independent

val d = Independent(3, 0, "gaussian", 10).generate(100000).transpose

d(0).groupBy(identity).mapValues(_.length)

val dd = Independent(3, 0, "gaussian", 3).generate(100000).transpose

dd(0).groupBy(identity).mapValues(_.length)

val ddd = Independent(3, 0, "gaussian", 2).generate(100000).transpose

ddd(0).groupBy(identity).mapValues(_.length)

val dddd = Independent(3, 0, "gaussian", 1).generate(100000).transpose

dddd(0).groupBy(identity).mapValues(_.length)