package com.grivera.generator.sensors;

/**
 * Represents a Sensor Node in a com.grivera.generator.Network that has overflow data packets to store.
 *
 * @see SensorNode
 */
public class DataNode extends SensorNode {

    private static int idCounter = 1;
    private int id;
    private int overflowPackets;
    private int overflowPacketsValue;
    private int packetsLeft;

    public DataNode(double x, double y, double tr, int power, int overflowPackets, int overflowPacketsValue) {
        super(x, y, tr, String.format("DN%02d", idCounter), power);
        this.id = idCounter++;
        this.setOverflowPackets(overflowPackets);
        this.overflowPacketsValue = overflowPacketsValue;
    }

    public void setOverflowPackets(int overflowPackets) {
        this.overflowPackets = overflowPackets;
        this.packetsLeft = overflowPackets;
    }

    public int getOverflowPackets() {
        return this.overflowPackets;
    }

    public boolean isEmpty() {
        return this.packetsLeft < 1;
    }

    @Override
    public boolean canTransmitTo(SensorNode receiver, int deltaPackets) {
        return super.canTransmitTo(receiver, deltaPackets) && this.packetsLeft - deltaPackets >= 0;
    }

    @Override
    public void transmitTo(SensorNode receiver, int packets) {
        super.transmitTo(receiver, packets);
        this.packetsLeft -= packets;
    }

    public void discardPackets(int packets) {
        if (packets > this.packetsLeft) {
            throw new IllegalArgumentException(String.format("DN with %d packets cannot discard %d packets", this.packetsLeft, packets));
        }
        this.overflowPackets -= packets;
    }

    @Override
    public boolean canStoreFrom(SensorNode senderNode, int packets) {
        return false;
    }

    @Override
    public void resetPackets() {
        this.packetsLeft = this.overflowPackets;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public int getPacketsLeft() {
        return this.packetsLeft;
    }

    public int getOverflowPacketValue() {
        return this.overflowPacketsValue;
    }

    public static void resetCounter() {
        idCounter = 1;
    }

}
