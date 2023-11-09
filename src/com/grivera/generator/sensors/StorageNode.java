package com.grivera.generator.sensors;

/**
 * Represents a Sensor Node in a com.grivera.generator.Network that has storage space for overflow data packets.
 *
 * @see SensorNode
 */
public class StorageNode extends SensorNode {

    private static final double E_store = 100e-9;

    private static int idCounter = 1;
    private int id;
    private int capacity;
    private int usedSpace;

    public StorageNode(double x, double y, double tr, int power, int capacity) {
        super(x, y, tr, String.format("SN%02d", idCounter), power);
        this.id = idCounter++;
        this.setCapacity(capacity);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        this.usedSpace = 0;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getUsedSpace() {
        return this.usedSpace;
    }

    public int getSpaceLeft() {
        return this.capacity - this.usedSpace;
    }

    public boolean isFull() {
        return this.usedSpace >= this.capacity;
    }

    @Override
    public boolean canStoreFrom(SensorNode senderNode, int packets) {
        return this.canReceiveFrom(senderNode, packets) && this.usedSpace + packets <= this.capacity;
    }

    @Override
    public void storeFrom(SensorNode senderNode, int packets) {
        if (!this.canStoreFrom(senderNode, packets)) {
            throw new IllegalArgumentException(String.format("%s with %d spaces left cannot store %d packets", this.getName(), this.getSpaceLeft(), packets));
        }

        super.receiveFrom(senderNode, packets);
        this.usedSpace += packets;
    }

    @Override
    public void resetPackets() {
        this.usedSpace = 0;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public int calculateStorageCost() {
        double cost = this.usedSpace * BITS_PER_PACKET * E_store;
        return (int) Math.round(cost * Math.pow(10, 6));
    } 

    public static void resetCounter() {
        idCounter = 1;
    }

    @Override
    public boolean canOffloadTo(SensorNode receiverNode, int packets) {
       return false;
    }

    @Override
    public void offloadTo(SensorNode receiverNode, int packets) {
        throw new UnsupportedOperationException("Storage Nodes cannot offload packets");
    }
}
