package ru.spb.sergeyz.projection

import cern.colt.matrix.DoubleMatrix2D
import cern.colt.matrix.impl.DenseDoubleMatrix2D
import cern.colt.matrix.linalg.Algebra
import cern.colt.matrix.linalg.SingularValueDecomposition
import kotlin.math.abs

fun main(args: Array<String>) {
    val v3d = DenseDoubleMatrix2D(CalibratedPosition3.getV3d())
    val v2d = DenseDoubleMatrix2D(CalibratedPosition3.getV2d())
    val transMat = v2d.mult(v3d.pseudoInverse())
    val reverseProjectionMat = transMat.pseudoInverse()
    val v3dReconstructed = reverseProjectionMat.mult(v2d)

    val v2dTesting = DenseDoubleMatrix2D(CalibratedPosition3.getV2dTesting())
    val v3dReconstructedTesting = reverseProjectionMat.mult(v2dTesting)

    println("V2d")
    println(v2d)
    println("V3d")
    println(v3d)
    println("V3d reconstructed")
    println(v3dReconstructed)

    println("V2d testing")
    println(v2dTesting)
    println("V3d reconstructed testing")
    println(v3dReconstructedTesting)

}

fun DoubleMatrix2D.pseudoInverse(): DoubleMatrix2D {
    val svd = SingularValueDecomposition(this)
    val u = svd.u
    val s = svd.s
    val v = svd.v
    return v.mult(s.diagInvInplace()).mult(u.transpose())
}

fun DoubleMatrix2D.transpose(): DoubleMatrix2D {
    return Algebra().transpose(this)
}

fun DoubleMatrix2D.diagInvInplace(): DoubleMatrix2D {
    for (i in 0..rows() - 1) {
        if (abs(get(i, i)) > 0.00000001) {
            set(i,i,1.0/get(i,i))
        }
    }
    return this
}

fun DoubleMatrix2D.mult(other: DoubleMatrix2D): DenseDoubleMatrix2D {
    val resMat = DenseDoubleMatrix2D(this.rows(), other.columns())
    this.zMult(other, resMat)
    return resMat
}