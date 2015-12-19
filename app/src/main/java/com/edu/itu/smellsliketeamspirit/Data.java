package com.edu.itu.smellsliketeamspirit;

public class Data {

    public Data() {

    }

    public Data(byte joystick, double power, double angle) {
        this.angle = angle;
        this.power = power;
        this.joystick = joystick;
    }

    public double angle;
    public double power;
    public byte joystick;
}
