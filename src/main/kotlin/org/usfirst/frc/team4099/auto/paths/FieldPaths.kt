package org.usfirst.frc.team4099.auto.paths

import java.io.File

enum class FieldPaths (val pathFileLeft: File, val pathFileRight: File) {
    LEFTH2_TO_LEFTROCKET3(File("/home/lvuser/pathFiles/LeftH2ToLeftRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/LeftH2ToLeftRocket3.right.pf1.csv")),
    RIGHTH2_TO_RIGHTROCKET3(File("/home/lvuser/pathFiles/RightH2ToRightRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/RightH2ToRightRocket3.right.pf1.csv")),
    STANDSTILL(File("/home/lvuser/pathFiles/RightH2ToRightRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/RightH2ToRightRocket3.left.pf1.csv"))
}