package com.grivera.generator.sensors;

public class TransitionNode extends SensorNode {
    private static int idCounter = 1;
    private int id;

    public TransitionNode(double x, double y, double tr, int power) {
        super(x, y, tr, String.format("TN%02d", idCounter), power);
        this.id = idCounter++;
    }

    @Override
    public void resetPackets() { /* Do Nothing */ }

    @Override
    public int getId() {
        return this.id;
    }

    public static void resetCounter() {
        idCounter = 1;
    }

    @Override
    public boolean canStoreFrom(SensorNode senderNode, int packets) {
       return false;
    }

    @Override
    public void storeFrom(SensorNode senderNode, int packets) {
        throw new UnsupportedOperationException("Transition Nodes cannot store packets");
    }

    @Override
    public boolean canOffloadTo(SensorNode receiverNode, int packets) {
        return false;
    }

    @Override
    public void offloadTo(SensorNode receiverNode, int packets) {
        throw new UnsupportedOperationException("Transition Nodes cannot offload packets");
    }

}
