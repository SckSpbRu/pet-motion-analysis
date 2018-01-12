package ru.spb.sergeyz.projection

import org.apache.commons.math3.optim.ConvergenceChecker
import org.apache.commons.math3.optim.InitialGuess
import org.apache.commons.math3.optim.MaxEval
import org.apache.commons.math3.optim.MaxIter
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer
import javax.vecmath.Point3d
import javax.vecmath.Point4d

fun main(args: Array<String>) {

    val point3d_31 = Point3d(42.0, 162.0, 0.0)
    val point3d_32 = Point3d(77.0, 220.0, 0.0)
    val point3d_33 = Point3d(90.0, 314.0, 0.0)
    val point3d_34 = Point3d(160.0, 288.0, 0.0)
    val points3d = listOf(point3d_31, point3d_32, point3d_33, point3d_34)

    val point2d_31 = Point3d(286.0, 80.0, 0.0)
    val point2d_32 = Point3d(244.0, 271.0, 0.0)
    val point2d_33 = Point3d(98.0, 428.0, 0.0)
    val point2d_34 = Point3d(239.0, 555.0, 0.0)
    val points2d = listOf(point2d_31, point2d_32, point2d_33, point2d_34)


    val optimizer = SimplexOptimizer(ConvergenceChecker { iteration, previous, current ->
        point4dFromMat(previous.pointRef)
                .distanceSquared(point4dFromMat(current.pointRef)) < 0.0001
    })

    val obj = ObjectiveFunction({ mat: DoubleArray ->
        val currentTransMatrix = point4dFromMat(mat)
        - points3d.zip(points2d).sumByDouble { (p3d, p2d) ->
            p3d.project(currentTransMatrix)
            p3d.distanceSquared(p2d)
        }
    })

    val init = InitialGuess(doubleArrayOf(0.0, 1.0, 0.0, 1.0))

    val optimize = optimizer.optimize(obj, init,
            NelderMeadSimplex(4),
            MaxIter.unlimited(),
            MaxEval.unlimited())

    val solution = point4dFromMat(optimize.point)
    println(solution)

    val test = Point3d(42.0, 162.0, 0.0)
    println(test)
    test.project(solution)
    println(test)

}

fun point4dFromMat(mat: DoubleArray): Point4d {
    val x: Double = mat[0]
    val y: Double = mat[1]
    val z: Double = mat[2]
    val w: Double = mat[3]
    return Point4d(x, y, z, w)
}