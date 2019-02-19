package org.usfirst.frc.team4099.auto.paths

import java.io.File

enum class FieldPaths (val pathFileLeft: File, val pathFileRight: File) {
    LEFTH2_TO_LEFTROCKET3(File("/home/lvuser/pathFiles/LeftH2ToLeftRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/LeftH2ToLeftRocket3.right.pf1.csv")),
    RIGHTH2_TO_RIGHTROCKET3(File("/home/lvuser/pathFiles/RightH2ToRightRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/RightH2ToRightRocket3.right.pf1.csv")),
    RIGHTROCKET3_TO_LOADSTATION(File("/home/lvuser/pathFiles/RightRocket3ToLoadStation.left.pf1.csv"), File("/home/lvuser/pathFiles/RightRocket3ToLoadStation.right.pf1.csv")),
    LEFTROCKET3_TO_LOADSTATION(File("/home/lvuser/pathFiles/LeftRocket3ToLoadStation.left.pf1.csv"), File("/home/lvuser/pathFiles/LeftRocket3ToLoadStation.right.pf1.csv")),
    LEFTROCKET1_TO_LOADEDSTATION(File("/home/lvuser/pathFiles/LeftRocket1ToLoadStation.left.pf1.csv"), File("/home/lvuser/pathFiles/LeftRocket1ToLoadStation.right.pf1.csv")),
    RIGHTROCKET1_TO_LOADSTATION(File("/home/lvuser/pathFiles/RightRocket1ToLoadStation.left.pf1.csv"), File("/home/lvuser/pathFiles/RightRocket1ToLoadStation.right.pf1.csv")),
    LEFTH1_TO_LEFTROCKET3(File("/home/lvuser/pathFiles/LeftH1ToLeftRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/LeftH1ToLeftRocket3.right.pf1.csv")),
    RIGHTH1_TO_RIGHTROCKET3(File("/home/lvuser/pathFiles/RightH1ToRightRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/RightH1ToRightRocket3.right.pf1.csv")),
    CENTERH1_TO_LEFTROCKET3(File("/home/lvuser/pathFiles/CenterH1ToLeftRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/CenterH1ToLeftRocket3.right.pf1.csv")),
    CENTERH1_TO_RIGHTROCKET3(File("/home/lvuser/pathFiles/CenterH1ToRightRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/CenterH1ToRightRocket3.right.pf1.csv")),
    CENTERH1_TO_LEFTROCKET1(File("/home/lvuser/pathFiles/CenterH1ToLeftRocket1.left.pf1.csv"), File("/home/lvuser/pathFiles/CenterH1ToLeftRocket1.right.pf1.csv")),
    CENTERH1_TO_RIGHTROCKET1(File("/home/lvuser/pathFiles/CenterH1ToRightRocket1.left.pf1.csv"), File("/home/lvuser/pathFiles/CenterH1ToRightRocket1.right.pf1.csv")),
    LEFTH1_TO_LEFTROCKET1(File("/home/lvuser/pathFiles/LeftH1ToLeftRocket1.left.pf1.csv"), File("/home/lvuser/pathFiles/LeftH1ToLeftRocket1.right.pf1.csv")),
    RIGHTH1_TO_RIGHTROCKET1(File("/home/lvuser/pathFiles/RightH1ToRightRocket1.left.pf1.csv"), File("/home/lvuser/pathFiles/RightH1ToRightRocket1.right.pf1.csv")),
    LEFTH2_TO_LEFTROCKET1(File("/home/lvuser/pathFiles/LeftH2toLeftRocket1.left.pf1.csv"), File("/home/lvuser/pathFiles/LeftH2toLeftRocket1.right.pf1.csv")),
    RIGHTH2_TO_RIGHTROCKET1(File("/home/lvuser/pathFiles/RightH2ToRightRocket1.left.pf1.csv"), File("/home/lvuser/pathFiles/RightH2ToRightRocket1.right.pf1.csv")),
    STANDSTILL(File("/home/lvuser/pathFiles/RightH2ToRightRocket3.left.pf1.csv"), File("/home/lvuser/pathFiles/RightH2ToRightRocket3.left.pf1.csv"))
}