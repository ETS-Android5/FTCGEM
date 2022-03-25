
// Made by Isaaq Khanooni

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name = "Concept: ControllerTeleOp", group = "ControllerTeleOp")



public class ControllerTeleOp extends OpMode {
    final static double CLAW_MIN_RANGE = 0.0;
    final static double CLAW_MAX_RANGE = 1.0;

    double claw_1Position;
    double claw_2Position;

    double clawDelta = 0.1;

    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor arm;
    DcMotor testDC;

    Servo claw_1;
    Servo claw_2;


    @Override
    public void init() {

        // Initialize the hardware variables. Note that the strings used here as parameters

        // create the left motor on the robot and the right motor on the robot

        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");

        // create the arm motor on the robot

        arm = hardwareMap.dcMotor.get("motor_3");
        testDC = hardwareMap.dcMotor.get("motor_4");

        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        testDC.setPower(0.0);

        claw_1 = hardwareMap.servo.get("servo_1");
        claw_2 = hardwareMap.servo.get("servo_6");

        // Claw 1 is Left facing Front
        // Claw 2 is Right facing Front

        claw_1Position = -0.5;
        claw_2Position = 0.9;

    }

    @Override
    public void loop() {

        float throttle = 0;

        if (gamepad1.left_trigger > 0) {
            throttle = -gamepad1.left_trigger;
        }

        if (gamepad1.right_trigger > 0) {
            throttle = gamepad1.right_trigger;
        }

        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;

        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        right = (float) scaleInput(right);
        left = (float) scaleInput(left);

        motorRight.setPower(right);
        motorLeft.setPower(left);

        arm.setPower(0.0);
        boolean apressed = gamepad1.a;
        boolean ypressed = gamepad1.y;
        boolean bpressed = gamepad1.b;
        boolean xpressed = gamepad1.x;
        if (apressed) {
            arm.setPower(0.10);
        }

        if (ypressed) {
            arm.setPower(-0.20);
        }

        if (xpressed) {
            claw_1Position += clawDelta;
            claw_2Position -= clawDelta;

        }

        if (bpressed) {
            claw_1Position -= clawDelta;
            claw_2Position += clawDelta;
        }

        claw_1Position = Range.clip(claw_1Position, CLAW_MIN_RANGE, CLAW_MAX_RANGE);
        claw_2Position = Range.clip(claw_2Position, CLAW_MIN_RANGE, CLAW_MAX_RANGE);

        claw_1.setPosition(claw_1Position);
        claw_2.setPosition(claw_2Position);
    }

    @Override
    public void stop() {
        motorRight.setPower(0.0);
        motorLeft.setPower(0.0);
        arm.setPower(0.0);
        // stop the robot
    }


    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        int index = (int) (dVal * 16.0);

        if (index < 0) {
            index = -index;
        }

        if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }
}
