package silent;

public class TimingPoint {
	public int offset;
	public int volume;
	public int ending; 
	public TimingPoint(int offset, int volume) {
		this.offset = offset;
		this.volume = volume;		
		ending = -1;
	}
	
	
}
