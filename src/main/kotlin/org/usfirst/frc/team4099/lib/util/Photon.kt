package org.usfirst.frc.team4099.lib.util

import edu.wpi.first.wpilibj.SerialPort
import edu.wpi.first.wpilibj.SerialPort.Port


/**
 * Used to command a Teensy running the photon LED control library.
 * Commands the Photon library are just strings of comma seperated numbers with a semicolon at the end
 * 2 = SetAnimation=  2, StripNumber, Animation, Color1, Color2, rate, fade;
 * You can leave off values and defaults will be set for the rest of the values
 * 3 = SetNumberOfLeds = 3, StripNumber, NumberOfLEDsInStrip
 *
 * Color Chart =
 * Red (0..)
 * Orange (32..)
 * Yellow (64..)
 * Green (96..)
 * Aqua (128..)
 * Blue (160..)
 * Purple (192..)
 * Pink(224..)
 * White(255) - This isn't normally on the HUE chart but used in this library to make animaitons easier
 * @author Spectrum3847
 */
class Photon() {

    private val usb: SerialPort

    //Set your default values here. You can set default colors that match your team colors, refer to the Color chart Above
    private val kDefaultAnimation = 4
    private var kDefaultColor1 = 192
    private var kDeafultColor2 = 255
    private val kDefaultRate = 2
    private val kDefaultFade = 150


    private var kAnimation = kDefaultAnimation
    private var kColor1 = kDefaultColor1
    private var kColor2 = kDeafultColor2
    private var kRate = kDefaultRate
    private var kFade = kDefaultFade

    enum class Color {
        RED, ORANGE, YELLOW, GREEN, AQUA, BLUE, PURPLE, PINK, WHITE
    }

    enum class Animation {
        OFF, SOLID, SOLID_DUAL, BLINK, BLINK_DUAL, SIREN,
        PULSE_TO_BLACK, PULSE_TO_WHITE, FADE_ALTERNATE, PULSE_DUAL,
        CYLON, CYLON_DUAL, BOUNCE_BAR, BOUNCE_BAR_DUAL, CYLON_MIDDLE, CYLON_MIDDLE_DUAL,
        TRACER, TRACER_ALTERNATE, WIPE_FWD, WIPE_REV, WIPE_FWD_REV, WIPE_DOWN, WIPE_UP_DOWN, WIPE_UP_DOWN_DUAL,
        WIPE_OUT, WIPE_IN, WIPE_IN_OUT, WIPE_IN_OUT_BACK,
        PERCENTAGE, RAINBOW, JUGGLE, SPARKLES
    }

    init {
        usb = SerialPort(115200, Port.kUSB)
    }

    //USe this constructor to setup default colors when you construct it
    constructor(Color1: Int, Color2: Int) : this() {
        kDefaultColor1 = Color1
        kDeafultColor2 = Color2
    }

    //Build out more construcitons to set default colors, etc

    //Set how many LEDs are in one of the strips, call this for each strip that you want to use.
    fun SetNumberOfLEDs(StripNum: Int, LEDcount: Int) {
        usb.writeString("3,$StripNum,$LEDcount;")
    }

    //Set a new animation on a strip, must include a strip number all other values are optional and can be set seperatly.
    fun setAnimation(StripNum: Int, vararg vals: Int) {//int Animation, int Color1, int Color2, int rate, int fade){
        val Animation = if (vals.size > 0) vals[0] else this.kAnimation
        val Color1 = if (vals.size > 1) vals[1] else this.kColor1
        val Color2 = if (vals.size > 2) vals[2] else this.kColor2
        val Rate = if (vals.size > 3) vals[3] else this.kRate
        val Fade = if (vals.size > 4) vals[4] else this.kFade
        usb.writeString("2,$StripNum,$Animation,$Color1,$Color2,$Rate,$Fade;")
    }

    fun setAnimation(StripNum: Int, a: Animation, c1: Color, c2: Color, rate: Int, fade: Int) {
        setAnimation(StripNum, getAnimation(a), getColor(c1), getColor(c2), rate, fade)
    }

    fun setAnimation(StripNum: Int, a: Animation, c1: Color, c2: Color, rate: Int) {
        setAnimation(StripNum, getAnimation(a), getColor(c1), getColor(c2), rate)
    }

    fun setAnimation(StripNum: Int, a: Animation, c1: Color, c2: Color) {
        setAnimation(StripNum, getAnimation(a), getColor(c1), getColor(c2))
    }

    fun setAnimation(StripNum: Int, a: Animation, c1: Color) {
        setAnimation(StripNum, getAnimation(a), getColor(c1))
    }

    fun setAnimation(StripNum: Int, a: Animation, c1: Color, rate: Int, fade: Int) { //Allows only a single color to be set
        setAnimation(StripNum, getAnimation(a), getColor(c1), kColor2, rate, fade)
    }

    fun setAnimation(StripNum: Int, a: Animation, c1: Color, rate: Int) { //Allows only a single color to be set but you can adjust the rate
        setAnimation(StripNum, getAnimation(a), getColor(c1), kColor2, rate)
    }

    fun setAnimation(StripNum: Int, a: Animation) {
        setAnimation(StripNum, getAnimation(a))
    }

    //Reset all the values to your default values, useful if you want an animation to use default values, but they may have been already changed.
    fun resetValue() {
        kAnimation = kDefaultAnimation
        kColor1 = kDefaultColor1
        kColor2 = kDeafultColor2
        kRate = kDefaultRate
        kFade = kDefaultFade
    }

    fun getColor(c: Color): Int {
        when (c) {
            Photon.Color.RED -> return 0
            Photon.Color.ORANGE -> return 32
            Photon.Color.YELLOW -> return 64
            Photon.Color.GREEN -> return 96
            Photon.Color.AQUA -> return 128
            Photon.Color.BLUE -> return 160
            Photon.Color.PURPLE -> return 192
            Photon.Color.PINK -> return 224
            Photon.Color.WHITE -> return 255
            else -> return 0 //Default to RED
        }
    }

    fun getAnimation(a: Animation): Int {
        when (a) {
            Photon.Animation.OFF -> return 0
            Photon.Animation.SOLID -> return 1
            Photon.Animation.SOLID_DUAL -> return 2
            Photon.Animation.BLINK -> return 3
            Photon.Animation.BLINK_DUAL -> return 4
            Photon.Animation.SIREN -> return 5
            Photon.Animation.PULSE_TO_BLACK -> return 10
            Photon.Animation.PULSE_TO_WHITE -> return 11
            Photon.Animation.FADE_ALTERNATE -> return 12
            Photon.Animation.PULSE_DUAL -> return 13
            Photon.Animation.CYLON -> return 20
            Photon.Animation.CYLON_DUAL -> return 21
            Photon.Animation.BOUNCE_BAR -> return 22
            Photon.Animation.BOUNCE_BAR_DUAL -> return 23
            Photon.Animation.CYLON_MIDDLE -> return 24
            Photon.Animation.CYLON_MIDDLE_DUAL -> return 25
            Photon.Animation.TRACER -> return 30
            Photon.Animation.TRACER_ALTERNATE -> return 31
            Photon.Animation.WIPE_FWD -> return 32
            Photon.Animation.WIPE_REV -> return 33
            Photon.Animation.WIPE_FWD_REV -> return 34
            Photon.Animation.WIPE_DOWN -> return 35
            Photon.Animation.WIPE_UP_DOWN -> return 36
            Photon.Animation.WIPE_UP_DOWN_DUAL -> return 37
            Photon.Animation.WIPE_OUT -> return 40
            Photon.Animation.WIPE_IN -> return 41
            Photon.Animation.WIPE_IN_OUT -> return 42
            Photon.Animation.WIPE_IN_OUT_BACK -> return 43
            Photon.Animation.RAINBOW -> return 96
            Photon.Animation.PERCENTAGE -> return 97
            Photon.Animation.JUGGLE -> return 98
            Photon.Animation.SPARKLES -> return 99
            else -> return 3 //Default to Blink
        }
    }


    fun setColor1(c: Int) {
        kColor1 = c
    }

    fun setColor1(c: Color) {
        kColor1 = getColor(c)
    }

    fun setColor2(c: Int) {
        kColor2 = c
    }

    fun setColor2(c: Color) {
        kColor2 = getColor(c)
    }

    fun setAnimation(a: Int) {
        kAnimation = a
    }

    fun setAnimation(a: Animation) {
        kAnimation = getAnimation(a)
    }

    fun setRate(r: Int) {
        kRate = r
    }

    fun setFade(f: Int) {
        kFade = f
    }
}
