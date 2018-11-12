package silent;

public class TimingPoint {
    public int offset;
    public int volume;
    public int ending; 
    public double sv;
    public double beat;
    public TimingPoint(int offset, int volume, double sv, double beat) {
        this.offset = offset;
        this.volume = volume;        
        this.sv = sv;
        this.beat = beat;
        ending = -1;
    
    }
    
    
}