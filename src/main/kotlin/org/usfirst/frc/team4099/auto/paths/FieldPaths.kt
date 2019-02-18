package org.usfirst.frc.team4099.auto.paths

import java.io.File

enum class FieldPaths (val pathFileLeft: File, val pathFileRight: File) {
    LEFTH2_TO_LEFTROCKET3(File("/home/lvuser/pathFiles/LeftH2ToLeftRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/LeftH2ToLeftRocket3.right.pf1.csv")),
    RIGHTH2_TO_RIGHTROCKET3(File("/home/lvuser/pathFiles/RightH2ToRightRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/RightH2ToRightRocket3.right.pf1.csv")),
    RIGHTROCKET3_TO_LOADSTATION(File("/home/lvuser/pathFiles/RightRocket3ToLoadStation.left.pf1.csv"), File("/home/lvuser/pathFiles/RightRocket3ToLoadStation.right.pf1.csv")),
    LEFTROCKET3_TO_LOADSTATION(File("/home/lvuser/pathFiles/LeftRocket3ToLoadStation.left.pf1.csv"), File("/home/lvuser/pathFiles/LeftRocket3ToLoadStation.right.pf1.csv")),
    LEFTROCKET1_TO_LOADEDSTATION(),
    RIGHTROCKET1_TO_LOADSTATION(),
    LEFTH1_TO_LEFTROCKET3(),
    RIGHTH1_TO_RIGHTROCKET3(),
    CENTERH1_TO_LEFTROCKET3(),
    CENTERH1_TO_RIGHTROCKET3(),
    CENTERH1_TO_LEFTROCKET1(),
    CENTERH1_TO_RIGHTROCKET1(),
    LEFTH1_TO_LEFTROCKET1(),
    RIGHTH1_TO_RIGHTROCKET1(),
    LEFTH2_TO_LEFTROCKET1(),
    RIGHTH2_TO_RIGHTROCKET1(),
    STANDSTILL(File("/home/lvuser/pathFiles/RightH2ToRightRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/RightH2ToRightRocket3.left.pf1.csv"))
}