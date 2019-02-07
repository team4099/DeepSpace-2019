package org.usfirst.frc.team4099.auto.paths
import java.io.File

class Path(path: FieldPaths) {
    var leftVelocities: ArrayList<Double> = ArrayList<Double>()
    var rightVelocities: ArrayList<Double> = ArrayList<Double>()
    var leftDistances: ArrayList<Double> = ArrayList<Double>()
    var rightDistances: ArrayList<Double> = ArrayList<Double>()
    var robotHeadings: ArrayList<Double> = ArrayList<Double>()
    val timeDelta: Double = 0.02
    init {
        val leftFile: File
        val rightFile: File
//        if (path == FieldPaths.LEFTH2_TO_LEFTROCKET3){
//            leftFile = File("/pathFiles/LeftH2ToLeftRocket3.left.pf1.csv")
//            rightFile = File("/pathFiles/LeftH2ToLeftRocket3.right.pf1.csv")
//        }
//        else if (path == FieldPaths.RIGHTH2_TO_RIGHTROCKET3){
//            leftFile = File("/pathFiles/RightH2ToRightRocket3.left.pf1.csv")
//            rightFile = File("/pathFiles/RightH2ToRightRocket3.right.pf1.csv")
//        }
//        else {
//            leftFile = File("")
//            rightFile = File("")
//        }
        leftFile = path.pathFileLeft
        rightFile = path.pathFileRight
        fillVelocities(leftFile, rightFile)
        fillDistances(leftFile, rightFile)
        fillHeadings(leftFile)
        println("********"+ leftVelocities.size)



    }
    private fun fillVelocities(leftTraj: File, rightTraj: File){
        var linesL:List<String> = leftTraj.readLines()
        for(i in 1..linesL.lastIndex){
            val separated:List<String> = linesL.get(i).split(",")
//            println("Velocity: " + separated.get(4) + " Acceleration: " + separated.get(5))
//            val velocity = separated.get(4)
//            val accel = separated.get(5)
            leftVelocities.add(separated.get(4).toDouble())
        }
        var linesR:List<String> = rightTraj.readLines()
        for(i in 1..linesR.lastIndex){
            val separated:List<String> = linesR.get(i).split(",")
//            println("Velocity: " + separated.get(4) + " Acceleration: " + separated.get(5))
//            val velocity = separated.get(4)
//            val accel = separated.get(5)
            rightVelocities.add(separated.get(4).toDouble())
        }
    }
    private fun fillDistances(leftTraj: File, rightTraj: File){
        var linesL:List<String> = leftTraj.readLines()
        for(i in 1..linesL.lastIndex){
            val separated:List<String> = linesL.get(i).split(",")
//            println("Velocity: " + separated.get(4) + " Acceleration: " + separated.get(5))
//            val velocity = separated.get(4)
//            val accel = separated.get(5)
            leftDistances.add(separated.get(3).toDouble())
        }
        var linesR:List<String> = rightTraj.readLines()
        for(i in 1..linesR.lastIndex){
            val separated:List<String> = linesR.get(i).split(",")
//            println("Velocity: " + separated.get(4) + " Acceleration: " + separated.get(5))
//            val velocity = separated.get(4)
//            val accel = separated.get(5)
            rightDistances.add(separated.get(3).toDouble())
        }
    }
    public fun fillHeadings(traj: File){
        var lines:List<String> = traj.readLines()
        for(i in 1..lines.lastIndex){
            val separated:List<String> = lines.get(i).split(",")
//            println("Velocity: " + separated.get(4) + " Acceleration: " + separated.get(5))
//            val velocity = separated.get(4)
//            val accel = separated.get(5)
            robotHeadings.add(separated.get(7).toDouble())
        }
    }
    public fun getLeftVelocity(time: Double): Double {
        return leftVelocities.get((time/timeDelta).toInt())
    }
    public fun getRightVelocity(time: Double): Double {
        return rightVelocities.get((time/timeDelta).toInt())
    }
    public fun getLeftVelocityIndex(index: Int): Double {
        return leftVelocities.get(index)
    }
    public fun getRightVelocityIndex(index: Int): Double {
        return rightVelocities.get(index)
    }
    public fun getLeftDistanceIndex(index: Int): Double {
        return leftDistances.get(index)
    }
    public fun getRightDistanceIndex(index: Int): Double {
        return rightDistances.get(index)
    }
    public fun getHeading(time: Double): Double {
        return robotHeadings.get((time/timeDelta).toInt())
    }
    public fun getHeadingIndex(index: Int): Double{
        return robotHeadings.get(index)
    }
    public fun getTrajLength(): Int {
        return leftVelocities.size
    }
    public fun getDeltaTime(): Double {
        return timeDelta
    }
}