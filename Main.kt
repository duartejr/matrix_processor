package processor

import java.util.*
import kotlin.math.pow

fun main() {
    MatrixProcessor().menu()
}

class MatrixProcessor() {
    private val scanner = Scanner(System.`in`)

    fun menu() {
        loop@ while (true) {
            println("1. Add matrices\n" +
                    "2. Multiply matrix to a constant\n" +
                    "3. Multiply matrices\n" +
                    "4. Transpose matrix\n" +
                    "5. Calculate a determinant\n" +
                    "6. Inverse matrix\n" +
                    "0. Exit\n")
            print("Your choice: ")
            when(scanner.nextInt()) {
                0 -> break@loop
                1 -> addMatrices()
                2 -> multConstant()
                3 -> multMatrices()
                4 -> transpMatrices()
                5 -> determinantMatrix()
                6 -> transposeMatrix()
            }
        }
    }

    private fun readMatrix(name: String): Matrix {
        print("Enter size of $name matrix: ")
        var l = scanner.nextInt()
        var c = scanner.nextInt()
        println("Enter $name matrix:")
        return Matrix().read(l, c)
    }

    private fun addMatrices() {
        val A = readMatrix("first")
        val B = readMatrix("second")
        if (A.add(B)) {
            println("The add product is:")
            A.print()
        }
        println("")
    }

    private fun multConstant() {
        val A = readMatrix("the")
        print("Enter the number to multiply the matrix: ")
        val n = scanner.nextInt()
        A.dot(n.toDouble())
        println("The multiplication result is:")
        A.print()
    }

    private fun multMatrices() {
        val A = readMatrix("first")
        val B = readMatrix("second")
        val C = Matrix()
        if (A.columns() != B.lines()) {
            println("ERROR")
        } else {
            println("The multiplication result is:")
            A.product(B).print()
        }
        println("")
    }

    private fun transpMatrices() {
        println("\n1. Main diagonal\n" +
                "2. Side diagonal\n" +
                "3. Vertical line\n" +
                "4. Horizontal line")
        print("Your choice: ")
        val option = scanner.nextInt()
        val A = readMatrix("the")
        when(option) {
            1 -> A.transṕMainDiag()
            2 -> A.transpSideDiag()
            3 -> A.transpVertLine()
            4 -> A.transpHorizDiag()
        }
        println("The result is:")
        A.print()
    }

    private fun determinantMatrix() {
        val A = readMatrix("the")
        val det = A.determinant()
        println("The result is:")
        println("$det")
    }

    private fun transposeMatrix() {
        val A = readMatrix("the")
        A.transpose()
        println("The result is;")
        A.print()
    }
}

class Matrix() {
    private val scanner = Scanner(System.`in`)
    private var matrix = mutableListOf<DoubleArray>()

    fun read(lins: Int, cols: Int): Matrix {
        val M = Matrix()
        for (l in 0 until lins) {
            val line = DoubleArray(cols)
            for (c in 0 until cols) {
                line[c] = scanner.nextDouble()
            }
            M.matrix.add(line)
        }
        return M
    }

    fun getMatrix(): MutableList<DoubleArray> {
        return matrix
    }

    fun defMatrix(lines: Int, columns: Int) {
        matrix = MutableList(lines) { DoubleArray(columns) }
    }

    fun lines(): Int{
        return matrix.size
    }

    fun columns(): Int{
        return matrix[0].size
    }

    fun add(m2: Matrix): Boolean{
        if ((lines() != m2.lines()) && (columns() != m2.columns())) {
            println("ERROR")
            return false
        } else {
            for (l in 0 until lines()) {
                for (c in 0 until columns()) {
                    matrix[l][c] += m2.matrix[l][c]
                }
            }
        }
        return true
    }

    fun dot(n: Double) {
        for (l in matrix.indices) {
            for (c in matrix[l].indices) {
                matrix[l][c] *= n
            }
        }
    }

    fun product(m2: Matrix): Matrix {
        val prod = Matrix()
        prod.defMatrix(lines(), m2.columns())

        for (i in 0 until lines()) {
            for (j in 0 until m2.columns()) {
                for (k in 0 until columns()) {
                    prod.matrix[i][j] += matrix[i][k] * m2.matrix[k][j]
                }
            }
        }

        return prod
    }

    fun transṕMainDiag() {
        val m2 = Matrix()
        // (m, le=len(m), ce=len(m[0]))
        for (l in 0 until lines()) {
            val line = DoubleArray(columns())
            for (c in 0 until columns()) {
                line[c] = matrix[c][l]
            }
            m2.matrix.add(line)
        }
        matrix = m2.matrix
    }

    fun transpSideDiag() {
        val m2 = Matrix()
        // (m, ls=len(m)-1, le=-1, lp=-1,
        //                         cs=len(m[0])-1, ce=-1, cp=-1)
        for (l in lines()-1 downTo 0 ) {
            val line = DoubleArray(columns())
            var p = 0
            for (c in columns()-1 downTo  0) {
                line[p] = matrix[c][l]
                p += 1
            }
            m2.matrix.add(line)
        }
        matrix = m2.matrix
    }

    fun transpVertLine() {
        val m2 = Matrix()

//        transpose(m, le=len(m), cs=len(m[0])-1, ce=-1, cp=-1, diag=False)

        for ( l in 0 until lines()){
            val line = DoubleArray(columns())
            val mLine = matrix[l]
            var p = 0
            for (c in mLine.size -1  downTo 0) {
                line[p] = mLine[c]
                p += 1
            }
            m2.matrix.add(line)
        }
        matrix = m2.matrix
    }

    fun transpHorizDiag() {
        val m2 = Matrix()
        //transpose(m, ls=len(m)-1, le=-1, lp=-1, ce=len(m[0]), diag=False)
        for ( l in lines()-1 downTo 0){
            val line = DoubleArray(columns())
            var p = 0
            for (c in 0 until columns()) {
                line[p] = matrix[l][c]
                p += 1
            }
            m2.matrix.add(line)
        }
        matrix = m2.matrix
    }

    private fun removeColumn(x: DoubleArray, id: Int): DoubleArray {
        if (id < 0 || id >= x.size) {
            return x
        }
        val result = x.toMutableList()
        result.removeAt(id)
        return result.toDoubleArray()
    }

    private fun cofactor(m: MutableList<DoubleArray>, l: Int, c: Int): Double{

        if (m[0].size == 2) {
            return m[0][0] * m[1][1] - m[0][1] * m[1][0]
        }
        var aux = m.toMutableList()
        aux.removeAt(l)
        for (i in 0 until aux.size) {
            aux[i] = removeColumn(aux[i], c)
        }
        if (aux.size == 2) {
            return cofactor(aux, 0, 0)
        }
        var dt: Double = 0.0
        for (i in 0 until aux.size) {
            dt += (-1.0).pow(i) * aux[0][i]*cofactor(aux, 0, i)

        }

        return dt
    }

    fun determinant(): Double {
        var det: Double = 0.0
        if ((lines() == 1) && (columns() == 1)) {
            return matrix[0][0]
        }
        if ((lines() == 2) && (columns() == 2)) {
            return cofactor(matrix, 0, 0)
        }

        for (i in 0 until lines()){

            det += (-1.0).pow(i) * matrix[0][i] * cofactor(matrix, 0, i)
        }
        return det
    }

    fun transpose() {
        val dt = determinant()

        if (dt == 0.0) {
            println("This matrix doesn't have an inverse")
        }

        val m2 = Matrix()

        for (i in 0 until lines()){
            var line = DoubleArray(columns())
            for (j in 0 until columns()) {
                line[j] = (-1.0).pow(i+j) * cofactor(matrix, i, j)
            }
            m2.matrix.add(line)
        }

        m2.transṕMainDiag()
        m2.dot(1 / dt)
        matrix = m2.matrix
    }

    fun print() {
        for (l in matrix){
            for (c in l){
                print("%.2f ".format(c))
            }
            println()
        }
    }
    
}

private operator fun DoubleArray.set(c: Int, value: Int) {

}
