public class Gate {
    private String gateId;

    public Gate(String gateId) {
        this.gateId = gateId;
    }

    public String getGateId() { return gateId; }

    @Override
    public String toString() { return "Gate(" + gateId + ")"; }
}
